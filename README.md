# 🧾 Business Management System

A robust **Spring Boot** backend application designed to manage business operations such as accounts, contacts, products, and sales orders.  
It ensures secure role-based access, soft deletion, and accurate order calculations with 18% tax integration.

---

##  Overview

This project provides an end-to-end backend solution for a Business Management System.  
It includes modules for **Account Management**, **Contact Management**, **Product Management**, and **Sales Order Management** — all connected with clean entity relationships and validations.

---

##  Core Modules

### 🏢 Account Management
- Perform full CRUD operations on business accounts.  
- Auto-generate unique account numbers (`ACC-1001`, `ACC-1002`, ...).  
- Search accounts by name with **pagination** and sorting.  

### Contact Management
- Manage contacts (employees/students) linked to accounts.  
- Maintain one-to-many relationships between accounts and contacts.  
- Full CRUD functionality with validation.

###  Product Management (Soft Delete)
- Create, update, and delete products.  
- Auto-generate product codes (`PRD-1001`, `PRD-1002`, ...).  
- Implements **soft delete**: deleted products are inactive and cannot be used in new orders.  

###  Sales Order Management (Soft Delete)
- Create sales orders containing multiple products.  
- Auto-generate order numbers (`SO-YYYYMMDD-0001`).  
- Calculates:
  - **Subtotal** (sum of item prices × quantities)
  - **Tax (18%)**
  - **Total amount (subtotal + tax)**
- Only **customers** can create or cancel their own orders.  
- Soft delete for orders — cancelled orders remain in the database but marked inactive.

---

##  Tech Stack

| Technology | Description |
|-------------|--------------|
| **Java 17+** | Core backend language |
| **Spring Boot** | Framework for rapid backend development |
| **Spring Data JPA** | ORM for database management |
| **Spring Security + JWT** | Authentication and authorization |
| **MySQL** | Relational database |
| **Lombok** | To reduce boilerplate code |
| **Maven** | Build and dependency management tool |

---
