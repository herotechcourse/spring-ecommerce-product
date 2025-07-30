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
- [x] Integration Tests for "Top 5 Most Added Products" Report (GET /api/admin/reports/top-products-30-days)
  - [x] Should return 200 OK and correct data with a valid ADMIN token.
  - [x] Should return 403 Forbidden with a valid USER token.
  - [x] Should return 401 Unauthorized with no token.
  - [x] Should return 200 OK and an empty list if no relevant events exist.
- [x] Integration Tests for "Members Who Added Items" Report (GET /api/admin/reports/members-added-to-cart-7-days)
  - [x] Should return 200 OK and correct unique member data with a valid ADMIN token.
  - [x] Should return 403 Forbidden with a valid USER token.
  - [x] Should return 401 Unauthorized with no token.

## Cart API Controller Tests
- [x] Integration Tests for Cart Retrieval (GET /api/cart)
  - [x] Should return 200 OK with no cart contents for a valid USER token.
- [x] Should return 200 OK with cart contents for a valid USER token.
  - [x] Should return 401 Unauthorized with no token.
  - [x] Should return 401 Unauthorized with an invalid token.
- [x] Integration Tests for Adding Products to Cart (POST /api/cart/items)
  - [x] Should return 200 OK and update cart for a valid USER token.
  - [x] Should return 200 OK and update quantity if product already in cart
  - [x] Should return 401 Unauthorized with no token.
  - [x] Should return 401 Unauthorized with an invalid token.
  - [x] Should return 400 Bad Request for invalid request body (e.g., negative quantity).
  - [x] Should return 400 Bad Requests if the quantity exceeds product stock.
- [x] Integration Tests for Removing Products from Cart (DELETE /api/cart/items/{productId})
  - [x] Should return 204 No Content for a valid USER token.
  - [x] Should return 401 Unauthorized with no token.
  - [x] Should return 401 Unauthorized with an invalid token.
  - [x] Should return 404 Not Found if product not in carts.

## Member API Controller Tests
- [x] Integration Tests for User Registration (POST /api/members/register)
  - [x] Should register a new member and return 201 Created.
  - [x] Should return 400 for blank username.
  - [x] Should return 400 for blank email.
  - [x] Should return 400 for invalid email format.
  - [x] Should return 400 for blank password.
  - [x] Should return 400 for password shorter than 8 characters.
  - [x] Should return 400 for duplicate email registration.
- [x] Integration Tests for User Login (POST /api/members/login)
  - [x] Should log in an existing member and return 200 OK with token.
  - [x] Should return 401 for non-existent email.
  - [x] Should return 401 for incorrect password.
  - [x] Should return 400 for blank email.
  - [x] Should return 400 for blank password.
  - [x] Should return 400 for invalid email format.

## Auth Interceptor Tests
- [x] Unit Tests for preHandle Logic
  - [x] Should throw UnauthorizedException for missing/invalid token.
  - [x] Should throw ForbiddenException when a user role is not in PATH_ROLE_REQUIREMENTS.
  - [x] Should return true when a user role is authorized for the path.
  - [x] Should correctly set AUTHENTICATED_MEMBER_ATTRIBUTE in request.
  - [x] Should handle path prefix matching correctly.

## JWT Token Provider Tests
- [x] Unit Tests for Token Generation
  - [x] Should generate a non-empty token.
  - [x] Should include correct subject (member ID).
  - [x] Should include correct email claim.
  - [x] Should include correct role claim.
- [x] Unit Tests for Token Validation
  - [x] Should return true for a valid, non-expired token.
  - [x] Should return false for an expired token.
  - [x] Should return false for a token with an invalid signature.
- [x] Unit Tests for Claim Extraction
  - [x] Should correctly extract subject from a valid token.
  - [x] Should correctly extract a role from a valid token.
  - [x] Should throw exception if the required claim is missing/invalid.

## Global Exception Handler Tests
- [x] Unit Tests for Exception Mapping
  - [x] Should map UnauthorizedException to 401 Unauthorized.
  - [x] Should map ForbiddenException to 403 Forbidden.
  - [x] Should map NoSuchElementException to 404 Not Found.
  - [x] Should map IllegalArgumentException to 400 Bad Request.
  - [x] Should map MethodArgumentNotValidException to 400 Bad Request with field errors.
  - [x] Should map MethodArgumentTypeMismatchException to 400 Bad Request.
  - [x] Should map generic Exception to 500 Internal Server Error.
