import { defineConfig, devices } from '@playwright/test';

/**
 * Playwright configuration for Spring PetClinic UI tests
 * @see https://playwright.dev/docs/test-configuration
 */
export default defineConfig({
  testDir: './tests',
  // Maximum time one test can run
  timeout: 30 * 1000,
  expect: {
    // Maximum time expect() should wait
    timeout: 5000
  },
  // Run tests in files in parallel
  fullyParallel: true,
  // Fail the build on CI if you accidentally left test.only in the source code
  forbidOnly: !!process.env.CI,
  // Retry on CI only
  retries: process.env.CI ? 2 : 0,
  // Opt out of parallel tests on CI
  workers: process.env.CI ? 1 : undefined,
  // Reporter to use
  reporter: 'html',
  
  // Shared settings for all projects
  use: {
    // Base URL for navigation
    baseURL: 'http://localhost:8080',
    // Collect trace when retrying the failed test
    trace: 'on-first-retry',
    // Screenshot on failure
    screenshot: 'only-on-failure',
  },

  // Configure projects for chromium only (as per requirements)
  projects: [
    {
      name: 'chromium',
      use: { ...devices['Desktop Chrome'] },
    },
  ],

  // Run local dev server before starting tests
  webServer: {
    command: 'npm run start:server',
    url: 'http://localhost:8080',
    reuseExistingServer: !process.env.CI,
    timeout: 120 * 1000,
  },
});
