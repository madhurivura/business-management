package com.example.business_management.service;

import com.example.business_management.dto.DeletedDto;
import com.example.business_management.dto.productsDto.ProductRequest;
import com.example.business_management.dto.productsDto.ProductResponse;
import com.example.business_management.entity.Account;
import com.example.business_management.entity.Product;
import com.example.business_management.exception.ResourceNotFoundException;
import com.example.business_management.exception.UnauthorizedException;
import com.example.business_management.repo.AccountRepo;
import com.example.business_management.repo.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepo productRepo;
    private final AccountRepo accountRepo;

    public List<ProductResponse> getProducts(String search, int page, int size) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Account account = accountRepo.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found or inactive"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Product> products;

        if (search.isEmpty()) {
            products = productRepo.findByAccountAndIsActiveTrue(account, pageable);
        } else {
            products = productRepo.findByAccountAndNameContainingIgnoreCaseAndIsActiveTrue(account, search, pageable);
        }

        return products
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }


    public ProductResponse getProductById(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Account account = accountRepo.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found or inactive"));

        Product product = productRepo.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (!product.getAccount().getId().equals(account.getId())) {
            throw new UnauthorizedException("Access denied — not your product");
        }

        return toResponse(product);
    }

    public ProductResponse createProduct(ProductRequest req) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        if (!auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new ResourceNotFoundException("Only ADMIN can create products");
        }


        Account account = accountRepo.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found or inactive"));


        Product product = new Product();
        product.setName(req.getName());
        product.setDescription(req.getDescription());
        product.setPrice(req.getPrice());
        product.setStock(req.getStock());
        product.setProductCode("PRD-" + (1000 + productRepo.count() + 1));
        product.setAccount(account);

        productRepo.save(product);

        return toResponse(product);
    }


    public ProductResponse updateProduct(Long id, ProductRequest req) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Account account = accountRepo.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found or inactive"));

        Product product = productRepo.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (!auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new UnauthorizedException("Only ADMIN can update products");
        }

        if (!product.getAccount().getId().equals(account.getId())) {
            throw new UnauthorizedException("Access denied — cannot update another account’s product");
        }

        if (req.getName() != null) product.setName(req.getName());
        if (req.getDescription() != null) product.setDescription(req.getDescription());
        if (req.getPrice() != null) product.setPrice(req.getPrice());
        if (req.getStock() != null) product.setStock(req.getStock());

        productRepo.save(product);
        return toResponse(product);
    }

    public DeletedDto deleteProduct(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Account account = accountRepo.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found or inactive"));

        Product product = productRepo.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (!auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new UnauthorizedException("Only ADMIN can delete products");
        }

        if (!product.getAccount().getId().equals(account.getId())) {
            throw new UnauthorizedException("Access denied — cannot delete another account’s product");
        }

        product.setActive(false);
        productRepo.save(product);
        return DeletedDto.builder()
                .message("Product soft deleted successfully")
                .build();
    }

    private ProductResponse toResponse(Product p) {
        return new ProductResponse(
                p.getId(),
                p.getProductCode(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getStock(),
                p.isActive()
        );
    }
}
