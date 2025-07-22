# spring-ecommerce-product

## Advice
### GlobalExceptionHandler
- `handleEmptyResult` -> if no element is found with the ID
- `handleHttpMessageNotReadable` -> if there is a missing field
- `handleDuplicateProductName` -> if there is a duplicate nae present
- `handleValidationException` -> if there is a error for validation

## Controller
### ProductController
- RestController
- [x] `Get` all products
- [x] `Get` product by Id
- [x] `POST` Create a Product
- [x] `PUT` update the whole product by ID
- [x] `PATCH` update one or more attributes of Product by ID
- [x] `DELETE` delete the product by ID

## dto
### ProductDTO
- Validation for product modal
- [x] `name`: Not blank, Maximum of 15, Minimum of 1, starts with http or https
- [x] `description`: Not blank, Minimum of 3
- [x] `price`: is Positive
- [x] `imageUrl` Not Blank, Follows pattern
- [x] `quantity` Cannot be negative (0 included)

### ErrorResponse
- To standardise return error messages

## mapper
### ProductRowMapper
- [x] Converts the DB data to Kotlin class object

## repository
### ProductRepository
- Handle communication with db
- `findAll()`: List<ProductDTO>
- `findById(id: Long)`: ProductDTO?
- `create(product: ProductDTO)`: Long
- `update(id: Long, product: ProductDTO)`
- `deleteById(id: Long)`
- `existsByName(name: String)`: Boolean

## service

### interfaces
- [x] `ProductServiceInterface`

### ProductService
- Handles the logic between Controller and DB
- [x] `getAllProducts()`: ResponseEntity<List<ProductDTO>>
- [x] `getProductById(id: Long)`: ResponseEntity<ProductDTO>
- [x] `createProduct(product: ProductDTO):`  ResponseEntity<Void>
- [x] `updateProduct(id: Long, product: ProductDTO)`: ResponseEntity<Void>
- [x] `fun deleteProduct(id: Long)`: ResponseEntity<Void>

### Schema
#### Products
- id, name, description, price, image_url, quantity
- [x] id is `UNIQUE`
