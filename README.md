# spring-ecommerce-product

## Features:

HTTP API that allows users to retrieve, add, update, and delete products.

- HTTP requests and responses must be in JSON format.
- Since no separate database is used at this point, store data in memory using an appropriate Kotlin Collection
  Framework.

---

### Todo

The following tasks are based on the feedback received
in [PR #15](https://github.com/herotechcourse/spring-ecommerce-product/pull/15). These improvements aim to enhance
functionality, readability, and test reliability.

- [x] **remove application.yml**
  For Spring Boot configuration, only needs either application.properties or application.yml

- [x] **Fix Edit/Delete button issue**  
  Resolve the problem where Edit and Delete buttons do not function on the HTML page.

- [x] **Improve constructor readability**  
  If a class constructor has many parameters, split them into multiple lines for clarity.

- [x] **Rename or refactor `Product.toEntity`**
  The current method name may be misleading. Consider renaming it or using a constructor with named arguments.
- [x] Check/Read about Entity and DTO

- [x] **Remove manual ID generation**  
  H2 database handles IDs via AUTO_INCREMENT. Remove controller logic that manually assigns IDs.

- [x] **Adjust controller for JSON responses**  
  Replace `@Controller` with `@RestController` or add `@ResponseBody` to return JSON directly.

- [x] **Clarify update return type**  
  Refactor `ProductRepository.update()` to return a meaningful type (e.g., `Boolean`) indicating success or failure.

- [x] **Optimize database column types**  
  Update `schema.sql` to use appropriate types like `VARCHAR(n)` instead of `TEXT` for `image_url`.

---

### Test

- [x] **Review `@DirtiesContext` usage**  
  Evaluate if resetting the context before each test is necessary. Remove if redundant.

- [x] **Verify each response field**  
  Ensure that tests validate not only response status and list size, but also every field in the returned objects.

- [x] **Separate test fixtures**  
  Move hardcoded `Product` objects in test classes into a dedicated fixture or utility class.

- [x] **Add tests for uncovered features**  
  Identify and write tests for product-related features that are currently missing test coverage.
    
---

### Additional
- [x] Change price to BigDecimal
- [x] Create an object to collect all SQL lines