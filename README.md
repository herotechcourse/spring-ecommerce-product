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

#### Member
- [ ] create a member model
- [ ] create a member controller
  - [ ] create a register method to save users as members POST
  - [ ] create a GET method to retrieve member
  - [ ] create a PATCH method to update user password
  - [ ] create a Delete method to delete member
- [ ] create a login method
- [ ] implement the Authentication and Authorization methods
- [ ] implement response to authentication

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
