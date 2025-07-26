# spring-ecommerce-product

## Product
- [x] Create a simple CRUD HTTP API
- [x] Create a Product
- [x] Retrieve a Product
- [x] Retrieve a list of Products
- [x] Update a Product
- [x] Delete a product
- [x] Tests for CRUD operations

## Admin
- [x] Login for Admin
- [x] Admin can view, add, update, and delete products
- [x] Template for Product Page
- [x] Add Button for Edit a Product
- [x] Add Button for Delete a Product
- [x] Add Button for Add a Product

## Database
- [x] Build a Repository
- [x] Database configuration
- [x] Connect the repository with the DB through the service layer
- [x] Build a schema for the database

## Validation & Handling Exception
- [x] Implement layered input validation:
  - [x] DTO layer validation
  - [x] Service layer business rule validation for uniqueness and semantic ID validations
  - [x] Database layer constraints in the SQL schema
- [x] Centralized API exception handling using `@ControllerAdvice`
  - [x] Handle MethodArgumentNotValidException to return 400 Bad Request
  - [x] Handle ResourceNotFoundException to return 404 Not Found
  - [x] Handle IllegalArgumentException to return 400 Bad Request
  - [x] Handle InvalidInputException to return 400 Bad Request
  - [x] Handle IllegalStateException to return 400 Bad Request
  - [x] Handle DataIntegrityViolationException to return 409 Conflict
  - [x] Handle all other Exceptions to return 500 Internal Server Error
- [x] Differentiate error handling between API and View Controllers:
  - [x] API controllers throw exceptions for GlobalExceptionHandler to catch
  - [x] View controllers use try-catch blocks, Model attributes, and RedirectAttributes for HTML error display

## User Management / Authentication
- [x] 1.User registration:
  - [x] API Endpoint: POST /api/members/register
  - [x] Request Body: Email & Password
  - [x] Response 201 Created with Token
  - [x] Validation: email format, password complexity, email uniqueness (business logic, checked in the service layer)
  - [x] Password hashing with BCrypt
  - [x] JWT Generation - issue an access token after a successful registration
- [x] 2.User Signin:
  - [x] API Endpoint: POST /api/members/signin
  - [x] Request Body: Email & Password
  - [x] Response 200 OK with Token
  - [x] Validation email and password (same as register)
  - [x] Authentication - verify credentials against stored hash
  - [x] JWT Generation - issue an access token after a successful signin
- [x] 3.Authentication / Authorization
  - [x] Unauthorized for missing or invalid token
  - [x] Forbidden for incorrect login

## Tests for User Authentication
  - [ ] test(register): should register a new member and return 201 Created with token
  - [ ] test(register): should return 400 for blank user name
  - [ ] test(register): should return 400 for blank email
  - [ ] test(register): should return 400 for invalid email format
  - [ ] test(register): should return 400 for blank password
  - [ ] test(register): should return 400 for password shorter than 8 characters
  - [ ] test(register): should return 400 for duplicate email registration
  - [ ] test(login): should log in an existing member and return 200 OK with token
  - [ ] test(login): should return 401 for non-existent email
  - [ ] test(login): should return 401 for incorrect password
  - [ ] test(login): should return 401 for blank email (handled by Auth Service)
  - [ ] test(login): should return 401 for blank password (handled by Auth Service)
  - [ ] test(login): should return 400 for invalid email format
  - [ ] test(login): should return 400 for password shorter than 8 characters

## Cart Functionality & API Security

- [x] Implement API Security with JWT:
  - [x] Extract JWT from `Authorization: Bearer <token>` header.
  - [x] Validate JWT (signature and expiration).
  - [x] Resolve authenticated `Member` object for controller methods.
  - [x] Return `401 Unauthorized` for missing/invalid tokens.
- [x] Implement Shopping Cart Features:
  - [x] Add `Cart` domain entity/concept to manage product items.
  - [x] Create `CartItem` domain entity/concept to represent products in the cart.
  - [x] Build `CartRepository` for persistence of cart data.
  - [x] Develop `CartService` for business logic related to cart operations.
  - [x] Implement `Cart` related DTOs for API requests and responses.
  - [x] Expose REST API endpoints for cart operations:
    - [x] `GET /api/cart`: Retrieve user's cart contents.
    - [x] `POST /api/cart`: Add a product to the user's cart.
    - [x] `DELETE /api/cart/{productId}`: Remove a product from the user's cart.
  - [x] Ensure all cart endpoints are protected and require a valid JWT.

## Cart Statistics (Admin Reports)

- [x] Implement Cart Event Logging:
  - [x] Add CartEvent domain entity to track ADD_TO_CART actions.
  - [x] Create CartEventRepository for persistence of cart event data.
  - [x] Integrate event logging into CartService's addProductToCart method.
- [x] Implement "Top 5 Most Added Products" Report:
  - [x] Create ProductCartCountDTO for report results.
  - [x] Add query method (findTop5MostAddedProductsInLast30Days) to CartEventRepository.
  - [x] Create ReportService with business logic for the report.
  - [x] Expose GET /api/admin/reports/top-products-30-days endpoint.
  - [x] Ensure endpoint is protected and requires ADMIN role.

