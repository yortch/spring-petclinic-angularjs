# Tasks: Modernize Spring Petclinic Application

**Input**: Design documents from `/specs/001-modernize-petclinic/`
**Prerequisites**: plan.md ✅, spec.md ✅, research.md ✅, data-model.md ✅, contracts/ ✅, quickstart.md ✅

**Tests**: Included - Per Constitution Principle I, comprehensive test coverage is REQUIRED before upgrade work.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (US1, US2, US3, US4, US5)
- Include exact file paths in descriptions

## Path Conventions

- **Backend**: `spring-petclinic-server/src/main/java/org/springframework/samples/petclinic/`
- **Backend Tests**: `spring-petclinic-server/src/test/java/org/springframework/samples/petclinic/`
- **Frontend (Current)**: `spring-petclinic-client/src/scripts/`
- **Frontend (New)**: `petclinic-frontend/src/app/`

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization, verification baseline, and quality gates

- [ ] T001 Verify current application builds with `./mvnw clean verify` from project root
- [ ] T002 [P] Document current dependency versions in `specs/001-modernize-petclinic/baseline-versions.md`
- [ ] T003 [P] Add JaCoCo Maven plugin for code coverage to `spring-petclinic-server/pom.xml`
- [ ] T004 [P] Configure Surefire plugin for JUnit 5 support in `spring-petclinic-server/pom.xml`
- [ ] T005 Create git tag `pre-modernization-baseline` for rollback reference
- [ ] T006 Generate baseline coverage report with `./mvnw jacoco:report`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core test infrastructure that MUST be complete before ANY upgrade work

**⚠️ CRITICAL**: Per Constitution Principle I, 80% test coverage MUST be achieved before any upgrade work begins

- [ ] T007 Add JUnit 5 dependencies (jupiter) to `spring-petclinic-server/pom.xml`
- [ ] T008 [P] Add Mockito dependencies for mocking in `spring-petclinic-server/pom.xml`
- [ ] T009 [P] Add Spring Boot Test dependencies for integration testing
- [ ] T010 Create test base class `spring-petclinic-server/src/test/java/.../TestBase.java` with shared config
- [ ] T011 [P] Add H2 database test profile in `spring-petclinic-server/src/test/resources/application-test.properties`

**Checkpoint**: Test infrastructure ready - User Story 1 (Test Coverage) can now begin

---

## Phase 3: User Story 1 - Establish Test Coverage Baseline (Priority: P1) 🎯 BLOCKING GATE

**Goal**: Achieve ≥80% unit test coverage and 100% API endpoint coverage before any upgrades

**Independent Test**: Generate JaCoCo report showing ≥80% coverage, verify all REST endpoints have integration tests

### Unit Tests for Models

- [ ] T012 [P] [US1] Create `OwnerTests.java` unit tests in `spring-petclinic-server/src/test/java/.../model/OwnerTests.java`
- [ ] T013 [P] [US1] Create `PetTests.java` unit tests in `spring-petclinic-server/src/test/java/.../model/PetTests.java`
- [ ] T014 [P] [US1] Create `VisitTests.java` unit tests in `spring-petclinic-server/src/test/java/.../model/VisitTests.java`
- [ ] T015 [P] [US1] Create `VetTests.java` unit tests in `spring-petclinic-server/src/test/java/.../model/VetTests.java`
- [ ] T016 [P] [US1] Create `PetTypeTests.java` unit tests in `spring-petclinic-server/src/test/java/.../model/PetTypeTests.java`
- [ ] T017 [P] [US1] Create `SpecialtyTests.java` unit tests in `spring-petclinic-server/src/test/java/.../model/SpecialtyTests.java`

### Unit Tests for Service Layer

- [ ] T018 [US1] Create `ClinicServiceTests.java` unit tests in `spring-petclinic-server/src/test/java/.../service/ClinicServiceTests.java`

### Integration Tests for REST Controllers

