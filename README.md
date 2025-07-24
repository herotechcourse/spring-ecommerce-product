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
- [ ] Login for Admin
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
- [ ] 3.Authentication / Authorization
  - [ ] Unauthorized for missing or invalid token
  - [ ] Forbidden for incorrect login

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

- [ ] Implement API Security with JWT:
  - [ ] Extract JWT from `Authorization: Bearer <token>` header.
  - [ ] Validate JWT (signature and expiration).
  - [ ] Resolve authenticated `Member` object for controller methods.
  - [ ] Return `401 Unauthorized` for missing/invalid tokens.
- [ ] Implement Shopping Cart Features:
  - [ ] Add `Cart` domain entity/concept to manage product items.
  - [ ] Create `CartItem` domain entity/concept to represent products in the cart.
  - [ ] Build `CartRepository` for persistence of cart data.
  - [ ] Develop `CartService` for business logic related to cart operations.
  - [ ] Implement `Cart` related DTOs for API requests and responses.
  - [ ] Expose REST API endpoints for cart operations:
    - [ ] `GET /api/cart`: Retrieve user's cart contents.
    - [ ] `POST /api/cart/items`: Add a product to the user's cart.
    - [ ] `DELETE /api/cart/items/{productId}`: Remove a product from the user's cart.
  - [ ] Ensure all cart endpoints are protected and require a valid JWT.