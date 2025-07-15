# spring-ecommerce-product

## CRUD Operations for `Product`
### Model
- [x] Product
  - [x] id: long?
  - [x] name: String
  - [x] price: Double
  - [x] imageUrl: String

### Controller
- [ ] Create
  - [ ] Create and returns the new Product
- [ ] Read
  - [ ] Returns the Product
  - [ ] Throws NotFoundException if Product not found via id 
- [ ] Update
  - [ ] Returns the Updated Product
  - [ ] Returns the Product
  - [ ] Throws NotFoundException if Product not found
- [ ] Delete
  - [ ] Returns ok status if deleted
  - [ ] Throws NotFoundException if Product not found

### Exceptions
- [ ] NotFoundException
  - [ ] Handled using ControllerAdvice