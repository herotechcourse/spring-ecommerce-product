# spring-ecommerce-product

# STEP 1-1

## Functional Requirements

Implement a simple HTTP API that allows users to retrieve, add, update, and delete products.

- HTTP requests and responses must be in JSON format.
- Since no separate database is used at this point, store data in memory using an appropriate Kotlin Collection
  Framework.

Implement the API to send and receive HTTP messages as shown in the example below.

### Request

```kotlin
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

## Features

- [x] Implement Product entity class
- [x] implement the rest controller
    - [x] @GetMapping endpoint to retrieve the Product
      `/api/products HTTP/1.1`
    - [x] @PostMapping to add a product
    - [x] @PutMapping to update
    - [x] @DeleteMapping to delete
- [x] store data in memory using a suitable collection `private val products: MutableMap<Long, Product> = HashMap()`

# STEP 1-2

## Functional Requirements

- Implement an admin interface that allows users to view, add, update, and delete products.
- Use Thymeleaf to implement server-side rendering (SSR).
  The default behavior should be based on traditional HTML form submission and page navigation.
- For product images, do not upload files; instead, use direct image URLs.

## Features

- [x] Use Thymeleaf to implement server-side rendering (SSR)
- [x] Create the template table.html
- [x] Connect controller with table.html

# STEP 1-3

## Functional Requirements

Store product information in a database instead of keeping it in memory using Kotlin collections.
Database tables must be initialized automatically when the application starts.

## Features

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

# Step 2-1

## Functional Requirements

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

## Features

- [x] Add Spring Validation dependency:
   ```kotlin
  implementation("org.springframework.boot:spring-boot-starter-validation")
  ```
- [x] Add validation annotations in ProductRequest:
    - @NotBlank for name
    - @Size(max = 15)
    - @Pattern for allowed characters
    - @Positive for price
    - @Pattern for image URL
- [x] Use @Valid in controller endpoints
- [x] Check uniqueness of product name in service layer
- [x] Extend @RestControllerAdvice to handle validation errors
- [x] Return 400 Bad Request with structured error message

# Step 2-2 – Member Authentication API

This step introduces authentication using email/password login and JWT tokens to allow access to member-only
functionality in the future.

## Requirements

### Register

Request

```kotlin
POST / api / members / register HTTP /1.1
Content - Type: application/json
host: localhost:8080

{
    "email": "admin@email.com",
    "password": "password"
}
```

Response

```kotlin
HTTP / 1.1 201
Content - Type: application/json

{
    "token": ""
}
```

### Login
Request

```kotlin
POST / api / members / login HTTP /1.1
Content - Type: application/json
host: localhost:8080

{
    "email": "admin@email.com",
    "password": "password"
}
```

Response

```kotlin
HTTP / 1.1 200
Content - Type: application/json

{
    "token": ""
}

```

## Features

- [x] Implement member registration with a valid email and password.
- [x] Allow members to log in by submitting their credentials.
- [x] If login is successful, the API returns a valid JWT token.
- [x] Return 403 Forbidden for incorrect login attempts or denied actions (e.g., password reset or change with invalid
  input).
- [x] Use the JJWT library to generate tokens.
  ```
  implementation("io.jsonwebtoken:jjwt:0.9.1")
  ```
# Step 2-3
This step implements cart-related features for authenticated users, allowing each user to manage their own shopping cart.

## Features

- [ ] **Create and assign cart to user (if not exists)**
  When a user adds a product to their cart for the first time, a new cart is created and linked to that user.

- [ ] **Add product to cart**
  `POST /api/wishes`
  Adds a product to the authenticated user's cart.
  Requires JWT token via `Authorization: Bearer <token>`.
  Payload:

  ```json
  {
    "productId": 1
  }
  ```

- [ ] **Get products in user's cart**
  `GET /api/wishes`
  Retrieves all products currently in the authenticated user's cart.

- [ ] **Remove product from cart**
  `DELETE /api/wishes/{productId}`
  Removes the specified product from the authenticated user's cart.

###  Authentication & Member Injection

All endpoints require the user to be authenticated.
A custom `@LoginMember` argument resolver is used to inject the authenticated `Member` into controller methods, based on the JWT token.



