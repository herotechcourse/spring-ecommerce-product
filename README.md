# spring-ecommerce-product

## Step 1-1

Implements a simple HTTP API that allows users to **retrieve**, **add**, **update**, and **delete** products.<br/>
HTTP requests and responses must be in **JSON** format. <br/>
Since no separate database is used at this point, data is stored in memory using an appropriate Kotlin Collection
Framework. <br/>

### Feature list - Step 1-1

- [x] resource representation class to store data
    - [x] is wrapped in a collection
- [x] HTTP requests and responses in JSON format
- [x] retrieve product - GET
- [x] add new product - POST
- [x] update product parameters - PUT
- [x] delete product from products - DELETE

## Step 1-2

Implements an admin interface that allows users to view, add, update, and delete products. <br/>

Use Thymeleaf to implement server-side rendering (SSR). <br/>
The default behavior should be based on traditional HTML form submission and page navigation. <br/>
However, if you're interested in asynchronous behavior using JavaScript, feel free to use the previously built product
API to apply AJAX or similar techniques.<br/>
For product images, do not upload files; instead, use direct image URLs. <br/>

### Feature list - Step 1-2

- [x] admin interface
    - [x] uses Thymeleaf
    - [x] based on HTML form submission and page navigation
    - [x] use direct image URLs for products

## Step 1-3

Refactor the application using an H2 in-memory database instead of Kotlin collections.
The required database tables are initialized automatically when the application starts.

### Feature list - Step 1-3

- [x] connect to a database
    - [x] Add the required Gradle dependencies
    - [x] Define the database schema
    - [x] Configure the database settings
    - [x] use Spring's JdbcTemplate

## Step 2-1 
Implement validation of user input.

### Feature List - Step 2-1
- Product Name
  - [x] Must be no more than 15 characters, including spaces. 
  - [x] Allowed special characters: ( ), [ ], +, -, &, /, _
  - [x] All other special characters are not allowed.
  - [x] The name must be unique across all products.
- Product Price
  - [x] Must be greater than 0.
- Product Image URL
  - [x] Must start with http:// or https://.

## Step 2-2
Implement user account features including registration, login, and authentication 
so that users can access member-only functionality in the future.

### Feature List - Step 2-2
- Member
- [x] create `Member` model with:
  - [x] id
  - [x] password
  - [x] email
  - [ ] maybe username?
  - [x] role
- [x] validate these attributes with Jakarta
- [x] catch exceptions on invalid data
- [x] define a database table in `schema.sql`
- [x] create a `MemberRepository`

- Authentication
- [x] A member registers with an email and password.
- [x] To receive an access token, the client must send email and password.
- [x] If the credentials match a registered user, issue a token.
  - [x] JWT token
- [x] Return 401 Unauthorized if the Authorization header is missing or the token is invalid.
- [x] Return 403 Forbidden for incorrect login attempts or denied actions (e.g., password reset or change with invalid input).
- [x] Let user use token to retrieve information.
- [x] Implement the API to send and receive HTTP messages as shown below:
1. **Register**
- [x] Request
```http
POST /api/members/register HTTP/1.1
Content-Type: application/json
host: localhost:8080

{
"email": "admin@email.com",
"password": "password"
}
```
- [x] Response
```http
HTTP/1.1 201
Content-Type: application/json

{
"token": ""
}
```

2. **Login**
- [x] Request
```http
POST /api/members/login HTTP/1.1
Content-Type: application/json
host: localhost:8080

{
"email": "admin@email.com",
"password": "password"
}
```
- [x] Response
```http
HTTP/1.1 200
Content-Type: application/json

{
"token": ""
}
```

## Step 2.3 - Cart
Implement a functionality that allows users to add or remove items from a cart. Authentication happens with a token.

## Feature list - Step 2-3
### Authentication
- [x] Users use the token received after login
  - [x] Use `Authorization: Bearer <token>` header
- [x] Throw 401 if the token is invalid

