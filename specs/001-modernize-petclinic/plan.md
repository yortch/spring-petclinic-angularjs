# Implementation Plan: Modernize Spring Petclinic Application

**Branch**: `001-modernize-petclinic` | **Date**: 2025-12-05 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/001-modernize-petclinic/spec.md`

## Summary

Modernize the Spring Petclinic application by upgrading the backend from Java 8/Spring Boot 2.1.3 to Java 21/Spring Boot 3.5.x, and migrating the frontend from AngularJS 1.6.4 to Angular 19 (latest stable). All existing business functionality must be preserved, verified through comprehensive test coverage established before any upgrade work begins.

## Technical Context

**Language/Version**: Java 8 → Java 21 (LTS)
**Primary Dependencies**: 
- Spring Boot 2.1.3.RELEASE → 3.5.x
- AngularJS 1.6.4 → Angular 19.x
- Node.js 10.24.1 → 22.x (LTS)
- Maven 3.x (wrapper maintained)
**Storage**: HSQLDB (in-memory, dev), MySQL 8.x (production)
**Testing**: JUnit 4/5, Spring Boot Test, Jasmine/Karma → Jest/Cypress
**Target Platform**: JVM 21, Linux/Windows server, modern browsers
**Project Type**: Web application (frontend + backend multi-module Maven)
**Performance Goals**: API response <200ms p95, page load <3s
**Constraints**: Zero functionality regression, incremental upgrades per constitution
**Scale/Scope**: ~15 Java classes, 6 REST endpoints, 7 frontend screens

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

| Principle | Status | Evidence |
|-----------|--------|----------|
| I. Test Coverage Baseline | ⚠️ REQUIRED | Must achieve ≥80% coverage before upgrades |
| II. Incremental Upgrade Strategy | ✅ PLANNED | Spring Boot: 2.1→2.2→2.3→2.4→2.5→2.6→2.7→3.0→3.1→3.2→3.3→3.4→3.5 |
| III. Compatibility Verification | ✅ PLANNED | Each step includes build/test/runtime verification |
| IV. Deprecation Tracking | ✅ PLANNED | javax→jakarta migration documented, property migrations tracked |
| V. Rollback Planning | ✅ PLANNED | Separate commits per upgrade step, Docker image tagging |

**Pre-Phase 0 Gate**: Test coverage baseline MUST be established before proceeding to upgrade phases.

## Project Structure

### Documentation (this feature)

```text
specs/001-modernize-petclinic/
├── plan.md              # This file
├── research.md          # Phase 0 output - upgrade path research
├── data-model.md        # Phase 1 output - preserved entity model
├── quickstart.md        # Phase 1 output - developer setup guide
├── contracts/           # Phase 1 output - API contracts (OpenAPI)
│   └── petclinic-api.yaml
└── tasks.md             # Phase 2 output - implementation tasks
```

### Source Code (repository root)

```text
spring-petclinic-angularjs/          # Root (multi-module Maven project)
├── pom.xml                          # Parent POM (Spring Boot parent upgrade)
├── spring-petclinic-server/         # Backend module
│   ├── pom.xml
│   └── src/
│       ├── main/
│       │   ├── java/org/springframework/samples/petclinic/
│       │   │   ├── config/          # Spring configuration
│       │   │   ├── model/           # JPA entities (6 entities)
│       │   │   ├── repository/      # Spring Data repositories (4)
│       │   │   ├── service/         # Business logic
│       │   │   ├── web/             # REST controllers (4)
│       │   │   └── PetClinicApplication.java
│       │   └── resources/
│       │       ├── application*.properties
│       │       └── db/              # SQL scripts (HSQLDB, MySQL)
│       └── test/
│           └── java/                # Unit + Integration tests
├── spring-petclinic-client/         # Frontend module (REPLACE)
│   ├── pom.xml                      # Will be removed
│   └── src/                         # AngularJS (to be replaced)
└── petclinic-frontend/              # NEW: Angular 19 frontend
    ├── angular.json
    ├── package.json
    ├── src/
    │   ├── app/
    │   │   ├── core/                # Services, interceptors
    │   │   ├── features/            # Feature modules
    │   │   │   ├── owners/
    │   │   │   ├── pets/
    │   │   │   ├── vets/
    │   │   │   └── visits/
    │   │   └── shared/              # Common components
    │   └── environments/
    └── e2e/                         # Cypress E2E tests
