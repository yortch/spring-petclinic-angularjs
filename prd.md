# Product Requirements Document (PRD)
## Spring PetClinic Application

### Version: 2.1.3 (Legacy)
### Date: October 13, 2025
### Status: Current Analysis for Modernization

---

## 1. Executive Summary

The Spring PetClinic is a sample application demonstrating Spring Framework capabilities combined with AngularJS frontend. The application manages a veterinary clinic's operations including pet owners, their pets, veterinarians, and medical visits.

### 1.1 Purpose
This application serves as a reference implementation showcasing:
- RESTful API design with Spring Boot
- Single Page Application (SPA) using AngularJS
- JPA/Hibernate data persistence
- Spring Data repositories
- Spring caching with EhCache
- Build automation with Maven and frontend tooling

### 1.2 Current Technology Stack
- **Backend**: Java 8, Spring Boot 2.1.3, Spring MVC, Spring Data JPA
- **Database**: HSQLDB (in-memory), MySQL (optional)
- **Frontend**: AngularJS 1.6.10, Bootstrap 3.3.7, jQuery 3.2.1
- **Build Tools**: Maven 3.9.9, Node.js v10.24.1, Bower, Gulp
- **Testing**: JUnit 4, Spring Test, MockMvc

---

## 2. System Architecture

### 2.1 Architecture Overview
The application follows a layered architecture with clear separation of concerns:

```
┌─────────────────────────────────────┐
│     AngularJS Client (SPA)          │
│  (spring-petclinic-client module)   │
└──────────────┬──────────────────────┘
               │ HTTP/REST
┌──────────────▼──────────────────────┐
│   REST Controllers (@RestController) │
│  (OwnerResource, PetResource, etc.)  │
├─────────────────────────────────────┤
│      Service Layer                   │
│   (ClinicService interface)          │
├─────────────────────────────────────┤
│   Repository Layer                   │
│  (Spring Data JPA Repositories)      │
├─────────────────────────────────────┤
│      JPA/Hibernate                   │
├─────────────────────────────────────┤
│      Database (HSQLDB/MySQL)         │
└─────────────────────────────────────┘
```

### 2.2 Module Structure
1. **spring-petclinic-client**: Static resources packaged as a webjar
   - AngularJS application code
   - HTML templates
   - CSS styles (LESS compiled)
   - Images and fonts
   
2. **spring-petclinic-server**: Spring Boot backend application
   - REST API endpoints
   - Business logic
   - Data access layer
   - Thymeleaf index.html template serving

---

## 3. Data Model

### 3.1 Entity Relationship Diagram

```
Owner (1) ──────< (M) Pet (M) >────── (1) PetType
Person                │
                      │
                      └──────< (M) Visit
                      
Vet (M) ──────< (M) Specialty
Person
```

### 3.2 Domain Entities

#### Owner (extends Person)
- **Fields**: 
  - `id` (Integer, primary key)
  - `firstName` (String, from Person)
  - `lastName` (String, from Person)
  - `address` (String, not null)
  - `city` (String, not null)
  - `telephone` (String, not null, 10 digits max)
  - `pets` (Set<Pet>, one-to-many)

#### Pet (extends NamedEntity)
- **Fields**:
  - `id` (Integer, primary key)
  - `name` (String, from NamedEntity)
  - `birthDate` (Date)
  - `type` (PetType, many-to-one)
  - `owner` (Owner, many-to-one)
  - `visits` (Set<Visit>, one-to-many, eager fetch)

#### Visit (extends BaseEntity)
- **Fields**:
  - `id` (Integer, primary key)
  - `date` (Date, not null)
  - `description` (String, max 255 chars)
  - `pet` (Pet, many-to-one)

#### Vet (extends Person)
- **Fields**:
  - `id` (Integer, primary key)
  - `firstName` (String, from Person)
  - `lastName` (String, from Person)
  - `specialties` (Set<Specialty>, many-to-many)

#### PetType (extends NamedEntity)
- **Fields**:
  - `id` (Integer, primary key)
  - `name` (String) - e.g., "cat", "dog", "bird"

#### Specialty (extends NamedEntity)
- **Fields**:
  - `id` (Integer, primary key)
  - `name` (String) - e.g., "radiology", "surgery", "dentistry"

---

## 4. Functional Requirements

### 4.1 Owner Management

