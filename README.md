# spring-ecommerce-product

## Advice
### GlobalExceptionHandler

## Controller
### ProductController
- RestController
- [ ] `Get` all products
- [ ] `Get` product by Id
- [ ] `POST` Create a Product
- [ ] `PUT` update the whole product by ID
- [ ] `PATCH` update one or more attributes of Product by ID
- [ ] `DELETE` delete the product by ID

## dto
### ProductDTO
- Validation for product modal
- [ ] `name`: Not blank, Maximum of 15, Minimum of 1, starts with http or https
- [ ] `description`: Not blank, Minimum of 3
- [ ] `price`: is Positive
- [ ] `imageUrl` Not Blank, Follows pattern
- [ ] `quantity` Cannot be negative (0 included)

## mapper
### ProductRowMapper
- [ ] Converts the DB data to Kotlin class object

## repository
### ProductRepository
- Handle communication with db
- `findAll()`: List<ProductDTO>
- `findById(id: Long)`: ProductDTO?
- `create(product: ProductDTO)`: Long
- `update(id: Long, product: ProductDTO)`
- `deleteById(id: Long)`

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
- [ ] id is `UNIQUE`
