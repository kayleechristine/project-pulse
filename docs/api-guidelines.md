# API Guidelines

## REST Conventions

- GET /api/items — list or search
- GET /api/items/{id} — get by ID
- POST /api/items — create
- PUT /api/items/{id} — update
- DELETE /api/items/{id} — delete

---

## Response Format

All responses are wrapped in a `Result` object (`com.tcu.projectpulse.shared.Result`):

```json
{
  "flag": true,
  "code": 200,
  "message": "Find All Success",
  "data": {}
}
```

Fields:

- `flag` — `true` for success, `false` for failure
- `code` — application-level status code (see Status Codes below)
- `message` — human-readable response message
- `data` — response payload (object, array, or null)

---

## Status Codes

Defined in `com.tcu.projectpulse.shared.StatusCode`:

| Code | Constant              | Meaning                              |
|------|-----------------------|--------------------------------------|
| 200  | SUCCESS               | Request succeeded                    |
| 400  | INVALID_ARGUMENT      | Bad request (e.g., invalid params)   |
| 401  | UNAUTHORIZED          | Authentication failed                |
| 403  | FORBIDDEN             | No permission                        |
| 404  | NOT_FOUND             | Resource not found                   |
| 409  | CONFLICT              | Resource already exists              |
| 423  | LOCKED                | Resource is locked                   |
| 500  | INTERNAL_SERVER_ERROR | Server internal error                |

---

## Error Handling

- Use `@RestControllerAdvice` to handle exceptions globally
- All exceptions should be caught and returned as `Result` objects
- Do not let raw stack traces leak to the client
- Example error response:

```json
{
  "flag": false,
  "code": 404,
  "message": "User not found with id 42",
  "data": null
}
```

---

## Pagination

Use Spring Data's `Pageable` and `Page` for paginated endpoints.

- Clients pass pagination parameters as query params: `?page=0&size=10&sort=name,asc`
- The `data` field in the `Result` contains the `Page` object, which includes:
  - `content` — the list of items
  - `totalElements` — total number of items
  - `totalPages` — total number of pages
  - `number` — current page number (0-based)
  - `size` — page size

Example response:

```json
{
  "flag": true,
  "code": 200,
  "message": "Find All Success",
  "data": {
    "content": [],
    "totalElements": 50,
    "totalPages": 5,
    "number": 0,
    "size": 10
  }
}
```

---

## Authentication

- **Login:** `POST /api/auth/login` with a JSON body containing `email` and `password`
- **Subsequent requests:** JWT token in the `Authorization` header

```
Authorization: Bearer <token>
```
