# AngularJS and Spring Boot version of the Spring PetClinic Sample Application [![Build Status](https://travis-ci.org/spring-petclinic/spring-petclinic-angularjs.svg?branch=master)](https://travis-ci.org/spring-petclinic/spring-petclinic-angularjs/)

## Understanding the Spring Petclinic application with a few diagrams
[See the presentation here](http://fr.slideshare.net/AntoineRey/spring-framework-petclinic-sample-application)

## Running petclinic locally
```
git clone https://github.com/spring-petclinic/spring-petclinic-angularjs.git
cd spring-petclinic-angularjs
./mvnw clean install
cd spring-petclinic-server
../mvnw spring-boot:run
```

You can then access petclinic here: http://localhost:8080/

<img width="782" alt="spring-petclinic" src="https://cloud.githubusercontent.com/assets/838318/19653851/61c1986a-9a16-11e6-8b94-03fd7f775bb3.png">

## In case you find a bug/suggested improvement for Spring Petclinic
Our issue tracker is available here: https://github.com/spring-petclinic/spring-petclinic-angularjs/issues

## Database configuration

In its default configuration, Petclinic uses an in-memory database (HSQLDB) which gets populated at startup with data.
A similar setups is provided for MySql in case a persistent database configuration is needed.
To run petclinic locally using MySQL database, it is needed to change profile defined in the  application.properties` file.

For MySQL database, it is needed to switch profile. There is two ways:

1. Update application properties: open the `application.properties` file, then change the value `hsqldb` to `mysql`
2. Use a Spring Boot JVM parameter: simply start the JVM with the `-Dspring.profiles.active=mysql.prod` parameter.


Before do this, it would be good to change JDBC url properties defined in the `application-mysql.properties` file:

```
spring.datasource.url = jdbc:mysql://localhost:3306/petclinic?useUnicode=true
spring.datasource.username=root
spring.datasource.password=petclinic 
```      

The `localhost` host should be set for a MySQL dabase instance started on your local machine.
You may also start a MySql database with docker:

```
docker run --name mysql-petclinic -e MYSQL_ROOT_PASSWORD=petclinic -e MYSQL_DATABASE=petclinic -p 3306:3306 mysql:5.7
```


## Docker

### Run an image

To run a Docker image of Petclinic with its embedded HSQL database, you may 

```
docker run -p 8080:8080 -t --name springboot-petclinic arey/springboot-petclinic
```

If you want to use MySQL, you first have to change the `spring.datasource.url` declared in the `application-mysql.properties` file.
You have to rebuild the image (see next section).
Then you could activated the `mysql` profile:

```
docker run -e "SPRING_PROFILES_ACTIVE=mysql,prod" -p 8080:8080 -t --name springboot-petclinic arey/springboot-petclinic
```

### Use Docker Compose

The simplest way is to use docker-compose

```
docker-compose up
```

### Build an image

To rebuild a Docker image on your device:
```
./mvnw clean install
cd spring-petclinic-server
mvn clean package docker:build
```

To publish a new image into Docker Hub:
```
mvn clean package docker:build -DpushImageTag
```

## Working with Petclinic in Eclipse/STS

### prerequisites
The following items should be installed in your system:
* Maven 3 (http://www.sonatype.com/books/mvnref-book/reference/installation.html)
* git command line tool (https://help.github.com/articles/set-up-git)
* Eclipse with the m2e plugin (m2e is installed by default when using the STS (http://www.springsource.org/sts) distribution of Eclipse)

Note: when m2e is available, there is an m2 icon in Help -> About dialog.
If m2e is not there, just follow the install process here: http://eclipse.org/m2e/download/


### Steps:

1) In the command line
```
git clone https://github.com/spring-projects/spring-petclinic.git
```
2) Inside Eclipse
```
File -> Import -> Maven -> Existing Maven project
```

### Active the dev Spring profile

In development mode, we recommand you yo use the ```dev``` Spring profile.
Just add the following VM option:
```
-Dspring.profiles.active=dev
```
All static resources changes will be monitored by the embedded LiveReload server of Spring Boot Devtools.
See [application-dev.properties](spring-petclinic-server/src/main/resources/application-dev.properties) for details.

## Client-side Architecture

Compared to the [standard Petclinic based on JSP pages](https://github.com/spring-projects/spring-petclinic), 
this SpringBoot AngularJS Petclinic is splitted in 2 modules - a client module and a server module:
* spring-petclinic-client : static resources (images, fonts, style, angular JS code) packaged as a webjar.
* spring-petclinic-server : Spring MVC REST API and an index.html template


## Looking for something in particular?

| Spring Boot Configuration     | Files |
|-------------------------------|-------|
| The Main Class                | [PetClinicApplication.java](spring-petclinic-server/src/main/java/org/springframework/samples/petclinic/PetClinicApplication.java)  |
| Common properties file        | [application.properties](spring-petclinic-server/src/main/resources/application.properties)  |
| Development properties file   | [application-dev.properties](spring-petclinic-server/src/main/resources/application-dev.properties)  |
| Production properties file    | [application-prod.properties](spring-petclinic-server/src/main/resources/application-prod.properties)  |
| Caching: Cache with EhCache   | [CacheConfig.java](spring-petclinic-server/src/main/java/org/springframework/samples/petclinic/config/CacheConfig.java) |
| Homepage                      | Map root context to the index.html template: [WebConfig.java](spring-petclinic-server/src/main/java/org/springframework/samples/petclinic/config/WebConfig.java) |


| Front-end module  | Files |
|-------------------|-------|
| Node and NPM      | [The frontend-maven-plugin plugin downloads/installs Node and NPM locally then runs Bower and Gulp](spring-petclinic-client/pom.xml)  |
| Bower             | [JavaScript libraries are defined by the manifest file bower.json](spring-petclinic-client/bower.json)  |
| Gulp              | [Tasks automated by Gulp: minify CSS and JS, generate CSS from LESS, copy other static resources](spring-petclinic-client/gulpfile.js)  |
| Angular JS        | [app.js, controllers and templates](spring-petclinic-client/src/scripts/)  |


## Interesting Spring Petclinic forks

The Spring Petclinic master branch in the main [spring-projects](https://github.com/spring-projects/spring-petclinic)
GitHub org is the "canonical" implementation, currently based on Spring Boot and Thymeleaf.

This [spring-petclinic-angularjs][] project is one of the [several forks](https://spring-petclinic.github.io/docs/forks.html) 
hosted in a special GitHub org: [spring-petclinic](https://github.com/spring-petclinic).
If you have a special interest in a different technology stack
that could be used to implement the Pet Clinic then please join the community there.

## UI Testing with Playwright

This project includes a comprehensive UI test suite built with Playwright that validates the functionality of the Spring PetClinic AngularJS application. The tests cover all major features including owner management, pet registration, veterinarian directory, visit management, and cross-cutting concerns like navigation and form validation.

### Prerequisites

Before running UI tests, ensure you have the following installed:

- **Node.js** (v14 or later)
- **npm** (comes with Node.js) 
- **Maven** (for running the Spring Boot server)
- **Java 8 or later** (for the Spring Boot application)

### Quick Start

1. **Navigate to the UI tests directory:**
   ```bash
   cd spring-petclinic-ui-tests
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Run all tests:**
   ```bash
   npx playwright test
   ```
   This will automatically start the Spring Boot server, run all tests, and stop the server.

4. **View test results:**
   ```bash
   npx playwright show-report
   ```

### Test Structure

The UI tests are organized into the following test suites:

#### Core Application Tests
- **`app-loading.spec.ts`** - Basic application loading and navigation validation (4 tests)
- **`welcome.spec.ts`** - Home page functionality and navigation (5 tests)

### Feature-Specific Tests  
- **`owner-management.spec.ts`** - Owner CRUD operations, search, and validation (7 tests)
- **`pet-management.spec.ts`** - Pet registration, editing, and association with owners (6 tests)
- **`veterinarian.spec.ts`** - Veterinarian directory and information display (4 tests)
- **`visit-management.spec.ts`** - Pet visit records and medical history (5 tests)

#### Cross-Cutting Concern Tests
- **`navigation.spec.ts`** - Navigation consistency, form validation, and error handling (12 tests)

### Running Specific Tests

#### Run a specific test file:
```bash
npx playwright test tests/app-loading.spec.ts
```

#### Run tests matching a pattern:
```bash
npx playwright test -g "should display welcome message"
```

#### Run tests in headed mode (visible browser):
```bash
npx playwright test --headed
```

#### Run tests in debug mode:
```bash
npx playwright test --debug
```

### Test Configuration

The test configuration is managed in `playwright.config.ts`:

- **Browser**: Chromium only (as specified in requirements)
- **Base URL**: http://localhost:8080
- **Timeout**: 30 seconds per test
- **Retries**: 1 retry on failure
- **Parallel execution**: 4 workers
- **Automatic server management**: Tests start/stop the Spring Boot server automatically

### Server Management

The test suite includes automatic server lifecycle management:

- **`utils/start-server.js`** - Starts the Spring Boot server with the correct profile
- **`utils/stop-server.js`** - Gracefully stops the server after tests complete
- **Global setup/teardown** - Ensures the server is running before tests and cleaned up after

The server is configured to:
- Use the HSQLDB profile (default, in-memory database)
- Run on port 8080
- Wait for full startup before beginning tests
- Provide proper logging for debugging

### Page Object Model

Tests use the Page Object Model pattern with reusable page components:

- **`BasePage`** - Common navigation and utility methods
- **`WelcomePage`** - Home page interactions
- **`OwnerListPage`** - Owner list and search functionality  
- **`OwnerFormPage`** - Owner creation and editing forms
- **`OwnerDetailsPage`** - Owner details and pet management
- **`PetFormPage`** - Pet registration and editing
- **`VetListPage`** - Veterinarian directory
- **`VisitsPage`** - Visit management and medical records

### Test Data Management

Tests use the following approach for test data:

- **Read-only tests** use existing data from the HSQLDB seed data
- **Create/Update tests** generate unique test data to avoid conflicts
- **Cleanup** is handled automatically by using in-memory database that resets between test runs

### Debugging Test Failures

When tests fail, Playwright automatically generates:

1. **Screenshots** - Visual state at the time of failure
2. **Videos** - Recording of the test execution  
3. **Trace files** - Detailed execution timeline
4. **Error context** - Relevant DOM state and console logs

These artifacts are stored in the `test-results/` directory.

#### Common debugging commands:

```bash
# Run a single test with full logging
npx playwright test tests/app-loading.spec.ts --headed --timeout=60000

# Generate and view test report
npx playwright show-report

# Run tests with additional debugging
npx playwright test --debug tests/welcome.spec.ts
```

### Known Issues

1. **Server startup time** - The Maven server can take 30-60 seconds to start, which is factored into the global setup timeout
2. **URL pattern matching** - Some tests may need adjustment for specific AngularJS routing patterns
3. **Timing sensitivity** - AngularJS state transitions may require additional wait conditions for complex interactions

### Integration with CI/CD

The test suite is designed to run in CI/CD environments:

- All dependencies are managed through npm
- Server startup is automated
- Tests run in headless mode by default  
- Test results are available in standard formats
- Exit codes properly indicate success/failure

### Contributing to Tests

When adding new tests:

1. Follow the existing Page Object Model structure
2. Use descriptive test names and organize by feature
3. Include proper assertions and error messages
4. Add appropriate waits for AngularJS state changes
5. Ensure tests are independent and can run in any order

For more information about the test implementation, see the test files and page object classes in the `spring-petclinic-ui-tests/` directory.

# Contributing

The [issue tracker](https://github.com/spring-petclinic/spring-petclinic-angularjs/issues) is the preferred channel for bug reports, features requests and submitting pull requests.

For pull requests, editor preferences are available in the [editor config](https://github.com/spring-projects/spring-petclinic/blob/master/.editorconfig) for easy use in common text editors. Read more and download plugins at <http://editorconfig.org>.

