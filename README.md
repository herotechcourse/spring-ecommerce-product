# spring-ecommerce-product

## Functional Requirements

### Step 2.1

When a product is created or updated, the client may send invalid data.
In such cases, your application must respond with enough information for the client to understand **what is wrong and why**.

**Validation Rules:**
- **Product Name** 
  - Must be no more than **15 characters**, including spaces. 
  - Allowed special characters: `( )`, `[ ]`, `+`, `-`, `&`, `/`, `_`
    - All other special characters are not allowed.
  - The name must be **unique** across all products.
- **Product Price** 
  - Must be greater than 0.
- **Product Image URL** 
  - Must start with `http://` or `https://`.


### Step 1

Implement a simple HTTP API that allows users to retrieve, add, update, and delete products.

- HTTP requests and responses must be in JSON format.
- Since no separate database is used at this point, store data in memory using an appropriate Kotlin Collection
  Framework.

Implement the API to send and receive HTTP messages as shown in the example.

## Features List

### Model

Product

- [x] is a class
- [x] has property `id: Long`
    - [x] get `id` from controller with `AtomicLong(1)`
- [x] has property `name: String`
- [x] has property `price: Double`
- [x] has property `imageUrl: String`

### DAO (Data Access Object)

ProductRepository

- [x] `findAll()` - query to database to get all products
- [x] `findById()` - query for an object to database
- [x] `insert()` - query to database to create new product
- [x] `update()` - query to database to update a product
- [x] `delete()` - query to database to delete a product


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

- [x] `displayMain()` shows the main page
    - currently, it redirects to `/products`
- [x] `displayProducts()` shows list of products with view of products.html
- [x] `displayCreateProductForm()` shows page for create product form with view of createProductForm.html
- [x] `displayUpdateProductForm()` shows page for update product form with view of updateProductForm.html

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