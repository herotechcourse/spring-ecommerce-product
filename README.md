# spring-ecommerce-product

## CRUD Operations for `Product` -> `Step 1-1`
### Model
- [x] Product
  - [x] id: long?
  - [x] name: String
  - [x] price: Double
  - [x] imageUrl: String

### Controller
- [x] Create
  - [x] Create and returns the new Product
- [x] Read All
  - [x] Returns the products
- [x] Update
  - [x] Returns the Updated Product
  - [x] Throws NotFoundException if Product not found
- [x] Delete
  - [x] Returns ok status if deleted
  - [x] Throws NotFoundException if Product not found
- private fun findProduct
  - [x] finds the product
  - [x] Throws notFoundException

### Exceptions
- [x] NotFoundException
  - [x] Handled using ControllerAdvice

## Admin Interface Implementation for `Product` -> `Step 1-2`

### Views
- [x] View all products
 - [x] added new page for `products.html`
 - [x] Changed Read All for ThymeLeaf returns the page as String
- [x] Add a new product
  - [x] `products/new` method for showing the new product form
  - [x] create request handled by JS
- [x] Update a product
  - [x] `products/edit/${id}` method for showing the product form to edit
  - [x] update request handled by JS
- [x] Delete a product
  - [x] Send delete request using JS
- [x] Template for styling Footer and Header

## Integrate DB in Project `Product` -> `Step 1-3`
- [x] install db dependency
- [x] create schema.sql
- [x] create data.sql
- [x] add rules in application.properties
- [x] create table
- [x] Convert Controller for db usage
- [x] Create Product Repository to handle DB logic
- [ ] Implement Services to move business logic from Controller