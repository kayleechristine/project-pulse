<p align="center">
  <img src="https://hogwartsartifactsonline.blob.core.windows.net/artifact-image-container/logo.png" alt="Project Pulse Logo" width="400" />
  <br/>
  <a href="https://github.com/kayleechristine/project-pulse/actions/workflows/ci.yml" style="text-decoration: none;">
    <img src="https://img.shields.io/github/actions/workflow/status/kayleechristine/project-pulse/ci.yml?logo=apachemaven&label=Maven%20Build" alt="Maven Build Status">
  </a>
  <a href="https://github.com/kayleechristine/project-pulse/actions/workflows/deploy.yml" style="text-decoration: none;">
    <img src="https://img.shields.io/github/actions/workflow/status/kayleechristine/project-pulse/deploy.yml?logo=microsoftazure&label=Azure%20Deployment" alt="Azure Deployment Status">
  </a>
  <img src="https://img.shields.io/badge/dynamic/json?url=https%3A%2F%2Fraw.githubusercontent.com%2Fkayleechristine%2Fproject-pulse%2Fmain%2Ffrontend%2Fpackage.json&query=%24.dependencies.vue&label=Vue" alt="Vue Version">
  <img src="https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fkayleechristine%2Fproject-pulse%2Fmain%2Fbackend%2Fpom.xml&query=%2F*%5Blocal-name()%3D'project'%5D%2F*%5Blocal-name()%3D'properties'%5D%2F*%5Blocal-name()%3D'java.version'%5D&label=Java" alt="Java Version">
  <img src="https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fkayleechristine%2Fproject-pulse%2Fmain%2Fbackend%2Fpom.xml&query=%2F*%5Blocal-name()%3D'project'%5D%2F*%5Blocal-name()%3D'parent'%5D%2F*%5Blocal-name()%3D'version'%5D&label=Spring%20Boot" alt="Spring Boot Version">
  <img src="https://img.shields.io/badge/Spring%20Security-6.3-green?logo=springsecurity" alt="Spring Security Version">
  <img src="https://img.shields.io/github/license/kayleechristine/project-pulse" alt="License">
</p>

**Project Pulse** is a web application designed to support senior design or capstone projects where students work in teams. The goal is to create a structured and transparent environment for students and instructors. By encouraging frequent reporting and peer evaluation, the platform aims to foster collaboration, accountability, and productive team dynamics throughout the project.

The application is deployed at: **https://orange-pebble-06afc810f.7.azurestaticapps.net**

## Features

### 1. Weekly Activity Submission

Students can submit a weekly activity report detailing their contribution to the team project. Activities are categorized to help both the student and the instructor track the type of work being done. Available categories include:

- **Development** · **Testing** · **Bug Fix** · **Communication** · **Documentation**
- **Design** · **Planning** · **Learning** · **Deployment** · **Support** · **Miscellaneous**

### 2. Peer Evaluation Submission

Students submit weekly peer evaluations to assess the performance of their teammates using a configurable rubric. This helps identify and address issues such as social loafing, free-riding, and undesirable team dynamics early in the project.

### 3. Instructor Reports

Instructors can generate peer evaluation and WAR reports across their assigned teams — at the section level, team level, or for individual students — to monitor progress and identify students who may need support.

### 4. Admin Management

Admins manage the full lifecycle of a senior design section: creating sections, configuring active weeks, building rubrics, managing teams, and inviting students and instructors.

## Repository Structure

