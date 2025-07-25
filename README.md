# spring-ecommerce-product

## Features:
HTTP API that allows users to retrieve, add, update, and delete products.
- HTTP requests and responses must be in JSON format.
- since no separate database is used at this point, store data in memory using an appropriate Kotlin Collection Framework.
- users should have a registration and login request to use the application

### Product
- [x] create product with id, name, price, image URL

### Product Controller
- [x] implement `create`
- [x] implement `read`
  - if a product map is empty, return noContent()
- [x] implement `update`
  - if a product map is empty, return notFound()
- [x] implement `delete`
  - if a product map is empty, return notFound()
  - if cannot find a product with id, return notFound()

### Product Service
- [x] create a product service class to validate requirements of product name, price and image URL
- [x] connect validation to the methods responsible for creating and updating functions

### Authentication and Authorization 
- [ ] create a register method to save users as members
- [ ] create a login method to allow users to save their sessions
- [ ] implement the Authentication and Authorization methods

### Test: Product Controller
- [x] test CRUD methods
- [x] positive tests
- [x] negative tests

### Test: Product Repository
- [x] test main functions for users to interact with DB
- [x] test updateById, deleteById, count list of products and findById

### Test: Authentication and Authorization
- [ ] test if user was able to register
- [ ] test if user has authorization to login
