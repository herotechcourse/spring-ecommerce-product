# spring-ecommerce-product

## Functional Requirements

Implement a simple HTTP API that allows users to retrieve, add, update, and delete products.

- HTTP requests and responses must be in JSON format.
- Since no separate database is used at this point, store data in memory using an appropriate Kotlin Collection
  Framework.

Implement the API to send and receive HTTP messages as shown in the example.

## Features List

### Domain

Product

- [x] is a class
- [x] has property `id: Long`
    - [x] get `id` from controller with `AtomicLong(1)`
- [x] has property `name: String`
- [x] has property `price: Double`
- [x] has property `imageUrl: String`

### Controller

ProductController

- HTTP Methods
- [x] add - POST
    - [x] `createProduct(@RequestBody)`
        - [x] Request -> POST /api/products HTTP/1.1
- [x] retrieve - GET
    - [x] `readProducts()`
        - [x] Request -> GET /api/products HTTP/1.1
    - [x] `readProduct()`
        - [x] Request -> GET /api/products/{id} HTTP/1.1
- [x] update - PUT
    - [x] `updateProduct(@PathVariable id: Long, @RequestBody)`
        - [x] Request -> PUT /api/products/{id} HTTP/1.1
- [x] delete - DELETE
    - [x] `deleteProduct(@PathVariable id: Long)`
        - [x] Request -> DELETE /api/products/{id} HTTP/1.1

ProductViewController

- [x] `displayProducts()` shows list of products with view of products.html
- [x] `displayCreateProductForm()` shows page for create product form with view of createProductForm.html
- [x] `displayUpdateProductForm()` shows page for update product form with view of updateProductForm.html

### Service

ProductService

- [x] `findAll()` - query to database to get all products
- [x] `findById()` - query for an object to database
- [x] `insert()` - query to database to create new product
- [x] `update()` - query to database to update a product
- [x] `delete()` - query to database to delete a product

### View

products.html

- [x] use table to show list of product
- [x] add JS code to send DELETE request to Product api
- [x] make the delete button color in RED!!!!
- [x] add JS code to request GET request to Product api to receive all products data

createProductForm.html

- [x] use form to submit data to backend
- [x] add JS code to send POST request to Product api

updateProductForm.html

- [x] take product with id
- [x] use form to submit data to backend
- [x] add JS code to send PUT request to Product api
- [x] add JS code to request GET request to Product api to receive an updating product data

### etc.

- [x] connect to an H2 in-memory database
  - [x] define the product schema
  - [x] add some sample data
  - [x] configure the database setting with `application.properties`

## Features 2.1

- [ ] Product name Validation
  - [ ] Apart from alphanumerics character, it can contain  `( ), [ ], +, -, &, /, _`
  - [ ] I should contain less than 15 characters
  - [ ] Name should be unique
- [ ] Price should be a positive number
- [ ] Image URL must start with `http:// or https://`.
