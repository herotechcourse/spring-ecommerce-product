# spring-ecommerce-product

## feature list

### step1

- [x] configure API that services the following requests:
  - [x] add
  - [x] retrieve
  - [x] update
  - [x] delete

- [x] UI representation of products
  - [x] retrieve
  - [x] add
  - [x] update
  - [x] delete

- [x] database integration (instead of global properties)

### step2

- [x] validate user input
  - [x] product name
    - [x] length is maximum 15 characters
    - [x] allow only the following special characters: `()`, `[]`, `+`, `-`, `&`, `/`, `_`
    - [x] must be unique
  - [x] product price must be greater than 0
  - [x] product image must start with `http://` or `https://`

- [x] error handling
  - [x] return `400 Bad Request` for invalid input
  - [x] respond a clear error message for each invalid field
