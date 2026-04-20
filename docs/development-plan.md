# Development Plan

## Phase 1 – Foundation

- Initialize frontend and backend
- Setup database
- Setup CI/CD
- Establish domain module structure
- Define coding standards

---

## Phase 2 – Core Domains

| Module | Owner | Entities |
|--------|-------|----------|
| `auth` | Shared | User, InvitationToken, JWT |
| `section` | Leiton | Section, Rubric, RubricCriterion, ActiveWeek |
| `team` | Leiton | Team, TeamMember |
| `war` | Kaylee | WarActivity |
| `evaluation` | Kaylee | PeerEvaluation, PeerEvaluationItem |
| `user` | Amarachi | User account lifecycle, instructor admin |
| `report` | Amarachi | WAR and peer evaluation report generation |
| `shared` | All | Result, StatusCode, exceptions, utilities |

---

## Team Ownership

### Leiton — Admin / Section Setup / Team Management

Modules: `section`, `team`

Use cases: UC-1, UC-2, UC-3, UC-4, UC-5, UC-6, UC-7, UC-8, UC-9, UC-10, UC-14

Frontend:
- Admin dashboard
- Section list/detail pages
- Team list/detail pages
- Rubric builder/editor
- Active weeks setup UI

Backend/Database:
- Section, Team, Rubric, RubricCriterion, ActiveWeek
- CRUD endpoints
- Duplicate-name validation
- Section/team relationships

---

### Kaylee — Student Experience / WAR / Peer Evaluation

Modules: `war`, `evaluation`

Use cases: UC-25, UC-26, UC-27, UC-28, UC-29
Support: UC-11, UC-12, UC-13

Frontend:
- Student registration/login/profile
- Weekly WAR form
- Peer evaluation submission form
- Student report page

Backend/Database:
- WarActivity, PeerEvaluation, PeerEvaluationItem
- Active-week validation
- "Previous week only" submission logic
- Report aggregation for student view

---

### Amarachi — Instructor / People Management / Reporting

Modules: `user`, `report`

Use cases: UC-15, UC-16, UC-17, UC-18, UC-19, UC-20, UC-21, UC-22, UC-23, UC-24, UC-30, UC-31, UC-32, UC-33, UC-34

Frontend:
- Instructor registration/login
- Instructor roster/student lookup pages
- Report filters and report views
- Admin instructor management pages

Backend/Database:
- Instructor/team assignment tables
- Student lookup/report APIs
- WAR report queries
- Peer evaluation grade calculation
- Account activation/deactivation
- Email invitation hooks

---

## Phase 3 – Use Case Implementation

Within each domain:

1. Implement core entities and Flyway migration
2. Implement repository layer
3. Implement service logic
4. Implement controller endpoints
5. Implement frontend views

---

## Shared Responsibilities

- `auth` module — all three members depend on this; coordinate changes
- `shared/` module — Result, StatusCode, exceptions; changes require team review
- Flyway migrations — append-only; see `/docs/database-design.md` for migration order and ownership
- CI/CD pipeline
- Deployment
- API consistency

---

## Definition of Done

- Meets requirement specification
- Follows architecture rules
- Code reviewed
- Tested
- Integrated successfully