```

**Structure Decision**: Maintain multi-module Maven structure. Replace `spring-petclinic-client` module with new `petclinic-frontend` Angular project. Backend serves static assets from frontend build output.

## Complexity Tracking

> No constitution violations requiring justification.

## Upgrade Path

### Spring Boot Incremental Upgrade Sequence

Per Constitution Principle II, upgrade one minor version at a time:

| Step | From | To | Key Changes |
|------|------|----|----|
| 1 | 2.1.3 | 2.2.x | Lazy initialization, health groups |
| 2 | 2.2.x | 2.3.x | Graceful shutdown, liveness probes |
| 3 | 2.3.x | 2.4.x | Config file processing changes |
| 4 | 2.4.x | 2.5.x | SQL script data source initialization |
| 5 | 2.5.x | 2.6.x | Circular references prohibited by default |
| 6 | 2.6.x | 2.7.x | Last 2.x release, prepare for 3.x |
| 7 | 2.7.x | 3.0.x | **MAJOR**: Java 17+ required, javax→jakarta |
| 8 | 3.0.x | 3.1.x | Docker Compose support |
| 9 | 3.1.x | 3.2.x | Virtual threads support |
| 10 | 3.2.x | 3.3.x | CDS support |
| 11 | 3.3.x | 3.4.x | Structured logging |
| 12 | 3.4.x | 3.5.x | Latest stable (target) |

### Java Upgrade Sequence

| Step | From | To | When |
|------|------|----|------|
| 1 | 8 | 11 | Before Spring Boot 2.5.x |
| 2 | 11 | 17 | Before Spring Boot 3.0.x (required) |
| 3 | 17 | 21 | After Spring Boot 3.2.x |

### Frontend Migration Strategy

1. **Parallel Development**: New Angular 19 frontend developed alongside existing AngularJS
2. **API Compatibility**: Both frontends use same REST APIs
3. **Feature Parity**: Screen-by-screen migration ensuring identical functionality
4. **Cutover**: Remove AngularJS module after Angular passes all E2E tests

---

## Phase 0: Research (NEEDS CLARIFICATION Resolution)

### Research Tasks

1. **Spring Boot 3.5.x Migration Guide** - Official migration documentation
2. **javax to jakarta Namespace Migration** - Impact on JPA entities, validation
3. **Angular 19 Best Practices** - Signals, standalone components, SSR considerations
4. **Node.js 22 LTS Compatibility** - npm/Angular CLI requirements
5. **MySQL 8 Compatibility** - JDBC driver updates for Spring Boot 3.x

### Resolved Decisions

| Decision | Choice | Rationale |
|----------|--------|-----------|
| Target Java Version | 21 | Latest LTS, virtual threads support, best performance |
| Target Spring Boot | 3.5.x | Latest stable as of Dec 2025 |
| Target Angular Version | 19.x | Latest stable, signals-based reactivity |
| Frontend Build Tool | Angular CLI | Standard tooling, esbuild for fast builds |
| E2E Testing | Cypress | Modern, reliable, good Angular support |
| API Contract Format | OpenAPI 3.0 | Industry standard, code generation support |

---

## Phase 1: Design & Contracts

### Data Model (Preserved)

All entities preserved with namespace migration (javax.persistence → jakarta.persistence):

| Entity | Fields | Relationships |
|--------|--------|---------------|
| Owner | id, firstName, lastName, address, city, telephone | 1:N → Pet |
| Pet | id, name, birthDate, type | N:1 → Owner, N:1 → PetType, 1:N → Visit |
| PetType | id, name | 1:N → Pet |
| Vet | id, firstName, lastName | N:N → Specialty |
| Specialty | id, name | N:N → Vet |
| Visit | id, date, description | N:1 → Pet |

### API Contracts (Preserved)

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/owners` | POST | Create owner |
| `/api/owners/list` | GET | List all owners |
| `/api/owners/{id}` | GET | Get owner by ID |
| `/api/owners/{id}` | PUT | Update owner |
| `/api/owners/{ownerId}/pets` | POST | Add pet to owner |
| `/api/owners/{ownerId}/pets/{petId}` | PUT | Update pet |
| `/api/owners/*/pets/{petId}` | GET | Get pet details |
| `/api/owners/{ownerId}/pets/{petId}/visits` | GET | List pet visits |
| `/api/owners/{ownerId}/pets/{petId}/visits` | POST | Create visit |
| `/api/petTypes` | GET | List pet types |
| `/api/vets` | GET | List veterinarians |

