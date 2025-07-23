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
- [ ] 1.User registration:
  - [ ] API Endpoint: POST /api/members/register
  - [ ] Request Body: Email & Password
  - [ ] Response 201 Created with Token
  - [ ] Validation: email format, password complexity, email uniqueness (business logic, checked in the service layer)
  - [ ] Password hashing with BCrypt
  - [ ] JWT Generation - issue an access token after a successful registration
- [ ] 2.User Signin:
  - [ ] API Endpoint: POST /api/members/signin
  - [ ] Request Body: Email & Password
  - [ ] Response 200 OK with Token
  - [ ] Validation email and password (same as register)
  - [ ] Authentication - verify credentials against stored hash
  - [ ] JWT Generation - issue an access token after a successful signin
- [ ] 3.Authentication / Authorization
  - [ ] Unauthorized for missing or invalid token
  - [ ] Forbidden for incorrect login
