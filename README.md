# spring-ecommerce-product

## CRUD Operations for `Product`
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
  - [x] Returns empty array if no products
- [x] Read
  - [x] Returns the Product
  - [x] Throws NotFoundException if Product not found via id 
- [x] Update
  - [x] Returns the Updated Product
  - [x] Returns the Product
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