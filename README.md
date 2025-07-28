# spring-ecommerce-product

## Features:

HTTP API that allows users to retrieve, add, update, and delete products.

- HTTP requests and responses must be in JSON format.
- Since no separate database is used at this point, store data in memory using an appropriate Kotlin Collection
  Framework.

---

## Review

### Code Structure & Refactoring

-[x] Consider switching from `application.properties` to `application.yaml`
-[x] In `ProductStore.update()`, `ProductStore.delete()` throw an **exception**.
-[x] Remove the `Product.toResponse()` extension function and move it inside a relevant class
 Prevent the model from depending on higher layers like Controller or Response.
-[x] Likewise, move `ProductRequest.toEntity()` from an extension function to an internal method
-[x] Avoid unnecessary use of extensions → Delete all extensions

### Class Design Review

-[x] Explain or reconsider the use of `data class` for `Product`, `ProductRequest`, and `ProductResponse`
 Check if `equals`, `hashCode`, and `toString` are truly needed.

### Cleanup & Simplification

- Remove manual ID generation logic from `ProductStore.createAndReturnId()`
  → _this is not ID creator, ask reviewer again_.
-[ ] In `TextFixture.kt`, move `FLAT_WHITE` and `AMERICANO` under the `Dummy` object or remove `Dummy` altogether
-[x] Reconsider the placement and responsibility of `AssertTemplate` → Delete
 Decide whether assertion logic should be part of the fixture or separated.

---

## Step 2

### 2-1 Validate 
#### Product
-[x] Validate `ProductRequest.name`
  -[x] Must be **no more than 15 characters**, including spaces.
  -[x] Allowed special characters: `()`, `[]`, `+`, `-`, `&`, `/`, `_`
  -[x] All other special characters are **not allowed**.
  -[x] Must be **unique** across all products.
-[x] Validate `ProductRequest.price`
  -[x] Must be **greater than 0.00**.
-[x] Validate `ProductRequest.imageUrl`
  -[x] Must start with **http://** or **https://**

---

#### Member
-[x] Validate `ProductRequest.email`
  -[x] Must be Email format
  -[x] Must be **unique** across all members.
  -[x] Not black
-[x] Validate `ProductRequest.password`
  -[x] Basic size check
  -[ ] more?

#### Refactor
-[x] The application is now structured into Controller, Service, and Repository layers in Member.
  - Business logic is handled in the Service layer, which throws specific custom exceptions depending on the condition.
  Repository methods are allowed to return null, and the responsibility for interpreting the result lies in the Service.
  - related with...  "In `ProductStore.update()`, `ProductStore.delete()` throw an exception".
-[x] The application is now structured into Controller, Service, and Repository layers in Product.


#### Member controller
- [x] implement `register`
  - success: 201 created
  - failure
    -[x] duplicated email: 409 Conflict
    -[x] duplicated id: 422 Unprocessable Entity ->?
    -[x] cannot find data with id: 424 Failed Dependency ->?
- [x] implement `login`
  - success: 200 ok
  - failure
    -[x] Invalid email or password → 401 Unauthorized

---

### 2-2, 2-3 Cart 
#### Create Entity
-[x] Implement `CartItem entity`
  -[x] Fields: id, memberId, productId, quantity, createdAt, updatedAt
  -[x] Ensure updatedAt is automatically updated in DB using `ON UPDATE`
#### Create Request / Response DTOs
-[x] `CartRequest` (fields: productId, quantity)
-[x] `CartResponse` (fields: id, productId, quantity, updatedAt)
#### Update Schema SQL
-[x] Add `cart_items` table to schema.sql
#### Implement Repository
-[x] Create `repository` methods for:
  -[x] Find all cart items by member ID
  -[x] Add or update a cart item
  -[x] Remove a cart item
#### Implement Service
-[x] `CartService` methods:
  - upsertCartItems
  - upsertCartItem
  - deleteBy
#### Create Controller
-[x] CartController endpoints:
  -[x] `GET /api/cart` → get cart items
  -[x] `POST /api/cart` → add/update item
  -[x] `DELETE /api/cart/{productId}` → remove item
  -[x] Use `@LoginMember` to inject the authenticated Member. 
#### Authentication Setup
-[x] Create `@LoginMember` annotation
-[x] Implement `WebConfig`
-[x] Implement `LoginMemberArgumentResolver`
-[ ] Implement `AuthInterceptor` to validate tokens globally -> step 2-4?
-[x] Register resolver and interceptor in `WebConfig`
#### Exception Handling
-[x] Define UnauthorizedException for missing or invalid tokens

---

### 2-4- Admin Statistics
### Database Schema Updates
-[x] Fix `cart_items` table schema in `schema.sql`
  -[x] Change `member_id` from `VARCHAR(255)` to `BIGINT`
  -[x] Change `product_id` from `VARCHAR(255)` to `BIGINT`
  -[ ] Add proper foreign key constraints if needed

### Create Statistics DTOs
-[x] Create `ProductStatsResponse`
  -[x] Fields: productId, addCount, lastAddedAt
-[x] Create `MemberStatsResponse`
  -[x] Fields: memberId, email

### Implement Statistics Repository
-[x] Add statistics methods to `CartRepository` or create new `AdminRepository`

### Create Statistics Service
-[x] Create `AdminService`
  -[x] `getTopProducts()` - business logic for top products
  -[x] `getActiveMembers()` - business logic for active members

### Create Admin Controller
-[x] Create `AdminController`, `@RequestMapping("/api/admin")`
  -[x] `GET /stats/products/top` - retrieve top 5 products
  -[x] `GET /stats/members/active` - retrieve active members

### Add SQL Constants
-[x] Create `AdminConstsSQL` or add to existing SQL constants
  -[x] Top products query (30 days, with tie-breaking)
  -[x] Active members query (7 days, distinct)

### (Optional) Admin Role Authorization
-[ ] Add `role` field to `Member` entity and database
-[ ] Implement `LoginInterceptor` (currently TODO in `WebConfig`)
  -[ ] Apply to `/api/admin/**` endpoints
  -[ ] Check for `ADMIN` role in JWT token or member data
-[ ] Update JWT token creation to include role information
-[ ] Update `LoginMemberArgumentResolver` to handle role-based access

### Testing & Validation
-[x] Test top products endpoint with sample data
-[x] Test active members endpoint with sample data
-[x] Verify correct ordering and tie-breaking logic
-[ ] Test admin role restrictions (if implemented)

---