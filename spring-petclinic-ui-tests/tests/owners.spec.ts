import { test, expect } from '@playwright/test';

/**
 * Tests for Owner management functionality
 */
test.describe('Owner Management', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/');
    await page.click('text=Find owners');
    await page.waitForLoadState('networkidle');
  });

  test('should display list of owners', async ({ page }) => {
    // Click on Find Owner button without entering a name (shows all)
    await page.click('button:has-text("Find Owner")');
    await page.waitForLoadState('networkidle');
    
    // Should display owners table
    await expect(page.locator('table')).toBeVisible();
    
    // Should have column headers
    await expect(page.locator('th:has-text("Name")')).toBeVisible();
    await expect(page.locator('th:has-text("Address")')).toBeVisible();
    await expect(page.locator('th:has-text("City")')).toBeVisible();
    await expect(page.locator('th:has-text("Telephone")')).toBeVisible();
    await expect(page.locator('th:has-text("Pets")')).toBeVisible();
  });

  test('should search for owner by last name', async ({ page }) => {
    // Enter last name
    await page.fill('input[name="lastName"]', 'Franklin');
    await page.click('button:has-text("Find Owner")');
    await page.waitForLoadState('networkidle');
    
    // Should display matching owner(s)
    await expect(page.locator('text=Franklin')).toBeVisible();
  });

  test('should view owner details', async ({ page }) => {
    // Find all owners
    await page.click('button:has-text("Find Owner")');
    await page.waitForLoadState('networkidle');
    
    // Click on first owner
    await page.click('table tr:nth-child(1) a');
    await page.waitForLoadState('networkidle');
    
    // Should display owner information
    await expect(page.locator('text=Owner Information')).toBeVisible();
    await expect(page.locator('text=Name')).toBeVisible();
    await expect(page.locator('text=Address')).toBeVisible();
    await expect(page.locator('text=City')).toBeVisible();
    await expect(page.locator('text=Telephone')).toBeVisible();
    
    // Should display pets and visits section
    await expect(page.locator('text=Pets and Visits')).toBeVisible();
  });

  test('should add new owner', async ({ page }) => {
    // Click Add Owner button
    await page.click('text=Add Owner');
    await page.waitForLoadState('networkidle');
    
    // Fill in owner details
    await page.fill('input[name="firstName"]', 'Test');
    await page.fill('input[name="lastName"]', 'Owner');
    await page.fill('input[name="address"]', '123 Test Street');
    await page.fill('input[name="city"]', 'Test City');
    await page.fill('input[name="telephone"]', '1234567890');
    
    // Submit form
    await page.click('button:has-text("Submit")');
    await page.waitForLoadState('networkidle');
    
    // Should redirect to owner details page
    await expect(page.locator('text=Owner Information')).toBeVisible();
    await expect(page.locator('text=Test Owner')).toBeVisible();
  });

  test('should edit owner information', async ({ page }) => {
    // Find all owners
    await page.click('button:has-text("Find Owner")');
    await page.waitForLoadState('networkidle');
    
    // Click on first owner
    await page.click('table tr:nth-child(1) a');
    await page.waitForLoadState('networkidle');
    
    // Click Edit Owner button
    await page.click('text=Edit Owner');
    await page.waitForLoadState('networkidle');
    
    // Modify owner details
    await page.fill('input[name="city"]', 'Updated City');
    
    // Submit form
    await page.click('button:has-text("Update Owner")');
    await page.waitForLoadState('networkidle');
    
    // Should display updated information
    await expect(page.locator('text=Updated City')).toBeVisible();
  });

  test('should show validation errors for invalid input', async ({ page }) => {
    // Click Add Owner button
    await page.click('text=Add Owner');
    await page.waitForLoadState('networkidle');
    
    // Try to submit empty form
    await page.click('button:has-text("Submit")');
    
    // Should display validation errors
    await expect(page.locator('.has-error, .error')).toHaveCount({ min: 1 });
  });
});
