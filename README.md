# spring-ecommerce-product

## Functional Requirements

Implement a simple HTTP API that allows users to retrieve, add, update, and delete products.

- HTTP requests and responses must be in JSON format.
- Since no separate database is used at this point, store data in memory using an appropriate Kotlin Collection
  Framework.

Implement the API to send and receive HTTP messages as shown in the example.

## Features List

Product

- [ ] is a class
- [ ] has property `id: Long`
    - [ ] get `id` from controller with `AtomicLong(1)`
- [ ] has property `name: String`
- [ ] has property `price: Double`
- [ ] has property `imageUrl: String`

ProductController

- HTTP Methods
- [ ] add - POST
    - [ ] `createProduct(@RequestBody)`
        - [ ] Request -> POST /api/products HTTP/1.1
- [ ] retrieve - GET
    - [ ] `readProducts()`
        - [ ] Request -> GET /api/products HTTP/1.1
    - [ ] `readProduct()`
        - [ ] Request -> GET /api/products/{id} HTTP/1.1
- [ ] update - PUT
    - [ ] `updateProduct(@PathVariable id: Long, @RequestBody)`
        - [ ] Request -> PUT /api/products/{id} HTTP/1.1
- [ ] delete - DELETE
    - [ ] `deleteProduct(@PathVariable id: Long)`
        - [ ] Request -> DELETE /api/products/{id} HTTP/1.1
