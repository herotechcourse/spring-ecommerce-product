# spring-ecommerce-product

## Features List

### Model

#### Product

- [x] is a class
- [x] has property `id: Long`
- [x] has property `name: String`
- [x] has property `price: Double`
- [x] has property `imageUrl: String`

#### Member

- [x] is a class
- [x] has property `id: Long?`
- [x] has property `email: String`
- [x] has property `password: String`
- companion object
  - [x] `toEntity()`
  - [x] `from(loginForm: LoginForm)`
  - [x] `from(registerForm: RegisterForm)`

### DTO (Data Transfer Object)

#### ProductForm

- [x] is a data class, as a DTO to validate form from request
- [x] has property `name: String`
    - [x] validate 'NotBlank'
    - [x] validate 'Pattern' for regex
    - [x] validate 'Size'
- [x] has property `price: Double`
    - [x] validate 'Positive'
- [x] has property `imageUrl: String`
    - [x] validate 'NotBlank'
    - [x] validate 'Pattern' for regex

#### RegisterForm

- [x] is a data class, as a DTO to validate form from member registration request
- [x] has property `email: String`
    - [x] validate 'NotBlank'
    - [x] validate 'Email'
- [x] has property `password: String`
    - [x] validate 'Size'

#### LoginForm

- [x] is a data class, as a DTO to validate form from member login request
- [x] has property `email: String`
    - [x] validate 'NotBlank'
- [x] has property `password: String`
    - [x] validate 'NotBlank'

#### AuthResponse

- [ ] is a data class, as a DTO to send access token as response
- [ ] has property `accessToken: String`

### DAO (Data Access Object)

#### interface ProductDAO

- [x] `findAll()` - query database to get all products
- [x] `findById()` - query database for an object
- [x] `insert()` - query database to create new product
- [x] `update()` - query database to update a product
- [x] `delete()` - query database to delete a product
- [x] `existsByName` - query database to get count of product with the name

#### JdbcProductDAO : ProductDAO

#### interface MemberADO

- [ ] `insert()` - query database to create new member
- [ ] `findByEmail()` - query database for an object by email
- [ ] `findById()` - query database for an object by id
- [ ] `existsByEmail()` - query database to get count of member with the email

#### JdbcMemberDAO: MemberDAO

### Service

#### AuthService

- [ ] `register()`
- [ ] `login()`
- [ ] `findMemberById()`
- [ ] `findMemberByToken()`
- [ ] `findMemberByEmail()`
- [ ] `checkMemberEmailExists()`

#### ProductService

- [x] is a service layer between `ProductController` and `JdbcProductDAO`
- [x] `insert()` takes 'ProductForm' which is validated with `jakarta` from controller, and validate the product name
  exists by name or not from DAO, then insert the product if it is valid

### Controller

#### AuthController

- HTTP Methods
- [ ] `registerMember(@RequestBody)`
    ```
    POST /api/members/register HTTP/1.1
    Content-Type: application/json
    host: localhost:8080
        
    {
        "email": "admin@email.com",
        "password": "password"
    }
    ```
- [ ] `loginMember(@RequestBody)`
    ```
    POST /api/members/login HTTP/1.1
    Content-Type: application/json
    host: localhost:8080
    
    {
        "email": "admin@email.com",
        "password": "password"
    }
    ```

#### ProductController

- HTTP Methods
- [x] add - POST
    - [x] `createProduct(@RequestBody @Valid)`
        - [x] Request -> POST /api/products HTTP/1.1
- [x] retrieve - GET
    - [x] `readProducts()`
        - [x] Request -> GET /api/products HTTP/1.1
    - [x] `readProduct()`
        - [x] Request -> GET /api/products/{id} HTTP/1.1
- [x] update - PUT
    - [x] `updateProduct(@PathVariable id: Long, @RequestBody @Valid)`
        - [x] Request -> PUT /api/products/{id} HTTP/1.1
- [x] delete - DELETE
    - [x] `deleteProduct(@PathVariable id: Long)`
        - [x] Request -> DELETE /api/products/{id} HTTP/1.1
- [x] handleProductNameAlreadyExistsException

#### ProductViewController

- [x] `displayMain()` shows the main page
    - currently, it redirects to `/products`
- [x] `displayProducts()` shows list of products with view of products.html
- [x] `displayCreateProductForm()` shows page for create product form with view of createProductForm.html
- [x] `displayUpdateProductForm()` shows page for update product form with view of updateProductForm.html

### View

#### products.html

- [x] use table to show list of product
- [x] add JS code to send DELETE request to Product api
- [x] make the delete button color in RED!!!!
- [x] add JS code to request GET request to Product api to receive all products data

#### createProductForm.html

- [x] use form to submit data to backend
- [x] add JS code to send POST request to Product api

#### updateProductForm.html

- [x] take product with id
- [x] use form to submit data to backend
- [x] add JS code to send PUT request to Product api
- [x] add JS code to request GET request to Product api to receive an updating product data

### Exception

#### GlobalExceptionHandler

- [x] `@ControllerAdvice`
- [x] has `handleNotFoundException()`
- [x] has `handleInternalServerErrorException()`
- [x] has `handleDataAccessException()`
- [x] has `handlerIllegalStateException()`
- [x] has `handleValidationException()`

- [x] NotFoundException

- [x] InternalServerErrorException

- [x] ProductNameAlreadyExistsException

- [ ] MemberEmailAlreadyExistsException

- [ ] AuthorizationException

### Auth

#### JwtTokenProvider

- [ ] is a `@Component` to handle JWT
- [ ] has a property `secretKey: String`
- [ ] has a property `validityInMilliseconds: Long`
- [ ] has a method `createToken(): String` to generate and return an access token
- [ ] has a method `getPayLoad(): String` to decrypt token into payload
- [ ] has a method `validateToken(): Boolean` to decrypt and validate token

### etc.

- [x] connect to an H2 in-memory database
    - [x] define the product schema
    - [x] add some sample data
    - [x] configure the database setting with `application.properties`

## Functional Requirements

### Step 2.2

Implement user account features including registration, login, and authentication so that users can access member-only
functionality in the future.

- A member registers with an **email** and **password**.
- To receive an access token, the client must send email and password.
    - If the credentials match a registered user, issue a token.

Implement the API to send and receive HTTP messages as shown below.

### Step 2.1

When a product is created or updated, the client may send invalid data.
In such cases, your application must respond with enough information for the client to understand **what is wrong and
why**.

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