#### FR-OM-001: List All Owners
- **Description**: Display a paginated list of all pet owners
- **URL**: `/#!/owners`
- **API Endpoint**: `GET /owners/list`
- **Acceptance Criteria**:
  - Show owner first name, last name, address, city, and telephone
  - Allow search/filter by last name
  - Display count of pets per owner
  - Clickable rows to view owner details

#### FR-OM-002: View Owner Details
- **Description**: Display detailed information about a specific owner
- **URL**: `/#!/owners/{ownerId}`
- **API Endpoint**: `GET /owners/{ownerId}`
- **Acceptance Criteria**:
  - Display owner's full contact information
  - List all pets belonging to the owner
  - Show pet names, birth dates, and types
  - Provide actions: Edit Owner, Add New Pet

#### FR-OM-003: Create New Owner
- **Description**: Add a new pet owner to the system
- **URL**: `/#!/owners/new`
- **API Endpoint**: `POST /owners`
- **Input Fields**:
  - First Name (required)
  - Last Name (required)
  - Address (required)
  - City (required)
  - Telephone (required, numeric, max 10 digits)
- **Validation**:
  - All fields must be non-empty
  - Telephone must be numeric and ≤10 digits
- **Acceptance Criteria**:
  - Form validation with error messages
  - Success: Redirect to owner details page
  - Failure: Display validation errors

#### FR-OM-004: Update Owner Information
- **Description**: Modify existing owner's information
- **URL**: `/#!/owners/{ownerId}/edit`
- **API Endpoint**: `PUT /owners/{ownerId}`
- **Acceptance Criteria**:
  - Pre-populate form with existing data
  - Same validation as create
  - Success: Return to owner details page
  - Failure: Display validation errors

### 4.2 Pet Management

#### FR-PM-001: Add New Pet to Owner
- **Description**: Register a new pet for an existing owner
- **URL**: `/#!/owners/{ownerId}/pets/new`
- **API Endpoint**: `POST /owners/{ownerId}/pets`
- **Input Fields**:
  - Name (required)
  - Birth Date (required, date picker)
  - Type (required, dropdown: cat, dog, lizard, snake, bird, hamster)
- **Validation**:
  - Pet name must be unique within owner's pets
  - Birth date cannot be in the future
- **Acceptance Criteria**:
  - Form validation with error messages
  - Date picker for birth date selection
  - Success: Return to owner details showing new pet

#### FR-PM-002: Update Pet Information
- **Description**: Modify existing pet's details
- **URL**: `/#!/owners/{ownerId}/pets/{petId}/edit`
- **API Endpoint**: `PUT /owners/{ownerId}/pets/{petId}`
- **Acceptance Criteria**:
  - Cannot change pet's owner
  - Same validation as adding new pet
  - Pet name uniqueness validated per owner

#### FR-PM-003: View Pet Details
- **Description**: Display comprehensive pet information
- **Included in**: Owner details page
- **API Endpoint**: `GET /owners/{ownerId}/pets/{petId}`
- **Display Information**:
  - Name, birth date, type
  - Visit history with dates and descriptions
  - Owner information

### 4.3 Visit Management

#### FR-VM-001: Add Visit to Pet
- **Description**: Record a veterinary visit for a pet
- **URL**: `/#!/owners/{ownerId}/pets/{petId}/visits/new`
- **API Endpoint**: `POST /owners/{ownerId}/pets/{petId}/visits`
- **Input Fields**:
  - Date (required, default: today)
  - Description (required, max 255 characters)
- **Acceptance Criteria**:
  - Default visit date to current date
  - Visit date cannot be in the future
  - Description is mandatory
  - Success: Return to owner details page

#### FR-VM-002: View Visit History
- **Description**: Display all visits for a pet
- **Included in**: Owner details page (per pet)
- **Acceptance Criteria**:
  - Visits sorted by date (newest first)
  - Show visit date and description
  - Display under each pet in owner details

### 4.4 Veterinarian Management

#### FR-VET-001: List All Veterinarians
- **Description**: Display all veterinarians and their specialties
- **URL**: `/#!/vets`
- **API Endpoint**: `GET /vets`
- **Acceptance Criteria**:
  - Show vet first name and last name
  - List specialties for each vet
  - Handle vets with no specialties gracefully
  - Present in tabular format

### 4.5 Home/Welcome Page

#### FR-WEL-001: Display Welcome Page
- **Description**: Landing page with application overview
- **URL**: `/#!/welcome` (default route)
- **Content**:
  - Welcome message
  - Pet clinic logo/image
  - Brief description of the application
  - Link to GitHub repository

