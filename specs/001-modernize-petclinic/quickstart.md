# Quickstart: Spring Petclinic Modernization

**Feature**: 001-modernize-petclinic
**Branch**: `001-modernize-petclinic`

## Prerequisites

### Required Software

| Tool | Required Version | Verification Command |
|------|------------------|---------------------|
| JDK | 8 (current), 11, 17, 21 (target) | `java -version` |
| Maven | 3.8+ | `mvn -version` |
| Node.js | 22.x LTS | `node --version` |
| npm | 10.x | `npm --version` |
| Docker | 24+ | `docker --version` |
| Git | 2.40+ | `git --version` |

### JDK Installation (Multiple Versions)

You'll need multiple JDK versions during the upgrade:

```bash
# Using SDKMAN (recommended)
sdk install java 8.0.432-tem
sdk install java 11.0.25-tem
sdk install java 17.0.13-tem
sdk install java 21.0.5-tem

# Switch between versions
sdk use java 8.0.432-tem  # For initial testing
sdk use java 21.0.5-tem   # For target version
```

### IDE Setup

**VS Code Extensions** (recommended):
- Extension Pack for Java (`vscode-java-pack`)
- Spring Boot Extension Pack (`vmware.vscode-boot-dev-pack`)
- Angular Language Service (`angular.ng-template`)

**IntelliJ IDEA**:
- Import as Maven project
- Configure Project SDK to JDK 8 initially

---

## Getting Started

### 1. Clone and Setup

```bash
# Clone the repository
git clone <repository-url>
cd spring-petclinic-angularjs

# Switch to feature branch
git checkout 001-modernize-petclinic

# Verify current state builds
mvn clean verify -DskipTests
```

### 2. Run Current Application

```bash
# Terminal 1: Start backend (port 9966)
cd spring-petclinic-server
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Terminal 2: Start frontend dev server (port 8080)
cd spring-petclinic-client
npm install
npm run dev
```

**Access the application**:
- Frontend: http://localhost:8080/
- Backend API: http://localhost:9966/petclinic/api/
- Swagger UI (if enabled): http://localhost:9966/petclinic/swagger-ui.html

### 3. Run Tests (Baseline)

```bash
# Run all existing tests
mvn test

# Run with coverage report
mvn test jacoco:report

# View coverage report
open spring-petclinic-server/target/site/jacoco/index.html
```

---

## Project Structure

```
spring-petclinic-angularjs/
├── .specify/                    # Specification framework
│   ├── memory/
│   │   └── constitution.md      # Governance principles
│   └── templates/
├── specs/
│   └── 001-modernize-petclinic/ # This feature
│       ├── spec.md              # Requirements
│       ├── plan.md              # Implementation plan
│       ├── research.md          # Technical decisions
│       ├── data-model.md        # Entity documentation
│       ├── contracts/           # API specifications
│       │   └── petclinic-api.yaml
│       └── quickstart.md        # This file
├── spring-petclinic-server/     # Backend (Spring Boot)
│   ├── src/main/java/
│   │   └── org/springframework/samples/petclinic/
│   │       ├── model/           # JPA entities
│   │       ├── repository/      # Data access
│   │       ├── service/         # Business logic
│   │       └── web/             # REST controllers
│   └── src/test/java/           # Unit tests
├── spring-petclinic-client/     # Frontend (AngularJS 1.x)
│   └── src/
│       ├── scripts/             # AngularJS modules
│       ├── css/                 # Stylesheets
│       └── images/              # Static assets
└── pom.xml                      # Parent POM
```

---

## Key Files to Know

### Backend (Server)

| File | Purpose |
|------|---------|
| `pom.xml` (root) | Parent POM, Spring Boot version |
| `spring-petclinic-server/pom.xml` | Backend dependencies |
| `application.properties` | Default configuration |
| `application-dev.properties` | Development profile |
| `application-hsqldb.properties` | In-memory database |
| `application-mysql.properties` | MySQL database |

### Frontend (Client)