### Quickstart (Post-Upgrade)

```bash
# Prerequisites
java --version  # Java 21+
node --version  # Node 22+
./mvnw --version  # Maven 3.9+

# Build and run
./mvnw clean install
./mvnw spring-boot:run -pl spring-petclinic-server

# Access application
open http://localhost:8080
```

---

## Dependencies Summary

### Backend Dependencies (Post-Upgrade)

| Dependency | Current | Target |
|------------|---------|--------|
| spring-boot-starter-parent | 2.1.3.RELEASE | 3.5.x |
| spring-boot-starter-web | (managed) | (managed) |
| spring-boot-starter-data-jpa | (managed) | (managed) |
| spring-boot-starter-validation | N/A | NEW (extracted) |
| hsqldb | (managed) | (managed) |
| mysql-connector-j | mysql-connector-java | mysql-connector-j |
| ehcache | 2.x API | 3.x (jakarta) |

### Frontend Dependencies (New)

| Dependency | Version |
|------------|---------|
| @angular/core | 19.x |
| @angular/router | 19.x |
| @angular/forms | 19.x |
| @angular/common/http | 19.x |
| bootstrap | 5.x |
| typescript | 5.6.x |

---

## Risk Assessment

| Risk | Likelihood | Impact | Mitigation |
|------|------------|--------|------------|
| Breaking change in transitive dependency | Medium | High | Pin versions, test each upgrade step |
| javax→jakarta migration issues | High | Medium | Use OpenRewrite recipes, comprehensive tests |
| Frontend feature parity gaps | Medium | Medium | E2E tests against both frontends during migration |
| Performance regression | Low | Medium | Baseline metrics before upgrade, compare after |
| Database compatibility issues | Low | High | Test both HSQLDB and MySQL at each step |

---

## Phase 0 & 1 Artifacts (Completed)

| Artifact | Status | Path |
|----------|--------|------|
| research.md | ✅ Complete | `specs/001-modernize-petclinic/research.md` |
| data-model.md | ✅ Complete | `specs/001-modernize-petclinic/data-model.md` |
| petclinic-api.yaml | ✅ Complete | `specs/001-modernize-petclinic/contracts/petclinic-api.yaml` |
| quickstart.md | ✅ Complete | `specs/001-modernize-petclinic/quickstart.md` |
| Agent context | ✅ Updated | `.github/agents/copilot-instructions.md` |

## Next Steps

1. **Run `/speckit.tasks`** to generate detailed implementation tasks
2. **Phase 0 Blocker**: Establish test coverage baseline (≥80%) before any upgrade work

---

**Plan Status**: Phase 0 & 1 complete, ready for task generation
**Constitution Compliance**: All principles addressed
**Blocking Gate**: Test coverage baseline (Constitution Principle I)
