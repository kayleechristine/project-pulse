# Project Pulse — Database Schema

## Overview

All tables use surrogate integer or UUID primary keys. Foreign key relationships are enforced at the database level. Flyway manages migrations — files are append-only and must never be edited after creation.

---

## Entity Relationship Diagram

```mermaid
erDiagram

    users {
        uuid id PK
        string first_name
        string last_name
        string email UK
        string password_hash
        string role "ADMIN | INSTRUCTOR | STUDENT"
        string status "ACTIVE | DEACTIVATED"
        timestamp created_at
    }

    invitation_tokens {
        uuid id PK
        uuid user_id FK
        string token UK
        string role "STUDENT | INSTRUCTOR"
        boolean used
        timestamp expires_at
        timestamp created_at
    }

    sections {
        int id PK
        string name UK "e.g. 2024-2025"
        date start_date
        date end_date
        int rubric_id FK
    }

    rubrics {
        int id PK
        string name UK
        timestamp created_at
    }

    rubric_criteria {
        int id PK
        int rubric_id FK
        string name
        string description
        decimal max_score
        int sort_order
    }

    teams {
        int id PK
        string name UK
        string description
        string website_url
        int section_id FK
    }

    team_members {
        int id PK
        int team_id FK
        uuid user_id FK
        string role "STUDENT | INSTRUCTOR"
    }

    active_weeks {
        int id PK
        int section_id FK
        date week_start "Monday of the week"
        boolean is_active
    }

    war_activities {
        int id PK
        uuid student_id FK
        int week_id FK
        string category "DEVELOPMENT | TESTING | BUGFIX | COMMUNICATION | DOCUMENTATION | DESIGN | PLANNING | LEARNING | DEPLOYMENT | SUPPORT | MISCELLANEOUS"
        string activity
        string description
        decimal planned_hours
        decimal actual_hours
        string status "IN_PROGRESS | UNDER_TESTING | DONE"
    }

    peer_evaluations {
        int id PK
        uuid evaluator_id FK
        uuid evaluatee_id FK
        int week_id FK
        text public_comments
        text private_comments
        timestamp submitted_at
    }

    peer_evaluation_items {
        int id PK
        int evaluation_id FK
        int criterion_id FK
        int score "integer 1–max_score"
    }

    users ||--o{ invitation_tokens : "invited via"
    users ||--o{ team_members : "belongs to"
    users ||--o{ war_activities : "submits"
    users ||--o{ peer_evaluations : "evaluates (evaluator)"
    users ||--o{ peer_evaluations : "is evaluated (evaluatee)"

    sections ||--o{ teams : "contains"
    sections ||--o{ active_weeks : "has"
    sections }o--|| rubrics : "uses"

    teams ||--o{ team_members : "has"

    rubrics ||--o{ rubric_criteria : "defines"

    active_weeks ||--o{ war_activities : "scopes"
    active_weeks ||--o{ peer_evaluations : "scopes"

    peer_evaluations ||--o{ peer_evaluation_items : "scored by"
    rubric_criteria ||--o{ peer_evaluation_items : "measured by"
```

---

## Flyway Migration Order

| File | Owner | Contents |
|------|-------|----------|
| `V1__create_users.sql` | Shared | `users`, `invitation_tokens` |
| `V2__create_sections_rubrics_teams_weeks.sql` | P1 | `sections`, `rubrics`, `rubric_criteria`, `teams`, `active_weeks` |
| `V3__create_invitations_team_members.sql` | P2 | `invitation_tokens` indexes, `team_members` |
| `V4__create_war_activities.sql` | P2 | `war_activities` |
| `V5__create_peer_evaluations.sql` | P2 | `peer_evaluations`, `peer_evaluation_items` |
| `V6__create_instructor_assignments.sql` | P3 | Instructor-specific indexes and constraints |

> **Rule:** Never edit an existing migration file. Add a new versioned file for any schema change.

---

## Key Constraints (for agent reference)

- `users.email` — unique across all roles
- `sections.name` — unique (e.g. `2024-2025`)
- `teams.name` — unique within a section
- `rubrics.name` — unique; when a rubric is edited at section-creation time, the system duplicates it first (a new rubric is created, not mutated)
- `peer_evaluations` — unique on `(evaluator_id, evaluatee_id, week_id)`; one submission per evaluator per evaluatee per week
- `peer_evaluation_items` — unique on `(evaluation_id, criterion_id)`
- `team_members` — unique on `(team_id, user_id)`

---

## Grade Calculation Algorithm (UC-31)

Used in `GradeCalculator.java` and `PeerEvalReportService.java`:

```
For each student S in a week W:
  1. Collect all peer_evaluations where evaluatee_id = S and week_id = W
  2. For each evaluation E:
       total_score(E) = SUM of peer_evaluation_items.score for that evaluation
  3. grade(S, W) = AVG(total_score(E)) across all evaluations received

Example:
  Tim gives John:  10 + 9 + 10 + 9 + 10 + 10 = 58
  Lily gives John:  5 + 5 + 10 + 10 + 10 + 10 = 50
  John's grade = (58 + 50) / 2 = 54
```