### Cart
- [x] Users can retrieve the list of products in their cart
- [x] Users can add products to their cart
- [x] Users can remove products from their cart

- [x] Create a Cart
- [x] Add Cart table to the database
- [x] Map Cart to Products -> many-to-many database table
- [x] Create Repository for cart
- [x] Create Controller for cart

#### Return values
| Action                    | Status Code    | Body                         | Notes                      |
| ------------------------- | -------------- | ---------------------------- | -------------------------- |
| Add new product           | 201 Created    | `ItemDto`                    | Optional `Location` header |
| Increase quantity         | 200 OK         | `ItemDto`                    |                            |
| Remove product completely | 204 No Content | None                         | Lightweight, RESTful       |
| Reduce quantity           | 200 OK         | `ItemDto` (updated quantity) |                            |
| Remove non-existent item  | 404 Not Found  | Error body (optional)        |                            |


## Step 2.4 - Cart Statistics
The administrator should be able to view statistical in fo based on users' carts.

## Featurer list - Step 2.4
- [x] Retrieve the Top 5 Most Added Products to Cart in the Last 30 Days
  - [x] Returns the top 5 products that were added to carts most frequently in the past 30 days.
  - [x] If multiple products have the same count, the one most recently added should be listed first.
  - [x] The response must include the product name, the number of times it was added, and the most recent added time.
- [x] Retrieve Members Who Added Items to Their Cart in the Last 7 Days
    - [x] Returns a list of members who added at least one product to their cart in the past 7 days.
    - [x] Each member should appear only once, even if they added multiple products.
    - [x] The response must include the member ID, name, and email.
- [x] (Optional) Restrict access to those stat API so that only users with the ADMIN role can access it.

## Learnings
### JDBC & DAO
- my `ProductRepository` class is a DAO (Data Access Object): a design pattern for data access abstraction, meaning that
I don't expose how the products are stored and retrieved
- JDBC is a Java API that defines how a client can access a database 
-> I can execute SQL statements, retrieve results, handle database connections

### HashCode & Equals
- the `hashCode()` and `equals()` functions are provided by data classes in Kotlin. Equals checks the equality of 
two objects and hashcode generates a hashcode that can be useful when using collections like HashMap or HashSet.
- In this project we need to override them,
because our products are the same if their `id` is the same, the other attributes can differ
```
For example, if you have

id = 1, name = "green apple"
id = 1, name = "red apple"
these should be considered the same apple — it just changed color over time.

On the other hand

id = 1, name = "green apple"
id = 2, name = "green apple"
represent completely different apples, because their IDs are different.
```

### Exceptions in Controllers
- `@Controller` and `@ControllerAdvice` classes can have 
`@ExceptionHandler` methods to handle exceptions from controller methods, as the following example shows:
```kotlin
@ExceptionHandler(IllegalArgumentException::class)
fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<String> {
return ResponseEntity.badRequest().body(e.message)
}
```
- `@ControllerAdvice` can handle exceptions on a global level:
```kotlin
@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler
    fun handleNotFoundException(e: NotFoundException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
    }
}
```
- we can use `jakarta.validation.constraints.*` to validate user input
- it does not directly throw an exception, but the Spring framework does
- Spring throws:
  - javax.validation.ConstraintViolationException when a constraint is violated on method parameters.
  - org.springframework.web.bind.MethodArgumentNotValidException when validation of request bodies fails.

### MvcConfigurer
- What is better, using `WebMvcConfigurer` or writing a controller directly? Why do you think so?
  - It is a simple routing solution and if we use addViewController inside the MvcConfiguration we do not need a Controller class.
    If we don't need to inject data and use a Model for it, then it is a simple approach. However, if we need
    a model with products, we need to use a @Controller.


