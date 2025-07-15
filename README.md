# spring-ecommerce-product

## Functional Requirements

Implement a simple HTTP API that allows users to retrieve, add, update, and delete products.
- HTTP requests and responses must be in JSON format.
- Since no separate database is used at this point, store data in memory using an appropriate Kotlin Collection Framework.

Implement the API to send and receive HTTP messages as shown in the example below.

### Request
```aiignore
GET /api/products HTTP/1.1
Response
HTTP/1.1 200
Content-Type: application/json

[
    {
        "id": 8146027,
        "name": "Iced Americano T",
        "price": 4.50,
        "imageUrl": "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg"
    }
]
```
## Feature

- [ ] Implement Product entity class
- [ ] implement the rest controller
  - [ ] @GetMapping endpoint to retrieve the Product
    `/api/products HTTP/1.1`
  - [ ] @PostMapping to add a product
  - [ ] @PutMapping to update
  - [ ] @DeleteMapping to delete
- [ ] store data in memory using a suitable collection `private val products: MutableMap<Long, Product> = HashMap()`
