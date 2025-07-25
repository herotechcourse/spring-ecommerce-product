# spring-ecommerce-product

## Features
### Step 1.1: introduce class `product` and its id will be automatically handled
- [x] Create a Product class
  - [x] contains id: Long, name: String, price: Double, imageUrl: String
  - [x] use AtomicLong to create the Id
- [x] Create ProductController
  - [x] use @RestController to return always JSON
  - [x] Create the "Database" in the form of HashMap()
  - [x] Create CRUD operations
- [x] Create a GlobalControllerAdvise to handle Exceptions

### Step 1.2: inject product service dependency to controller
- [x] Implement a controller that return html
- [x] Detach the "Database" to be accessible by the two controllers
- [x] Create a ProductService to simulate the connection with a real DataBase
- [x] Inject the ProductService dependency to the controllers
- [x] Create a template html with the list of all products
- [x] Add JS for CRUD request in the frontend.
- [x] Display image instead of a string on image URL
-
### Step 1.3: introduce H2 database and replace product service with *product repository
#### *contains helper functions for building responses
- [x] Configure H2 database
- [x] Create product repository for database operation
- [x] Create data schema and initialize and insert data to it
- [x] Modify the controller to use the new product repository

### Step 2.1: Product Validation System
- [x] **Comprehensive data validation for product operations**
  - [x] **Product name validation**
    - [x] Maximum 15 characters including spaces
    - [x] Allow only these special characters: `( )`, `[ ]`, `+`, `-`, `&`, `/`, `_`
    - [ ] Ensure unique names across all products (needs custom `@UniqueProductName` validator)
  - [x] **Product price validation**
    - [x] Must be greater than 0 (using `@DecimalMin`)
  - [x] **Product image URL validation**
    - [x] Must start with `http://` or `https://` (using `@Pattern` regex)
- [x] **Proper error handling with Jakarta Bean Validation**
  - [x] Return appropriate HTTP status codes for validation failures (400 Bad Request)
  - [x] Provide clear, specific error messages for each validation case
  - [ ] Handle duplicate name conflicts with meaningful responses
- [x] **Implementation details**
  - [x] Created `ProductRequest.kt` with validation annotations
  - [x] Used `@Valid` in controller methods
  - [x] GlobalControllerAdvice handles `MethodArgumentNotValidException`

### Step 2.2: User Authentication System
- [ ] **User registration feature**
  - [ ] Accept email and password in JSON format (`POST /api/members/register`)
  - [ ] Validate email format and password requirements
  - [ ] Generate and return JWT access token upon successful registration
  - [ ] Store user credentials securely in database (hash passwords)
- [ ] **User login feature**
  - [ ] Accept email and password credentials (`POST /api/members/login`)
  - [ ] Verify credentials against registered users
  - [ ] Issue JWT token for authenticated users
  - [ ] Include user claims (id, name, role) in token
- [ ] **JWT token management**
  - [ ] Add JJWT library dependency (`io.jsonwebtoken:jjwt-api:0.12.6`)
  - [ ] Generate secure tokens with configurable expiration
  - [ ] Implement token validation for protected endpoints
  - [ ] Create token service for generation and verification
- [ ] **Database design**
  - [ ] Create `members` table with id, email, password, name, role columns
  - [ ] Create `MemberRepository` with JDBC operations
  - [ ] Create `Member` domain model and `MemberRequest` DTO
- [ ] **Authentication error handling**
  - [ ] Return `401 Unauthorized` for missing/invalid tokens
  - [ ] Return `403 Forbidden` for incorrect login attempts
  - [ ] Provide clear error messages for authentication failures

### Step 2.3: Shopping Cart System
- [ ] **User authentication integration**
  - [ ] Implement `@LoginMember` annotation for user injection
  - [ ] Create `LoginMemberArgumentResolver` for token-based authentication
  - [ ] Use `Authorization: Bearer <token>` header format
  - [ ] Register resolver in WebMvcConfigurer
- [ ] **Cart management features**
  - [ ] **Get cart contents** (`GET /api/cart`)
    - [ ] Retrieve all products in user's personal cart
    - [ ] Return cart items with product details and quantities
  - [ ] **Add to cart** (`POST /api/cart`)
    - [ ] Add products to user's personal cart
    - [ ] Handle quantity and duplicate product additions
  - [ ] **Remove from cart** (`DELETE /api/cart/{productId}`)
    - [ ] Remove specific items from cart
    - [ ] Support clearing entire cart (`DELETE /api/cart`)
- [ ] **Database design**
  - [ ] Create `cart` table with user_id, product_id, quantity, added_at columns
  - [ ] Create `CartRepository` with JDBC operations
  - [ ] Create `Cart` domain model and `CartRequest` DTO
- [ ] **Authentication service layer**
  - [ ] Create `MemberService` for token validation and user lookup
  - [ ] Implement JWT token parsing and validation
  - [ ] Handle authentication exceptions properly

### Step 2.4: Admin Analytics Dashboard
- [ ] **Admin authorization system**
  - [ ] Implement `HandlerInterceptor` for role-based access control
  - [ ] Restrict `/admin/*` endpoints to ADMIN role only
  - [ ] Create `AuthInterceptor` to check user permissions
  - [ ] Register interceptor in WebMvcConfigurer