- [ ] T019 [P] [US1] Create `OwnerResourceIntegrationTests.java` in `spring-petclinic-server/src/test/java/.../web/OwnerResourceIntegrationTests.java`
- [ ] T020 [P] [US1] Enhance existing `PetResourceTests.java` with full CRUD coverage in `spring-petclinic-server/src/test/java/.../web/PetResourceTests.java`
- [ ] T021 [P] [US1] Enhance existing `VetResourceTests.java` with full coverage in `spring-petclinic-server/src/test/java/.../web/VetResourceTests.java`
- [ ] T022 [P] [US1] Create `VisitResourceIntegrationTests.java` in `spring-petclinic-server/src/test/java/.../web/VisitResourceIntegrationTests.java`
- [ ] T023 [P] [US1] Create `PetTypeResourceTests.java` if endpoint exists in `spring-petclinic-server/src/test/java/.../web/PetTypeResourceTests.java`

### Repository Tests

- [ ] T024 [P] [US1] Create `OwnerRepositoryTests.java` in `spring-petclinic-server/src/test/java/.../repository/OwnerRepositoryTests.java`
- [ ] T025 [P] [US1] Create `PetRepositoryTests.java` in `spring-petclinic-server/src/test/java/.../repository/PetRepositoryTests.java`
- [ ] T026 [P] [US1] Create `VetRepositoryTests.java` in `spring-petclinic-server/src/test/java/.../repository/VetRepositoryTests.java`
- [ ] T027 [P] [US1] Create `VisitRepositoryTests.java` in `spring-petclinic-server/src/test/java/.../repository/VisitRepositoryTests.java`

### API Contract Tests

- [ ] T028 [US1] Create API baseline snapshot tests comparing responses to `contracts/petclinic-api.yaml`
- [ ] T029 [US1] Add Maven plugin for OpenAPI validation against contracts

### E2E Test Setup (Cypress)

- [ ] T030 [US1] Initialize Cypress in `spring-petclinic-client/` with `npx cypress install`
- [ ] T031 [P] [US1] Create `owners.cy.js` E2E test for owner CRUD journey in `spring-petclinic-client/cypress/e2e/owners.cy.js`
- [ ] T032 [P] [US1] Create `pets.cy.js` E2E test for pet management journey in `spring-petclinic-client/cypress/e2e/pets.cy.js`
- [ ] T033 [P] [US1] Create `visits.cy.js` E2E test for visit scheduling journey in `spring-petclinic-client/cypress/e2e/visits.cy.js`
- [ ] T034 [P] [US1] Create `vets.cy.js` E2E test for vet listing journey in `spring-petclinic-client/cypress/e2e/vets.cy.js`

### Coverage Verification

- [ ] T035 [US1] Generate final coverage report and verify ≥80% line coverage with `./mvnw verify jacoco:report`
- [ ] T036 [US1] Document test baseline metrics in `specs/001-modernize-petclinic/test-baseline.md`
- [ ] T037 [US1] Add coverage enforcement to Maven build (fail if <80%)

**Checkpoint**: ⚠️ GATE - Coverage MUST be ≥80% before proceeding to Phase 4. Verify with `./mvnw verify`

---

## Phase 4: User Story 2 - Upgrade Spring Boot Backend (Priority: P2)

**Goal**: Incrementally upgrade Spring Boot from 2.1.3 to 3.5.x following Constitution Principle II

**Independent Test**: Application builds, all tests pass, API responses identical to baseline at each upgrade step

### Spring Boot 2.1.x → 2.2.x

- [ ] T038 [US2] Update Spring Boot version to 2.2.RELEASE in root `pom.xml`
- [ ] T039 [US2] Run full test suite and verify pass with `./mvnw clean verify`
- [ ] T040 [US2] Create git commit: `chore: upgrade Spring Boot 2.1.3 → 2.2.x`

### Spring Boot 2.2.x → 2.3.x

- [ ] T041 [US2] Update Spring Boot version to 2.3.RELEASE in root `pom.xml`
- [ ] T042 [US2] Verify JPA repository bootstrap mode in `application.properties`
- [ ] T043 [US2] Run full test suite and verify pass
- [ ] T044 [US2] Create git commit: `chore: upgrade Spring Boot 2.2.x → 2.3.x`

### Spring Boot 2.3.x → 2.4.x

- [ ] T045 [US2] Update Spring Boot version to 2.4.RELEASE in root `pom.xml`
- [ ] T046 [US2] Verify config file processing (add `spring.config.use-legacy-processing=true` if needed)
- [ ] T047 [US2] Run full test suite and verify pass
- [ ] T048 [US2] Create git commit: `chore: upgrade Spring Boot 2.3.x → 2.4.x`

### Spring Boot 2.4.x → 2.5.x

