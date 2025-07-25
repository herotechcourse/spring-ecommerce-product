# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot e-commerce product management system built with Kotlin. The application provides both REST API endpoints and a web interface for CRUD operations on products. It uses H2 in-memory database for development and includes both JSON API responses and HTML template rendering with Thymeleaf.

## Project Requirement(important)
### Step 1-1
Functional Requirements
Implement a simple HTTP API that allows users to retrieve, add, update, and delete products.

HTTP requests and responses must be in JSON format.
Since no separate database is used at this point, store data in memory using an appropriate Kotlin Collection Framework.
Implement the API to send and receive HTTP messages as shown in the example below.
```
Request
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
### Step 1-2
Functional Requirements
Implement an admin interface that allows users to view, add, update, and delete products.

Use Thymeleaf to implement server-side rendering (SSR).
The default behavior should be based on traditional HTML form submission and page navigation.
However, if you're interested in asynchronous behavior using JavaScript, feel free to use the previously built product API to apply AJAX or similar techniques.
For product images, do not upload files; instead, use direct image URLs.

### Step 1-3

**Pre-implementation Requirements:**
- Before implementing features, update README.md with a structured list of the functionalities you plan to develop
- Each commit should correspond to an individual functionality listed in README.md
- Follow the AngularJS Git Commit Message Conventions to maintain clear and structured commit messages

**Functional Requirements:**
Store product information in a database instead of keeping it in memory using Kotlin collections.

**Programming Requirements:**
- Refactor your application to use an H2 in-memory database instead of Kotlin collections
- Use spring-jdbc to access your database
- The required database tables must be initialized automatically when the application starts

**Guided Learning Tests:**
```
spring-learning-test-with-kotlin/spring-jdbc-1
```

**Hints:**
To connect to a database, you'll need to:
- Add the required Gradle dependencies
- Define the database schema
- Configure the database settings

You can use Spring's `JdbcTemplate`, `SimpleJdbcInsert`, or `JdbcClient` with Kotlin.

### Step 2-1

**Functional Requirements:**
When a product is created or updated, the client may send invalid data.
In such cases, your application must respond with enough information for the client to understand what is wrong and why.

**Validation Rules:**
- **Product Name:**
  - Must be no more than 15 characters, including spaces
  - Allowed special characters: `( )`, `[ ]`, `+`, `-`, `&`, `/`, `_`
  - All other special characters are not allowed
  - The name must be unique across all products
- **Product Price:**
  - Must be greater than 0
- **Product Image URL:**
  - Must start with `http://` or `https://`

### Step 2-2

**Functional Requirements:**
Implement user account features including registration, login, and authentication so that users can access member-only functionality in the future.

- A member registers with an email and password
- To receive an access token, the client must send email and password
- If the credentials match a registered user, issue a token
- Implement the API to send and receive HTTP messages as shown below

**API Examples:**

**Register:**
```http
POST /api/members/register HTTP/1.1
Content-Type: application/json
Host: localhost:8080

{
  "email": "admin@email.com",
  "password": "password"
}
```

Response:
```http
HTTP/1.1 201
Content-Type: application/json

{
  "token": ""
}
```

**Login:**
```http
POST /api/members/login HTTP/1.1
Content-Type: application/json
Host: localhost:8080

{
  "email": "admin@email.com",
  "password": "password"
}
```

Response:
```http
HTTP/1.1 200
Content-Type: application/json

{
  "token": ""
}
```

**Guided Learning Tests:**
```
spring-learning-test-with-kotlin/spring-auth-1
```

**Hints:**

**JSON Web Token:**
You can use the JJWT library to generate JSON Web Tokens easily.

To use the same version as in the learning test, add the following dependency:
```kotlin
dependencies {
    implementation("io.jsonwebtoken:jjwt:0.9.1")
}

val secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E="
val accessToken = Jwts.builder()
    .setSubject(member.id.toString())
    .claim("name", member.name)
    .claim("role", member.role)
    .signWith(Keys.hmacShaKeyFor(secretKey.toByteArray()))
    .compact()
```

Or you can use the latest version:
```kotlin
dependencies {
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
}
```

**HTTP Response Codes:**
- Return `401 Unauthorized` if the Authorization header is missing or the token is invalid
- Return `403 Forbidden` for incorrect login attempts or denied actions (e.g., password reset or change with invalid input)

### Step 2-3

**Functional Requirements:**
Using the token received after login, implement functionality that allows the user to add products to their own cart.

- Users can retrieve the list of products in their cart
- Users can add products to their cart
- Users can remove products from their cart