- [ ] **Top products analytics** (`GET /admin/analytics/top-products`)
  - [ ] Get top 5 most added products to cart in last 30 days
  - [ ] Handle tie-breaking by most recent addition time
  - [ ] Use SQL: `WHERE`, `DATE`, `GROUP BY`, `ORDER BY`, `LIMIT`
  - [ ] Response includes:
    - [ ] Product name
    - [ ] Number of times added to cart
    - [ ] Most recent addition timestamp
- [ ] **Active users analytics** (`GET /admin/analytics/active-members`)
  - [ ] Get members who added items to cart in last 7 days
  - [ ] Ensure unique member list (no duplicates)
  - [ ] Use SQL: `EXISTS`, `DISTINCT`, `JOIN`
  - [ ] Response includes:
    - [ ] Member ID
    - [ ] Member name
    - [ ] Member email
- [ ] **SQL optimization and implementation**
  - [ ] Create analytics queries in repository layer
  - [ ] Create `AnalyticsRepository` for complex queries
  - [ ] Create response DTOs for analytics data
  - [ ] Implement proper error handling for admin endpoints


## Implementation Roadmap: Complete Beginner Guide

### Prerequisites & Dependencies Setup

**1. Complete build.gradle.kts Setup:**
```kotlin
plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
}

dependencies {
    // Spring Boot starters
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    
    // JWT Authentication
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
    
    // Password Hashing
    implementation("org.springframework.security:spring-security-crypto:6.3.3")
    
    // Database
    runtimeOnly("com.h2database:h2")
    
    // Kotlin support
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    
    // Development tools
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    
    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.rest-assured:rest-assured:5.5.0")
}
```

**2. Application Configuration (application.yml):**
```yaml
spring:
  h2:
    console:
      enabled: true
      path: /h2
  datasource:
    url: jdbc:h2:mem:testdb
    driverClassName: org.h2.Driver
    username: sa
    password: 
  sql:
    init:
      mode: always
      schema-locations: classpath:schema.sql
      data-locations: classpath:data.sql
  devtools:
    restart:
      enabled: true
  thymeleaf:
    cache: false

logging:
  level:
    ecommerce: DEBUG
    org.springframework.jdbc: DEBUG

server:
  port: 8080

jwt:
  secret: Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=
  expiration: 86400000  # 24 hours in milliseconds
```

### Implementation Order (Critical for Beginners!)

**Phase 1: Foundation (Step 2.1)**
1. Set up validation constants
2. Create ProductRequest with validation annotations
3. Update GlobalControllerAdvice for validation errors
4. Create custom UniqueProductName validator
5. Test validation thoroughly

**Phase 2: Authentication (Step 2.2)**
1. Create Member domain model and DTOs
2. Set up database schema (members table)
3. Create MemberRepository with JDBC operations
4. Implement password hashing service
5. Create TokenService for JWT operations
6. Create MemberService for authentication logic
7. Create MemberController for register/login endpoints

**Phase 3: Cart System (Step 2.3)**
1. Create cart database schema
2. Create Cart domain model and DTOs
3. Implement CartRepository
4. Create @LoginMember annotation
5. Implement LoginMemberArgumentResolver
6. Configure WebMvcConfigurer
7. Create CartService and CartController

**Phase 4: Admin Analytics (Step 2.4)**
1. Create analytics DTOs
2. Implement AuthInterceptor for admin routes
3. Create AnalyticsRepository with complex SQL queries
4. Create AnalyticsService and AnalyticsController
5. Configure interceptor registration

## Key concepts with Questions: Step 2

### Architecture Overview

**Complete Project Structure:**
```
src/main/kotlin/ecommerce/
├── advice/
│   └── GlobalControllerAdvice.kt         # Centralized exception handling
├── annotation/
│   └── LoginMember.kt                    # Custom annotation for user injection
├── config/
│   └── WebConfig.kt                      # Spring MVC configuration
├── controller/
│   ├── ProductController.kt              # REST API endpoints
│   ├── ProductViewController.kt          # Web UI endpoints
│   ├── MemberController.kt               # Auth endpoints
│   ├── CartController.kt                 # Cart endpoints
│   └── AnalyticsController.kt            # Admin analytics
├── dto/
│   ├── product/
│   │   ├── ProductRequest.kt             # Product validation
│   │   └── ProductPatchRequest.kt        # Partial updates
│   ├── member/
│   │   ├── LoginRequest.kt               # Login validation
│   │   ├── RegisterRequest.kt            # Registration validation
│   │   └── TokenResponse.kt              # JWT response
│   ├── cart/
│   │   └── CartRequest.kt                # Cart validation
│   └── analytics/
│       ├── TopProductResponse.kt         # Analytics response
│       └── ActiveMemberResponse.kt       # Analytics response
├── exception/
│   ├── ErrorResponse.kt                  # Error response format
│   ├── NotFoundException.kt              # 404 errors
│   └── UnauthorizedException.kt          # 401 errors
├── interceptor/
│   └── AuthInterceptor.kt                # Role-based access control
├── model/
│   ├── Product.kt                        # Product domain model
│   ├── Member.kt                         # User domain model
│   └── CartItem.kt                       # Cart domain model
├── repository/
│   ├── ProductRepository.kt              # Product data access
│   ├── MemberRepository.kt               # User data access
│   ├── CartRepository.kt                 # Cart data access
│   └── AnalyticsRepository.kt            # Analytics queries
├── resolver/
│   └── LoginMemberArgumentResolver.kt    # Token to user resolution
├── service/
│   ├── TokenService.kt                   # JWT operations
│   ├── MemberService.kt                  # User management
│   ├── CartService.kt                    # Cart business logic
│   ├── AnalyticsService.kt               # Analytics business logic
│   └── PasswordService.kt                # Password hashing
├── validation/
│   ├── ValidationConstants.kt            # Validation constants
│   ├── UniqueProductName.kt              # Custom validator annotation
│   └── UniqueProductNameValidator.kt     # Custom validator implementation
└── Application.kt                        # Spring Boot main class

src/main/resources/
├── schema.sql                            # Database schema
├── data.sql                              # Initial data
├── application.yml                       # Configuration
└── templates/                            # Thymeleaf templates
    └── products/
        └── list.html
``` 


