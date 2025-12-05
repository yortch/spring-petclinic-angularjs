# Research: Modernize Spring Petclinic Application

**Feature**: 001-modernize-petclinic
**Date**: 2025-12-05
**Purpose**: Resolve all NEEDS CLARIFICATION items and document upgrade decisions

## 1. Spring Boot 2.1.3 → 3.5.x Migration

### Decision: Incremental 12-Step Upgrade Path
**Rationale**: Per Constitution Principle II, never skip major versions. Each minor version may introduce breaking changes.
**Alternatives Considered**: Direct jump to 3.5.x (rejected - violates constitution, high risk)

### Key Breaking Changes by Version

#### 2.1.x → 2.2.x
- Lazy initialization available (opt-in)
- Health indicator groups
- **Action**: No code changes required

#### 2.2.x → 2.3.x
- Graceful shutdown support
- Liveness/readiness probes
- `spring.data.jpa.repositories.bootstrap-mode` default changed
- **Action**: Verify JPA repository bootstrap

#### 2.3.x → 2.4.x
- Config file processing changes (spring.config.use-legacy-processing)
- New config file format support
- **Action**: Verify application.properties loading

#### 2.4.x → 2.5.x
- SQL script data source initialization changes
- `spring.datasource.initialization-mode` → `spring.sql.init.mode`
- **Action**: Update SQL initialization properties

#### 2.5.x → 2.6.x
- Circular references prohibited by default
- PathPatternParser default for Spring MVC
- **Action**: Check for circular bean references

#### 2.6.x → 2.7.x
- Last 2.x release
- Deprecation warnings for 3.x preparation
- **Action**: Address all deprecation warnings

#### 2.7.x → 3.0.x ⚠️ MAJOR
- **Java 17+ REQUIRED**
- **javax.* → jakarta.* namespace migration**
- Spring Security major upgrade
- Trailing slash matching disabled by default
- **Action**: 
  - Upgrade to Java 17
  - Run OpenRewrite javax→jakarta recipes
  - Update all imports

#### 3.0.x → 3.1.x
- Docker Compose support
- TestContainers improvements
- **Action**: Minor, no breaking changes expected

#### 3.1.x → 3.2.x
- Virtual threads support (opt-in)
- RestClient introduction
- **Action**: Consider virtual threads for improved scalability

#### 3.2.x → 3.3.x
- CDS (Class Data Sharing) support
- Improved AOT
- **Action**: Minor, no breaking changes expected

#### 3.3.x → 3.4.x
- Structured logging
- Enhanced observability
- **Action**: Consider structured logging adoption

#### 3.4.x → 3.5.x
- Latest stable target
- **Action**: Final verification

---

## 2. javax → jakarta Namespace Migration

### Decision: Use OpenRewrite for Automated Migration
**Rationale**: Manual find/replace is error-prone; OpenRewrite provides tested recipes
**Alternatives Considered**: Manual migration (rejected - labor intensive, error-prone)

### Affected Packages

| Old Package | New Package | Usage in Project |
|-------------|-------------|------------------|
| javax.persistence.* | jakarta.persistence.* | All JPA entities (6 files) |
| javax.validation.* | jakarta.validation.* | Bean validation annotations |
| javax.servlet.* | jakarta.servlet.* | Web filters if any |
| javax.annotation.* | jakarta.annotation.* | @PostConstruct, @Resource |

### OpenRewrite Recipe
```xml
<plugin>
  <groupId>org.openrewrite.maven</groupId>
  <artifactId>rewrite-maven-plugin</artifactId>
  <version>5.x</version>
  <configuration>
    <activeRecipes>
      <recipe>org.openrewrite.java.spring.boot3.UpgradeSpringBoot_3_0</recipe>
    </activeRecipes>
  </configuration>
</plugin>
```

---

## 3. Java 8 → Java 21 Upgrade

### Decision: Three-Step Java Upgrade (8→11→17→21)
**Rationale**: Gradual upgrade allows catching compatibility issues early
**Alternatives Considered**: Direct 8→21 (rejected - too risky, misses intermediate issues)

### Java 8 → 11 Changes
- Module system (JPMS) - not required for this project
- `var` local variable type inference
- HTTP Client API
- **Action**: Verify no removed APIs used

### Java 11 → 17 Changes
- Sealed classes, records (optional adoption)
- Strong encapsulation of JDK internals
- Pattern matching for instanceof
- **Action**: Add `--add-opens` if reflection issues occur

### Java 17 → 21 Changes
- Virtual threads (Project Loom)
- Sequenced collections
- Pattern matching for switch
- **Action**: Consider adopting virtual threads for improved throughput

### Required JVM Arguments (if needed)
```bash
--add-opens java.base/java.lang=ALL-UNNAMED
--add-opens java.base/java.util=ALL-UNNAMED
```

---

## 4. AngularJS 1.6.4 → Angular 19 Migration

