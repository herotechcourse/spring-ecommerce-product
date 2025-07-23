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

- [x] user registration
  - [x] handle `POST /api/members/register` requests
    - [x] receive `email` and `password` in JSON body
    - [x] hash password
    - [x] create user in database
    - [x] return token when successful registration (`200`)

- [x] JWT token generation
  - [x] use hmac sha to sign token with secret key
  - [x] include these claims:
    - [x] user ID
    - [x] email
    - [x] role
  - [x] set token expiration time

- [x] user login
  - [x] handle `POST /api/members/login` requests
    - [x] receive `email` and `password` in JSON body
    - [x] verify credentials against stored users
    - [x] return token when successful login
    - [x] return `403` when failed login

- [ ] Error handling
  - [x] `400 Bad Request` if required fields missing or invalid 
  - [ ] `401 Unauthorized` if authorization header missing or invalid 
  - [x] `403 Forbidden` if failed login



## notes

- generate key via `openssl rand -base64 32`