### Step 2.1
- understand current structure -> then I'll plan how to validate product name/price/imageUrl regulation
- How i understood current structure
```aiignore
model/Product.kt(id, name,price,imageUrl) 
    - just data class
    - (step1) validate here -> (step2) removed and replaced with `ProductRequest.kt`
    
controller/ProductController.kt(ProductRepo-)
    - server prepare Reponse to the endpoint 
        -> client send Request to the endpoint
        -> I can see result
    - fun {create/update/patch/delete}
    
repository/ProductRepository.kt (JdbcTemplate)
    - helper function for building response
        - val productRowMapper
        - fun {find/save/update/delte/patch}
    
dto/product/CreateProductRequest.kt
    - validate: product name(length, special char + duplicates), product price, product imgUrl
    
dto/product/ProductPatchRequest.kt
    - just data class(name,price,imageUrl) but excluding id
    
+exception
advice/GlobalControllerAdvice.kt()
    - fun handleNotFoundException
    - fun handleValidationException
exception/ErrorResponse.kt(error, message, filedErrors)
exception/NotFoundException.kt(message): RuntimeException(message)

+a
-Application.kt
-validation/ValidationConstants.kt
```
### Questions & Answers

**Q1. How to validate duplicate names? Seems different from other rules**
**Answer:** Use a custom Bean Validation annotation that checks the database:

```kotlin
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [UniqueProductNameValidator::class])
annotation class UniqueProductName(
    val message: String = "Product name must be unique",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

@Component
class UniqueProductNameValidator(
    private val productRepository: ProductRepository
) : ConstraintValidator<UniqueProductName, String> {
    override fun isValid(name: String?, context: ConstraintValidatorContext?): Boolean {
        return name?.let { !productRepository.existsByName(it) } ?: true
    }
}
```

Then add `@field:UniqueProductName` to your ProductRequest.

**Q2. How to use JJWT library? Flow and details**
**Answer:** JWT Authentication Flow:

1. **Add dependency:**
```kotlin
dependencies {
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
}
```

2. **Registration/Login Flow:**
```
User sends credentials → Server validates → Generate JWT → Return token
```

3. **Token generation:**
```kotlin
val secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E="
val accessToken = Jwts.builder()
    .setSubject(member.id.toString())
    .claim("name", member.name)
    .claim("role", member.role)
    .signWith(Keys.hmacShaKeyFor(secretKey.toByteArray()))
    .compact()
```

4. **Usage:** Client sends token in `Authorization: Bearer <token>` header

**Q3. How should exceptions be handled? Why need GlobalControllerAdvice and ErrorResponse?**
**Answer:** Exception Handling Architecture:

```
1. ProductRequest validation fails (@Valid annotation)
2. Spring throws MethodArgumentNotValidException
3. GlobalControllerAdvice catches it with @ExceptionHandler
4. ErrorResponse created with field-specific errors
5. Client receives structured JSON error response


+ consider; ResponseStatus
```

**Why GlobalControllerAdvice?**
- **Centralized handling:** All validation errors handled in one place
- **Consistent format:** Same error structure across all endpoints
- **Clean controllers:** Controllers focus on business logic, not error handling
- **Reusability:** Same error handling for all validation failures

**Why ErrorResponse?**
- **Standardized format:** Consistent error JSON structure
- **Field-specific errors:** Shows exactly which fields failed validation
- **Client-friendly:** Easy for frontend to parse and display errors

**Q4. I'm not sure about the application flow. I understood like this, fix and give me advice**
**Answer:** Your understanding is mostly correct! Here's the accurate flow:

**Corrected Application Flow:**
```
1. Application.kt starts Spring Boot server
2. Client sends HTTP request to endpoint
3. Controller receives request
4. @Valid triggers ProductRequest validation (Jakarta Bean Validation)
5. If validation fails → Spring throws MethodArgumentNotValidException
6. GlobalControllerAdvice catches exception and returns error response
7. If validation passes → Controller processes request
8. Repository interacts with H2 database using JDBC
9. Controller returns successful response to client
```

**Key corrections:**
- **Controller doesn't "prepare response and send request"** - Controller **receives** requests and **sends** responses
- **Validation happens before** controller logic, not inside it
- **Product.kt is created only after** validation passes
- **Repository handles database operations**, not direct SQL in controller

**Q5. I'm not sure about the project structure. I plan like this, fix and give me advice**
**Answer:** Your structure is good! Here's the corrected explanation for each component:

