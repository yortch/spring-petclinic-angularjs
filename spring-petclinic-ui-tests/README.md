# Spring PetClinic UI Tests

This directory contains Playwright-based end-to-end UI tests for the Spring PetClinic application.

## Prerequisites

- Node.js (v14 or higher)
- npm
- Spring PetClinic application built and running

## Installation

```bash
# Install dependencies
npm install

# Install Playwright browsers (Chromium only)
npx playwright install chromium
```

## Running Tests

### Option 1: Run tests with automatic server startup

The test configuration will automatically start the Spring PetClinic server before running tests:

```bash
npm test
```

### Option 2: Run tests with server already running

If you have the server running separately:

```bash
# In terminal 1: Start the server
cd ..
./mvnw spring-boot:run -pl spring-petclinic-server

# In terminal 2: Run tests
npm test
```

### Additional Test Commands

```bash
# Run tests in headed mode (see browser)
npm run test:headed

# Run tests in debug mode
npm run test:debug

# Run tests with UI mode (interactive)
npm run test:ui

# View test report
npm run test:report
```

## Test Structure

```
spring-petclinic-ui-tests/
├── tests/                    # Test specifications
│   ├── smoke.spec.ts        # Basic smoke tests
│   ├── owners.spec.ts       # Owner management tests
│   ├── pets.spec.ts         # Pet management tests (to be added)
│   ├── visits.spec.ts       # Visit management tests (to be added)
│   └── vets.spec.ts         # Veterinarian tests (to be added)
├── scripts/                 # Helper scripts
│   └── start-server.sh      # Script to build and start server
├── playwright.config.ts     # Playwright configuration
├── tsconfig.json           # TypeScript configuration
└── package.json            # Dependencies and scripts
```

## Test Scenarios

### Smoke Tests (`smoke.spec.ts`)
- ✓ Application loads successfully
- ✓ Navigation menu is visible and functional
- ✓ All main pages are accessible

### Owner Management (`owners.spec.ts`)
- ✓ List all owners
- ✓ Search owners by last name
- ✓ View owner details
- ✓ Add new owner
- ✓ Edit owner information
- ✓ Form validation errors

### Pet Management (to be implemented)
- Add pet to owner
- Edit pet information
- View pet details and visit history
- Pet type selection

### Visit Management (to be implemented)
- Add visit to pet
- View visit history
- Date validation

### Veterinarian Tests (to be implemented)
- View list of veterinarians
- Display veterinarian specialties

## Configuration

The Playwright configuration (`playwright.config.ts`) is set up for:

- **Browser**: Chromium only (desktop)
- **Base URL**: http://localhost:8080
- **Timeout**: 30 seconds per test
- **Retries**: 2 retries on CI, 0 locally
- **Screenshots**: On failure
- **Trace**: On first retry

## Continuous Integration

Tests are configured to run in CI with:
- Sequential execution (`workers: 1`)
- Automatic server startup
- Retry on failure
- HTML report generation

## Debugging

### Debug a specific test
```bash
npx playwright test --debug tests/smoke.spec.ts
```

### View trace for failed test
```bash
npx playwright show-trace test-results/.../trace.zip
```

### Generate and view report
```bash
npx playwright show-report
```

## Best Practices

1. **Selectors**: Use data-testid attributes when available, otherwise use role-based or text-based selectors
2. **Waits**: Use `waitForLoadState('networkidle')` after navigation
3. **Assertions**: Use Playwright's built-in assertions with auto-retry
4. **Page Objects**: Consider extracting common page interactions into reusable functions
5. **Test Data**: Tests should be independent and not rely on specific data state

## Troubleshooting

### Server doesn't start
- Check if port 8080 is already in use
- Ensure the application builds successfully: `./mvnw clean install`
- Check logs in `spring-petclinic-ui-tests/.server.pid`

### Tests fail with timeout
- Increase timeout in `playwright.config.ts`
- Check if server is responding: `curl http://localhost:8080`
- Run tests in headed mode to see what's happening

### Browser download fails
- Try installing with dependencies: `npx playwright install --with-deps chromium`
- Check network connectivity
- Use system-installed browser as fallback

## License

Same as Spring PetClinic project - Apache License 2.0
