package com.example.business_management.service;

import com.example.business_management.dto.DeletedDto;
import com.example.business_management.dto.salesDto.SalesOrderItemResponse;
import com.example.business_management.dto.salesDto.SalesOrderRequest;
import com.example.business_management.dto.salesDto.SalesOrderResponse;
import com.example.business_management.entity.*;
import com.example.business_management.exception.ResourceNotFoundException;
import com.example.business_management.exception.UnauthorizedException;
import com.example.business_management.repo.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesOrderService {

    private final SalesOrderRepo orderRepo;
    private final SalesOrderItemRepo itemRepo;
    private final AccountRepo accountRepo;
    private final ProductRepo productRepo;
    private final ContactRepo contactRepo;
    private final CustomerRepo customerRepo;

    @Transactional
    public SalesOrderResponse createOrder(SalesOrderRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Account acc = accountRepo.findByIdAndIsActiveTrue(request.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found or unavailable"));

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new ResourceNotFoundException("Order must contain at least one item to proceed");
        }


        Customer customer = customerRepo.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found or inactive with email: " + email));


        if (accountRepo.findByEmailAndIsActiveTrue(email).isPresent() ||
                contactRepo.findByEmailAndIsActive(email,true).isPresent()) {
            throw new UnauthorizedException("Only customers can place orders");
        }


        SalesOrder order = new SalesOrder();
        order.setOrderDate(LocalDate.now());
        order.setOrderNumber(generateOrderNumber());
        order.setAccount(acc);
        order.setCustomer(customer);


        List<SalesOrderItem> items = request.getItems().stream().map(i -> {
            Product product = productRepo.findByIdAndIsActiveTrue(i.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found or inactive"));

            product.setStock(product.getStock()-i.getQuantity());
            productRepo.save(product);

            SalesOrderItem item = new SalesOrderItem();
            item.setProduct(product);
            item.setQuantity(i.getQuantity());
            item.setPrice(product.getPrice());
            item.setSalesOrder(order);
            return item;
        }).collect(Collectors.toList());

        double subtotal = items.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
        double tax = subtotal * 0.18;
        double total = subtotal + tax;

        order.setSubtotal(subtotal);
        order.setTax(tax);
        order.setTotal(total);
        order.setItems(items);

        orderRepo.save(order);
        return toResponse(order);
    }

    public List<SalesOrderResponse> getAllOrders(int page, int size) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Sort s = Sort.by("id").descending();

        Pageable pageable = PageRequest.of(page, size, s);


        Optional<Account> account = accountRepo.findByEmailAndIsActiveTrue(email);
        Optional<Contact> contact = contactRepo.findByEmailAndIsActive(email,true);

        Long accId;
        if(account.isPresent()){
            accId=account.get().getId();
        }else if(contact.isPresent()){
            accId=contact.get().getAccount().getId();
        }else{
            throw new UnauthorizedException("only admin or employees of active accounts can view their respective orders");
        }

        Page<SalesOrder> orders = orderRepo.findByAccountIdAndIsActiveTrue(accId, pageable);

        return  orders
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public DeletedDto cancelOrder(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        if (accountRepo.findByEmailAndIsActiveTrue(email).isPresent() ||
                contactRepo.findByEmailAndIsActive(email, true).isPresent()) {
            throw new UnauthorizedException("Only customers can cancel orders");
        }

        SalesOrder order = orderRepo.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("order not found"));

        Customer customer = customerRepo.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new UnauthorizedException("you can cancel your order or your acc is inactive"));


        if (!order.getCustomer().getId().equals(customer.getId())) {
            throw new UnauthorizedException("Access denied - you can only cancel your own order");
        }


        order.setActive(false);
        orderRepo.save(order);

        return DeletedDto.builder()
                .message("Order cancelled successfully")
                .build();
    }




    private String generateOrderNumber() {
        String date = LocalDate.now().toString().replace("-", "");
        long count = orderRepo.count() + 1;
        return String.format("SO-%s-%04d", date, count);
    }


    private SalesOrderResponse toResponse(SalesOrder order) {
        List<SalesOrderItemResponse> itemResponses = order.getItems().stream()
                .map(i -> new SalesOrderItemResponse(
                        i.getProduct().getName(),
                        i.getQuantity(),
                        i.getPrice()
                )).collect(Collectors.toList());

        return new SalesOrderResponse(
                order.getOrderNumber(),
                order.getOrderDate(),
                order.getSubtotal(),
                order.getTax(),
                order.getTotal(),
                order.getAccount().getId(),
                itemResponses
        );
    }
}