```
ecommerce/
├── advice/
│   └── GlobalControllerAdvice.kt
│       → Centralized exception handling for all controllers
│       → Catches validation errors and converts to HTTP responses
├── controller/
│   ├── ProductController.kt
│   │   → REST API endpoints (/api/products) returning JSON
│   │   → Receives HTTP requests, processes them, returns responses
│   └── ProductViewController.kt
│       → Web UI endpoints returning HTML templates
│       → Handles browser-based interactions with Thymeleaf
├── dto/
│   ├── ProductRequest.kt
│   │   → Input validation for create/update operations
│   │   → Contains Jakarta validation annotations
│   └── ProductPatchRequest.kt
│       → Input validation for partial updates (PATCH)
├── exception/
│   ├── ErrorResponse.kt
│   │   → Standardized error response format for API
│   │   → Contains error message and field-specific validation errors
│   └── NotFoundException.kt
│       → Custom exception for missing resources (404 errors)
├── model/
│   └── Product.kt
│       → Domain entity representing business object
│       → Keep here! It's not a DTO, it's the core domain model
├── repository/
│   └── ProductRepository.kt
│       → Database operations using Spring JDBC
│       → NOT just "helper functions" - it's the data access layer
├── validation/
│   └── ValidationConstants.kt
│       → Constants used in validation annotations
└── Application.kt
    → Spring Boot main class that starts the server
```