```
project-pulse/
├── .github/
│   └── workflows/
│       ├── ci.yml               # Maven build and test on pull requests
│       └── deploy.yml           # Deploy to Azure on push to main
├── backend/                     # Spring Boot backend (Java 17, Maven)
│   └── src/
│       ├── main/
│       │   ├── java/edu/tcu/projectpulse/
│       │   │   ├── activeweek/  # Active week management
│       │   │   ├── auth/        # JWT authentication
│       │   │   ├── config/      # Security config, data seeding
│       │   │   ├── instructor/  # Instructor management
│       │   │   ├── invitation/  # Email invitation tokens
│       │   │   ├── peerevaluation/
│       │   │   ├── report/      # WAR and peer eval report generation
│       │   │   ├── rubric/      # Rubric and criteria management
│       │   │   ├── section/     # Senior design sections
│       │   │   ├── shared/      # Result wrapper, exceptions, utilities
│       │   │   ├── student/     # Student management
│       │   │   ├── team/        # Team management
│       │   │   ├── user/        # User account lifecycle
│       │   │   └── war/         # Weekly activity reports
│       │   └── resources/
│       │       ├── db/migration/  # Flyway migrations (V1–V7)
│       │       ├── application.yml
│       │       ├── application-dev.yml
│       │       └── application-prod.yml
│       └── test/                # Unit and integration tests
├── docs/                        # Architecture, coding standards, dev plan, API guidelines
├── frontend/                    # Vue.js 3 frontend (Vite)
│   └── src/
│       ├── api/                 # API client functions
│       ├── components/          # Shared Vue components
│       ├── layouts/             # AuthLayout, MainLayout
│       ├── plugins/             # Vuetify, axios configuration
│       ├── router/              # Vue Router
│       ├── stores/              # Pinia stores (auth)
│       └── views/
│           ├── admin/           # Admin pages (sections, teams, students, etc.)
│           ├── instructor/      # Instructor pages (reports, dashboard)
│           └── student/         # Student pages (WAR, peer eval, report)
├── requirements/
│   └── use-cases.md             # Full use case specification (UC-1 through UC-34)
├── README.md
└── pom.xml
```

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Spring Boot 3.3.5, Java 17 |
| Security | Spring Security 6, JWT (stateless) |
| Database | MySQL 8, Flyway migrations |
| Frontend | Vue 3, Vuetify 3, Vite, Pinia |
| HTTP Client | Axios |
| Testing | JUnit 5, Mockito |
| CI/CD | GitHub Actions |
| Hosting | Azure App Service (backend), Azure Static Web Apps (frontend) |

## Documentation

### Project Use Cases

🔗 [Use Cases](requirements/use-cases.md)

### API Documentation

🔗 [API Documentation](https://app.swaggerhub.com/apis/Washingtonwei/project-pulse)

### UML Class Diagram

🔗 [UML Class Diagram](https://www.mermaidchart.com/raw/1f4be78a-0597-4fc4-8986-1ceb8937250e?theme=dark&version=v0.1&format=svg)

## Getting Started

### Prerequisites

- Java 17+
- Node.js 20+
- MySQL 8 running locally on port 3306 with a database named `project_pulse`

### 1. Clone the repository

```bash
git clone https://github.com/kayleechristine/project-pulse.git
cd project-pulse
```

### 2. Start the backend

```bash
./mvnw spring-boot:run --file backend/pom.xml -Dspring-boot.run.profiles=dev
```

The backend will be available at http://localhost:8080.

### 3. Start the frontend

```bash
cd frontend
npm install
npm run dev
```

The frontend will be available at http://localhost:5173.

### 4. Log in

The application seeds the following accounts on first startup:

| Role | Email | Password |
|------|-------|----------|
| Admin | admin@test.com | password123 |
| Instructor | prof@tcu.edu | password123 |
| Student | jimbo@tcu.edu | password123 |
| Student | bobinator@tcu.edu | password123 |
| Student | timtam@tcu.edu | password123 |

## Contributing

The [issue tracker](https://github.com/kayleechristine/project-pulse/issues) is the preferred channel for bug reports, feature requests, and submitting pull requests.

## License

The _Project Pulse_ application is released under version 2.0 of the [Apache License](https://www.apache.org/licenses/LICENSE-2.0). If you use, modify, or distribute this project, please retain the `NOTICE` file, and provide proper attribution by including the following statement in your documentation or product:

> "This product includes software developed by **Bingyang Wei** under the Apache License, Version 2.0."

Any modifications to the project should be clearly marked, and all copyright and attribution notices should be retained.
