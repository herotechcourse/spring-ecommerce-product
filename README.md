# spring-ecommerce-product

## Controller
### Admin
#### AdminProductController
- [x] `GET /api/admin/products` all products
- [x] `GET /api/admin/products/:id` product by ID
- [x] `POST /api/admin/products` Create a Product
- [x] `PUT /api/admin/products/:id` update the whole product by ID
- [x] `PATCH /api/admin/products/:id` update one or more attributes of Product by ID
- [x] `DELETE /api/admin/products/:id` delete the product by ID

### Guest
#### GuestProductController
- [x] `GET /api/products` all products

### Member
#### AuthController
- [x] `POST /api/member/auth/signUp` creates user and returns JWT token
- [x] `POST /api/member/auth/signIn` checks and returns JWT token
#### CartController
- `GET /api/member/cart` getCartProducts
- `POST /api/member/cart/:id` incrementCartProduct
- `DELETE /api/member/cart/:id` decrementCartProduct

## Config
### AuthInterceptor
### LoginMemberArgumentResolver
### WebConfig

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

### CartService
- [x] `getCartProducts`: List<CartProductResponseDTO>
- [x] `addProductToCart`: Long
- [x] `removeProductFromCart`: Void

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

### CartRepository
- `createCartForUser`
- `findMembersCart`

### CartProductRepository
- `getCartProducts`
- `findCartProduct`
- `updateProductQuantity`
- `removeProduct`
- `addProduct`

## DTO
### Auth
#### AuthTokenPayload
- email: String
#### LoginRequest
- email: String
- password: String
### Cart
#### CartDTO
- id: Long
- userId: Long
### CartProduct
#### CartProductDTO
- id: Long
- cartId: Long
- productID: Long
- quantity: Int
### Error
#### ErrorResponse
- timestamp: Instant = Instant.now()
- status: Int
- error: String
- message: Any
- path: String? = null
### Products
#### ProductDTO
- Validation for product modal
- [x] `name`: Not blank, Maximum of 15, Minimum of 1, starts with http or https
- [x] `description`: Not blank, Minimum of 3
- [x] `price`: is Positive
- [x] `imageUrl` Not Blank, Follows pattern
- [x] `quantity` Cannot be negative (0 included)
#### ProductPatchDTO
- Same as `ProductDTO` but allowed null
### User
#### UserRequestDTO
- Validation for user Modal
- [x] `email`: Not blank and should be email
- [x] `password`: Not blank and min length of 6
- [x] `name`: Not blank
- [x] `role`: should be admin or user `[Default = user]`

#### UserCreateResponse
- uri: URI
- token: String

#### MemberUserDTO
- id: Long? = null
- email: String
- password: String
- name: String
- role: UserRole = UserRole.USER
## Infrastructure
### JwtTokenProvider
- [x] `createToken`: String
- [x] `getPayload`: AuthTokenPayload
- [x] `validateToken`: Boolean

## Mapper
### CartProductMapper
### CartProductResponseMapper
### CartRowMapper
### ProductRowMapper
### UserRowMapper

## Advice
### GlobalExceptionHandler
- `handleEmptyResult` -> if no element is found with the ID
- `handleHttpMessageNotReadable` -> if there is a missing field
- `handleDuplicateProductName` -> if name is already taken
- `handleValidationException` -> if there is error for validation

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

### Cart
- id
- user_id (FK)

### CartProducts
- id, quantity
- cart_id (FK)
- product_id (FK)

### CartStatistics
- id, created_at, action
- user_id, product_id (FK)
