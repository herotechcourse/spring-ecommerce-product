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

- [x] error handling
  - [x] `400 Bad Request` if required fields missing or invalid 
  - [x] `401 Unauthorized` if authorization header missing or invalid 
  - [x] `403 Forbidden` if failed login

#### step 2-3

- [x] set up authenticated access
  - [x] implement interceptor
- [x] test authentication

- [x] add `CartItem` entity/model (contains productId, quantity, userId)
- [x] add `CartRepository` and according table in database 
- [x] add `CartService` with logic to
  - [x] add item `POST /api/cart`
    - [x] if item already in cart, increase its quantity by 1
  - [x] retrieve item(s) `GET /api/cart`
  - [x] remove item `DELETE /api/cart/{productID}`
  - [x] change quantity of item `PATCH /api/cart/{productId}`
    - [x] if quantity is 0, remove item

- [x] security and error handling
  - [x] require a valid Authorization: Bearer <token> header for cart endpoints 
  - [x] token is missing or invalid: `401 Unauthorized`
  - [x] payload is invalid: `400 Bad Request`
  - [x] item to update/delete is not in cart: `404 Not Found`

#### step 2-4

- [ ] admin statistics
  - [ ] retrieve most added products via `GET /admin/stats/top-products`
    - [ ] returns top 5 products most frequently added to carts in the last 30 days 
    - [ ] if counts are equal, sort by most recently added 
    - [ ] response includes:
      - [ ] product name
      - [ ] number of times added
      - [ ] most recent added time
  - [ ]  retrieve recently active members via `GET /admin/stats/recent-members`
    - [ ] returns members who added items to their cart in the last 7 days 
      - [ ] no duplicate members even if multiple items were added 
      - [ ] response includes:
        - [ ] member ID 
        - [ ] name
        - [ ] email

- [ ] restrict admin access to /admin/** endpoints 
  - [ ] only allow users with ADMIN role
  - [ ] implement HandlerInterceptor to:
    - [ ] extract and validate JWT token 
    - [ ] check user role (from token or DB?)
    - [ ] respond with 401 Unauthorized if access is denied

## notes

- generate key via `openssl rand -base64 32`

## future work

- replace `jdbcTemplate` with `jdbcClient`
