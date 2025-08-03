# spring-ecommerce-product

## Product
  -[x] Create a simple CRUD HTTP API
- [x] Create a Product
- [x] Retrieve a Product
- [x] Retrieve a list of Products
- [x] Update a Product
- [x] Delete a product
- [x] Tests for CRUD operations
- [x] Validation
  - Name (no more than 15 characters, special characters, all unique)
  - Price (must be greater than 0)
  - Image Url (must start with http:// or https://)

## Admin
- [x] Login for Admin
- [x] Admin can view, add, update, and delete products
- [x] handle exception during add, delete and update of products
- [x] Template for Product Page
- [x] Add Button for Edit a Product
- [x] Add Button for Delete a Product
- [x] Add Button for Add a Product

## Database
- [x] Build a Repository
- [x] Database configuration
- [x] Connect the repository with the DB through the service layer
- [x] Build a schema for the database

## User Account and Authentication
- [x] user Registration
  - [x] POST api/members/register - request Body: Email and Password
  - [x] successful registration response - returns 201 Created with JSON body containing access token
  - [x] Validation - format of email, password, unique email
  - [x] Password Hashing/Security - store password after hashing
- [x] User login with email and password
  - [x] Credential validation - RequestBody Email and Password
  - [x] Access token issuance - JWT
- [x] Error Handling with Authentication/Authorization 
  - 401 - unauthorized
  - 403 - forbidden

## Cart Functionality
- [x] Each user has their own shopping cart
- [x] User can add product to cart
  - add specified quantity of product
  - if product present in cart, quantity -> +1
  - validation to ensure product exists and quantity is valid
- [x] retrieve products from cart
- [x] remove products from cart
- [x] all cart operations require valid JWT for authentication
- [x] user can only access and modify their own cart

## Cart Statistics
- [x] Retrieve top 5 products added to any cart
  - ranking based on count of additions
  - tie breaking - most recent add listed first
  - response includes - product name, total times added, most recent added time
- [x] Recently Active Members 
  - List of members who added at least one product in last 7 days
  - response includes - member ID, name, email
- [x] Role based access control(Admin only)
  - statistical API endpoints - admins only
  - 401 Unauthorized if user is not authorized
