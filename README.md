# spring-ecommerce-product

## Features:

HTTP API that allows users to retrieve, add, update, and delete products.

- HTTP requests and responses must be in JSON format.
- Since no separate database is used at this point, store data in memory using an appropriate Kotlin Collection
  Framework.

---
## Review
### Code Structure & Refactoring

-[ ] Consider switching from `application.properties` to `application.yaml`
-[ ] In `ProductStore.update()`, `ProductStore.delete()` throw an **exception** 
→ try-catch in ProductStore/ProductController
-[x] Remove the `Product.toResponse()` extension function and move it inside a relevant class
  Prevent the model from depending on higher layers like Controller or Response.
-[x] Likewise, move `ProductRequest.toEntity()` from an extension function to an internal method 
-[x] Avoid unnecessary use of extensions → Delete all extensions

### Class Design Review

-[x] Explain or reconsider the use of `data class` for `Product`, `ProductRequest`, and `ProductResponse`
  Check if `equals`, `hashCode`, and `toString` are truly needed.

### Cleanup & Simplification

-[ ] Remove manual ID generation logic from `ProductStore.createAndReturnId()`
  → this is not ID creator, ask to review again.
-[ ] In `TextFixture.kt`, move `FLAT_WHITE` and `AMERICANO` under the `Dummy` object or remove `Dummy` altogether
-[x] Reconsider the placement and responsibility of `AssertTemplate` → Delete
  Decide whether assertion logic should be part of the fixture or separated.

### Test Design

-[ ] Keep test data inline in `ProductControllerTest`, rather than extracting into helper methods
  No need to refactor if current structure is clear and simple.


---
## Step 2
### ProductRequest Name
- [x] Must be **no more than 15 characters**, including spaces.
- [x] Allowed special characters: `()`, `[]`, `+`, `-`, `&`, `/`, `_`
- [x] All other special characters are **not allowed**.
- [x] Must be **unique** across all products.

### ProductRequest Price
- [x] Must be **greater than 0.00**.

### ProductRequest Image URL
- [x] Must start with **http://** or **https://**

