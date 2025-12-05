<!--
SYNC IMPACT REPORT
==================
Version Change: 0.0.0 → 1.0.0 (MAJOR - initial constitution ratification)
Modified Principles: N/A (initial creation)
Added Sections:
  - Core Principles (5 principles focused on upgrade guidelines)
  - Technology Stack Constraints
  - Upgrade Quality Gates
  - Governance
Removed Sections: None
Templates Requiring Updates:
  - .specify/templates/plan-template.md: ✅ Compatible (Constitution Check section exists)
  - .specify/templates/spec-template.md: ✅ Compatible (testing requirements align)
  - .specify/templates/tasks-template.md: ✅ Compatible (test task structure aligns)
Follow-up TODOs: None
-->

# Spring PetClinic AngularJS Constitution

## Core Principles

### I. Test Coverage Baseline (NON-NEGOTIABLE)

Before ANY upgrade work begins, the project MUST establish a comprehensive test coverage baseline:

- **Unit Test Coverage**: MUST achieve minimum 80% code coverage on all server-side Java code before initiating upgrades
- **Integration Test Coverage**: MUST have integration tests covering all REST API endpoints (`/api/vets`, `/api/owners`, `/api/pets`, `/api/visits`)
- **End-to-End Test Coverage**: MUST implement E2E tests covering all critical user journeys (owner CRUD, pet CRUD, visit scheduling, vet listing)
- **Coverage Verification**: Test coverage reports MUST be generated and reviewed before each upgrade phase
- **Baseline Documentation**: Current test coverage metrics MUST be documented in the upgrade plan before proceeding

**Rationale**: Upgrades introduce breaking changes; comprehensive test coverage ensures behavioral regressions are detected immediately. Without baseline tests, upgrade success cannot be objectively verified.

### II. Incremental Upgrade Strategy

All upgrades MUST follow an incremental, phased approach:

- **Spring Boot Upgrades**: MUST upgrade one minor version at a time (e.g., 2.1.x → 2.2.x → 2.3.x), never skip major versions
- **Java Version Upgrades**: MUST validate compatibility with current Spring Boot version before upgrading JDK
- **AngularJS Migration**: MUST maintain parallel functionality during any frontend framework migration; legacy and new must coexist until migration is complete
- **Dependency Upgrades**: MUST upgrade dependencies in isolation; one dependency per commit to enable easy rollback
- **Database Schema Changes**: MUST use versioned migrations (Flyway/Liquibase); never modify schemas directly

**Rationale**: Incremental upgrades reduce blast radius, simplify debugging when issues arise, and allow for controlled rollback at any phase.

### III. Compatibility Verification

Every upgrade step MUST include explicit compatibility verification:

- **Build Verification**: Project MUST compile without errors after each upgrade step (`./mvnw clean compile`)
- **Test Suite Execution**: All existing tests MUST pass after each upgrade step (`./mvnw test`)
- **Runtime Verification**: Application MUST start successfully and respond to health checks
- **API Contract Verification**: All REST endpoints MUST return identical response structures (use contract tests or API snapshots)
- **Database Compatibility**: Schema migrations MUST be forward-compatible; existing data MUST remain accessible
- **Frontend Compatibility**: AngularJS client MUST function correctly against upgraded backend APIs

**Rationale**: Each verification gate ensures the upgrade has not introduced silent failures or behavioral changes that would compound in later phases.

### IV. Deprecation and Migration Tracking

All deprecated APIs, configurations, and patterns MUST be tracked and addressed:

- **Deprecation Inventory**: Before upgrading, MUST document all deprecated APIs/features currently in use
- **Migration Path Documentation**: Each deprecated item MUST have a documented migration path to the replacement
- **Suppression Prohibition**: `@SuppressWarnings("deprecation")` MUST NOT be added without documented justification and a migration ticket
- **Configuration Updates**: Deprecated Spring Boot properties MUST be migrated to current equivalents (use `spring-boot-properties-migrator` during upgrades)
- **AngularJS Patterns**: Deprecated AngularJS patterns (e.g., `$scope`, `$http` callbacks) SHOULD be tracked for future Angular migration

**Rationale**: Deprecated features become removal candidates in future versions; tracking ensures the project remains upgradeable long-term.

### V. Rollback and Recovery Planning

Every upgrade MUST have an explicit rollback strategy:

- **Branch Strategy**: Upgrades MUST be performed on dedicated feature branches; main/master remains stable
- **Commit Granularity**: Each logical upgrade step MUST be a separate commit with descriptive message
- **Rollback Procedure**: Document explicit rollback steps before starting upgrade work
- **Database Rollback**: Schema migrations MUST include down migrations or rollback scripts
- **Dependency Lockfile**: `pom.xml` versions MUST be explicit (no version ranges); consider using `maven-enforcer-plugin`
- **Docker Image Tagging**: Pre-upgrade images MUST be tagged and preserved for rapid rollback

**Rationale**: Failed upgrades happen; explicit rollback planning minimizes downtime and reduces pressure during incident response.

## Technology Stack Constraints

The following technology constraints apply to this project:

| Component | Current Version | Upgrade Target | Constraints |
|-----------|-----------------|----------------|-------------|
| Java | 8+ | 11, 17, or 21 | Must align with Spring Boot compatibility matrix |
| Spring Boot | 2.1.3.RELEASE | 2.7.x → 3.x | Follow official migration guides |
| AngularJS | 1.x | Angular 2+ (eventual) | Requires complete rewrite; prioritize backend first |
| Maven | 3.x | Latest 3.x | Maintain wrapper (`mvnw`) for reproducibility |
| HSQLDB/MySQL | Current | Compatible versions | Test both database profiles after upgrades |

**Frontend Migration Note**: AngularJS (1.x) is end-of-life. Long-term strategy SHOULD include migration to Angular, React, or Vue. Backend upgrades SHOULD be completed first to establish stable API contracts.

## Upgrade Quality Gates

Before merging any upgrade work, the following gates MUST pass:

| Gate | Requirement | Verification Method |
|------|-------------|---------------------|
| Build Success | Zero compilation errors | `./mvnw clean compile` |
| Unit Tests | 100% pass rate, ≥80% coverage maintained | `./mvnw test` + coverage report |
| Integration Tests | All API endpoints verified | Integration test suite |
| E2E Tests | All critical user journeys pass | E2E test suite (Selenium/Cypress) |
| No New Deprecations | No new deprecation warnings without tickets | Build output review |
| Performance Baseline | Response times within 10% of pre-upgrade | Load test comparison |
| Security Scan | No new high/critical vulnerabilities | OWASP dependency check |

## Governance

This constitution supersedes all other development practices for upgrade work on this project:

- **Amendment Process**: Changes require documented justification, team review, and version increment
- **Versioning Policy**: Constitution follows semantic versioning (MAJOR.MINOR.PATCH)
  - MAJOR: Principle removal or incompatible redefinition
  - MINOR: New principle or section addition
  - PATCH: Clarifications and wording improvements
- **Compliance Review**: All upgrade PRs MUST reference this constitution and verify principle adherence
- **Exception Process**: Deviations MUST be documented in PR description with explicit justification

**Version**: 1.0.0 | **Ratified**: 2025-12-05 | **Last Amended**: 2025-12-05
