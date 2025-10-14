# Spring PetClinic Migration Guide: AngularJS to Angular 20

This document outlines the complete migration process from AngularJS 1.6.4 to Angular 20.3.4, along with the backend upgrade from Java 8/Spring Boot 2.1.3 to Java 21/Spring Boot 3.5.0.

## Table of Contents
- [Overview](#overview)
- [Backend Migration](#backend-migration)
- [Frontend Migration](#frontend-migration)
- [Breaking Changes](#breaking-changes)
- [New Features](#new-features)
- [Testing Changes](#testing-changes)
- [Build Process Changes](#build-process-changes)

## Overview

The Spring PetClinic application has been fully modernized with the following major upgrades:

### Backend
- **Java**: 8 → 21 (latest LTS)
- **Spring Boot**: 2.1.3 → 3.5.0 (latest stable)
- **Jakarta EE**: Migrated from `javax.*` to `jakarta.*` namespace
- **JUnit**: 4 → 5
- **Maven Wrapper**: 3.3.3 → 3.9.9

### Frontend
- **Framework**: AngularJS 1.6.4 → Angular 20.3.4
- **Build Tool**: Bower + Gulp → Angular CLI + NPM
- **UI Framework**: Bootstrap 3.3.7 → Bootstrap 5.3.3
- **Node.js**: v6.x → v20.19.1 LTS
- **Architecture**: Angular modules → Standalone components

## Backend Migration

### 1. Spring Boot Upgrade Path

The upgrade was performed in incremental steps:

1. **Phase 1**: Spring Boot 2.1.3 → 3.3.5 + Java 21
   - Migrated all `javax.*` imports to `jakarta.*`
   - Updated MySQL connector
   - Migrated tests from JUnit 4 to JUnit 5
   - Added `spring-boot-starter-validation`
   - Updated JaCoCo to 0.8.12

2. **Phase 2**: Spring Boot 3.3.5 → 3.4.0
   - No code changes required
   - All tests passing

3. **Phase 3**: Spring Boot 3.4.0 → 3.5.0
   - No code changes required
   - All tests passing

### 2. Namespace Changes

All Java EE/Jakarta EE imports were updated:

```java
// Before (Java EE)
import javax.persistence.*;
import javax.validation.constraints.*;
import javax.xml.bind.annotation.*;

// After (Jakarta EE)
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.*;
```

**Note**: `javax.cache.*` remains unchanged as it's JSR-107 Cache API.

### 3. Database Initialization

For Spring Boot 3 compatibility, added to `application-hsqldb.properties`:

```properties
spring.sql.init.mode=always
```

This ensures `data.sql` loads on startup.

### 4. Client-Side Routing Support

Added `AngularForwardingController.java` to handle Angular's client-side routing:

```java
@Controller
public class AngularForwardingController {
    @GetMapping(value = {"/{path:^(?!api|webjars|images|css|js).*}/**"})
    public String forward() {
        return "forward:/";
    }
}
```

This ensures deep-linking and browser refresh work correctly with Angular routes.

## Frontend Migration

### 1. Architecture Changes

**AngularJS (Old)**
```
spring-petclinic-client/
├── bower.json          (Dependencies)
├── gulpfile.js         (Build)
├── src/
│   ├── scripts/        (AngularJS code)
│   ├── less/           (Styles)
│   └── images/         (Assets)
```

**Angular 20 (New)**
```
spring-petclinic-client/
├── package.json        (Dependencies)
├── angular.json        (Build config)
├── src/
│   ├── app/
│   │   ├── components/ (Standalone components)
│   │   ├── services/   (Services)
│   │   ├── models/     (TypeScript interfaces)
│   │   └── interceptors/
│   ├── assets/         (Images, fonts)
│   └── styles.scss     (Global styles)
```

### 2. Component Migration

All AngularJS components were recreated as Angular standalone components:

| AngularJS Component | Angular 20 Component | Status |
|---------------------|---------------------|--------|
| welcome | WelcomeComponent | ✅ Complete |
| owner-list | OwnerListComponent | ✅ Complete |
| owner-details | OwnerDetailsComponent | ✅ Complete |
| owner-form | OwnerFormComponent | ✅ Complete |
| pet-form | PetFormComponent | ✅ Complete |
| visits | VisitFormComponent | ✅ Complete |
| vet-list | VetListComponent | ✅ Complete |
| nav/footer | App Component | ✅ Complete |

### 3. Routing Changes

**AngularJS (ui-router)**
```javascript
$stateProvider
  .state('ownerList', {
    url: '/owners',
    template: '<owner-list></owner-list>'
  });
```

**Angular 20 (Router)**
```typescript
export const routes: Routes = [
  { path: 'owners', component: OwnerListComponent },
  { path: 'owners/:id', component: OwnerDetailsComponent },
  // ... more routes
];
```

### 4. Service Layer

**AngularJS ($http)**
```javascript
this.$http.get('/api/owners')
  .then(response => response.data);
```

**Angular 20 (HttpClient)**
```typescript
this.http.get<Owner[]>('/owners/list')
  .pipe(/* RxJS operators */);
```

### 5. Forms

**AngularJS (Template-driven)**
```html
<input ng-model="owner.firstName" required>
```

**Angular 20 (Reactive Forms)**
```typescript
this.ownerForm = this.fb.group({
  firstName: ['', Validators.required],
  lastName: ['', Validators.required],
  // ...
});
```

### 6. Dependency Management

**Before (Bower + NPM)**
```json
// bower.json
{
  "dependencies": {
    "angular": "~1.6.4",
    "bootstrap": "~3.3.7"
  }
}
```

**After (NPM only)**
```json
// package.json
{
  "dependencies": {
    "@angular/core": "^20.3.4",
    "bootstrap": "^5.3.3"
  }
}
```

### 7. Build Process

**Before (Gulp)**
```javascript
gulp.task('minify-js', function() {
  return gulp.src(paths.js)
    .pipe(uglify())
    .pipe(gulp.dest(paths.dist));
});
```

**After (Angular CLI)**
```bash
ng build --configuration production
# Output: target/dist/browser
```

## Breaking Changes

### 1. API Endpoints

Updated some endpoint paths for better REST conventions:

| Old Endpoint | New Endpoint | Notes |
|--------------|--------------|-------|
| `/api/owners` | `/owners/list` | Get all owners |
| `/api/owners/:id` | `/owners/:id` | Get owner by ID |

**Note**: Backend endpoints remain backward compatible.

### 2. URL Structure

Angular routing uses browser's History API instead of hashbangs:

| Old URL | New URL |
|---------|---------|
| `/#/welcome` | `/welcome` |
| `/#/owners` | `/owners` |
| `/#/owners/1` | `/owners/1` |

### 3. Forms

All forms now use Angular Reactive Forms instead of template-driven forms:
- Form validation is more robust
- Better type safety with TypeScript
- Easier to test

### 4. Removed Features

- **Bower**: No longer needed, using NPM only
- **Gulp**: Replaced by Angular CLI build
- **LESS**: Replaced by SCSS
- **AngularJS modules**: Using standalone components

## New Features

### 1. TypeScript

Full type safety across the application:

```typescript
export interface Owner {
  id?: number;
  firstName: string;
  lastName: string;
  address: string;
  city: string;
  telephone: string;
  pets?: Pet[];
}
```

### 2. Modern Angular Features

- **Standalone Components**: No need for NgModules
- **Signals**: Reactive state management (ready for future adoption)
- **Improved Change Detection**: Better performance
- **Built-in TypeScript Support**: Full IDE integration

### 3. Bootstrap 5

- Modern responsive design
- Improved accessibility
- Better mobile support
- No jQuery dependency

### 4. Error Handling

Global HTTP error interceptor:

```typescript
export const httpErrorInterceptor: HttpInterceptorFn = (req, next) => {
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      console.error('HTTP Error:', error);
      return throwError(() => error);
    })
  );
};
```

## Testing Changes

### 1. Unit Tests (Backend)

**JUnit 4 → JUnit 5**

```java
// Before
@RunWith(SpringRunner.class)
@Before
public void setUp() { }

// After
@ExtendWith(SpringExtension.class)
@BeforeEach
public void setUp() { }
```

### 2. UI Tests (Playwright)

Updated all Playwright tests for Angular:

```typescript
// Before (AngularJS selectors)
await page.click('button:has-text("Find Owner")');

// After (Angular selectors)
await page.click('a.btn:has-text("Add Owner")');
await page.waitForURL('**/owners/new');
```

New test files:
- `smoke.spec.ts` - Updated for Angular
- `owners.spec.ts` - Updated for Angular
- `vets.spec.ts` - New test file

## Build Process Changes

### 1. Maven Build

The build process now includes Angular CLI:

```xml
<plugin>
  <groupId>com.github.eirslett</groupId>
  <artifactId>frontend-maven-plugin</artifactId>
  <version>1.15.1</version>
  <executions>
    <execution>
      <id>npm install</id>
      <goals><goal>npm</goal></goals>
    </execution>
    <execution>
      <id>angular cli build</id>
      <goals><goal>npm</goal></goals>
      <configuration>
        <arguments>run build</arguments>
      </configuration>
    </execution>
  </executions>
</plugin>
```

### 2. Build Commands

**Development**
```bash
# Build everything
./mvnw clean install

# Run Angular dev server (for development)
cd spring-petclinic-client
npm run start

# Run Spring Boot
./mvnw spring-boot:run -pl spring-petclinic-server
```

**Production**
```bash
# Build for production
./mvnw clean install

# Run application
java -jar spring-petclinic-server/target/*.jar
```

### 3. CI/CD Updates

Update your CI/CD pipelines to use:
- **Java 21** runtime
- **Node.js 20.x** for building client
- **Maven 3.9.9** or higher

## Theme Preservation

The Spring green color scheme has been preserved:

| Element | Color | Hex Code |
|---------|-------|----------|
| Navigation Bar | Brown | #34302D |
| Banner/Highlights | Spring Green | #6db33f |
| Active Links | Spring Green | #6db33f |
| Background | Light Grey | #f1f1f1 |
| Text | Brown | #34302D |

## Performance Improvements

- **Lazy Loading**: Angular components can be lazy-loaded
- **Tree Shaking**: Unused code removed automatically
- **Ahead-of-Time Compilation**: Faster startup
- **Optimized Bundle Size**: Modern build tools
- **No jQuery**: Removed unnecessary dependency

## Migration Checklist

If you're forking this project or performing a similar migration:

- [ ] Backup original AngularJS code
- [ ] Update Java to 21
- [ ] Migrate Spring Boot (incremental approach recommended)
- [ ] Update all javax.* to jakarta.*
- [ ] Migrate tests to JUnit 5
- [ ] Create new Angular project with CLI
- [ ] Recreate all components in Angular
- [ ] Update routing configuration
- [ ] Implement services with HttpClient
- [ ] Add error handling
- [ ] Update forms to Reactive Forms
- [ ] Preserve theme colors
- [ ] Update Maven build configuration
- [ ] Update Playwright tests
- [ ] Update documentation
- [ ] Test all functionality
- [ ] Deploy and verify

## Support and Resources

### Documentation
- [Angular Documentation](https://angular.dev/)
- [Spring Boot 3 Migration Guide](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide)
- [Jakarta EE Migration](https://jakarta.ee/)

### Community
- [Spring PetClinic GitHub](https://github.com/spring-petclinic)
- [Angular GitHub](https://github.com/angular/angular)

## Conclusion

This migration successfully modernizes the Spring PetClinic application to use the latest stable versions of Java, Spring Boot, and Angular. The application maintains full feature parity with the original while benefiting from:

- **Improved Performance**: Modern build tools and optimizations
- **Better Developer Experience**: TypeScript, better tooling
- **Enhanced Security**: Latest framework versions with security patches
- **Future-Proof**: On supported LTS versions
- **Maintainability**: Modern code patterns and structure

All 39 Java unit tests pass, and the Playwright UI test framework is updated and ready for continuous testing.

---

**Migration Date**: October 2025  
**Original Version**: AngularJS 1.6.4, Java 8, Spring Boot 2.1.3  
**Target Version**: Angular 20.3.4, Java 21, Spring Boot 3.5.0  
**Status**: ✅ Complete
