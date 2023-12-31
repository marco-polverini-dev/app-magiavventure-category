# Magiavventure - Category
[![codecov](https://codecov.io/gh/marco-polverini-dev/app-magiavventure-category/graph/badge.svg?token=ZT248ZN1S1)](https://codecov.io/gh/marco-polverini-dev/app-magiavventure-category)

This service allows to create/update and find the categories used by Magiavventure App.

## Configuration
The properties exposed to configure this project are:
```properties
spring.data.mongodb.uri="string"                                    # Uri for mongo connection
spring.data.mongodb.uuid-representation="string"                    # Format representation for uuid - use default
spring.data.mongodb.auto-index-creation=boolean                     # Create index automatically
logging.level.app.magiavventure="string"                            # Logging level package magiavventure
category.errors.error-messages.{error-key}.code="string"            # The exception key error code
category.errors.error-messages.{error-key}.message="string"         # The exception key error message
category.errors.error-messages.{error-key}.description="string"     # The exception key error description
category.errors.error-messages.{error-key}.status=integer           # The exception key error status
```

## Error message map
The error message map is a basic system for return the specific message in the error response, 
the configuration path at the moment is only for one branch **error-messages**.
This branch setting a specific error message to **app.magiavventure.category.error.CategoryException**


## API
### Create Category
This request allow to create a new category

`POST /v1/categories`

```bash
curl -X 'POST' \
  '<hostname>:<port>/v1/categories' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "<name>",
  "background": "<background>",
  "active": <active>
}'
```
`Response`

```text
HTTP/1.1 201 CREATED
connection: keep-alive 
content-type: application/json 
date: Sun,10 Dec 2023 09:24:25 GMT 
keep-alive: timeout=60 
transfer-encoding: chunked 

{
  "id": "<id>",
  "name": "<name>",
  "background": "<background>"
}
```

`Errors`

List of code errors that the api can return

```properties
category-exists     #(403 - in case there is already a category with the same name)
```
### Update Category
This request allow to update a category

`PUT /v1/categories`

```bash
curl -X 'PUT' \
  '<hostname>:<port>/v1/categories' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "id": "<id>",
  "name": "<name>",
  "background": "<background>",
  "active": <active>
}'
```
`Response`

```text
HTTP/1.1 200 OK
connection: keep-alive 
content-type: application/json 
date: Sun,10 Dec 2023 09:24:25 GMT 
keep-alive: timeout=60 
transfer-encoding: chunked 

{
  "id": "<id>",
  "name": "<name>",
  "background": "<background>"
}
```

`Errors`

List of code errors that the api can return

```properties
category-exists     #(403 - in case there is already a category with the same name)
category-not-found  #(404 - in case a category not found with the id in body request)
```

### Find All Categories
This request allow to find all categories

`GET /v1/categories`

```bash
curl -X 'GET' \
  '<hostname>:<port>/v1/categories' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json'
```
`Response`

```text
HTTP/1.1 200 OK
connection: keep-alive 
content-type: application/json 
date: Sun,10 Dec 2023 09:24:25 GMT 
keep-alive: timeout=60 
transfer-encoding: chunked 

[
  {
    "id": "<id>",
    "name": "<name>",
    "background": "<background>"
  }
]
```

### Find Category by ID
This request allow to find a category by ID

`PUT /v1/categories/{id}`

```bash
curl -X 'GET' \
  '<hostname>:<port>/v1/categories/{id}' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json'
```
`Response`

```text
HTTP/1.1 200 OK
connection: keep-alive 
content-type: application/json 
date: Sun,10 Dec 2023 09:24:25 GMT 
keep-alive: timeout=60 
transfer-encoding: chunked 

{
  "id": "<id>",
  "name": "<name>",
  "background": "<background>"
}
```

`Errors`

List of code errors that the api can return

```properties
category-not-found  #(404 - in case a category not found)
```

### Delete Category by ID
This request allow to delete a category by ID

`DELETE /v1/categories/{id}`

```bash
curl -X 'DELETE' \
  '<hostname>:<port>/v1/categories/{id}' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json'
```
`Response`

```text
HTTP/1.1 204 NO-CONTENT
connection: keep-alive 
content-type: application/json 
date: Sun,10 Dec 2023 09:24:25 GMT 
keep-alive: timeout=60 
transfer-encoding: chunked 
```

`Errors`

List of code errors that the api can return

```properties
category-not-found  #(404 - in case a category not found)
```

## Running local
For run the service in local environment need to execute following actions

### Running service
Run the service with the following profile:
1. "local" for local environment configuration