## Considerations
- Read and learn SQL commands in H2 console
- What do you return on incorrect password and email? Is 404 bad practice,
since hackers could guess that the email already exists?
- Is it okay to throw this kind of exception inside the extractor and not inside the Service?
Shall all validation happen inside the Service?
```kotlin
fun extract(request: HttpServletRequest): String {
    val headers = request.getHeaders(HEADER)
    if (!headers.hasMoreElements()) {
        throw UnauthorizedException()
    }
}
```
- If the user gets deleted, do I delete the cart?
- Should `fun removeProductFromCart(productId: Long, quantity: Long, cartId: Long)` have instead
of Long types Value Class types like ProductId, or should it take a CartRequestDTO as argument?
- What happens if the product ID gets updated? How does that influence the cart?
- If I show all items in the cart, what should I do if one or more products do not exist anymore? Throw an exception, or just return the products that exist?
-> I use INNER JOIN to ignore non-existent products for now

### Future work
- Statistics still need to be tested with the new interception feature

## Considerations

- TODO read about difference btw RestController and Controller
- TODO why do we combine Controller and ControllerView
- read and learn SQL commands in H2 console

---
## Refactoring List

### Feedback from the reviewer
- [x] declare enum class for ADMIN and USER
- [ ] delegate the logic inside @Admin argument resolver to the `AuthService` 
  - reason: resolver can solely focus on parameter passing, business logic is inside the service
- [ ] do not expose error message when handling `500 Internal Server Error`
  - reason: info can be sensitive and we might not know where it comes from, so the message is just for debugging purposes
- [ ] send back a kind message to the user on `404 Not Found`
- [ ] return values of a repository should align with the aggregate or entity it represents
  - [ ] refactor Cart repository 
- [ ] test password validation
- [ ] return another error in the code below in `AdminArgumentResolver.kt`
  - ```kotlin
    val request =
    webRequest.getNativeRequest(HttpServletRequest::class.java)
    ?: throw UnauthorizedException()
  ```
- [ ] refactor the code below in `ProductController.kt` by creating a custom exception for "duplicate product" and mapping it cleanly to HTTP 409
  - ```kotlin
    if (productRepository.existsByName(product.name)) {
            throw IllegalArgumentException("Product with name ${product.name} already exists")
        }
  ```
- [ ] rename with time unit or append comment to `security.jwt.token.expire-length=3600000`
- [ ] override equals() and hashcode() for Member ??
- [ ] inside `findMemberByToken()`: `if (jwtTokenProvider.isInvalid(token)) { ... }` or throw exception in getPayload method 
- [ ] try to refactor structure according to these principles:
  - [ ] everything related to the HTTP protocol should be managed by the Controller, and anything business-related should be passed to the Service
    - A DTO is simply a "truck" that moves data between layers.
    - A Domain model is a collection of business behaviors (methods), with no concern for how data is stored.
    - An Entity is a representation of data in the database and usually includes an id.
  - e.g.: MemberRegisterRequest(DTO) → RegisterMember(DM) → MemberEntity → RegisteredMember(DM) → MemberRegisteredResponse(DTO)

### Feedback from coaches
- [ ] all Controllers use DTOs
  - [ ] change DTO names to more specific names e.g. *DTO -> *Response
  - [ ] move validation logic to DTOs, do not validate inside Models
  - [ ] bundle related DTOs into the same file
- [ ] repos use Entity (or sometimes DTO?)
  - [ ] StatRepository can return Member and ProductStat
- [ ] separate CartsRepository from CartItemsRepository
- [ ] more OOP style inside the service e.g. -> retrieve cart -> retrieve product -> create cart item -> add item to cart -> return response
- [ ] turn data class into simple class if you implement your own hashcode() and equals() implementations
- [ ] Member should have name attribute

### Tests
- [ ] test refactored CartRepository
- [ ] use init.sql script with @Sql inside CartE2ETest
- [ ] test for Services
- [ ] try Mock for E2E

### Optional
- [ ] chain operations e.g.:
```kotlin
fun createToken(tokenRequest: TokenRequest): TokenResponse {
        return jwtTokenProvider
            .createToken(tokenRequest.email)
            .let(::TokenResponse)
    }
```

### Miscellaneous
- [ ] change from INT to BIGINT inside `schema.sql`
---
