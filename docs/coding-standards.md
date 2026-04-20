# Coding Standards

## General

- Use meaningful names
- Keep functions small
- Avoid duplication

---

## Frontend

### Style

- Follow the Airbnb JavaScript Style Guide
- If using TypeScript, follow the Airbnb TypeScript variant

### Vue 3

- Use the **Composition API** (`<script setup>`) — do not use the Options API
- One component per file
- Components: PascalCase (e.g., `TeamCard.vue`, `PeerEvalForm.vue`)
- Variables/functions: camelCase
- Props: camelCase in script, kebab-case in templates

### Vuetify

- Use Vuetify components for all UI elements — do not build custom replacements for components Vuetify provides
- Do not use ElementPlus or any other UI library

### State Management (Pinia)

- One store per domain (e.g., `useAuthStore`, `useTeamStore`)
- Keep stores focused — no cross-domain logic in a single store
- Store files live in `src/features/<domain>/` or `src/shared/stores/` if truly shared

### API Calls (Axios)

- All Axios calls belong in service files (e.g., `authService.js`, `teamService.js`)
- Components never call Axios directly
- Service files live in `src/features/<domain>/services/` or `src/shared/services/`

---

## Backend (Java)

### Naming

- Classes: PascalCase
- Methods/variables: camelCase
- Constants: SCREAMING_SNAKE_CASE
- Class suffix conventions:
  - `*Controller` — REST controllers
  - `*Service` — business logic
  - `*Repository` — data access interfaces
  - `*Entity` — JPA entities
  - `*Request` — inbound DTOs (e.g., `CreateTeamRequest`)
  - `*Response` — outbound DTOs (e.g., `TeamResponse`)

### Spring Boot 4.x

- Use `jakarta.*` imports — **not** `javax.*` (Spring Boot 4.x is based on Jakarta EE 10)
  - e.g., `jakarta.persistence.Entity`, `jakarta.validation.constraints.NotNull`

### Dependency Injection

- Use constructor injection — not field injection (`@Autowired` on fields)
- Declare dependencies as `final` fields

### DTOs

- Use separate Request and Response DTOs — never expose entities directly in API responses
- DTOs live in the `dto/` package within their domain module

### Exception Handling

- Use `@RestControllerAdvice` for all global exception handling
- All exceptions must be caught and returned as `Result` objects (see `/docs/api-guidelines.md`)
- Never let raw stack traces reach the client

### Lombok

- Lombok is used in this project
- Annotate entities and DTOs with `@Data`, `@Getter`, `@Setter`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor` as appropriate
- Do not manually write constructors, getters, or setters that Lombok can generate

---

## API

See `/docs/api-guidelines.md` for all REST conventions, response format, status codes, error handling, pagination, and authentication standards.

---

## Git

### Commit Message Prefixes

| Prefix | Use for |
|--------|---------|
| `feat:` | New feature or use case implementation |
| `fix:` | Bug fix |
| `refactor:` | Code restructure with no behavior change |
| `test:` | Adding or updating tests |
| `docs:` | Documentation changes only |
| `chore:` | Build config, dependency updates, tooling |
| `style:` | Formatting, whitespace — no logic change |
| `build:` | CI/CD pipeline or build system changes |

Examples:
- `feat: add login API`
- `fix: correct peer evaluation submission validation`
- `test: add unit tests for GradeCalculator`

### Branches

Follow the naming convention defined in `/docs/team-workflow.md`: `feature/use-case-name`