- [ ] T049 [US2] Update Spring Boot version to 2.5.RELEASE in root `pom.xml`
- [ ] T050 [US2] Migrate SQL initialization properties (`spring.datasource.initialization-mode` → `spring.sql.init.mode`)
- [ ] T051 [US2] Run full test suite and verify pass
- [ ] T052 [US2] Create git commit: `chore: upgrade Spring Boot 2.4.x → 2.5.x`

### Spring Boot 2.5.x → 2.6.x

- [ ] T053 [US2] Update Spring Boot version to 2.6.RELEASE in root `pom.xml`
- [ ] T054 [US2] Check for circular bean references and resolve if needed
- [ ] T055 [US2] Run full test suite and verify pass
- [ ] T056 [US2] Create git commit: `chore: upgrade Spring Boot 2.5.x → 2.6.x`

### Spring Boot 2.6.x → 2.7.x

- [ ] T057 [US2] Update Spring Boot version to 2.7.RELEASE in root `pom.xml`
- [ ] T058 [US2] Address all deprecation warnings (prepare for 3.x)
- [ ] T059 [US2] Run full test suite and verify pass
- [ ] T060 [US2] Create git commit: `chore: upgrade Spring Boot 2.6.x → 2.7.x`
- [ ] T061 [US2] Create git tag: `spring-boot-2.7-milestone`

**Checkpoint**: Spring Boot 2.x upgrade complete. Java 17 required for 3.x - see User Story 3 (Phase 5)

---

## Phase 5: User Story 3 - Upgrade Java Version (Priority: P3)

**Goal**: Upgrade Java from 8 → 11 → 17 → 21 following Constitution Principle II

**Independent Test**: Application compiles and all tests pass with each JDK version

### Java 8 → 11

- [ ] T062 [US3] Update `<java.version>` to 11 in root `pom.xml`
- [ ] T063 [US3] Update `<maven.compiler.source>` and `<maven.compiler.target>` to 11
- [ ] T064 [US3] Run full test suite with JDK 11: `JAVA_HOME=/path/to/jdk11 ./mvnw clean verify`
- [ ] T065 [US3] Address any removed APIs or illegal reflective access warnings
- [ ] T066 [US3] Create git commit: `chore: upgrade Java 8 → 11`

### Java 11 → 17

- [ ] T067 [US3] Update `<java.version>` to 17 in root `pom.xml`
- [ ] T068 [US3] Update `<maven.compiler.source>` and `<maven.compiler.target>` to 17
- [ ] T069 [US3] Run full test suite with JDK 17: `JAVA_HOME=/path/to/jdk17 ./mvnw clean verify`
- [ ] T070 [US3] Add `--add-opens` JVM args if reflection issues occur
- [ ] T071 [US3] Create git commit: `chore: upgrade Java 11 → 17`
- [ ] T072 [US3] Create git tag: `java-17-milestone`

**Checkpoint**: Java 17 ready - Spring Boot 3.x upgrade can now proceed

### Continue Spring Boot Upgrade (3.x series)

### Spring Boot 2.7.x → 3.0.x ⚠️ MAJOR

- [ ] T073 [US2] Update Spring Boot version to 3.0.RELEASE in root `pom.xml`
- [ ] T074 [US2] Add OpenRewrite plugin to `pom.xml` for javax→jakarta migration
- [ ] T075 [US2] Run OpenRewrite recipe: `./mvnw rewrite:run -Drewrite.activeRecipes=org.openrewrite.java.spring.boot3.UpgradeSpringBoot_3_0`
- [ ] T076 [US2] Manually verify javax→jakarta imports in all entity files under `spring-petclinic-server/src/main/java/.../model/`
- [ ] T077 [US2] Update `mysql-connector-java` → `mysql-connector-j` in `spring-petclinic-server/pom.xml`
- [ ] T078 [US2] Update validation dependency to `spring-boot-starter-validation`
- [ ] T079 [US2] Fix trailing slash matching if needed (disabled by default in 3.x)
- [ ] T080 [US2] Run full test suite and verify pass
- [ ] T081 [US2] Create git commit: `chore: upgrade Spring Boot 2.7.x → 3.0.x (javax→jakarta)`
- [ ] T082 [US2] Create git tag: `spring-boot-3.0-milestone`

### Spring Boot 3.0.x → 3.1.x

