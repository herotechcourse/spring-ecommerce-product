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
  - [ ] Must be no more than 15 characters, including spaces. 
  - [ ] Allowed special characters: ( ), [ ], +, -, &, /, _
  - [ ] All other special characters are not allowed.
  - [ ] The name must be unique across all products.
- Product Price
  - [ ] Must be greater than 0.
- Product Image URL
  - [ ] Must start with http:// or https://.

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
- `@ControllerAdvice` can hanlde exceptions on a global level:
```kotlin
@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler
    fun handleNotFoundException(e: NotFoundException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
    }
}
- ```

## Considerations

- TODO read about difference btw RestController and Controller
- TODO why do we combine Controller and ControllerView
- read and learn SQL commands in H2 console
- 



