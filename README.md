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

## Functional Requirements Step 2-1 – Validation & Exception Handling

When a product is created or updated, the client may send invalid data.  
In such cases, the API must respond with a clear and structured error message to help the client understand what went wrong.

###  Validation Rules

- **Product Name**
  - Maximum length: 15 characters (including spaces)
  - Only these special characters are allowed: `()`, `[]`, `+`, `-`, `&`, `/`, `_`
  - Must be unique across all products

- **Product Price**
  - Must be greater than 0

- **Product Image URL**
  - Must start with `http://` or `https://`

###  Error Handling
- If validation fails, the API should return a `400 Bad Request` with a meaningful error message.
- Responses must be in JSON format, containing information about the invalid fields and reasons.

## Features Step 2-1

- [x] Create a `ProductRequest` DTO class to receive and validate incoming product data
- [x] Annotate fields in the DTO using `jakarta.validation.constraints` (e.g., `@Size`, `@Pattern`, `@Min`)
- [x] Replace usage of the `Product` class in `@RequestBody` with `ProductRequest` in both `ProductRestController` and `ProductAdminController`
- [x] Use `@Valid` in controller methods to trigger validation automatically
- [x] Add a global exception handler using `@RestControllerAdvice` to return readable error messages when validation fails
- [x] Ensure that the name is checked for uniqueness before creating/updating products

## Functional Requirements Step 2-2 – Authentication & Authorization

Implement token-based authentication using JSON Web Tokens (JWT).
Users can register and log in with their email and password. Upon success, the server issues a token that will be used for future authenticated requests.

### Login and Registration Flow

- A new user can register by sending their email and password to the /api/members/register endpoint.

- An existing user can log in using the /api/members/login endpoint.

- If authentication is successful, the server responds with a JWT token.

- The token is included in the Authorization header of future requests in the format:
Authorization: Bearer <token>

## Features Step 2-2

- [ ] Create Member entity class to represent user accounts

- [ ] Implement MemberRequest and TokenResponse DTOs for request/response handling

- [ ] Add MemberRepository to manage member data

- [ ] Use BCryptPasswordEncoder to hash user passwords before saving

- [ ] Implement MemberService with:

- [ ] register() method to create a new user and issue a JWT

- [ ] login() method to authenticate credentials and issue a JWT

- [ ] Create JWTProvider utility to generate and validate tokens

- [ ] Implement MemberController with:

- [ ] POST /api/members/register endpoint

- [ ] POST /api/members/login endpoint

- [ ] Return appropriate HTTP status codes:

   - 201 Created on successful registration

   - 200 OK on successful login

   - 403 Forbidden for invalid credentials

   - 401 Unauthorized for missing or invalid token

- [ ] Extend `JWTProvider` with a `getSubject(token: String)` method to extract member ID from token
- [ ] Implement `findByToken(token: String)` in `MemberService` to return the authenticated member  

