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

#### step 2-1

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

#### step 2-2

- [ ] user registration
  - [ ] handle `POST /api/members/register` requests
    - [x] receive `email` and `password` in JSON body
    - [x] hash password
    - [x] create user in database
    - [ ] return token when successful registration (`200`)

- [ ] user login
  - [ ] handle `POST /api/members/login` requests
    - [ ] receive `email` and `password` in JSON body
    - [ ] verify credentials against stored users
    - [ ] return token when successful login
    - [ ] return `403` when failed login

- [ ] JWT token generation
  - [ ] use hmac sha to sign token with secret key
  - [ ] include these claims:
    - [ ] user ID
    - [ ] email
    - [ ] role
  - [ ] set token expiration time?

- [ ] Error handling
  - `400 Bad Request` if required fields missing or invalid 
  - `401 Unauthorized` if authorization header missing or invalid 
  - `403 Forbidden` if failed login

