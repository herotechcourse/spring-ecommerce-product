# spring-ecommerce-product

## Features:

HTTP API that allows users to retrieve, add, update, and delete products.

- HTTP requests and responses must be in JSON format.
- Since no separate database is used at this point, store data in memory using an appropriate Kotlin Collection
  Framework.

---

## Step 2
### ProductRequest Name
- [ ] Must be **no more than 15 characters**, including spaces.
- [ ] Allowed special characters: `()`, `[]`, `+`, `-`, `&`, `/`, `_`
- [ ] All other special characters are **not allowed**.
- [ ] Must be **unique** across all products.

### ProductRequest Price
- [ ] Must be **greater than 0**.

### ProductRequest Image URL
- [ ] Must start with **http://** or **https://**
