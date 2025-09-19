# Copilot Instructions for Spring PetClinic AngularJS

## Architecture Overview

This is a **multi-module Maven project** demonstrating a Spring Boot REST API with an AngularJS frontend. The architecture splits client and server into separate Maven modules:

- **`spring-petclinic-server/`**: Spring Boot 2.1.3 REST API with JPA/Hibernate
- **`spring-petclinic-client/`**: AngularJS 1.6.4 SPA packaged as a WebJar

The client module is served as static resources by the Spring Boot server, creating a single deployable JAR.

## Key Architectural Patterns

### Module Structure Pattern
- **Parent POM**: Root `pom.xml` coordinates both modules
- **WebJar Integration**: Client assets packaged as JAR, consumed by server
- **Component Modules**: Each AngularJS feature (owner-list, vet-list, etc.) is a separate Angular module with routing

### REST API Conventions
All controllers extend `AbstractResourceController` and follow these patterns:
- **Resource naming**: `OwnerResource`, `PetResource`, `VetResource`
- **URL patterns**: `/api/owners`, `/api/pets`, `/api/vets`
- **HTTP methods**: Standard REST verbs with `@GetMapping`, `@PostMapping`, etc.
- **Validation**: Use `@Valid` with custom validators like `PetValidator`

### AngularJS Architecture
- **Module per feature**: Each domain (owners, pets, vets) has its own Angular module
- **UI-Router**: State-based routing with parent/child state hierarchy
- **Component pattern**: Each feature uses Angular components (`<owner-list>`, `<vet-list>`)
- **Centralized error handling**: `HttpErrorHandlingInterceptor` for global error display

## Essential Development Commands

### Build and Run (from project root)
```bash
./mvnw clean install          # Build entire project
cd spring-petclinic-server
../mvnw spring-boot:run       # Start server on http://localhost:8080
```

### Frontend Development
The client module uses **frontend-maven-plugin** to manage Node.js/npm:
- **Node version**: v10.24.1 (defined in client `pom.xml`)
- **Build tools**: Gulp for minification, Bower for dependencies
- **CSS**: LESS compilation to CSS via Gulp tasks

### Database Profiles
Switch between databases via Spring profiles:
- **HSQLDB (default)**: `spring.profiles.active=hsqldb,prod`
- **MySQL**: `spring.profiles.active=mysql,prod`
- **Development mode**: Add `dev` profile for live reload

### Docker Development
```bash
docker-compose up    # Starts MySQL + PetClinic containers
```

## Project-Specific Conventions

### Frontend Module Organization
Each AngularJS feature follows this structure:
```
src/scripts/[feature]/
├── [feature].js           # Module definition and routing
├── [feature].controller.js # Controller logic
├── [feature].component.js  # Component definition
└── [feature].template.html # Template
```

### Backend Service Layer
- **Service interface**: `ClinicService` provides business logic
- **Repository pattern**: JPA repositories for data access
- **Model validation**: JSR-303 annotations with custom validators
- **Exception handling**: Spring's `@ControllerAdvice` for error responses

### Configuration Management
- **Property files**: Environment-specific configs in `application-{profile}.properties`
- **Cache configuration**: EhCache setup in `CacheConfig.java`
- **Web configuration**: Static resource mapping in `WebConfig.java`

## Integration Points

### Client-Server Communication
- **Base URL**: AngularJS calls relative URLs (e.g., `owners/list`)
- **Error handling**: Global interceptor displays server validation errors
- **Content type**: JSON for all API communication
- **Caching**: HTTP cache headers configured via `$httpProvider`

### Build Integration
- **Maven lifecycle**: Client builds during Maven `compile` phase
- **Asset pipeline**: Gulp processes LESS→CSS, minifies JS/CSS
- **Resource copying**: Built assets copied to `target/dist/` for packaging

### Database Integration
- **JPA configuration**: Hibernate with deferred bootstrap for performance
- **Connection pooling**: HikariCP (Spring Boot default)
- **Schema management**: `ddl-auto=none`, use Liquibase/Flyway for migrations
- **Transaction management**: Spring's `@Transactional` on service layer

## Development Workflow

1. **Backend changes**: Modify Java code, restart with `mvnw spring-boot:run`
2. **Frontend changes**: Client auto-rebuilds via Maven, refresh browser
3. **Database changes**: Update schema files, restart to apply
4. **Testing**: Use `dev` profile for faster development cycle with live reload

## Common Gotchas

- **Build order**: Always build from root to ensure client WebJar is available
- **Profile conflicts**: MySQL profile requires database running first
- **Asset paths**: Static resources served from `/` (mapped in `WebConfig`)
- **CORS**: Not needed since client/server are same origin
- **Cache issues**: Dev profile disables caching, prod enables aggressive caching