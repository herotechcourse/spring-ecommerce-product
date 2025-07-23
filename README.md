# spring-ecommerce-product

## Controller
### ProductController
- [x] `Get` all products
- [x] `Get` product by Id
- [x] `POST` Create a Product
- [x] `PUT` update the whole product by ID
- [x] `PATCH` update one or more attributes of Product by ID
- [x] `DELETE` delete the product by ID

### AuthController
- [x] `signup` creates user and returns JWT token
- [x] `login` checks and returns JWT token

## Service
### ProductService
- [x] `getAllProducts()`: List<ProductDTO>
- [x] `getProductById(id: Long)`: ProductDTO
- [x] `createProduct(product: ProductDTO):`  Void
- [x] `updateProduct(id: Long, product: ProductDTO)`: Void
- [x] `fun deleteProduct(id: Long)`: Void

### AuthService
- [x] `signUp`: String `[AuthToken]`
- [x] `login`: String `[AuthToken]`
- [] `logout`: Void

## Repository
### ProductRepository
- `findAll()`: List<ProductDTO>
- `findById(id: Long)`: ProductDTO?
- `create(product: ProductDTO)`: Long
- `update(id: Long, product: ProductDTO)`
- `deleteById(id: Long)`
- `existsByName(name: String)`: Boolean

### UserRepository
- `create`: Long
- `findByEmailAndPassword`: UserDTO?
- `existsByEmail`: Boolean

## DTO
### ProductDTO
- Validation for product modal
- [x] `name`: Not blank, Maximum of 15, Minimum of 1, starts with http or https
- [x] `description`: Not blank, Minimum of 3
- [x] `price`: is Positive
- [x] `imageUrl` Not Blank, Follows pattern
- [x] `quantity` Cannot be negative (0 included)
### UserDTO
- Validation for user Modal
- [x] `email`: Not blank and should be email
- [x] `password`: Not blank and min length of 6
- [x] `name`: Not blank
- [x] `role`: should be admin or user `[Default = user]`
### AuthTokenPayload
### UserCreateResponse
### ProductPatchDTO
### ErrorResponse
### TokenResponse
### TokenRequest

## Infrastructure
### JwtTokenProvider
- [x] `createToken`: String
- [x] `getPayload`: AuthTokenPayload
- [x] `validateToken`: Boolean

## Mapper
### ProductRowMapper
- [x] Converts the DB data to Kotlin class object

### UserRowMapper
- [x] Converts the DB data to Kotlin class object

## Advice
### GlobalExceptionHandler
- `handleEmptyResult` -> if no element is found with the ID
- `handleHttpMessageNotReadable` -> if there is a missing field
- `handleDuplicateProductName` -> if name is already taken
- `handleValidationException` -> if there is a error for validation

## exception
- `DuplicateProductNameException`
- `EntityNotFoundException`
- `UserAlreadyExistsException`
- `UserCredentialException`


## enums
### UserRoles
- [x] Admin
- [x] User

## Schema
### Products
- id, name, description, price, image_url, quantity
- [x] name is `UNIQUE`

### Users
- id, email, password, name, role
- [x] email is unique