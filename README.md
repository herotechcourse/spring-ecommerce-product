# spring-ecommerce-product

## Features
### Step 1.1
- [x] Create a Product class
  - [x] contains id: Long, name: String, price: Double, imageUrl: String
  - [x] use AtomicLong to create the Id
- [x] Create ProductController
  - [x] use @RestController to return always JSON
  - [x] Create the "Database" in the form of HashMap()
  - [x] Create CRUD operations
- [x] Create a GlobalControllerAdvise to handle Exceptions
### Step 1.2
- [x] Implement a controller that return html
- [x] Detach the "Database" to be accessible by the two controllers
- [x] Create a ProductService to simulate the connection with a real DataBase
- [x] Inject the ProductService dependency to the controllers
- [x] Create a template html with the list of all products
- [x] Add JS for CRUD request in the frontend.
- [x] Display image instead of a string on image URL
### Step 1.3
- [x] Configure H2 database
- [x] Create product repository for database operation
- [x] Create data schema and initialize and insert data to it
- [x] Modify the controller to use the new product repository

### Step 2.1
- [] Implement comprehensive data validation for product operations
  - [] Product name validation
    - [] Maximum 15 characters including spaces
    - [] Allow only these special characters: ( ), [ ], +, -, &, /, _
    - [] Ensure unique names across all products
  - [] Product price validation
    - [] Must be greater than 0
  - [] Product image URL validation
    - [] Must start with 'http://' or 'https://'
- [] Implement proper error handling
  - [] Return appropriate HTTP status codes for validation failures
  - [] Provide clear, specific error messages for each validation case
  - [] Handle duplicate name conflicts with meaningful responses