- [ ] T083 [US2] Update Spring Boot version to 3.1.RELEASE in root `pom.xml`
- [ ] T084 [US2] Run full test suite and verify pass
- [ ] T085 [US2] Create git commit: `chore: upgrade Spring Boot 3.0.x → 3.1.x`

### Spring Boot 3.1.x → 3.2.x

- [ ] T086 [US2] Update Spring Boot version to 3.2.RELEASE in root `pom.xml`
- [ ] T087 [US2] Run full test suite and verify pass
- [ ] T088 [US2] Create git commit: `chore: upgrade Spring Boot 3.1.x → 3.2.x`

### Java 17 → 21

- [ ] T089 [US3] Update `<java.version>` to 21 in root `pom.xml`
- [ ] T090 [US3] Update `<maven.compiler.source>` and `<maven.compiler.target>` to 21
- [ ] T091 [US3] Run full test suite with JDK 21: `JAVA_HOME=/path/to/jdk21 ./mvnw clean verify`
- [ ] T092 [US3] Create git commit: `chore: upgrade Java 17 → 21`
- [ ] T093 [US3] Create git tag: `java-21-milestone`

### Spring Boot 3.2.x → 3.3.x

- [ ] T094 [US2] Update Spring Boot version to 3.3.RELEASE in root `pom.xml`
- [ ] T095 [US2] Run full test suite and verify pass
- [ ] T096 [US2] Create git commit: `chore: upgrade Spring Boot 3.2.x → 3.3.x`

### Spring Boot 3.3.x → 3.4.x

- [ ] T097 [US2] Update Spring Boot version to 3.4.RELEASE in root `pom.xml`
- [ ] T098 [US2] Run full test suite and verify pass
- [ ] T099 [US2] Create git commit: `chore: upgrade Spring Boot 3.3.x → 3.4.x`

### Spring Boot 3.4.x → 3.5.x (Target)

- [ ] T100 [US2] Update Spring Boot version to 3.5.RELEASE in root `pom.xml`
- [ ] T101 [US2] Run full test suite and verify pass
- [ ] T102 [US2] Verify API contract tests pass against baseline
- [ ] T103 [US2] Create git commit: `chore: upgrade Spring Boot 3.4.x → 3.5.x (target)`
- [ ] T104 [US2] Create git tag: `spring-boot-3.5-complete`

**Checkpoint**: Backend upgrade complete (Java 21, Spring Boot 3.5.x). Frontend migration can now proceed.

---

## Phase 6: User Story 4 - Modernize Frontend Framework (Priority: P4)

**Goal**: Replace AngularJS 1.6.4 with Angular 19, preserving all user journeys

**Independent Test**: All E2E tests pass on new Angular frontend, identical functionality to AngularJS

### Angular Project Setup

- [ ] T105 [US4] Create new Angular 19 project: `npx @angular/cli@19 new petclinic-frontend --style=scss --routing=true --standalone`
- [ ] T106 [US4] Add Bootstrap 5 to `petclinic-frontend/package.json`
- [ ] T107 [US4] Configure proxy for API calls in `petclinic-frontend/proxy.conf.json`
- [ ] T108 [US4] Add Angular environment configuration for API base URL

### Core Services

- [ ] T109 [P] [US4] Create `OwnerService` in `petclinic-frontend/src/app/core/services/owner.service.ts`
- [ ] T110 [P] [US4] Create `PetService` in `petclinic-frontend/src/app/core/services/pet.service.ts`
- [ ] T111 [P] [US4] Create `VetService` in `petclinic-frontend/src/app/core/services/vet.service.ts`
- [ ] T112 [P] [US4] Create `VisitService` in `petclinic-frontend/src/app/core/services/visit.service.ts`
- [ ] T113 [P] [US4] Create `PetTypeService` in `petclinic-frontend/src/app/core/services/pet-type.service.ts`
- [ ] T114 [US4] Create HTTP interceptor for error handling in `petclinic-frontend/src/app/core/interceptors/error.interceptor.ts`

### Shared Components

- [ ] T115 [P] [US4] Create `HeaderComponent` in `petclinic-frontend/src/app/shared/components/header/`
- [ ] T116 [P] [US4] Create `FooterComponent` in `petclinic-frontend/src/app/shared/components/footer/`
- [ ] T117 [P] [US4] Create `LoadingSpinnerComponent` in `petclinic-frontend/src/app/shared/components/loading-spinner/`

