# spring-ecommerce-product

## CRUD Operations for `Product`
### Model
- [ ] Product
  - [ ] id: long?
  - [ ] name: String
  - [ ] price: Double
  - [ ] imageUrl: String

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