**Key Points:**
- **Product.kt belongs in model/**, not dto/ - it's your domain entity
- **Repository is the data access layer**, not just helper functions
- **Controllers receive requests and send responses**, they don't "prepare and send requests"

## Practical Implementation Guides

### Step 2.1 Implementation Guide: Product Validation

**Step-by-Step Implementation:**

**1. Create Validation Constants:**
```kotlin
// src/main/kotlin/ecommerce/validation/ValidationConstants.kt
package ecommerce.validation

const val NAME_LENGTH_MAXIMUM = 15
const val PRODUCT_PRICE_MINIMUM = "0.01"
```

**2. Create Custom Unique Validator:**
```kotlin
// src/main/kotlin/ecommerce/validation/UniqueProductName.kt
package ecommerce.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [UniqueProductNameValidator::class])
annotation class UniqueProductName(
    val message: String = "Product name must be unique",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
```

**3. Implement Validator Logic:**
```kotlin
// src/main/kotlin/ecommerce/validation/UniqueProductNameValidator.kt
package ecommerce.validation

import ecommerce.repository.ProductRepository
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.stereotype.Component

@Component
class UniqueProductNameValidator(
    private val productRepository: ProductRepository
) : ConstraintValidator<UniqueProductName, String> {
    
    override fun isValid(name: String?, context: ConstraintValidatorContext?): Boolean {
        return name?.let { !productRepository.existsByName(it) } ?: true
    }
}
```

**4. Add Repository Method:**
```kotlin
// Add to ProductRepository.kt
fun existsByName(name: String): Boolean {
    val sql = "SELECT COUNT(*) FROM products WHERE name = ?"
    val count = jdbcTemplate.queryForObject(sql, Int::class.java, name) ?: 0
    return count > 0
}
```

**5. Update ProductRequest with All Validations:**
```kotlin
// src/main/kotlin/ecommerce/dto/product/ProductRequest.kt
package ecommerce.dto.product

import ecommerce.validation.NAME_LENGTH_MAXIMUM
import ecommerce.validation.PRODUCT_PRICE_MINIMUM
import ecommerce.validation.UniqueProductName
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class ProductRequest(
    @field:Size(max = NAME_LENGTH_MAXIMUM, message = "Product name must be shorter than 15 characters")
    @field:NotBlank(message = "Product name cannot be blank")
    @field:Pattern(
        regexp = "^[a-zA-Z0-9 ()\\[\\]+\\-&/_]*$",
        message = "Product name contains invalid characters"
    )
    @field:UniqueProductName
    val name: String,

    @field:DecimalMin(value = PRODUCT_PRICE_MINIMUM, message = "Product price must be greater than 0")
    val price: Double,

    @field:NotBlank(message = "Product image URL cannot be blank")
    @field:Pattern(
        regexp = "^https?://.*",
        message = "Product image URL must start with http:// or https://"
    )
    val imageUrl: String
)
```

**6. Test Your Implementation:**
```bash
# Test validation with curl
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name": "Test Product!!!", "price": -1, "imageUrl": "invalid-url"}'

# Should return validation errors
```

### Step 2.2 Implementation Guide: JWT Authentication

**Step-by-Step Implementation:**

**1. Add Password Service:**
```kotlin
// src/main/kotlin/ecommerce/service/PasswordService.kt
package ecommerce.service

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class PasswordService {
    private val passwordEncoder = BCryptPasswordEncoder()
    
    fun encode(rawPassword: String): String = passwordEncoder.encode(rawPassword)
    
    fun matches(rawPassword: String, encodedPassword: String): Boolean =
        passwordEncoder.matches(rawPassword, encodedPassword)
}
```

**2. Create Member Domain Model:**
```kotlin
// src/main/kotlin/ecommerce/model/Member.kt
package ecommerce.model

data class Member(
    val id: Long = 0,
    val email: String,
    val password: String,  // Hashed password
    val name: String,
    val role: String = "USER"
)
```

**3. Create Member DTOs:**
```kotlin
// src/main/kotlin/ecommerce/dto/member/MemberRequest.kt
package ecommerce.dto.member

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RegisterRequest(
    @field:Email(message = "Invalid email format")
    @field:NotBlank(message = "Email cannot be blank")
    val email: String,
    
    @field:Size(min = 6, message = "Password must be at least 6 characters")
    @field:NotBlank(message = "Password cannot be blank")
    val password: String,
    
    @field:NotBlank(message = "Name cannot be blank")
    val name: String
)

data class LoginRequest(
    @field:Email(message = "Invalid email format")
    @field:NotBlank(message = "Email cannot be blank")
    val email: String,
    
    @field:NotBlank(message = "Password cannot be blank")
    val password: String
)

data class TokenResponse(
    val token: String,
    val type: String = "Bearer"
)
```

**4. Create Token Service:**
```kotlin
// src/main/kotlin/ecommerce/service/TokenService.kt
package ecommerce.service

import ecommerce.model.Member
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class TokenService(
    @Value("\${jwt.secret}") private val secretKey: String,
    @Value("\${jwt.expiration}") private val expiration: Long
) {
    
    fun generateToken(member: Member): String {
        return Jwts.builder()
            .setSubject(member.id.toString())
            .claim("email", member.email)
            .claim("role", member.role)
            .claim("name", member.name)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + expiration))
            .signWith(Keys.hmacShaKeyFor(secretKey.toByteArray()))
            .compact()
    }
    
    fun validateToken(token: String): Claims? {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.toByteArray()))
                .build()
                .parseClaimsJws(token)
                .body
        } catch (e: Exception) {
            null
        }
    }
    
    fun extractMemberId(token: String): Long? {
        val claims = validateToken(token)
        return claims?.subject?.toLongOrNull()
    }
}
```

**5. Create Member Repository:**
```kotlin
// src/main/kotlin/ecommerce/repository/MemberRepository.kt
package ecommerce.repository

import ecommerce.model.Member
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.Statement

@Repository
class MemberRepository(private val jdbcTemplate: JdbcTemplate) {
    
    private val memberRowMapper = RowMapper<Member> { rs, _ ->
        Member(
            id = rs.getLong("id"),
            email = rs.getString("email"),
            password = rs.getString("password"),
            name = rs.getString("name"),
            role = rs.getString("role")
        )
    }
    
    fun save(member: Member): Member {
        val sql = "INSERT INTO members (email, password, name, role) VALUES (?, ?, ?, ?)"
        val keyHolder = GeneratedKeyHolder()
        
        jdbcTemplate.update({ connection ->
            connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).apply {
                setString(1, member.email)
                setString(2, member.password)
                setString(3, member.name)
                setString(4, member.role)
            }
        }, keyHolder)
        
        return member.copy(id = keyHolder.key!!.toLong())
    }
    
    fun findByEmail(email: String): Member? {
        return try {
            val sql = "SELECT * FROM members WHERE email = ?"
            jdbcTemplate.queryForObject(sql, memberRowMapper, email)
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }
    
    fun findById(id: Long): Member? {
        return try {
            val sql = "SELECT * FROM members WHERE id = ?"
            jdbcTemplate.queryForObject(sql, memberRowMapper, id)
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }
    
    fun existsByEmail(email: String): Boolean {
        val sql = "SELECT COUNT(*) FROM members WHERE email = ?"
        val count = jdbcTemplate.queryForObject(sql, Int::class.java, email) ?: 0
        return count > 0
    }
}
```

**6. Create Member Service:**
```kotlin
// src/main/kotlin/ecommerce/service/MemberService.kt
package ecommerce.service

import ecommerce.dto.member.RegisterRequest
import ecommerce.exception.UnauthorizedException
import ecommerce.model.Member
import ecommerce.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordService: PasswordService,
    private val tokenService: TokenService
) {
    
    fun register(request: RegisterRequest): String {
        if (memberRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email already exists")
        }
        
        val hashedPassword = passwordService.encode(request.password)
        val member = Member(
            email = request.email,
            password = hashedPassword,
            name = request.name,
            role = "USER"
        )
        
        val savedMember = memberRepository.save(member)
        return tokenService.generateToken(savedMember)
    }
    
    fun authenticate(email: String, password: String): String {
        val member = memberRepository.findByEmail(email)
            ?: throw UnauthorizedException("Invalid credentials")
            
        if (!passwordService.matches(password, member.password)) {
            throw UnauthorizedException("Invalid credentials")
        }
        
        return tokenService.generateToken(member)
    }
    
    fun findByToken(token: String): Member {
        val memberId = tokenService.extractMemberId(token)
            ?: throw UnauthorizedException("Invalid token")
            
        return memberRepository.findById(memberId)
            ?: throw UnauthorizedException("User not found")
    }
}
```

**7. Create Member Controller:**
```kotlin
// src/main/kotlin/ecommerce/controller/MemberController.kt
package ecommerce.controller

import ecommerce.dto.member.LoginRequest
import ecommerce.dto.member.RegisterRequest
import ecommerce.dto.member.TokenResponse
import ecommerce.service.MemberService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/members")
class MemberController(private val memberService: MemberService) {
    
    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<TokenResponse> {
        val token = memberService.register(request)
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(TokenResponse(token))
    }
    
    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<TokenResponse> {
        val token = memberService.authenticate(request.email, request.password)
        return ResponseEntity.ok(TokenResponse(token))
    }
}
```

**8. Add Exception Classes:**
```kotlin
// src/main/kotlin/ecommerce/exception/UnauthorizedException.kt
package ecommerce.exception

class UnauthorizedException(message: String) : RuntimeException(message)
```

**9. Update GlobalControllerAdvice:**
```kotlin
// Add to GlobalControllerAdvice.kt
@ExceptionHandler(UnauthorizedException::class)
fun handleUnauthorizedException(e: UnauthorizedException): ResponseEntity<ErrorResponse> {
    val error = ErrorResponse(
        error = "Unauthorized",
        message = e.message ?: "Authentication failed"
    )
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error)
}

@ExceptionHandler(IllegalArgumentException::class)
fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<ErrorResponse> {
    val error = ErrorResponse(
        error = "Bad Request", 
        message = e.message ?: "Invalid request"
    )
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
}
```

**10. Test Your Implementation:**
```bash
# Test registration
curl -X POST http://localhost:8080/api/members/register \
  -H "Content-Type: application/json" \
  -d '{"email": "test@example.com", "password": "password123", "name": "Test User"}'

# Test login
curl -X POST http://localhost:8080/api/members/login \
  -H "Content-Type: application/json" \
  -d '{"email": "test@example.com", "password": "password123"}'
```

## Step 2.2 Deep Dive: JWT Authentication System

### Core Concepts Explained

**Q1. What's exactly the credential used to verify user?**
**Answer:** The credential is the **JWT token** itself. Here's how it works:
```
1. User provides email + password (initial credentials)
2. Server validates and generates JWT token (new credential)
3. Client uses JWT token for subsequent requests
4. Server validates JWT token instead of password every time
```

**Q2. How to generate/issue JWT token? What's needed in each step? What does it look like?**
**Answer:** JWT Token Generation Process:

**Step 1: User Registration/Login**
```kotlin
// User provides credentials
data class LoginRequest(val email: String, val password: String)
```

**Step 2: Server validates credentials**
```kotlin
val member = memberRepository.findByEmail(email)
if (passwordEncoder.matches(password, member.hashedPassword)) {
    // Valid credentials, generate token
}
```

**Step 3: Generate JWT token**
```kotlin
val secretKey = "your-secret-key"
val token = Jwts.builder()
    .setSubject(member.id.toString())           // User ID
    .claim("email", member.email)               // Email claim
    .claim("role", member.role)                 // Role claim
    .setIssuedAt(Date())                        // Issue time
    .setExpiration(Date(System.currentTimeMillis() + 86400000)) // 24h expiry
    .signWith(Keys.hmacShaKeyFor(secretKey.toByteArray()))
    .compact()
```

**Step 4: Token looks like this:**
```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjMiLCJlbWFpbCI6InVzZXJAZXhhbXBsZS5jb20iLCJyb2xlIjoiVVNFUiIsImlhdCI6MTYzMzAyNDgwMCwiZXhwIjoxNjMzMTExMjAwfQ.signature
```

**Q3. What's the difference: secure token vs. insecure token?**
**Answer:**

| **Secure Token (JWT)** | **Insecure Token** |
|------------------------|-------------------|
| ✅ Cryptographically signed | ❌ Plain text or simple encoding |
| ✅ Contains expiration time | ❌ No expiration |
| ✅ Cannot be tampered with | ❌ Can be easily modified |
| ✅ Contains user claims | ❌ Just random string |
| ✅ Self-contained | ❌ Requires database lookup |

**Q4. What are protected endpoints?**
**Answer:** Protected endpoints require valid JWT token to access:
```kotlin
// Protected - requires token
GET /api/cart              // User's cart
POST /api/cart             // Add to cart
DELETE /api/cart/{id}      // Remove from cart
GET /admin/analytics       // Admin only

// Public - no token needed
POST /api/members/register // Registration
POST /api/members/login    // Login
GET /api/products          // View products
```

**Q5. Token service means TokenService.kt class inside service directory? Yes! And what other services needed?**
**Answer:** Yes, you'll need several service classes:

```
src/main/kotlin/ecommerce/service/
├── TokenService.kt          // JWT generation and validation
├── MemberService.kt         // User management and authentication
├── CartService.kt           // Cart business logic (Step 2.3)
└── AnalyticsService.kt      // Admin analytics (Step 2.4)
```

**Q6. What's needed to have database? .sql files?**
**Answer:** You need these SQL files in `src/main/resources/`:

**schema.sql** (creates tables):
```sql
CREATE TABLE members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    role VARCHAR(20) DEFAULT 'USER'
);

CREATE TABLE cart (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT DEFAULT 1,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES members(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);
```

**data.sql** (inserts initial data):
```sql
INSERT INTO members (email, password, name, role) VALUES 
('admin@example.com', '$2a$10$hashed_password', 'Admin User', 'ADMIN'),
('user@example.com', '$2a$10$hashed_password', 'Regular User', 'USER');
```

**Q7. What are JDBC operations?**
**Answer:** JDBC operations are database interactions using Spring's JdbcTemplate:

```kotlin
class MemberRepository(private val jdbcTemplate: JdbcTemplate) {
    
    // CREATE
    fun save(member: Member): Member {
        val sql = "INSERT INTO members (email, password, name, role) VALUES (?, ?, ?, ?)"
        val keyHolder = GeneratedKeyHolder()
        jdbcTemplate.update({ connection ->
            connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).apply {
                setString(1, member.email)
                setString(2, member.password)
                setString(3, member.name)
                setString(4, member.role)
            }
        }, keyHolder)
        return member.copy(id = keyHolder.key!!.toLong())
    }
    
    // READ
    fun findByEmail(email: String): Member? {
        val sql = "SELECT * FROM members WHERE email = ?"
        return jdbcTemplate.queryForObject(sql, memberRowMapper, email)
    }
    
    // UPDATE
    fun update(id: Long, member: Member): Member {
        val sql = "UPDATE members SET email = ?, name = ? WHERE id = ?"
        jdbcTemplate.update(sql, member.email, member.name, id)
        return member.copy(id = id)
    }
    
    // DELETE
    fun delete(id: Long) {
        val sql = "DELETE FROM members WHERE id = ?"
        jdbcTemplate.update(sql, id)
    }
}
```

**Q8. Create `member` domain model means, include class `Member` inside directory `model`?**
**Answer:** Yes! Create these classes:

**src/main/kotlin/ecommerce/model/Member.kt**
```kotlin
data class Member(
    val id: Long = 0,
    val email: String,
    val password: String,  // This will be hashed
    val name: String,
    val role: String = "USER"
)
```

**src/main/kotlin/ecommerce/dto/member/MemberRequest.kt**
```kotlin
data class LoginRequest(
    @field:Email(message = "Invalid email format")
    val email: String,
    
    @field:Size(min = 6, message = "Password must be at least 6 characters")
    val password: String
)

data class RegisterRequest(
    @field:Email(message = "Invalid email format")
    val email: String,
    
    @field:Size(min = 6, message = "Password must be at least 6 characters")
    val password: String,
    
    @field:NotBlank(message = "Name cannot be blank")
    val name: String
)
```

**Q9. How to validate tokens? How to determine it's invalid?**
**Answer:** Token validation process:

```kotlin
@Service
class TokenService {
    private val secretKey = "your-secret-key"
    
    fun validateToken(token: String): Claims? {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.toByteArray()))
                .build()
                .parseClaimsJws(token)
                .body
        } catch (e: Exception) {
            null  // Invalid token
        }
    }
    
    fun extractMemberId(token: String): Long? {
        val claims = validateToken(token)
        return claims?.subject?.toLongOrNull()
    }
}
```

**Token is invalid when:**
- Signature doesn't match (tampered with)
- Token is expired
- Token format is incorrect
- Secret key is wrong

**Q10. How does `Member` class lookup users?**
**Answer:** The `Member` class is just a data model. User lookup happens in `MemberService`:

```kotlin
@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val tokenService: TokenService
) {
    fun findByToken(token: String): Member {
        val memberId = tokenService.extractMemberId(token) 
            ?: throw UnauthorizedException("Invalid token")
        
        return memberRepository.findById(memberId) 
            ?: throw UnauthorizedException("User not found")
    }
    
    fun authenticate(email: String, password: String): String {
        val member = memberRepository.findByEmail(email)
            ?: throw UnauthorizedException("Invalid credentials")
            
        if (!passwordEncoder.matches(password, member.password)) {
            throw UnauthorizedException("Invalid credentials")
        }
        
        return tokenService.generateToken(member)
    }
}
```

## Step 2.3 Deep Dive: Shopping Cart with Authentication

### Core Concepts Explained

**How Authentication Works in Cart System:**
1. **Client sends token:** `Authorization: Bearer <jwt-token>`
2. **Custom resolver extracts user:** `@LoginMember` annotation injects authenticated user
3. **Cart operations:** All cart operations are user-specific

**Key Components Implementation:**

**1. Custom Annotation for User Injection:**
```kotlin
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class LoginMember
```

**2. Argument Resolver (extracts user from token):**
```kotlin
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
        val token = webRequest.getHeader("Authorization")?.removePrefix("Bearer ") 
            ?: throw UnauthorizedException("Missing Authorization header")
        return memberService.findByToken(token)
    }
}
```

**3. Register Resolver in Configuration:**
```kotlin
@Configuration
class WebConfig : WebMvcConfigurer {
    @Autowired
    private lateinit var loginMemberArgumentResolver: LoginMemberArgumentResolver
    
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(loginMemberArgumentResolver)
    }
}
```

**4. Cart Controller with User Authentication:**
```kotlin
@RestController
@RequestMapping("/api/cart")
class CartController(private val cartService: CartService) {

    @GetMapping
    fun getCart(@LoginMember member: Member): List<CartItem> {
        return cartService.findByMemberId(member.id)
    }

    @PostMapping
    fun addToCart(
        @RequestBody request: CartRequest,
        @LoginMember member: Member
    ): ResponseEntity<CartItem> {
        val cartItem = cartService.addToCart(member.id, request.productId, request.quantity)
        return ResponseEntity.ok(cartItem)
    }
    
    @DeleteMapping("/{productId}")
    fun removeFromCart(
        @PathVariable productId: Long,
        @LoginMember member: Member
    ): ResponseEntity<Unit> {
        cartService.removeFromCart(member.id, productId)
        return ResponseEntity.noContent().build()
    }
}
```

**5. Cart Service Layer:**
```kotlin
@Service
class CartService(private val cartRepository: CartRepository) {
    
    fun addToCart(memberId: Long, productId: Long, quantity: Int): CartItem {
        // Business logic: check if product exists, handle duplicates, etc.
        return cartRepository.save(CartItem(memberId = memberId, productId = productId, quantity = quantity))
    }
    
    fun findByMemberId(memberId: Long): List<CartItem> = cartRepository.findByMemberId(memberId)
    
    fun removeFromCart(memberId: Long, productId: Long) = cartRepository.deleteByMemberIdAndProductId(memberId, productId)
}
```

**6. Cart Domain Models:**
```kotlin
// src/main/kotlin/ecommerce/model/CartItem.kt
data class CartItem(
    val id: Long = 0,
    val memberId: Long,
    val productId: Long,
    val quantity: Int,
    val addedAt: LocalDateTime = LocalDateTime.now()
)

// src/main/kotlin/ecommerce/dto/cart/CartRequest.kt
data class CartRequest(
    @field:Positive(message = "Product ID must be positive")
    val productId: Long,
    
    @field:Positive(message = "Quantity must be positive")
    val quantity: Int = 1
)
```

## Step 2.4 Deep Dive: Admin Analytics with Role-Based Security

### Core Concepts Explained

**Admin Authorization Flow:**
1. **Check token:** Extract JWT from Authorization header
2. **Validate role:** Ensure user has ADMIN role
3. **Allow/Deny:** Grant access to `/admin/*` endpoints or return 401

**Key Components Implementation:**

**1. Admin Interceptor (checks ADMIN role):**
```kotlin
@Component
class AuthInterceptor(
    private val memberService: MemberService
) : HandlerInterceptor {
    
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        // Only check admin paths
        if (!request.requestURI.startsWith("/admin")) {
            return true
        }
        
        val token = request.getHeader("Authorization")?.removePrefix("Bearer ")
        if (token == null) {
            response.status = 401
            response.writer.write("""{"error": "Missing Authorization header"}""")
            return false
        }
        
        return try {
            val member = memberService.findByToken(token)
            if (member.role != "ADMIN") {
                response.status = 403
                response.writer.write("""{"error": "Admin access required"}""")
                false
            } else {
                true
            }
        } catch (e: Exception) {
            response.status = 401
            response.writer.write("""{"error": "Invalid token"}""")
            false
        }
    }
}
```

**2. Register Interceptor:**
```kotlin
@Configuration
class WebConfig : WebMvcConfigurer {
    @Autowired
    private lateinit var authInterceptor: AuthInterceptor
    
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authInterceptor).addPathPatterns("/admin/**")
    }
}
```

**3. Admin Analytics Controller:**
```kotlin
@RestController
@RequestMapping("/admin/analytics")
class AnalyticsController(private val analyticsService: AnalyticsService) {

    @GetMapping("/top-products")
    fun getTopProducts(): List<TopProductResponse> {
        return analyticsService.getTop5ProductsLast30Days()
    }
    
    @GetMapping("/active-members")
    fun getActiveMembers(): List<ActiveMemberResponse> {
        return analyticsService.getActiveMembersLast7Days()
    }
}
```

**4. Analytics Service with Complex Queries:**
```kotlin
@Service
class AnalyticsService(private val analyticsRepository: AnalyticsRepository) {
    
    fun getTop5ProductsLast30Days(): List<TopProductResponse> {
        return analyticsRepository.findTop5ProductsLast30Days()
    }
    
    fun getActiveMembersLast7Days(): List<ActiveMemberResponse> {
        return analyticsRepository.findActiveMembersLast7Days()
    }
}
```

**5. Analytics Repository with SQL Queries:**
```kotlin
@Repository
class AnalyticsRepository(private val jdbcTemplate: JdbcTemplate) {
    
    fun findTop5ProductsLast30Days(): List<TopProductResponse> {
        val sql = """
            SELECT p.name, COUNT(c.id) as add_count, MAX(c.added_at) as latest_added
            FROM cart c
            JOIN products p ON c.product_id = p.id
            WHERE c.added_at >= DATE_SUB(CURRENT_DATE, INTERVAL 30 DAY)
            GROUP BY p.id, p.name
            ORDER BY add_count DESC, latest_added DESC
            LIMIT 5
        """
        
        return jdbcTemplate.query(sql) { rs, _ ->
            TopProductResponse(
                name = rs.getString("name"),
                addCount = rs.getInt("add_count"),
                latestAdded = rs.getTimestamp("latest_added").toLocalDateTime()
            )
        }
    }
    
    fun findActiveMembersLast7Days(): List<ActiveMemberResponse> {
        val sql = """
            SELECT DISTINCT m.id, m.name, m.email
            FROM members m
            WHERE EXISTS (
                SELECT 1 FROM cart c 
                WHERE c.member_id = m.id 
                AND c.added_at >= DATE_SUB(CURRENT_DATE, INTERVAL 7 DAY)
            )
            ORDER BY m.name
        """
        
        return jdbcTemplate.query(sql) { rs, _ ->
            ActiveMemberResponse(
                id = rs.getLong("id"),
                name = rs.getString("name"),
                email = rs.getString("email")
            )
        }
    }
}
```

**6. Response DTOs:**
```kotlin
// src/main/kotlin/ecommerce/dto/analytics/AnalyticsResponse.kt
data class TopProductResponse(
    val name: String,
    val addCount: Int,
    val latestAdded: LocalDateTime
)

data class ActiveMemberResponse(
    val id: Long,
    val name: String,
    val email: String
)
```

**SQL Query Breakdown:**

**Top Products Query:**
- `WHERE c.added_at >= DATE_SUB(CURRENT_DATE, INTERVAL 30 DAY)` - Last 30 days filter
- `GROUP BY p.id, p.name` - Group by product
- `ORDER BY add_count DESC, latest_added DESC` - Sort by count, then by recency
- `LIMIT 5` - Top 5 results

**Active Members Query:**
- `EXISTS (SELECT 1 FROM cart c WHERE ...)` - Check if member added items
- `DISTINCT m.id, m.name, m.email` - Unique members only
- `c.added_at >= DATE_SUB(CURRENT_DATE, INTERVAL 7 DAY)` - Last 7 days filter