**Details:**
Use the Authorization header to pass user credentials:
```
Authorization: Bearer <token>
```

**Guided Learning Tests:**
```
spring-learning-test-with-kotlin/spring-mvc-4
```

**Hints:**

**User Scenarios:**
- Add to Cart
- Remove from Cart

**HandlerMethodArgumentResolver:**
To inject the authenticated user into controller methods, use a custom argument resolver.

**A Custom Data Binder in Spring MVC:**
```kotlin
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class LoginMember

@Component
class LoginMemberArgumentResolver(
    private val memberService: MemberService
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(LoginMember::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any {
        val token = webRequest.getHeader("Authorization")?.removePrefix("Bearer ") ?: throw UnauthorizedException()
        return memberService.findByToken(token)
    }
}

@RestController
@RequestMapping("/api/wishes")
class WishController {

    @PostMapping
    fun addToCart(
        @RequestBody request: CartRequest,
        @LoginMember member: Member
    ) {
        // Add product to cart for this member
    }
}
```

### Step 2-4

**Functional Requirements:**
The administrator should be able to view statistical information based on users' carts to support service operations.

**1. Retrieve the Top 5 Most Added Products to Cart in the Last 30 Days:**
- Returns the top 5 products that were added to carts most frequently in the past 30 days
- If multiple products have the same count, the one most recently added should be listed first
- The response must include the product name, the number of times it was added, and the most recent added time

**2. Retrieve Members Who Added Items to Their Cart in the Last 7 Days:**
- Returns a list of members who added at least one product to their cart in the past 7 days
- Each member should appear only once, even if they added multiple products
- The response must include the member ID, name, and email

**3. (Optional) Restrict access to those stat API so that only users with the ADMIN role can access it.**

**Hints:**

**Ranking:**
- **For "Top 5 Most Added Products":**
  Use SQL features such as `WHERE`, `DATE`, `GROUP BY`, `ORDER BY`, and `LIMIT`.
- **For "Recently Active Members":**
  Use SQL features such as `EXISTS`, `DISTINCT`, and `JOIN`.

**(Optional) Authorization:**
Only members with the ADMIN role should be allowed to access APIs starting with `/admin`.
Use a `HandlerInterceptor` to check the user's role, and if the user does not have the required permission.

```kotlin
class AuthInterceptor : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        // ...

        if (member == null || member.role != "ADMIN") {
            response.status = 401
            return false
        }

        return true
    }
}
```

## Architecture

The application follows a standard Spring Boot layered architecture:

- **Controller Layer**: Two controllers handle different response types
  - `ProductController`: REST API endpoints returning JSON at `/api/products`
  - `ProductViewController`: Web endpoints returning HTML templates
- **Repository Layer**: `ProductRepository` uses Spring JDBC for database operations
- **Model Layer**: `Product` data class and `ProductPatchRequest` for partial updates
- **Exception Handling**: Global exception handling via `GlobalControllerAdvice`

The project has evolved through multiple phases (documented in README.md):
1. In-memory HashMap storage with REST API
2. Added HTML templates and frontend JavaScript
3. Migrated to H2 database with JDBC operations

## Development Commands

### Build and Run
```bash
./gradlew bootRun              # Start the application
./gradlew build                # Build the project
./gradlew test                 # Run tests
```

### Code Quality
```bash
./gradlew ktlintCheck          # Check Kotlin code style
./gradlew ktlintFormat         # Format Kotlin code
```

### Database Access
- H2 Console: http://localhost:8080/h2 (when running)
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`, Password: (empty)

## Key Technical Details

### Database Schema
- Products table with columns: id (auto-generated), name, price, imageUrl
- Initial data loaded via `data.sql` on startup
- Schema defined in `schema.sql`

### API Endpoints
- REST API base path: `/api/products`
- Supports full CRUD operations including PATCH for partial updates
- Web interface serves HTML templates with JavaScript for dynamic operations

### Configuration
- Application runs on default Spring Boot port (8080)
- H2 database configured for development with console access
- Dev tools enabled for hot reload
- Thymeleaf caching disabled for development

### Testing
- Uses JUnit 5 with Spring Boot Test
- REST Assured for API integration testing
- Test files located in `src/test/kotlin/ecommerce/`

## Dependencies
- Spring Boot 3.5.3 with Kotlin 1.9.25
- Spring JDBC for database operations
- Thymeleaf for HTML templating
- H2 database for development
- Jackson for JSON processing
- ktlint for code formatting

## Claude Code Guidelines
- Always read and follow @CLAUDE.md 