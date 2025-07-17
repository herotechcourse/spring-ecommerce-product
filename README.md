# spring-ecommerce-product

## Step 1-1
Implements a simple HTTP API that allows users to **retrieve**, **add**, **update**, and **delete** products.<br/>
HTTP requests and responses must be in **JSON** format. <br/>
Since no separate database is used at this point, data is stored in memory using an appropriate Kotlin Collection Framework. <br/>

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
However, if you're interested in asynchronous behavior using JavaScript, feel free to use the previously built product API to apply AJAX or similar techniques.<br/>
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