## Admin Report Controller Tests

- [ ] Integration Tests for "Top 5 Most Added Products" Report (GET /api/admin/reports/top-products-30-days)
  - [ ] Should return 200 OK and correct data with a valid ADMIN token.
  - [ ] Should return 403 Forbidden with a valid USER token.
  - [ ] Should return 401 Unauthorized with no token.
  - [ ] Should return 200 OK and an empty list if no relevant events exist.
- [ ] Integration Tests for "Members Who Added Items" Report (GET /api/admin/reports/members-added-to-cart-7-days)
  - [ ] Should return 200 OK and correct unique member data with a valid ADMIN token.
  - [ ] Should return 403 Forbidden with a valid USER token.
  - [ ] Should return 401 Unauthorized with no token.
  - [ ] Should return 200 OK and an empty list if no relevant member activity exists.

## Cart API Controller Tests

- [ ] Integration Tests for Cart Retrieval (GET /api/cart)
  - [ ] Should return 200 OK with cart contents for a valid USER or ADMIN token.
  - [ ] Should return 401 Unauthorized with no token.
  - [ ] Should return 401 Unauthorized with an invalid token.
- [ ] Integration Tests for Adding Products to Cart (POST /api/cart/items)
  - [ ] Should return 200 OK and updated cart for a valid USER or ADMIN token.
  - [ ] Should return 401 Unauthorized with no token.
  - [ ] Should return 401 Unauthorized with an invalid token.
  - [ ] Should return 400 Bad Request for invalid request body (e.g., negative quantity).
  - [ ] Should return 400 Bad Requests if the quantity exceeds product stock.
- [ ] Integration Tests for Removing Products from Cart (DELETE /api/cart/items/{productId})
  - [ ] Should return 204 No Content for a valid USER or ADMIN token.
  - [ ] Should return 401 Unauthorized with no token.
  - [ ] Should return 401 Unauthorized with an invalid token.
  - [ ] Should return 404 Not Found if product not in carts.

## Member API Controller Tests

- [ ] Integration Tests for User Registration (POST /api/members/register)
  - [x] Should register a new member and return 201 Created.
  - [x] Should return 400 for blank user name.
  - [x] Should return 400 for blank email.
  - [x] Should return 400 for invalid email format.
  - [x] Should return 400 for blank password.
  - [x] Should return 400 for password shorter than 8 characters.
  - [x] Should return 400 for duplicate email registration.
  - [ ] Integration Tests for User Login (POST /api/members/login)
  - [x] Should log in an existing member and return 200 OK with token.
  - [x] Should return 401 for non-existent email.
  - [x] Should return 401 for incorrect password.
  - [x] Should return 401 for blank email.
  - [x] Should return 401 for blank password.
  - [x] Should return 400 for invalid email format.
  - [x] Should return 400 for password shorter than 8 characters.
- [ ] Integration Tests for Member-Specific Operations (if any, e.g., GET /api/members/profile)
  - [ ] Should return 200 OK for a valid USER or ADMIN token.
    - [ ] Should return 401 Unauthorized with no token.

## Auth Interceptor Tests

- [ ] Unit Tests for preHandle Logic
  - [ ] Should return true for public paths.
  - [ ] Should throw UnauthorizedException for missing/invalid token.
  - [ ] Should throw ForbiddenException when a user role is not in PATH_ROLE_REQUIREMENTS.
  - [ ] Should return true when a user role is authorized for the path.
  - [ ] Should correctly set AUTHENTICATED_MEMBER_ATTRIBUTE in request.
  - [ ] Test specific path prefix matching logic (e.g., longer prefix takes precedence).

## JWT Token Provider Tests

- [ ] Unit Tests for Token Generation
  - [ ] Should generate a non-empty token.
  - [ ] Should include correct subject (member ID).
  - [ ] Should include correct email claim.
  - [ ] Should include correct role claim.
  - [ ] Should have correct issuedAt and expiration times.
- [ ] Unit Tests for Token Validation
  - [ ] Should return true for a valid, non-expired token.
  - [ ] Should return false for an expired token.
  - [ ] Should return false for a token with an invalid signature.
  - [ ] Should return false for a malformed token.
- [ ] Unit Tests for Claim Extraction
  - [ ] Should correctly extract subject from a valid token.
  - [ ] Should correctly extract a role from a valid token.
  - [ ] Should throw exception if the required claim is missing/invalid.

## Global Exception Handler Tests

- [ ] Unit Tests for Exception Mapping
  - [ ] Should map UnauthorizedException to 401 Unauthorized.
  - [ ] Should map ForbiddenException to 403 Forbidden.
  - [ ] Should map NoSuchElementException to 404 Not Found.
  - [ ] Should map IllegalArgumentException to 400 Bad Request.
  - [ ] Should map MethodArgumentNotValidException to 400 Bad Request with field errors.
  - [ ] Should map MethodArgumentTypeMismatchException to 400 Bad Request.
  - [ ] Should map generic Exception to 500 Internal Server Error.