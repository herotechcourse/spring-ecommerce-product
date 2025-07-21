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

- [ ] validate user input
  - [ ] product name
    - [ ] length is maximum 15 characters
    - [ ] allow only the following special characters: `()`, `[]`, `+`, `-`, `&`, `/`, `_`
    - [ ] must be unique
  - [ ] product price must be greater than 0
  - [ ] product image must start with `http://` or `https://`

- [ ] error handling
  - [ ] return `400 Bad Request` for invalid input
  - [ ] respond a clear error message for each invalid field
