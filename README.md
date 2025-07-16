# spring-ecommerce-product

## Functional Requirements Step 1-1

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

## Features Step1-1

- [x] Implement Product entity class
- [x] implement the rest controller
  - [x] @GetMapping endpoint to retrieve the Product
    `/api/products HTTP/1.1`
  - [x] @PostMapping to add a product
  - [x] @PutMapping to update
  - [x] @DeleteMapping to delete
- [x] store data in memory using a suitable collection `private val products: MutableMap<Long, Product> = HashMap()`

## Functional Requirements Step 1-2
- Implement an admin interface that allows users to view, add, update, and delete products.
- Use Thymeleaf to implement server-side rendering (SSR).
The default behavior should be based on traditional HTML form submission and page navigation.
- For product images, do not upload files; instead, use direct image URLs.

## Features Step1-2
- [ ] Use Thymeleaf to implement server-side rendering (SSR)
- [ ] Create the template table.html
- [ ] Connect controller with table.html
