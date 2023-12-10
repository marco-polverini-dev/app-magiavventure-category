# Magiavventure - Category
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
