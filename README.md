# spring-ecommerce-product

## Step 1-1

Implements a simple HTTP API that allows users to **retrieve**, **add**, **update**, and **delete** products.<br/>
HTTP requests and responses must be in **JSON** format. <br/>
Since no separate database is used at this point, data is stored in memory using an appropriate Kotlin Collection
Framework. <br/>

### Feature list

- [x] resource representation class to store data
    - [x] is wrapped in a collection
- [x] HTTP requests and responses in JSON format
- [x] retrieve product - GET
- [x] add new product - POST
- [x] update product parameters - PUT
- [x] delete product from products - DELETE

## Step 1-2

Implements an admin interface that allows users to view, add, update, and delete products. <br/>

Use Thymeleaf to implement server-side rendering (SSR). <br/>
The default behavior should be based on traditional HTML form submission and page navigation. <br/>
However, if you're interested in asynchronous behavior using JavaScript, feel free to use the previously built product
API to apply AJAX or similar techniques.<br/>
For product images, do not upload files; instead, use direct image URLs. <br/>

### Feature list

- [x] admin interface
    - [x] uses Thymeleaf
    - [x] based on HTML form submission and page navigation
    - [x] use direct image URLs for products

## Step 1-3

Refactor the application using an H2 in-memory database instead of Kotlin collections.
The required database tables are initialized automatically when the application starts.

### Feature List

- [x] connect to a database
    - [x] Add the required Gradle dependencies
    - [x] Define the database schema
    - [x] Configure the database settings
    - [x] use Spring's JdbcTemplate

## Considerations

- TODO read about difference btw RestController and Controller
- TODO why do we combine Controller and ControllerView
- read and learn SQL commands in H2 console

## Step 2-1

When a product is created or updated, the client may send invalid data.
In such cases, your application must respond with enough information for the client to understand what is wrong and why.

### Feature List

- [x] Product Name
    - [x] Product name is no longer than 15 characters (including spaces)
    - [x] Allow only specific special characters: `( ), [ ], +, -, &, /, _`. All other special characters are not
      allowed
    - [x] The name must be unique across all products

- [x] Product Price
    - [x] Must be greater than 0

- [x] Product Image URL
    - [x] Must start with ``http://`` or ``https://``

## Step 2-2

Implement user account features including registration, login, and authentication so that users can access member-only
functionality in the future.

### Feature List

- [x] create a ``Member`` data class
    - [x] Member has ID
    - [x] Member has email
    - [x] Member has password
    - [x] Member has role (initial "USER")

- [x] add Members table in ``schema.sql``

- [x] Create `MemberRepository` class - repository for working with the MEMBERS table, similar to `ProductRepository`

- [x] create ``TokenRequest`` data class
    - [x] has email
    - [x] has password

- [x] create ``TokenResponse`` data class
    - [x] has accessToken: String

- [x] create ``MemberResponse`` data class
    - [x] has id
    - [x] has email

- [x] create interface ``AuthorizationExtractor``
- [x] create  ``BearerAuthorizationExtractor``

  Needed to extract the token from the Authorization: Bearer <token> header

- [x] create class `AuthorizationException` - exception for handling authorization errors
- [x] create class `JwtTokenProvider` - class for creating, validating and extracting payload from JWT
- [x] create `AuthService` class to handle authentication logic, including token generation and user registration
- [x] create `AuthController` class with endpoints for user registration (`/api/members/register`), login (
  `/api/members/login`), and retrieving user info (`/api/members/me`)

    - implement authentication controller with endpoints for registration, login, and user info

## Step 2-3

Using the token received after login, implement functionality that allows the user to add products to their own cart

### Feature List

- [ ] Users can retrieve the list of products in their cart.
    - [ ] GET `/api/wishes` returns list of products in JSON format
    - [ ] Requires valid JWT token in `Authorization: Bearer <token>` header

- [ ] Users can add products to their cart.
    - [ ] POST `/api/wishes` accepts product ID in JSON body
    - [ ] Requires valid JWT token in `Authorization: Bearer <token>` header
    - [ ] Validates product existence and prevents duplicates

- [ ] Users can remove products from their cart.
    - [ ] DELETE `/api/wishes/{productId}` removes product by ID
    - [ ] Requires valid JWT token in `Authorization: Bearer <token>` header

- [x] Add `CART_ITEMS` table in `schema.sql`
    - [x] Links `member_id` to `MEMBERS` and `product_id` to `PRODUCTS`

- [x] Create `CartItem` data class
   - [x] Contains `id`, `memberId`, `productId`

- [x] Create `CartRequest` data class
  - [x] Contains `productId` with validation

- [x] Create `CartResponse` data class
  - [x] Contains `id` and `Product` object

- [ ] Create `CartRepository` class
  - [ ] Uses `JdbcTemplate` to manage `CART_ITEMS` table

- [ ] Create `CartService` class
  - [ ] Handles business logic for cart operations

- [ ] Create `WishController` class
    - [ ] Implements `/api/wishes` endpoints with `@LoginMember` for authentication

- [x] Create `LoginMember` annotation and `LoginMemberArgumentResolver`
  - [x] Injects authenticated `Member` into controller methods

- [x] Create `CheckLoginInterceptor`
  - [x] Protects `/api/wishes/**` routes with token validation