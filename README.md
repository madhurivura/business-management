Business Management System 

The Business Management System is a Spring Boot backend application that manages business accounts, contacts, products, and sales orders.
It simulates a real-world business workflow with proper entity relationships, automated number/code generation, soft deletion, tax calculation, and clean REST APIs.

1. Purpose of the Project

Manage business accounts and their linked contacts.

Manage products with auto-generated codes and soft delete functionality.

Create and manage sales orders, including multiple products per order.

Auto-generate unique account numbers, product codes, and order numbers.

Calculate totals, taxes (18%), and final amounts for sales orders.

Provide CRUD operations for all entities through REST APIs.

Enable soft deletion so records are not permanently removed but marked inactive.

Support search and pagination for large datasets.

2. Core Modules
a) Account Management

Manage business accounts with full CRUD operations.

Auto-generate unique account numbers (ACC-1001, ACC-1002, …).

Search accounts by name with pagination and sorting.

One-to-many relationship with contacts.

b) Contact Management

Manage contacts linked to accounts (employees, clients, etc.).

Full CRUD operations with validation.

Inactive contacts are ignored in listings.

c) Product Management (Soft Delete)

Create, update, and manage products.

Auto-generate product codes (PRD-1001, PRD-1002, …).

Soft delete: deleted products are marked inactive and cannot be used in new sales orders.

d) Sales Order Management (Soft Delete)

Create sales orders with multiple products.

Auto-generate order numbers (SO-YYYYMMDD-0001).

Calculate:

Subtotal (sum of product price × quantity)

Tax (18%)

Final total (subtotal + tax)

Soft delete: cancelled orders remain in the database but marked inactive.

Only active products can be added to sales orders.

3. Workflow

Account Creation
Admin creates a new business account with auto-generated account number.

Contact Management
Contacts are added and linked to accounts.

Product Management
Admin adds products with auto-generated codes. Soft delete ensures inactive products cannot be used in orders.

Sales Order Creation
Users create sales orders by adding products.
Totals, tax, and final amounts are automatically calculated.

Soft Delete Handling
Inactive accounts, products, contacts, or orders remain in the database but are ignored in operations.

Search & Pagination
Large datasets (accounts, products, orders) can be queried efficiently with search and pagination.

4. Technical Stack
Technology	Description
Java 17+	Backend programming language
Spring Boot	Framework for REST APIs and backend logic
Spring Data JPA	ORM for database interaction
MySQL	Relational database
Spring Security + JWT	Authentication and authorization
Lombok	Reduces boilerplate code
Maven	Dependency management and build tool
5. Features

CRUD operations for accounts, contacts, products, and sales orders.

Auto-generated identifiers for accounts, products, and orders.

Soft deletion to maintain historical data.

Tax calculation for sales orders (18%).

Search and pagination for large datasets.

Secure REST APIs with JWT-based authentication.

6. API Endpoints (Overview)

/accounts – CRUD and search for accounts

/contacts – CRUD for contacts linked to accounts

/products – CRUD and soft delete for products

/sales-orders – CRUD, soft delete, and calculation of totals for sales orders

Email notifications for order confirmations.

Integration with front-end UI.
