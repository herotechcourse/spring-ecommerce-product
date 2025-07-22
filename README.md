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
- [ ] catch exceptions on invalid data
- [x] define a database table in `schema.sql`
- [ ] create a `MemberRepository`

- Authentication
- [ ] A member registers with an email and password.
- [ ] To receive an access token, the client must send email and password.
- [ ] If the credentials match a registered user, issue a token.
- [ ] Return 401 Unauthorized if the Authorization header is missing or the token is invalid.
- [ ] Return 403 Forbidden for incorrect login attempts or denied actions (e.g., password reset or change with invalid input).
- [ ] Implement the API to send and receive HTTP messages as shown below:
1. **Register**
- [ ] Request
```http request
POST /api/members/register HTTP/1.1
Content-Type: application/json
host: localhost:8080

{
"email": "admin@email.com",
"password": "password"
}
```
- [ ] Response
```http request
HTTP/1.1 201
Content-Type: application/json

{
"token": ""
}
```

2. **Login**
- [ ] Request
```http request
POST /api/members/login HTTP/1.1
Content-Type: application/json
host: localhost:8080

{
"email": "admin@email.com",
"password": "password"
}
```
- [ ] Response
```http request
HTTP/1.1 200
Content-Type: application/json

{
"token": ""
}
```


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


## Considerations

- TODO read about difference btw RestController and Controller
- TODO why do we combine Controller and ControllerView
- read and learn SQL commands in H2 console
- 



