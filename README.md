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
- [x] Read
  - [x] Returns the Product
  - [x] Throws NotFoundException if Product not found via id 
- [ ] Update
  - [ ] Returns the Updated Product
  - [ ] Returns the Product
  - [ ] Throws NotFoundException if Product not found
- [ ] Delete
  - [ ] Returns ok status if deleted
  - [ ] Throws NotFoundException if Product not found
- private fun findProduct
  - [x] finds the product
  - [x] Throws notFoundException

### Exceptions
- [x] NotFoundException
  - [x] Handled using ControllerAdvice