| File | Purpose |
|------|---------|
| `spring-petclinic-client/pom.xml` | Frontend Maven build |
| `bower.json` | AngularJS dependencies |
| `package.json` | npm scripts and build tools |
| `gulpfile.js` | Build automation |
| `src/scripts/app.js` | Main AngularJS module |

### Specification

| File | Purpose |
|------|---------|
| `specs/001-modernize-petclinic/spec.md` | Requirements & user stories |
| `specs/001-modernize-petclinic/plan.md` | Implementation sequence |
| `specs/001-modernize-petclinic/research.md` | Technical decisions |
| `contracts/petclinic-api.yaml` | OpenAPI specification |

---

## Development Workflow

### Phase 1: Test Coverage (BLOCKING)

Before any upgrade work:

```bash
# 1. Add missing unit tests
# Target: 80% coverage on server module

# 2. Add integration tests for REST endpoints
mvn test -Dtest=*ResourceTests

# 3. Generate coverage report
mvn jacoco:report

# 4. Verify coverage meets threshold
# If coverage < 80%, DO NOT proceed with upgrades
```

### Phase 2: Spring Boot Upgrade

Each step requires full test verification:

```bash
# Example: Upgrade from 2.1.3 to 2.2.x
# 1. Update pom.xml version
# 2. Build and test
mvn clean verify

# 3. If tests pass, commit
git add -A
git commit -m "chore: upgrade Spring Boot to 2.2.x"

# 4. Repeat for each version step
```

### Phase 3: Java Upgrade

```bash
# Switch Java version
sdk use java 11.0.25-tem

# Update pom.xml java.version property
# <java.version>11</java.version>

# Build and test
mvn clean verify
```

### Phase 4: Frontend Migration

```bash
# Create new Angular project
cd spring-petclinic-angularjs
npx @angular/cli new petclinic-frontend --style=scss --routing=true

# Generate components
cd petclinic-frontend
ng generate component features/owners/owner-list
ng generate component features/owners/owner-detail
# ... continue for all components
```

---

## Database Profiles

### HSQLDB (Development)

```bash
# Default in-memory database
mvn spring-boot:run -Dspring-boot.run.profiles=dev,hsqldb
```

### MySQL (Production-like)

```bash
# Start MySQL via Docker
docker-compose up -d mysql

# Run with MySQL profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev,mysql
```

---

## Troubleshooting

### Build Failures

```bash
# Clean build artifacts
mvn clean

# Skip tests temporarily (not for commits!)
mvn package -DskipTests

# Force dependency refresh
mvn dependency:purge-local-repository
mvn clean install
```

### Port Conflicts

```bash
# Check what's using port 9966
lsof -i :9966
# or on Windows
netstat -ano | findstr :9966

# Change port if needed
mvn spring-boot:run -Dserver.port=9967
```

### JDK Issues

```bash
# Verify JAVA_HOME
echo $JAVA_HOME

# List installed JDKs (SDKMAN)
sdk list java

# Set specific version
export JAVA_HOME=/path/to/jdk
```

---

## Useful Commands

### Maven

```bash
# Run with specific profile
mvn spring-boot:run -P dev

# Build without tests
mvn package -DskipTests

# Generate dependency tree
mvn dependency:tree

# Check for dependency updates
mvn versions:display-dependency-updates
```

### Git

```bash
# View upgrade progress
git log --oneline

# Rollback last upgrade step
git revert HEAD

# Create savepoint
git tag pre-upgrade-<version>
```

### Testing

```bash
# Run single test class
mvn test -Dtest=OwnerResourceTests

# Run tests matching pattern
mvn test -Dtest=*Resource*

# Run with debug output
mvn test -X
```

---

## Next Steps

1. **Read `plan.md`** - Understand the upgrade sequence
2. **Review `constitution.md`** - Know the governance rules
3. **Run tests** - Establish current baseline
4. **Start Phase 1** - Achieve 80% test coverage before any upgrades

## Support

- **Feature Spec**: `specs/001-modernize-petclinic/spec.md`
- **Technical Decisions**: `specs/001-modernize-petclinic/research.md`
- **API Contract**: `specs/001-modernize-petclinic/contracts/petclinic-api.yaml`
