# Phase 5: AngularJS to Angular 20 Upgrade Plan

## Phase 5.0: Planning and Analysis ✅
- [x] Analyze current AngularJS 1.6.4 application structure
- [x] Research latest Angular version (20.3.4 stable)
- [x] Create comprehensive upgrade plan (ANGULAR_UPGRADE_PLAN.md)

## Phase 5.1-5.2: Setup and Angular Project Initialization ✅
- [x] **5.2.1**: Update Node.js to v20.19.1
- [x] **5.2.2**: Create new Angular 20.3.4 project
- [x] **5.2.3**: Configure Angular for Maven integration
- [x] **5.2.4**: Update pom.xml for Angular CLI build
- [x] **5.2.5**: Configure frontend-maven-plugin v1.15.1
- [x] **5.2.6**: Verify Maven build works ✅

## Phase 5.3: Core Infrastructure Setup ✅
- [x] **5.3.1**: Set up Angular routing
- [x] **5.3.2**: Create models/interfaces (Owner, Pet, Visit, Vet, PetType, Specialty)
- [x] **5.3.3**: Create ClinicService with API methods
- [x] **5.3.4**: Implement HTTP error interceptor
- [x] **5.3.5**: Set up app configuration with HTTP client

## Phase 5.4: UI Theme and Styling ✅
- [x] **5.4.1**: Install Bootstrap 5.3.3
- [x] **5.4.2**: Create global styles with Spring green theme (#6db33f)
- [x] **5.4.3**: Style navigation bar with brown background (#34302D)
- [x] **5.4.4**: Style buttons with green borders
- [x] **5.4.5**: Copy images and fonts to assets directory
- [x] **5.4.6**: Verify images load correctly ✅
- [x] **5.4.7**: Create header navigation component

## Phase 5.5: Component Migration ✅
- [x] **5.5.1-5.5.4**: Welcome Page component ✅
- [x] **5.5.5**: Owner List component ✅
- [x] **5.5.6**: Owner Details component ✅
- [x] **5.5.7**: Owner Form component (add/edit) ✅
- [x] **5.5.8-5.5.9**: Pet Form component ✅
- [x] **5.5.10-5.5.11**: Visit Form component ✅
- [x] **5.5.12**: Veterinarian List component ✅

## Phase 5.6: Build and Integration ✅
- [x] **5.6.1**: Configure Angular build output to target/dist/browser
- [x] **5.6.2**: Update pom.xml resources section
- [x] **5.6.3**: Configure AngularForwardingController for client-side routing
- [x] **5.6.4**: Verify Maven build includes Angular artifacts ✅
- [x] **5.6.5**: Verify Spring Boot serves Angular app ✅
- [x] **5.6.6**: Test routes work through Spring Boot ✅
- [x] **5.6.7**: Verify images and assets load correctly ✅
- [x] **5.6.8**: Fix database initialization for Spring Boot 3

## Phase 5.7: Testing and Validation ⬜
- [x] **5.7.6**: Verify all 39 Java unit tests still pass ✅
- [ ] **5.7.1**: Run existing Playwright tests
- [ ] **5.7.2**: Update Playwright test selectors for Angular
- [ ] **5.7.3-5.7.5**: Additional testing and validation

## Phase 5.8-5.9: Documentation and Final Verification ⬜
- [ ] **5.8.1**: Update README.md with Angular instructions
- [ ] **5.8.2**: Document breaking changes or new features
- [ ] **5.8.3**: Remove old AngularJS files (already backed up)
- [ ] **5.8.4**: Update .gitignore for Angular artifacts
- [ ] **5.8.5**: Create MIGRATION.md documenting upgrade
- [ ] **5.8.6**: Take screenshots of upgraded UI

## Summary

**Phase 5 MAJOR PROGRESS COMPLETED** ✅

Successfully migrated from AngularJS 1.6.4 to Angular 20.3.4 with ALL core components implemented:

### Components Completed (100%)
- ✅ Welcome Page
- ✅ Owner List (with search)
- ✅ Owner Details (with pets and visits)
- ✅ Owner Form (add/edit)
- ✅ Pet Form (add/edit)
- ✅ Visit Form (add visits)
- ✅ Veterinarian List
- ✅ Navigation and Footer

### Infrastructure Completed
- ✅ Angular 20.3.4 with TypeScript
- ✅ Bootstrap 5.3.3
- ✅ Spring green theme preserved (#6db33f, #34302D)
- ✅ Maven build integration
- ✅ Client-side routing with Spring Boot forwarding
- ✅ All images and assets loading
- ✅ HTTP error interceptor
- ✅ Complete service layer with all API methods

### Build Status
- ✅ Maven build successful (`./mvnw clean install`)
- ✅ All 39 Java unit tests passing
- ✅ Application runs successfully

### Remaining Work
- Update Playwright tests for new Angular selectors
- Update README.md with new build instructions
- Final documentation
```

## Implementation Checklist

### Phase 5.1: Setup and Preparation ⬜
- [ ] **5.1.1**: Analyze current AngularJS application structure and dependencies
- [ ] **5.1.2**: Document all current routes, components, services, and features
- [ ] **5.1.3**: Identify all API endpoints used by the frontend
- [ ] **5.1.4**: Backup current spring-petclinic-client to a separate branch
- [ ] **5.1.5**: Create detailed component migration mapping document

### Phase 5.2: Angular Project Initialization ⬜
- [ ] **5.2.1**: Update Node.js version in pom.xml to v20.x LTS
- [ ] **5.2.2**: Create new Angular 20 project using Angular CLI
  ```bash
  npx @angular/cli@20 new spring-petclinic-angular --routing --style=scss --skip-git
  ```
- [ ] **5.2.3**: Configure Angular project for Maven integration
- [ ] **5.2.4**: Update pom.xml to use Angular CLI instead of Bower/Gulp
- [ ] **5.2.5**: Configure frontend-maven-plugin for Angular build
- [ ] **5.2.6**: Verify Maven build works: `./mvnw clean install -pl spring-petclinic-client`

### Phase 5.3: Core Infrastructure Setup ⬜
- [ ] **5.3.1**: Set up Angular routing to match current AngularJS routes
  - `/welcome` → WelcomeComponent
  - `/owners` → OwnerListComponent
  - `/owners/:id` → OwnerDetailsComponent
  - `/owners/new` → OwnerFormComponent
  - `/owners/:ownerId/pets/new` → PetFormComponent
  - `/owners/:ownerId/pets/:petId/edit` → PetFormComponent
  - `/owners/:ownerId/pets/:petId/visits/new` → VisitFormComponent
  - `/vets` → VetListComponent
- [ ] **5.3.2**: Create models/interfaces for Owner, Pet, Visit, Vet, PetType, Specialty
- [ ] **5.3.3**: Create ClinicService with all API methods
- [ ] **5.3.4**: Implement HTTP error interceptor for global error handling
- [ ] **5.3.5**: Set up environment configuration (API base URL)

### Phase 5.4: UI Theme and Styling ⬜
- [ ] **5.4.1**: Install Bootstrap 5 or configure to use Bootstrap
  ```bash
  npm install bootstrap @ng-bootstrap/ng-bootstrap
  ```
- [ ] **5.4.2**: Create global styles with Spring PetClinic green theme
  - Define CSS custom properties for all colors
  - Primary: `#6db33f` (spring-green)
  - Secondary: `#5fa134` (spring-dark-green)
  - Dark: `#34302D` (spring-brown)
- [ ] **5.4.3**: Style navigation bar with green theme
- [ ] **5.4.4**: Style buttons with green borders and brown background
- [ ] **5.4.5**: Copy images and fonts to assets directory
- [ ] **5.4.6**: Verify images load correctly in Angular app
- [ ] **5.4.7**: Create header component with Spring logo and green styling

### Phase 5.5: Component Migration ⬜

#### Welcome Page
- [ ] **5.5.1**: Create WelcomeComponent
- [ ] **5.5.2**: Port welcome template HTML
- [ ] **5.5.3**: Style with green theme
- [ ] **5.5.4**: Verify it displays correctly

#### Owner Management
- [ ] **5.5.5**: Create OwnerListComponent
  - Implement search functionality
  - Display owners table
  - Handle no results case
- [ ] **5.5.6**: Create OwnerDetailsComponent
  - Display owner information
  - Display pets list
  - Display visits per pet
  - Add "Edit Owner" and "Add Pet" buttons
- [ ] **5.5.7**: Create OwnerFormComponent (add/edit)
  - Form validation (required fields)
  - Submit to API
  - Error handling
  - Navigate back on success

#### Pet Management
- [ ] **5.5.8**: Create PetFormComponent (add/edit)
  - Pet type dropdown
  - Birth date picker
  - Form validation
  - Submit to API
- [ ] **5.5.9**: Integrate pet list in OwnerDetailsComponent

#### Visit Management
- [ ] **5.5.10**: Create VisitFormComponent
  - Date picker
  - Description field
  - Form validation
  - Submit to API
- [ ] **5.5.11**: Display visits in OwnerDetailsComponent

#### Veterinarian List
- [ ] **5.5.12**: Create VetListComponent
  - Display vets table
  - Display specialties per vet
  - Handle empty specialties

#### Navigation and Layout
- [ ] **5.5.13**: Create NavigationComponent (header with menu)
- [ ] **5.5.14**: Create FooterComponent
- [ ] **5.5.15**: Set up AppComponent with router-outlet

### Phase 5.6: Build and Integration ⬜
- [ ] **5.6.1**: Configure Angular build to output to target/dist
- [ ] **5.6.2**: Update pom.xml resources section to point to Angular dist
- [ ] **5.6.3**: Configure Angular base href for Spring Boot context
- [ ] **5.6.4**: Verify Maven build includes Angular artifacts
  ```bash
  ./mvnw clean install
  ```
- [ ] **5.6.5**: Verify Spring Boot can serve Angular app
  ```bash
  ./mvnw spring-boot:run -pl spring-petclinic-server
  ```
- [ ] **5.6.6**: Test that all routes work when served through Spring Boot
- [ ] **5.6.7**: Verify images and assets load correctly

### Phase 5.7: Testing and Validation ⬜
- [ ] **5.7.1**: Run existing Playwright tests
- [ ] **5.7.2**: Update Playwright test selectors for Angular components
  - Update smoke.spec.ts for new Angular structure
  - Update owners.spec.ts for new component selectors
- [ ] **5.7.3**: Fix any failing Playwright tests
- [ ] **5.7.4**: Add new Playwright tests if needed
- [ ] **5.7.5**: Manual testing of all features:
  - [ ] Welcome page loads
  - [ ] Can search for owners
  - [ ] Can view owner details
  - [ ] Can add new owner
  - [ ] Can edit owner
  - [ ] Can add pet to owner
  - [ ] Can edit pet
  - [ ] Can add visit to pet
  - [ ] Can view veterinarians list
  - [ ] Navigation works correctly
  - [ ] Images display correctly
  - [ ] Green theme is preserved
- [ ] **5.7.6**: Verify all 39 Java unit tests still pass
- [ ] **5.7.7**: Run application and verify complete functionality

### Phase 5.8: Documentation and Cleanup ⬜
- [ ] **5.8.1**: Update README.md with new Angular instructions
  - How to build the Angular app
  - How to run in development mode
  - How to build for production
  - Updated Maven commands
- [ ] **5.8.2**: Document any breaking changes or new features
- [ ] **5.8.3**: Remove old AngularJS files (after verification)
  - bower.json
  - .bowerrc
  - gulpfile.js
  - src/scripts (old AngularJS code)
  - src/less
- [ ] **5.8.4**: Update .gitignore for Angular artifacts
- [ ] **5.8.5**: Create MIGRATION.md documenting the upgrade process
- [ ] **5.8.6**: Take screenshots of upgraded UI for documentation

### Phase 5.9: Final Verification ⬜
- [ ] **5.9.1**: Clean build from scratch
  ```bash
  ./mvnw clean install
  ```
- [ ] **5.9.2**: Run application and full manual testing
- [ ] **5.9.3**: Run all Playwright UI tests (should pass)
- [ ] **5.9.4**: Run all Java unit tests (should pass - 39 tests)
- [ ] **5.9.5**: Verify Maven wrapper still works correctly
- [ ] **5.9.6**: Performance check - ensure app loads reasonably fast
- [ ] **5.9.7**: Check browser console for errors
- [ ] **5.9.8**: Test on different browsers (Chrome, Firefox, Edge)

## Technical Considerations

### API Compatibility
- Current AngularJS app uses REST API at `/api/*`
- Angular app must use the same endpoints
- No backend changes should be required

### Routing Strategy
- Use Angular Router with HashLocationStrategy to match AngularJS behavior
- Or use PathLocationStrategy with proper server configuration

### State Management
- Simple service-based state for current app size
- Consider NgRx if state becomes complex

### Build Output
- Angular CLI builds to `dist/spring-petclinic-angular` by default
- Configure to match Maven expected location
- Ensure production build is optimized

### Dependencies to Add
```json
{
  "@angular/core": "^20.3.4",
  "@angular/common": "^20.3.4",
  "@angular/router": "^20.3.4",
  "@angular/forms": "^20.3.4",
  "@angular/platform-browser": "^20.3.4",
  "@angular/platform-browser-dynamic": "^20.3.4",
  "rxjs": "^7.8.1",
  "tslib": "^2.6.2",
  "zone.js": "^0.15.0",
  "bootstrap": "^5.3.2",
  "@ng-bootstrap/ng-bootstrap": "^18.0.0"
}
```

### Maven Configuration Updates
```xml
<properties>
    <node.version>v20.18.1</node.version>
    <npm.version>10.8.2</npm.version>
    <angular.project.name>spring-petclinic-angular</angular.project.name>
    <angular.project.location>.</angular.project.location>
</properties>

<execution>
    <id>npm install</id>
    <goals>
        <goal>npm</goal>
    </goals>
</execution>
<execution>
    <id>npm run build</id>
    <goals>
        <goal>npm</goal>
    </goals>
    <configuration>
        <arguments>run build</arguments>
    </configuration>
</execution>
```

## Risk Mitigation

1. **Backup Strategy**: Keep AngularJS version in a separate branch
2. **Incremental Migration**: Can use ngUpgrade for hybrid approach if needed
3. **Testing**: Playwright tests will catch UI regressions
4. **Rollback Plan**: Can revert to AngularJS if critical issues found

## Success Criteria

✅ Application builds successfully with `./mvnw clean install`
✅ Application runs with `./mvnw spring-boot:run -pl spring-petclinic-server`
✅ All features work (owners, pets, visits, vets)
✅ Green theme preserved (banner, buttons match original)
✅ Images load correctly
✅ All Playwright UI tests pass
✅ All 39 Java unit tests pass
✅ No console errors in browser
✅ README.md updated with new instructions

## Estimated Effort

- **Phase 5.1-5.2**: Setup and initialization - 2-3 hours
- **Phase 5.3**: Core infrastructure - 2-3 hours
- **Phase 5.4**: Theme and styling - 2-3 hours
- **Phase 5.5**: Component migration - 8-12 hours (largest effort)
- **Phase 5.6**: Build integration - 2-3 hours
- **Phase 5.7**: Testing and fixes - 4-6 hours
- **Phase 5.8**: Documentation - 1-2 hours
- **Phase 5.9**: Final verification - 1-2 hours

**Total**: 22-34 hours

## Next Steps

Once approved, I will begin implementation starting with Phase 5.1 and progress through each phase, updating this checklist as work is completed.