---

## 5. REST API Specifications

### 5.1 Owner Endpoints

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| GET | `/owners/list` | Get all owners | - | `Collection<Owner>` |
| GET | `/owners/{ownerId}` | Get owner by ID | - | `Owner` |
| POST | `/owners` | Create new owner | `Owner` (JSON) | HTTP 201 Created |
| PUT | `/owners/{ownerId}` | Update owner | `Owner` (JSON) | `Owner` |

### 5.2 Pet Endpoints

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| GET | `/owners/{ownerId}/pets/{petId}` | Get pet by ID | - | `Pet` |
| POST | `/owners/{ownerId}/pets` | Add pet to owner | `Pet` (JSON) | HTTP 201 Created |
| PUT | `/owners/{ownerId}/pets/{petId}` | Update pet | `Pet` (JSON) | `Pet` |
| GET | `/pettypes` | Get all pet types | - | `Collection<PetType>` |

### 5.3 Visit Endpoints

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| POST | `/owners/{ownerId}/pets/{petId}/visits` | Add visit | `Visit` (JSON) | HTTP 201 Created |
| GET | `/owners/*/pets/{petId}/visits` | Get pet visits | - | `Collection<Visit>` |

### 5.4 Veterinarian Endpoints

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| GET | `/vets` | Get all vets | - | `Collection<Vet>` |

---

## 6. User Interface Requirements

### 6.1 Navigation
- **Top Navigation Bar**: 
  - Home link (Welcome page)
  - Find Owners link (Owner list)
  - Veterinarians link (Vet list)
- **Footer**: Copyright information and links

