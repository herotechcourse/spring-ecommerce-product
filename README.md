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

#### CartItem

- [x] is a class
- [x] has property `id: Long?`
- [x] has property `memberId: Long`
- [x] has property `productId: Long`
- [x] has property `quantity: Int`

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

- [x] is a data class, as a DTO to send access token as response
- [x] has property `accessToken: String`

#### CartForm

- [x] is a data class, as a DTO to validate form for add and update item in cart
- [x] has a property `productId: Long`
  - [x] validate 'Min'
- [x] has a property `quantity: Int`
  - [x] validate 'Min'

### DAO (Data Access Object)

#### Interface CartDAO

- [x] `addItemToCart(memberId, productId, quantity)`
- [x] `getCartItemsByMemberId(memberId)`
- [x] `removeItemFromCart(memberId, productId)`
- [x] `updateItemQuantityInCart(memberId, productId, quantity)`

#### JdbcCartDAO : CartDAO

#### interface MemberADO

- [x] `insert()` - query database to create new member
- [x] `findByEmail()` - query database for an object by email
- [x] `findById()` - query database for an object by id
- [x] `existsByEmail()` - query database to get count of member with the email

#### JdbcMemberDAO : MemberDAO

#### interface ProductDAO

- [x] `findAll()` - query database to get all products
- [x] `findById()` - query database for an object
- [x] `insert()` - query database to create new product
- [x] `update()` - query database to update a product
- [x] `delete()` - query database to delete a product
- [x] `existsByName` - query database to get count of product with the name

#### JdbcProductDAO : ProductDAO

### Service

#### CartService

- [ ] `getCart()`
- [ ] `addToCart()`
- [ ] `removeFromCart()`
- [ ] `updateQuantity()`

#### AuthService

- [x] `register()`
- [x] `login()`
- [x] `findMemberById()`
- [x] `findMemberByToken()`
- [x] `findMemberByEmail()`
- [x] `checkMemberEmailExists()`

#### ProductService

- [x] is a service layer between `ProductController` and `JdbcProductDAO`
- [x] `insert()` takes 'ProductForm' which is validated with `jakarta` from controller, and validate the product name
  exists by name or not from DAO, then insert the product if it is valid

### Controller

#### CartController

- HTTP Methods
- [x] GET "/api/cart" - `viewCart()`
- [x] POST "/api/cart" - `addToCart(@RequestBody)`
- [x] DELETE "/api/cart/{id}" - `removeFromCart(@PathVariable)`
- [x] PUT "/api/cart/{id}" - `updateQuantity(@PathVariable, @RequestBody)`

#### AuthController

- HTTP Methods
- [x] `registerMember(@RequestBody)`
    ```
    POST /api/members/register HTTP/1.1
    Content-Type: application/json
    host: localhost:8080
        
    {
        "email": "admin@email.com",
        "password": "password"
    }
    ```
- [x] `loginMember(@RequestBody)`
    ```
    POST /api/members/login HTTP/1.1
    Content-Type: application/json
    host: localhost:8080
    
    {
        "email": "admin@email.com",
        "password": "password"
    }
    ```
- [x] `findMyInformation()`

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
- [x] has `handleAuthorizationException()`

- [x] NotFoundException

- [x] InternalServerErrorException

- [x] ProductNameAlreadyExistsException

- [x] MemberEmailAlreadyExistsException

- [x] AuthorizationException

### Auth

#### JwtTokenProvider

- [x] is a `@Component` to handle JWT
- [x] has a property `secret: String`
- [x] has a property `validityInMilliseconds: Long`
- [x] has a property `secretKey: SecretKey`
- [x] has a method `createToken(): String` to generate and return an access token
- [x] has a method `getPayLoad(): String` to decrypt token into payload
- [x] has a method `validateToken(): Boolean` to decrypt and validate token

### UI

#### LoginMemberArgumentResolver

- [x] is an `HandleMethodArgumentResolver` as `@Component` to handle `@LoginMember` annotation
- [x] override `supportsParameter()` to add annotation for `LoginMember`
- [x] override `resolveArgument()` to handle access token and return the member

#### LoginMember

- [x] is an annotation class for authorization of member, using argument resolver 

### Config

#### WebMvcConfiguration

- [x] is an `WebMvcConfigurer`
- [x] has private property as input `LoginMemberArgumentResolver`
- [x] override `addArgumentResolvers()` to add `LoginMemberArgumentResolver` component

### etc.

- [x] connect to an H2 in-memory database
    - [x] define the product schema
    - [x] add some sample data
    - [x] configure the database setting with `application.properties`

## Functional Requirements

### Step 2.3 - Cart

Using the token received after login, implement functionality that allows the user to add products to their own cart.

- Users can retrieve the list of products in their cart.
- Users can add products to their cart.
- Users can remove products from their cart.

Use the `Authorization` header to pass user credentials:

```http request
Authorization: Bearer <token>
```

### Step 2.2 - User Login

Implement user account features including registration, login, and authentication so that users can access member-only
functionality in the future.

- A member registers with an **email** and **password**.
- To receive an access token, the client must send email and password.
    - If the credentials match a registered user, issue a token.

Implement the API to send and receive HTTP messages as shown below.

### Step 2.1 - Validation & Handling Exception

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