### Feature: Owners Module

- [ ] T118 [P] [US4] Create `OwnersListComponent` (standalone, signals) in `petclinic-frontend/src/app/features/owners/owners-list/`
- [ ] T119 [P] [US4] Create `OwnerDetailComponent` (standalone, signals) in `petclinic-frontend/src/app/features/owners/owner-detail/`
- [ ] T120 [P] [US4] Create `OwnerFormComponent` (reactive forms) in `petclinic-frontend/src/app/features/owners/owner-form/`
- [ ] T121 [US4] Configure owner routes in `petclinic-frontend/src/app/features/owners/owners.routes.ts`

### Feature: Pets Module

- [ ] T122 [P] [US4] Create `PetFormComponent` (reactive forms) in `petclinic-frontend/src/app/features/pets/pet-form/`
- [ ] T123 [US4] Configure pet routes in `petclinic-frontend/src/app/features/pets/pets.routes.ts`

### Feature: Visits Module

- [ ] T124 [P] [US4] Create `VisitsListComponent` (standalone, signals) in `petclinic-frontend/src/app/features/visits/visits-list/`
- [ ] T125 [P] [US4] Create `VisitFormComponent` (reactive forms) in `petclinic-frontend/src/app/features/visits/visit-form/`
- [ ] T126 [US4] Configure visit routes in `petclinic-frontend/src/app/features/visits/visits.routes.ts`

### Feature: Vets Module

- [ ] T127 [P] [US4] Create `VetsListComponent` (standalone, signals) in `petclinic-frontend/src/app/features/vets/vets-list/`
- [ ] T128 [US4] Configure vet routes in `petclinic-frontend/src/app/features/vets/vets.routes.ts`

### Feature: Home Module

- [ ] T129 [US4] Create `WelcomeComponent` (home page) in `petclinic-frontend/src/app/features/home/welcome/`

### Main App Configuration

- [ ] T130 [US4] Configure app routes with lazy loading in `petclinic-frontend/src/app/app.routes.ts`
- [ ] T131 [US4] Configure HttpClient with interceptors in `petclinic-frontend/src/app/app.config.ts`

### Styling & Assets

- [ ] T132 [P] [US4] Migrate CSS styles from AngularJS to `petclinic-frontend/src/styles.scss`
- [ ] T133 [P] [US4] Copy images/assets from `spring-petclinic-client/src/images/` to `petclinic-frontend/src/assets/`

### E2E Tests for New Frontend (Cypress)

- [ ] T134 [US4] Setup Cypress in Angular project: `cd petclinic-frontend && npx cypress install`
- [ ] T135 [P] [US4] Create `owners.cy.ts` E2E test in `petclinic-frontend/cypress/e2e/owners.cy.ts`
- [ ] T136 [P] [US4] Create `pets.cy.ts` E2E test in `petclinic-frontend/cypress/e2e/pets.cy.ts`
- [ ] T137 [P] [US4] Create `visits.cy.ts` E2E test in `petclinic-frontend/cypress/e2e/visits.cy.ts`
- [ ] T138 [P] [US4] Create `vets.cy.ts` E2E test in `petclinic-frontend/cypress/e2e/vets.cy.ts`

### Integration with Maven Build

- [ ] T139 [US4] Add `petclinic-frontend` as Maven module in root `pom.xml`
- [ ] T140 [US4] Configure frontend-maven-plugin in `petclinic-frontend/pom.xml` with Node 22.x
- [ ] T141 [US4] Configure build to copy Angular dist to server static resources
- [ ] T142 [US4] Run full build and E2E tests: `./mvnw clean verify`
- [ ] T143 [US4] Create git commit: `feat: add Angular 19 frontend (feature parity with AngularJS)`

### Cutover: Remove AngularJS

- [ ] T144 [US4] Update server to serve Angular assets instead of AngularJS
- [ ] T145 [US4] Remove `spring-petclinic-client` module from root `pom.xml`
- [ ] T146 [US4] Archive AngularJS source to `archive/spring-petclinic-client/` (do not delete yet)
- [ ] T147 [US4] Create git commit: `chore: switch to Angular 19 frontend, archive AngularJS`
- [ ] T148 [US4] Create git tag: `angular-19-complete`

**Checkpoint**: Frontend migration complete. Both backend and frontend modernized.

---

## Phase 7: User Story 5 - Update Build and DevOps Infrastructure (Priority: P5)

