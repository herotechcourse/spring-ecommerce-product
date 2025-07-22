# spring-ecommerce-product

## Functional Requirements Step 1-1

Implement a simple HTTP API that allows users to retrieve, add, update, and delete products.
- HTTP requests and responses must be in JSON format.
- Since no separate database is used at this point, store data in memory using an appropriate Kotlin Collection Framework.

Implement the API to send and receive HTTP messages as shown in the example below.

### Request
```aiignore
GET /api/products HTTP/1.1
Response
HTTP/1.1 200
Content-Type: application/json

[
    {
        "id": 8146027,
        "name": "Iced Americano T",
        "price": 4.50,
        "imageUrl": "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg"
    }
]
```

## Features Step1-1

- [x] Implement Product entity class
- [x] implement the rest controller
  - [x] @GetMapping endpoint to retrieve the Product
    `/api/products HTTP/1.1`
  - [x] @PostMapping to add a product
  - [x] @PutMapping to update
  - [x] @DeleteMapping to delete
- [x] store data in memory using a suitable collection `private val products: MutableMap<Long, Product> = HashMap()`

## Functional Requirements Step 1-2
- Implement an admin interface that allows users to view, add, update, and delete products.
- Use Thymeleaf to implement server-side rendering (SSR).
The default behavior should be based on traditional HTML form submission and page navigation.
- For product images, do not upload files; instead, use direct image URLs.

## Features Step1-2
- [x] Use Thymeleaf to implement server-side rendering (SSR)
- [x] Create the template table.html
- [x] Connect controller with table.html


## Functional Requirements step 1-3
Store product information in a database instead of keeping it in memory using Kotlin collections.
Database tables must be initialized automatically when the application starts.

## Features step 1-3
- [x] use an H2 in-memory database instead of Kotlin collections.
  - [x] Add the required Gradle dependencies 
  - [x] Define the database schema 
  - [x] Configure the database settings
  - [x] Set up application.properties to enable H2-Console
- [x] use Spring's JdbcTemplate
- [x] Use SQL scripts for table creation and initial data.
- [x] Create a Service Layer to encapsulate business logic:
  - Define a ProductService interface with methods like findById, createProduct, updateProduct, and getAll, delete. 
  - Provide a concrete implementation ProductServiceImpl that delegates to the ProductRepository.
- [x] Implement centralized exception handling with a @RestControllerAdvice:
  - Handle ProductNotFoundException with a 404 Not Found response.
  - Handle ProductCreationException and ProductUpdateException with 500 Internal Server Error.
  - Catch generic RuntimeException and return a 400 Bad Request.
- [x] Return consistent error responses using a custom ErrorMessageModel

## Functional Requirements step 2-1
When a product is created or updated, the client may send invalid data.
In such cases, your application must respond with enough information for the client to understand what is wrong and why.

### Validation Rules:
- Product Name
  - Must be no more than 15 characters, including spaces.
  - Allowed special characters: ( ), [ ], +, -, &, /, _
    - All other special characters are not allowed.
  - The name must be unique across all products.
- Product Price
  - Must be greater than 0.
- Product Image URL
  - Must start with http:// or https://.

## Features Step 2-1
- [ ] Add Spring Validation dependency:
 ```
implementation("org.springframework.boot:spring-boot-starter-validation")
```
- [ ] Add validation annotations in ProductRequest:
  - @NotBlank for name
  - @Size(max = 15)
  - @Pattern for allowed characters 
  - @Positive for price 
  - @Pattern for image URL 
- [ ] Use @Valid in controller endpoints 
- [ ] Check uniqueness of product name in service layer 
- [ ] Extend @RestControllerAdvice to handle validation errors 
- [ ] Return 400 Bad Request with structured error message