### 6.2 Design Specifications
- **Framework**: Bootstrap 3.3.7
- **Color Scheme**: 
  - Primary: Green theme (#6db33f - Spring green)
  - Navigation bar: Green background with white text
  - Buttons: Green for primary actions
- **Responsive**: Desktop-focused, basic mobile compatibility
- **Icons**: Bootstrap Glyphicons
- **Fonts**: Default system fonts

### 6.3 Component Specifications

#### Owner List Table
- Columns: Name, Address, City, Telephone, Pets
- Sortable columns
- Search filter by last name
- Click row to view details

#### Forms (Owner, Pet, Visit)
- Bootstrap form styling
- Required field indicators (*)
- Client-side validation with error messages
- Submit and Cancel buttons
- Date picker for date fields

#### Pet Display Cards
- Within owner details
- Show pet icon/avatar
- Display name, birth date, type
- Edit pet button
- Add visit button
- List of visits

---

## 7. Non-Functional Requirements

### 7.1 Performance
- **Page Load Time**: < 2 seconds on standard broadband
- **API Response Time**: < 500ms for standard queries
- **Database Queries**: Optimized with proper indexing
- **Caching**: EhCache for veterinarian list

### 7.2 Security
- **Input Validation**: Server-side validation for all inputs
- **SQL Injection**: Protected via JPA/Hibernate parameterized queries
- **XSS Protection**: Automatic escaping in templates
- **CORS**: Not configured (same-origin policy)

### 7.3 Scalability
- **Database**: Support for both HSQLDB (dev) and MySQL (production)
- **Concurrent Users**: Designed for small clinic (~10-50 concurrent users)
- **Data Volume**: Optimized for thousands of records

### 7.4 Reliability
- **Uptime**: Standard web application availability
- **Data Integrity**: ACID transactions via JPA
- **Error Handling**: Global error interceptor in AngularJS
- **Logging**: Spring Boot default logging (Logback)

### 7.5 Maintainability
- **Code Quality**: Clean code with separation of concerns
- **Documentation**: JavaDoc for public APIs
- **Testing**: Unit tests for REST controllers
- **Configuration**: Externalized in application.properties

### 7.6 Usability
- **Accessibility**: Basic HTML accessibility
- **Browser Support**: Modern browsers (Chrome, Firefox, Safari, Edge)
- **User Feedback**: Toast notifications for actions
- **Error Messages**: Clear, user-friendly error messages

---

## 8. Database Configuration

### 8.1 HSQLDB (Default)
- **Type**: In-memory database
- **Profile**: `hsqldb`
- **Data Initialization**: Automatic via `data-hsqldb.sql` and `schema-hsqldb.sql`
- **Use Case**: Development and testing

### 8.2 MySQL (Optional)
- **Type**: Persistent database
- **Profile**: `mysql.prod`
- **Configuration**:
  ```properties
  spring.datasource.url=jdbc:mysql://localhost:3306/petclinic?useUnicode=true
  spring.datasource.username=root
  spring.datasource.password=petclinic
  ```
- **Use Case**: Production deployments

---

## 9. Build and Deployment

### 9.1 Build Process
```bash
./mvnw clean install
```
- Maven multi-module build
- Frontend build (npm, bower, gulp) integrated
- Creates executable JAR: `spring-petclinic-server/target/petclinic.jar`

### 9.2 Running Locally
```bash
./mvnw spring-boot:run -pl spring-petclinic-server
```
- Starts embedded Tomcat on port 8080
- Accesses at http://localhost:8080/
- Hot reload with Spring Boot DevTools

### 9.3 Running with Docker
```bash
docker-compose up
```
- Builds application container
- Starts MySQL container
- Application available at http://localhost:8080/

---

## 10. Testing Strategy

### 10.1 Current Test Coverage
- **Unit Tests**: 3 REST controller tests
  - OwnerResourceTests
  - PetResourceTests
  - VetResourceTests
- **Framework**: JUnit 4, Spring Test, MockMvc
- **Mocking**: Mockito for service layer
- **Coverage**: Minimal (~15-20% estimated)

### 10.2 Test Scenarios Covered
1. **Owner API**: 
   - Retrieve owner by ID in JSON format
2. **Pet API**:
   - Retrieve pet by ID in JSON format
3. **Vet API**:
   - Retrieve all vets in JSON format

### 10.3 Testing Gaps (To Be Addressed)
- Service layer tests
- Repository layer tests
- Model validation tests
- Integration tests
- End-to-end UI tests
- Performance tests

---

## 11. Known Limitations

### 11.1 Current Limitations
1. **No Authentication/Authorization**: Open access to all features
2. **No Owner Deletion**: UI only supports create, read, update
3. **No Pet Deletion**: Cannot remove pets from system
4. **Single Clinic**: No multi-tenancy support
5. **Limited Search**: Only owner last name search
6. **No Pagination**: All owners loaded at once
7. **No Audit Trail**: No record of who/when modified data
8. **Basic Error Handling**: Limited user-friendly error messages

### 11.2 Browser Compatibility
- Modern browsers required (ES5 support)
- Internet Explorer 11+ (AngularJS 1.6 limitation)
- No mobile-optimized views

---

## 12. Future Enhancement Opportunities

### 12.1 Functional Enhancements
1. User authentication and authorization
2. Appointment scheduling
3. Billing and invoicing
4. Medical records management
5. Prescription tracking
6. Email notifications
7. Reporting and analytics
8. Multi-clinic support
9. Mobile application

### 12.2 Technical Improvements
1. Upgrade to Java 21 and Spring Boot 3.5
2. Migrate from AngularJS to modern Angular 20
3. Add comprehensive unit test coverage (>80%)
4. Implement UI automation tests (Playwright)
5. Add API documentation (OpenAPI/Swagger)
6. Implement GraphQL alternative
7. Add monitoring and observability
8. Container orchestration (Kubernetes)
9. CI/CD pipeline
10. Security hardening

---

## 13. Glossary

- **Owner**: A pet owner/customer of the clinic
- **Pet**: An animal registered with the clinic
- **Visit**: A medical appointment/visit for a pet
- **Vet**: Veterinarian providing medical services
- **Specialty**: Area of veterinary expertise
- **PetType**: Classification of pet (dog, cat, etc.)
- **SPA**: Single Page Application
- **REST**: Representational State Transfer
- **JPA**: Java Persistence API
- **ORM**: Object-Relational Mapping

---

## 14. References

### 14.1 Documentation
- Spring PetClinic: https://spring-petclinic.github.io/
- Spring Framework: https://spring.io/projects/spring-framework
- Spring Boot: https://spring.io/projects/spring-boot
- AngularJS: https://angularjs.org/

### 14.2 Repository
- GitHub: https://github.com/spring-petclinic/spring-petclinic-angularjs
- Issue Tracker: https://github.com/spring-petclinic/spring-petclinic-angularjs/issues

---

## Document History

| Version | Date | Author | Changes |
|---------|------|--------|---------|
| 1.0 | 2025-10-13 | System Analysis | Initial PRD creation for modernization planning |

