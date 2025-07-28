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

- [x] Product name Validation
    - [x] Apart from alphanumerics character, it can contain  `( ), [ ], +, -, &, /, _`
    - [x] I should contain less than 15 characters
    - [x] Name should be unique
- [x] Price should be a positive number
- [x] Image URL must start with `http:// or https://`.

## Features 2.2

- [x] User account
    - [x] Registration with email and password
    - [x] Assign access token using JWT

## Features 2.3

- [x] Cart
    - [x] Create Cart
    - [x] Connect Cart to corresponding User
    - [x] Users can retrieve the list of products in their cart.
    - [x] Users can add products to their cart.
    - [x] Users can remove products from their cart.
- [x] (extra) Create Products and Cart View

## Features 2.4

- [x] Retrieve the Top 5 Most Added Products to Cart in the Last 30 Days.
    - [x] If multiple products have same count, the most recent added comes first.
    - [x] Response must include the product name, the number of times it was added, and the most recent added time.

- [x] Retrieve Members Who Added Items to Their Cart in the Last 7 Days.
    - [x] Each member should appear only once.
    - [x] The response must include the member ID, name, and email.
