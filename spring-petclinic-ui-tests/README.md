# Spring PetClinic UI Tests

This module contains end-to-end UI tests for the Spring PetClinic application using Microsoft Playwright.

## Overview

The UI tests validate the functionality defined in the Product Requirements Document (PRD.md) and ensure that:

- The application loads correctly and displays the welcome page
- Users can navigate between different sections (Owners, Veterinarians)
- Owner management features work as expected
- Veterinarian directory displays correctly
- The application is responsive across different screen sizes
- Performance meets the requirements (page load < 3 seconds)

## Test Structure

### Page Object Model
- `BaseUITest.java` - Base class with common test setup and utilities
- `pages/HomePage.java` - Home page interactions
- `pages/OwnersPage.java` - Owner management page interactions  
- `pages/VeterinariansPage.java` - Veterinarians page interactions

### Test Classes
- `HomePageTest.java` - Home page functionality and navigation
- `OwnerManagementTest.java` - Owner management features
- `VeterinarianManagementTest.java` - Veterinarian directory features

## Running the Tests

### Prerequisites
- Java 8 or higher
- Maven 3.6+
- Spring PetClinic application running on http://localhost:8080

### Running All UI Tests
```bash
# From the project root
mvn test -pl spring-petclinic-ui-tests

# Or from the ui-tests directory
cd spring-petclinic-ui-tests
mvn test
```

### Running Specific Test Classes
```bash
mvn test -pl spring-petclinic-ui-tests -Dtest=HomePageTest
mvn test -pl spring-petclinic-ui-tests -Dtest=OwnerManagementTest
mvn test -pl spring-petclinic-ui-tests -Dtest=VeterinarianManagementTest
```

### Running with Custom Application URL
```bash
mvn test -pl spring-petclinic-ui-tests -Dapp.url=http://localhost:9090
```

### Running in Headed Mode (for debugging)
Modify the `BaseUITest.java` file and set `setHeadless(false)` to see the browser during test execution.

## Test Configuration

### Browser Configuration
- Default: Chromium headless
- Viewport: 1280x720
- Screenshots saved to: `target/screenshots/`

### Test Data
Tests use the default sample data provided by the application, including:
- Sample owners (George Franklin, Betty Davis, etc.)
- Sample veterinarians (James Carter, Helen Leary, Linda Douglas, etc.)
- Various pet types and specialties

## Troubleshooting

### Application Not Running
Ensure the Spring PetClinic application is running before executing tests:
```bash
cd spring-petclinic-server
../mvnw spring-boot:run
```

### Browser Installation
If Playwright browsers are not installed, they will be automatically downloaded during the test compilation phase.

### Test Failures
- Check if the application is accessible at the configured URL
- Verify test data is present (run with sample data)
- Check browser console logs for JavaScript errors
- Use headed mode for visual debugging

## Performance Testing

The tests include performance validation:
- Page load times must be under 3 seconds (PRD requirement)
- Responsive design testing on mobile viewport (375x667)

## Future Enhancements

Potential additions for comprehensive testing:
- Owner creation and editing workflows
- Pet management functionality  
- Visit scheduling and recording
- Form validation testing
- Cross-browser testing (Firefox, Safari)
- API response validation
- Accessibility testing