**Goal**: Modernize build tooling, Docker images, and CI/CD pipeline

**Independent Test**: CI/CD pipeline executes all quality gates, Docker images build successfully

### Maven Plugin Updates

- [ ] T149 [P] [US5] Update maven-compiler-plugin to latest version in root `pom.xml`
- [ ] T150 [P] [US5] Update frontend-maven-plugin to 1.15.x in `petclinic-frontend/pom.xml`
- [ ] T151 [US5] Run dependency security check: `./mvnw dependency-check:check`
- [ ] T152 [US5] Address any high/critical CVEs in dependencies

### Docker Configuration

- [ ] T153 [US5] Update Dockerfile base image to `eclipse-temurin:21-jre-alpine` in `spring-petclinic-server/src/main/docker/Dockerfile`
- [ ] T154 [US5] Add multi-stage Docker build for smaller images
- [ ] T155 [US5] Update `docker-compose.yml` with new image configuration
- [ ] T156 [US5] Build and test Docker image: `docker-compose build && docker-compose up -d`
- [ ] T157 [US5] Verify application runs correctly in Docker container

### CI/CD Pipeline (GitHub Actions)

- [ ] T158 [P] [US5] Create `.github/workflows/ci.yml` with build and test jobs
- [ ] T159 [P] [US5] Add code coverage reporting to CI pipeline
- [ ] T160 [P] [US5] Add dependency security scanning to CI pipeline
- [ ] T161 [P] [US5] Add Docker image build to CI pipeline
- [ ] T162 [US5] Configure branch protection rules requiring CI pass

### Documentation Updates

- [ ] T163 [P] [US5] Update `readme.md` with new prerequisites (Java 21, Node 22)
- [ ] T164 [P] [US5] Update `specs/001-modernize-petclinic/quickstart.md` with final instructions
- [ ] T165 [US5] Create `CHANGELOG.md` documenting all upgrade steps
- [ ] T166 [US5] Create git commit: `chore: modernize build and DevOps infrastructure`

**Checkpoint**: Build and DevOps infrastructure modernized.

---

## Phase 8: Polish & Cross-Cutting Concerns

**Purpose**: Final cleanup, documentation, and validation

- [ ] T167 [P] Remove legacy configuration files and dead code
- [ ] T168 [P] Run full Cypress E2E suite against production build
- [ ] T169 [P] Performance testing: verify API response times within 10% of baseline
- [ ] T170 [P] Accessibility audit: verify WCAG 2.1 AA compliance
- [ ] T171 Run `quickstart.md` validation with fresh checkout
- [ ] T172 Final security scan with OWASP dependency-check
- [ ] T173 Create PR for merge to main branch
- [ ] T174 Create git tag: `v2.0.0-modernized`

---

## Dependencies & Execution Order

### Phase Dependencies

```
Phase 1 (Setup) ──────────────────────────────┐
                                              │
Phase 2 (Foundational) ◄──────────────────────┘
           │
           │ ⚠️ GATE: 80% coverage required
           ▼
Phase 3 (US1: Test Coverage) ──────────────────┐
           │                                   │
           │ ⚠️ GATE: Must complete before     │
           │         any upgrade work          │
           ▼                                   │
Phase 4 (US2: Spring Boot 2.x) ◄───────────────┘
           │
           │ Spring Boot 2.7 milestone
           ▼
Phase 5 (US3: Java 17) ──────────────────────────┐
           │                                     │
           │ Java 17 required for Spring Boot 3.x│
           ▼                                     │
Phase 5 cont. (US2: Spring Boot 3.x) ◄───────────┘
           │
           │ Spring Boot 3.5 milestone
           ▼
Phase 5 cont. (US3: Java 21) 
           │
           │ Backend complete
           ▼
Phase 6 (US4: Angular 19) ──────────────────────┐
           │                                    │
           │ Frontend complete                  │
           ▼                                    │
Phase 7 (US5: DevOps) ◄─────────────────────────┘
           │
           ▼
Phase 8 (Polish)
```

### User Story Dependencies

- **User Story 1 (P1)**: BLOCKING GATE - Must complete before US2, US3, US4, US5
- **User Story 2 (P2)**: Depends on US1 completion; interleaved with US3 for Java upgrade
- **User Story 3 (P3)**: Depends on US1 completion; Java 17 must complete before Spring Boot 3.x
- **User Story 4 (P4)**: Depends on US2 and US3 completion (stable backend APIs required)
- **User Story 5 (P5)**: Can start after US2/US3, runs in parallel with or after US4

