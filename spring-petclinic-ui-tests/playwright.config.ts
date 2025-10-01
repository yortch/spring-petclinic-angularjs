import { defineConfig, devices } from '@playwright/test';

/**
 * @see https://playwright.dev/docs/test-configuration
 */
export default defineConfig({
  testDir: './tests',
  /* Run tests in files in parallel */
  fullyParallel: true,
  /* Fail the build on CI if you accidentally left test.only in the source code. */
  forbidOnly: !!process.env.CI,
  /* Retry on CI only */
  retries: process.env.CI ? 2 : 0,
  /* Opt out of parallel tests on CI. */
  workers: process.env.CI ? 1 : undefined,
  /* Reporter to use. See https://playwright.dev/docs/test-reporters */
  reporter: [
    ['html'],
    ['json', { outputFile: 'test-results/results.json' }],
    ['list']
  ],
  /* Shared settings for all the projects below. See https://playwright.dev/docs/api/class-testoptions. */
  use: {
    /* Base URL to use in actions like `await page.goto('/')`. */
    baseURL: 'http://localhost:8080',

    /* Collect trace when retrying the failed test. See https://playwright.dev/docs/trace-viewer */
    trace: 'on-first-retry',
    
    /* Take screenshot on failure */
    screenshot: 'only-on-failure',
    
    /* Record video on failure */
    video: 'retain-on-failure',
    
    /* Maximum time for each action */
    actionTimeout: 10000,
    
    /* Maximum time for navigation */
    navigationTimeout: 30000,
  },

  /* Configure projects for major browsers */
  projects: [
    {
      name: 'chromium',
      use: { 
        ...devices['Desktop Chrome'],
        // Ensure consistent viewport for tests
        viewport: { width: 1280, height: 720 },
        // Disable web security for local testing
        launchOptions: {
          args: ['--disable-web-security', '--disable-features=VizDisplayCompositor']
        }
      },
    }
  ],

  /* Global setup and teardown */
  globalSetup: require.resolve('./utils/global-setup.ts'),
  globalTeardown: require.resolve('./utils/global-teardown.ts'),

  /* Configure test timeouts */
  timeout: 30000,
  expect: {
    timeout: 10000
  },

  /* Output directory for test results */
  outputDir: 'test-results/',
  
  /* Configure folders */
  testIgnore: ['**/node_modules/**'],
});