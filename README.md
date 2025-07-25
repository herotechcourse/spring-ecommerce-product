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
  - [x] retrieve most added products via `GET /admin/stats/top-products`
    - [x] returns top 5 products most frequently added to carts in the last 30 days 
    - [x] if counts are equal, sort by most recently added 
    - [x] response includes:
      - [x] product name
      - [x] number of times added
      - [x] most recent added time
  - [ ] retrieve recently active members via `GET /admin/stats/recent-members`
    - [ ] returns members who added items to their cart in the last 7 days 
      - [ ] no duplicate members even if multiple items were added 
      - [ ] response includes:
        - [ ] member ID 
        - [ ] name
        - [ ] email

- [x] restrict admin access to /admin/** endpoints 
  - [x] only allow users with ADMIN role
  - [x] implement HandlerInterceptor for admin
    - [x] extract and validate JWT token 
    - [x] check user role
    - [x] respond with 401 Unauthorized if access is denied

## interpretation of requirements

**Note on Statistics**

The query for top 5 most added products counts how often a product was added to carts over the last 30 days. Each row in the `cart_item_history` table with an `ADD` action counts as one addition.

So if one user adds the same product 5 times in separate calls, and 5 rows are logged, it counts as 5 additions.
If a user adds 5 units of a product in a single call and only one row is logged, it counts as 1 addition.

This means the count reflects the number of times products were added, not the quantity added.


## notes

- generate key via `openssl rand -base64 32`

## future work

- replace `jdbcTemplate` with `jdbcClient`