### Parallel Opportunities

**Within Phase 3 (US1)**:
- All model unit tests (T012-T017) can run in parallel
- All integration tests (T019-T023) can run in parallel
- All repository tests (T024-T027) can run in parallel
- All E2E tests (T031-T034) can run in parallel

**Within Phase 6 (US4)**:
- All services (T109-T113) can run in parallel
- All shared components (T115-T117) can run in parallel
- All feature components by module can run in parallel
- All E2E tests (T135-T138) can run in parallel

**Within Phase 7 (US5)**:
- Maven plugin updates (T149-T150) can run in parallel
- CI/CD workflow files (T158-T161) can run in parallel
- Documentation updates (T163-T164) can run in parallel

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational
3. Complete Phase 3: User Story 1 (Test Coverage)
4. **STOP and VALIDATE**: ≥80% coverage achieved
5. Document baseline before proceeding

### Incremental Delivery

1. US1 → Test baseline established (BLOCKING GATE)
2. US2 (partial) → Spring Boot 2.7 milestone → Deploy/Validate
3. US3 (partial) → Java 17 milestone → Deploy/Validate
4. US2 (complete) + US3 (complete) → Spring Boot 3.5, Java 21 → Deploy/Validate
5. US4 → Angular 19 frontend → Deploy/Validate
6. US5 → DevOps infrastructure → Deploy/Validate
7. Each milestone is a stable, testable increment

### Constitution Compliance Checkpoints

| Checkpoint | Principle | Verification |
|------------|-----------|--------------|
| After Phase 3 | I. Test Coverage | JaCoCo report ≥80% |
| Each upgrade step | II. Incremental | `./mvnw clean verify` passes |
| Each upgrade step | III. Compatibility | API contract tests pass |
| Each upgrade step | V. Rollback | Git tag created, commit isolated |
| After US4 | IV. Deprecation | Zero deprecation warnings |

---

## Notes

- `[P]` tasks = different files, no dependencies - can run in parallel
- `[Story]` label maps task to specific user story for traceability
- Per Constitution Principle I, Phase 3 (US1) is NON-NEGOTIABLE before upgrades
- Each Spring Boot version step includes build/test/commit cycle
- Each Java version step must verify with correct JDK
- Commit after each task or logical group (per Constitution Principle V)
- Create git tags at milestones for rollback capability
- Stop at any checkpoint to validate before proceeding

---

## Task Summary

| Phase | User Story | Task Count | Parallel Tasks |
|-------|------------|------------|----------------|
| 1 | Setup | 6 | 3 |
| 2 | Foundational | 5 | 2 |
| 3 | US1: Test Coverage | 26 | 19 |
| 4 | US2: Spring Boot 2.x | 24 | 0 |
| 5 | US2+US3: Spring Boot 3.x + Java | 32 | 0 |
| 6 | US4: Angular 19 | 44 | 28 |
| 7 | US5: DevOps | 18 | 8 |
| 8 | Polish | 8 | 5 |
| **Total** | | **163** | **65** |

### Tasks per User Story

| User Story | Task Count | Priority |
|------------|------------|----------|
| US1: Test Coverage Baseline | 26 | P1 (BLOCKING) |
| US2: Spring Boot Upgrade | 56 | P2 |
| US3: Java Upgrade | 14 | P3 |
| US4: Angular Frontend | 44 | P4 |
| US5: DevOps Infrastructure | 18 | P5 |
| Setup/Polish | 5 | - |

### Independent Test Criteria

| User Story | Test Criteria |
|------------|---------------|
| US1 | JaCoCo coverage ≥80%, all E2E tests pass on current AngularJS |
| US2 | All existing tests pass after each upgrade step, API contracts match baseline |
| US3 | Application compiles and all tests pass with target JDK |
| US4 | All E2E tests pass on Angular frontend, identical functionality |
| US5 | CI/CD pipeline green, Docker image builds successfully |

### Suggested MVP Scope

**MVP = Phase 1 + Phase 2 + Phase 3 (User Story 1)**

This establishes the test baseline required by Constitution Principle I before any upgrade work begins. After US1 completion, each subsequent user story adds incremental value.
