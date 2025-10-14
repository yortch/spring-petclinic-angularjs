# Angular 20 and Spring Boot version of the Spring PetClinic Sample Application

> **Modernized Version**: This application has been fully upgraded to Angular 20.3.4, Java 21, and Spring Boot 3.5.0. See [MIGRATION.md](MIGRATION.md) for details.

## Understanding the Spring Petclinic application with a few diagrams
[See the presentation here](http://fr.slideshare.net/AntoineRey/spring-framework-petclinic-sample-application)

## Technology Stack

### Backend
- **Java 21** (latest LTS)
- **Spring Boot 3.5.0** (latest stable)
- **Jakarta EE 10** (jakarta.* namespace)
- **Maven 3.9.9** (wrapper included)
- **JUnit 5** for testing
- **JaCoCo** for code coverage (62%)

### Frontend
- **Angular 20.3.4** (latest stable)
- **TypeScript 5.7+** for type safety
- **Bootstrap 5.3.3** for responsive UI
- **Angular CLI** for build and development
- **Standalone Components** (no NgModules)
- **Reactive Forms** for form handling

### Testing
- **JUnit 5** for Java unit tests
- **Playwright** for end-to-end UI tests

## Running petclinic locally

### Prerequisites
- **Java 21** or higher
- **Maven 3.9+** (or use the included Maven wrapper)
- **Node.js 20.x** (for local Angular development only, not required for Maven build)

### Quick Start
```bash
git clone https://github.com/spring-petclinic/spring-petclinic-angularjs.git
cd spring-petclinic-angularjs

# Build entire application (includes Angular compilation)
./mvnw clean install

# Run the application
./mvnw spring-boot:run -pl spring-petclinic-server
```

You can then access petclinic here: http://localhost:8080/

### Development Mode

For frontend development with live reload:

```bash
# Terminal 1: Run Spring Boot backend
./mvnw spring-boot:run -pl spring-petclinic-server

# Terminal 2: Run Angular dev server (optional)
cd spring-petclinic-client
npm install
npm run start
# Access at http://localhost:4200 (proxies to backend at :8080)
```

<img width="1200" alt="spring-petclinic-angular20" src="https://github.com/user-attachments/assets/1b47c69a-1978-4a71-ad10-680073263734">

## Testing

### Running Java Unit Tests
```bash
# Run all tests with coverage
./mvnw test

# View coverage report
open spring-petclinic-server/target/site/jacoco/index.html
```

**Current Coverage**: 62%
- Service layer: 100%
- Model layer: 91%
- Web layer: 45%

### Running Playwright UI Tests
```bash
cd spring-petclinic-ui-tests
npm install
npx playwright install chromium

# Run tests
npm test

# Run tests in headed mode
npm run test:headed

# Open Playwright UI for debugging
npm run test:ui
```

The Playwright tests automatically build and start the Spring Boot application before running.

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

## Working with Petclinic in your IDE

### Prerequisites
- **Java 21 JDK** installed
- **Maven 3.9+** (or use included wrapper)
- **Node.js 20.x** (for Angular development)
- **Your favorite IDE** (IntelliJ IDEA, Eclipse, VS Code, etc.)

### Import into IntelliJ IDEA
1. File → Open → Select `pom.xml` in the project root
2. Choose "Open as Project"
3. Wait for Maven to download dependencies
4. For Angular development: Open `spring-petclinic-client` folder in terminal and run `npm install`

### Import into VS Code
1. File → Open Folder → Select project root directory
2. Install recommended extensions:
   - Extension Pack for Java
   - Angular Language Service
   - Playwright Test for VSCode
3. Open terminal and run `./mvnw compile` to download dependencies
4. For Angular: `cd spring-petclinic-client && npm install`

### Activate the dev Spring profile

In development mode, use the `dev` Spring profile for hot-reload of static resources.

Add the following VM option:
```
-Dspring.profiles.active=dev
```

Or set as environment variable:
```bash
export SPRING_PROFILES_ACTIVE=dev
```

See [application-dev.properties](spring-petclinic-server/src/main/resources/application-dev.properties) for details.

## Client-side Architecture

This Spring Boot + Angular PetClinic is split into 2 modules:
* **spring-petclinic-client**: Angular 20 application with TypeScript, built with Angular CLI
* **spring-petclinic-server**: Spring Boot REST API that serves the compiled Angular app

The Angular client is built using Maven (via frontend-maven-plugin) and packaged into the Spring Boot JAR.

## Project Structure

```
spring-petclinic-angularjs/
├── spring-petclinic-client/          # Angular 20 frontend
│   ├── src/
│   │   ├── app/
│   │   │   ├── components/          # Angular standalone components
│   │   │   ├── services/            # REST API services
│   │   │   ├── models/              # TypeScript interfaces
│   │   │   └── interceptors/        # HTTP interceptors
│   │   ├── assets/                  # Images, fonts
│   │   └── styles.scss              # Global styles
│   ├── angular.json                 # Angular CLI config
│   ├── package.json                 # NPM dependencies
│   └── pom.xml                      # Maven build config
│
├── spring-petclinic-server/         # Spring Boot backend
│   ├── src/main/java/              # Java source code
│   ├── src/main/resources/         # Application properties
│   └── pom.xml                     # Maven config
│
└── spring-petclinic-ui-tests/      # Playwright E2E tests
    ├── tests/                       # Test specifications
    └── playwright.config.ts         # Playwright config
```

## Looking for something in particular?

| Spring Boot Configuration     | Files |
|-------------------------------|-------|
| The Main Class                | [PetClinicApplication.java](spring-petclinic-server/src/main/java/org/springframework/samples/petclinic/PetClinicApplication.java)  |
| Common properties file        | [application.properties](spring-petclinic-server/src/main/resources/application.properties)  |
| Development properties file   | [application-dev.properties](spring-petclinic-server/src/main/resources/application-dev.properties)  |
| Production properties file    | [application-prod.properties](spring-petclinic-server/src/main/resources/application-prod.properties)  |
| Caching: Cache with EhCache   | [CacheConfig.java](spring-petclinic-server/src/main/java/org/springframework/samples/petclinic/config/CacheConfig.java) |
| Angular Routing Support       | [AngularForwardingController.java](spring-petclinic-server/src/main/java/org/springframework/samples/petclinic/config/AngularForwardingController.java) |
| Homepage Template             | [WebConfig.java](spring-petclinic-server/src/main/java/org/springframework/samples/petclinic/config/WebConfig.java) |

| Angular Application  | Files |
|-------------------|-------|
| Angular CLI Config    | [angular.json](spring-petclinic-client/angular.json)  |
| NPM Dependencies      | [package.json](spring-petclinic-client/package.json)  |
| TypeScript Config     | [tsconfig.json](spring-petclinic-client/tsconfig.json)  |
| App Component         | [app.ts](spring-petclinic-client/src/app/app.ts)  |
| Routing Configuration | [app.routes.ts](spring-petclinic-client/src/app/app.routes.ts)  |
| Services              | [clinic.service.ts](spring-petclinic-client/src/app/services/clinic.service.ts)  |
| Components            | [src/app/components/](spring-petclinic-client/src/app/components/)  |
| Global Styles         | [styles.scss](spring-petclinic-client/src/app/styles.scss)  |

| Testing  | Files |
|----------|-------|
| Unit Tests (Java)     | [src/test/java/](spring-petclinic-server/src/test/java/)  |
| E2E Tests (Playwright) | [spring-petclinic-ui-tests/tests/](spring-petclinic-ui-tests/tests/)  |
| JaCoCo Reports        | `spring-petclinic-server/target/site/jacoco/` (after build)  |


## Interesting Spring Petclinic forks

The Spring Petclinic master branch in the main [spring-projects](https://github.com/spring-projects/spring-petclinic)
GitHub org is the "canonical" implementation, currently based on Spring Boot and Thymeleaf.

This [spring-petclinic-angularjs][] project is one of the [several forks](https://spring-petclinic.github.io/docs/forks.html) 
hosted in a special GitHub org: [spring-petclinic](https://github.com/spring-petclinic).
If you have a special interest in a different technology stack
that could be used to implement the Pet Clinic then please join the community there.

# Contributing

The [issue tracker](https://github.com/spring-petclinic/spring-petclinic-angularjs/issues) is the preferred channel for bug reports, features requests and submitting pull requests.

For pull requests, editor preferences are available in the [editor config](https://github.com/spring-projects/spring-petclinic/blob/master/.editorconfig) for easy use in common text editors. Read more and download plugins at <http://editorconfig.org>.

