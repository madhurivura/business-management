### **Business Management System**

A robust **Spring Boot** backend application to manage business accounts, contacts, products, and sales orders with auto-generated IDs, soft deletion, and tax calculations.

---

##  Overview

This backend project provides a complete solution for business management, including modules for:

- **Account Management**
- **Contact Management**
- **Product Management (with Soft Delete)**
- **Sales Order Management (with Soft Delete)**

It supports secure REST APIs, JWT authentication, pagination, and historical data retention with soft delete.

---

##  Core Modules

###  1. Account Management
- Full CRUD operations for business accounts.  
- Auto-generate unique account numbers (`ACC-1001`, `ACC-1002`, …).  
- Search accounts by name with pagination and sorting.

###  2. Contact Management
- CRUD operations for contacts linked to accounts.  
- Maintain **one-to-many relationship** between accounts and contacts.  
- Validation and data integrity ensured.

###  3. Product Management (Soft Delete)
- Create, update, and delete products.  
- Auto-generate product codes (`PRD-1001`, `PRD-1002`, …).  
- Implements **soft delete**: deleted products are inactive and cannot be used in new orders.  
- Only active products are listed and available.

###  4. Sales Order Management (Soft Delete)
- Create sales orders with multiple products.  
- Auto-generate order numbers (`SO-YYYYMMDD-0001`).  
- Calculates:
  - **Subtotal** (sum of item prices × quantities)  
  - **Tax (18%)**  
  - **Total amount (subtotal + tax)**
- Only **customers** can create and cancel their own orders.  
- Cancelled orders are **soft deleted**, not removed from the database.

---

##  Workflow

1. **Account Creation** → Create new accounts with unique account numbers.  
2. **Contact Management** → Add contacts linked to specific accounts.  
3. **Product Management** → Manage product catalog with soft delete.  
4. **Sales Orders** → Create orders with automatic tax and total calculation.  
5. **Order Cancellation** → Customers can cancel their own orders (soft delete).  
6. **Search & Pagination** → Available for all major entities (accounts, contacts, products, orders).

---

##  Technical Stack

| Technology | Description |
|-------------|-------------|
| **Java 17+** | Core backend language |
| **Spring Boot** | Framework for REST APIs and backend logic |
| **Spring Data JPA** | ORM for database operations |
| **MySQL** | Relational database |
| **Spring Security + JWT** | Authentication and authorization |
| **Lombok** | Reduces boilerplate code |
| **Maven** | Build & dependency management |
| **Spring Actuator** | Monitoring and app health endpoints |

---

##  Installation & Setup

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/madhurivura/business-management.git
cd business-management
````

### 2️⃣ Configure Database

Open `src/main/resources/application.properties` and update your MySQL credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/business_management
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

### 3️⃣ Build the Project

```bash
mvn clean install
```

### 4️⃣ Run the Application

```bash
mvn spring-boot:run
```

### 5️⃣ Access APIs

Base URL → `http://localhost:8080/api/`

---

##  API Endpoints

| Module       | Endpoint            | Description                     |
| ------------ | ------------------- | ------------------------------- |
| Account      | `/api/accounts`     | Manage account CRUD operations  |
| Contact      | `/api/contacts`     | Manage contacts for accounts    |
| Product      | `/api/products`     | CRUD + Soft delete for products |
| Sales Orders | `/api/sales-orders` | Create, view, and cancel orders |

🔹 All APIs support **pagination**, **sorting**, and **soft delete filtering**.

---

Business Logic Highlights

Auto-generated IDs for all major entities.

Soft delete implemented using an isActive flag.

18% Tax Calculation for all sales orders.

Role-based access ensuring only customers can create/cancel orders.

Historical data preservation through soft delete.

Example Order Calculation
{
  "subtotal": 2776.0,
  "tax": 277.6,
  "total": 3053.6
}


System applies 18% tax automatically.

##  Monitoring with Actuator

Spring Boot Actuator provides health and metrics endpoints:

| Endpoint            | Description               |
| ------------------- | ------------------------- |
| `/actuator/health`  | Application health status |
| `/actuator/info`    | Basic app info            |
| `/actuator/metrics` | Performance metrics       |

---

##  Developer Notes

* Use `@Valid` for DTO validation
* `@Max`, `@Min`, and `@NotNull` annotations for input rules
* Soft delete implemented using `isActive` boolean field
* Exception handling via `@ControllerAdvice`

---
## Summary
The Business Management System backend is a scalable, modular, and secure Spring Boot application designed to simplify business workflows — from managing accounts to processing sales orders with automated tax, validation, and soft delete mechanisms.
