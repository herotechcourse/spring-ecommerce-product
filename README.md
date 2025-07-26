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

### Validate
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
#### Member
-[ ] Validate `ProductRequest.email`
  -[x] Must be Email format
  -[x] Must be **unique** across all members.
  -[x] Not black
-[ ] Validate `ProductRequest.password`
  -[x] Basic size check
  -[ ] more?

### Refactor
-[x] The application is now structured into Controller, Service, and Repository layers in Member.
  - Business logic is handled in the Service layer, which throws specific custom exceptions depending on the condition.
  Repository methods are allowed to return null, and the responsibility for interpreting the result lies in the Service.
  - related with...  "In `ProductStore.update()`, `ProductStore.delete()` throw an exception".
-[x] The application is now structured into Controller, Service, and Repository layers in Product.


### Member controller
- [x] implement `register`
  - success: 201 created
  - failure
    -[x] duplicated email: 409 Conflict
    -[x] duplicated id: 422 Unprocessable Entity ->?
    -[x] can not find data with id: 424 Failed Dependency ->?
- [ ] implement `login`
  - success: 200 ok
  - failure
    -[ ] Invalid email or password → 401 Unauthorized 
    -[ ] Account isn't activated or suspended → 403 Forbidden 
    -[ ] Too many failed attempts → 429 Too Many Requests