### Decision: Complete Rewrite with Feature Parity
**Rationale**: AngularJS and Angular are fundamentally different frameworks; migration tool (ngUpgrade) adds complexity for small apps
**Alternatives Considered**: 
- ngUpgrade hybrid (rejected - complexity overhead for small app)
- React/Vue (rejected - user specified Angular)

### Angular 19 Architecture Decisions

| Decision | Choice | Rationale |
|----------|--------|-----------|
| Component Style | Standalone | Angular 19 default, simpler |
| State Management | Signals | Angular 19 native, reactive |
| Routing | @angular/router | Standard, lazy loading |
| HTTP | HttpClient | Standard, interceptors |
| Forms | Reactive Forms | Type-safe, validation |
| Styling | Bootstrap 5 | Maintain existing look |
| Testing | Jest + Cypress | Modern, fast |

### Screen Mapping (AngularJS → Angular)

| AngularJS Component | Angular Component | Notes |
|--------------------|-------------------|-------|
| `owner-list` | `OwnersListComponent` | Standalone, signals |
| `owner-details` | `OwnerDetailComponent` | Standalone, signals |
| `owner-form` | `OwnerFormComponent` | Reactive forms |
| `pet-form` | `PetFormComponent` | Reactive forms |
| `visits` | `VisitsComponent` | Standalone, signals |
| `vet-list` | `VetsListComponent` | Standalone, signals |
| `welcome` | `WelcomeComponent` | Home page |

### Frontend Build Integration

```xml
<!-- New Maven profile for Angular build -->
<profile>
  <id>angular</id>
  <build>
    <plugins>
      <plugin>
        <groupId>com.github.eirslett</groupId>
        <artifactId>frontend-maven-plugin</artifactId>
        <version>1.15.0</version>
        <configuration>
          <workingDirectory>petclinic-frontend</workingDirectory>
          <nodeVersion>v22.11.0</nodeVersion>
        </configuration>
        <executions>
          <execution>
            <id>install node and npm</id>
            <goals><goal>install-node-and-npm</goal></goals>
          </execution>
          <execution>
            <id>npm install</id>
            <goals><goal>npm</goal></goals>
          </execution>
          <execution>
            <id>npm build</id>
            <goals><goal>npm</goal></goals>
            <configuration>
              <arguments>run build</arguments>
            </configuration>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
</profile>
```

---

## 5. Database Compatibility

### Decision: Maintain HSQLDB + MySQL Dual Support
**Rationale**: HSQLDB for development/testing, MySQL for production
**Alternatives Considered**: PostgreSQL (rejected - scope creep)

### MySQL Connector Update
- Old: `mysql:mysql-connector-java`
- New: `com.mysql:mysql-connector-j`

### Schema Compatibility
- No schema changes required
- Existing SQL scripts compatible with both Spring Boot 2.x and 3.x
- Consider Flyway/Liquibase for future migrations

---

## 6. Testing Strategy

### Decision: Comprehensive Test Suite Before Upgrade
**Rationale**: Constitution Principle I - NON-NEGOTIABLE

### Test Categories

| Category | Framework | Coverage Target |
|----------|-----------|-----------------|
| Unit Tests | JUnit 5 + Mockito | ≥80% line coverage |
| Integration Tests | Spring Boot Test | 100% REST endpoints |
| Contract Tests | Spring Cloud Contract or Pact | API stability |
| E2E Tests | Cypress | All user journeys |

### Key Test Scenarios

1. **Owner CRUD**: Create, read, update owner
2. **Pet Management**: Add pet to owner, update pet
3. **Visit Scheduling**: Create visit for pet
4. **Vet Listing**: View all veterinarians
5. **Pet Types**: Retrieve pet type options
6. **Database Profiles**: Test with HSQLDB and MySQL

---

## 7. Build and DevOps

### Decision: Update Maven Plugins and Docker Base Images
**Rationale**: Security, performance, compatibility

### Maven Plugin Updates

| Plugin | Current | Target |
|--------|---------|--------|
| spring-boot-maven-plugin | 2.1.3 | 3.5.x |
| frontend-maven-plugin | 1.3 | 1.15.x |
| maven-compiler-plugin | (default) | 3.11+ |
| docker-maven-plugin | 1.2.0 | Consider jib-maven-plugin |

### Docker Base Image
- Old: Not specified (likely openjdk:8)
- New: `eclipse-temurin:21-jre-alpine` (small, secure)

---

## Summary of Resolved Decisions

| Item | Decision | Risk Level |
|------|----------|------------|
| Spring Boot Target | 3.5.x | Low (well-tested path) |
| Java Target | 21 | Low (LTS, mature) |
| Angular Target | 19.x | Low (latest stable) |
| Migration Approach | Incremental per constitution | Low |
| javax→jakarta | OpenRewrite automated | Medium |
| Frontend Strategy | Complete rewrite | Medium |
| Testing | Must achieve 80% before upgrade | N/A (gate) |

**All NEEDS CLARIFICATION items resolved. Ready for Phase 1 design.**
