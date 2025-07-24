# spring-ecommerce-product

## Features:
HTTP API that allows users to retrieve, add, update, and delete products.
- HTTP requests and responses must be in JSON format.
- Since no separate database is used at this point, store data in memory using an appropriate Kotlin Collection Framework.

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

### Test: Product Controller
- [x] refactor test names
- [x] positive tests
- [x] negative tests