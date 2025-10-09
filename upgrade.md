# Full Spring Pet Clinic App Modernization guide for GitHub Copilot

This file includes the script that can be used to modernize a legacy Java 8 and Spring Boot 2.4
application into Java 21 and Spring Boot 3.5 in the backend and to modernize the UI from AngularJS
into a modern Angular version while increasing junit test coverage and adding Playwright UI tests using a phased approach:

1. Phase 0: Setup and validation
1. Phase 1: Generate PRD (Product Requirements Document)
1. Phase 2: Generate Java Unit Tests
1. Phase 3: Generate UI tests using Playwright
1. Phase 4: Java and Spring Boot upgrade
1. Phase 5: AngularJS to Angular UI upgrade

## Phase 0: setup and validation

### Phase 0a: Generate Instructions File

#### Instructions File Prompt

Generate GitHub Copilot instructions file for this project

### Phase 0b: validate application runs

#### Explain Prompt

Explain this application and show me how to run and compile and run Pet clinic application to confirm that it is functional

## Phase 1: Generate PRD (Product Requirements Document)

### Phase 1a: Create new PRD

Prompt file was downloaded from [https://github.com/github/awesome-copilot/blob/main/prompts/breakdown-feature-prd.prompt.md](https://github.com/github/awesome-copilot/blob/main/prompts/breakdown-feature-prd.prompt.md). It was simplified and added here: [.github/prompts/generate-prd.prompt.md](.github/prompts/generate-prd.prompt.md)

#### PRD Prompt

/generate-prd generate PRD for Pet Clinic application based on existing code and available documentation

## Phase 2: Generate Java unit tests

### Phase 2a: Measure existing test coverage

#### Test Coverage Prompt

Use JaCoCo to help me measure the existing test coverage for the Java spring-petclinic-server project

### Phase 2b: Generate Junit test

Prompt file was downloaded from: [https://github.com/github/awesome-copilot/blob/main/prompts/java-junit.prompt.md](https://github.com/github/awesome-copilot/blob/main/prompts/java-junit.prompt.md?plain=1). Due to compatibility reasons, replaced 5 with 4 and saved here: [.github/prompts/junit-4-generate-tests.prompt.md](.github/prompts/junit-4-generate-tests.prompt.md)

#### JUnit Prompt

/junit-4-generate-tests add java unit tests for spring-petclinic-server Java project focusing on validating functional requirements from #file:prd.md and based on existing functionality implemented. Striving to increase java unit test coverage to at least 80%, and average of 50% coverage per package. Come up with a plan to implement unit tests, break it down into a task list and update the task list as unit tests are implemented

### Phase 3: Generate UI Tests using Playwright

#### Playwright Prompt

In preparation to upgrading Java and Spring Boot versions, I need to create UI tests to ensure UI is still functional after each incremental upgrade. Review implemented code in spring-petclinic-client and functional requirements from the #file:prd.md to implement UI tests and  that validate the UI is functioning correctly using Playwright. Create a plan for implementing such UI tests with the following criteria and break it into an implementation checklist, but don't implement it yet:

* Focus only on Desktop browser (chromium) tests
* Focus on UI functionality tests, do not implement UI performance tests
* Implement playwright tests using Typescript within the spring-petclinic-ui-tests directory
* Crete and use a local script to build and run local dev server before starting the tests using mvnw commands from #file:README.md, ensure default spring profile is used when testing
* Before implementing all tests, create a basic test to validate that the application is loading
* Update .gitignore file to add node_modules directory
* Iterate until all tests are passing
* Update #file:README.md with instructions on how to run playwright tests
* Update the checklist as you are progressing through implementing it

### Phase 4: Java and Spring Boot upgrade

#### Java Upgrade Prompt

#generate_upgrade_plan_for_java_project for spring-petclinic-server project into Java 21 and latest stable SpringBoot 3.5.x available using jdk 8 location: C:\Users\jbalderas\apps\jdk8u442-b06 and jdk21 location: C:\Program Files\Eclipse Adoptium\jdk-21.0.2.13-hotspot.
After upgrade is complete perform the following tasks:

* Update mvn wrapper version to a compatible version after upgrade and confirm that project can still be built using mvnw command from readme.md file
* Rerun Playwright tests to confirm they're still passing after upgrade

## Phase 5: AngularJS to Angular UI upgrade

### Angular Upgrade Prompt

Please upgrade spring-petclinic-client to Angular 20 upgrading any dependencies to ensure compatibility and avoiding known vulnerabilities. Create a plan for implementing the upgrade with the following criteria and break it into an implementation checklist, but don't implement it yet:

* Ensure the upgraded spring-petclinic-angular project can still be built using pom.xml and maven wrapper commands from readme.md file
* Ensure that the spring boot application can find the UI files from the upgraded angular application
* Ensure the images are loading in the upgraded angular app UI
* Keep the current green them colors in banner and buttons in the upgraded UI application
* Update playwright UI tests as needed and ensure they are passing
* Update #file:README.md to reflect changes after upgrade.
* Update the checklist as you are progressing through implementing it.
