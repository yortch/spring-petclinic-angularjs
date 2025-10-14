import { test, expect } from '@playwright/test';

/**
 * Tests for Owner management functionality in Angular 20
 */
test.describe('Owner Management (Angular 20)', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/');
    await page.click('a.nav-link:has-text("Find owners")');
    await page.waitForURL('**/owners');
  });

  test('should display list of owners with search filter', async ({ page }) => {
    // Angular shows owners list immediately
    // Should display owners table
    await expect(page.locator('table.table-striped')).toBeVisible();
    
    // Should have column headers
    await expect(page.locator('th:has-text("Name")')).toBeVisible();
    await expect(page.locator('th:has-text("City")')).toBeVisible();
    await expect(page.locator('th:has-text("Telephone")')).toBeVisible();
    
    // Should have search input
    await expect(page.locator('input[placeholder*="Search"]')).toBeVisible();
    
    // Should have Add Owner button
    await expect(page.locator('a.btn:has-text("Add Owner")')).toBeVisible();
  });

  test('should search for owner by last name using filter', async ({ page }) => {
    // Enter last name in search filter
    await page.fill('input[placeholder*="Search"]', 'Franklin');
    await page.waitForTimeout(300); // Wait for filter to apply
    
    // Should display matching owner(s)
    await expect(page.locator('text=Franklin')).toBeVisible();
    
    // Clear search to verify filter works
    await page.fill('input[placeholder*="Search"]', '');
    await page.waitForTimeout(300);
    
    // Should show more owners again
    const rowCount = await page.locator('table tbody tr').count();
    expect(rowCount).toBeGreaterThan(1);
  });

  test('should view owner details', async ({ page }) => {
    // Click on first owner link
    await page.click('table tbody tr:first-child a');
    await page.waitForURL('**/owners/**');
    
    // Should display owner information section
    await expect(page.locator('h2:has-text("Owner Information")')).toBeVisible();
    
    // Should have owner details (using th for labels in the table)
    await expect(page.locator('th:has-text("Name")')).toBeVisible();
    await expect(page.locator('th:has-text("Address")')).toBeVisible();
    await expect(page.locator('th:has-text("City")')).toBeVisible();
    await expect(page.locator('th:has-text("Telephone")')).toBeVisible();
    
    // Should have Edit Owner button
    await expect(page.locator('a:has-text("Edit Owner")')).toBeVisible();
    
    // Should display pets and visits section
    await expect(page.locator('h2:has-text("Pets and Visits")')).toBeVisible();
  });

  test('should add new owner', async ({ page }) => {
    // Click Add Owner button
    await page.click('a.btn:has-text("Add Owner")');
    await page.waitForURL('**/owners/new');
    
    // Should show form title
    await expect(page.locator('h2:has-text("Owner")')).toBeVisible();
    
    // Fill in owner details using Angular reactive forms
    await page.fill('input#firstName', 'Test');
    await page.fill('input#lastName', 'Owner');
    await page.fill('input#address', '123 Test Street');
    await page.fill('input#city', 'Test City');
    await page.fill('input#telephone', '1234567890');
    
    // Submit form
    await page.click('button[type="submit"]:has-text("Owner")');
    await page.waitForURL('**/owners/**');
    
    // Should redirect to owner details page
    await expect(page.locator('h2:has-text("Owner Information")')).toBeVisible();
    await expect(page.locator('text=Test Owner')).toBeVisible();
  });

  test('should edit owner information', async ({ page }) => {
    // Click on first owner
    await page.click('table tbody tr:first-child a');
    await page.waitForURL('**/owners/**');
    
    // Get the owner ID from URL
    const url = page.url();
    const ownerId = url.match(/owners\/(\d+)/)?.[1];
    
    // Click Edit Owner button
    await page.click('a:has-text("Edit Owner")');
    await page.waitForURL(`**/owners/${ownerId}/edit`);
    
    // Modify owner details
    await page.fill('input#city', 'Updated City');
    
    // Submit form
    await page.click('button[type="submit"]:has-text("Owner")');
    await page.waitForURL(`**/owners/${ownerId}`);
    
    // Should display updated information
    await expect(page.locator('td:has-text("Updated City")')).toBeVisible();
  });

  test('should show validation errors for invalid input', async ({ page }) => {
    // Click Add Owner button
    await page.click('a.btn:has-text("Add Owner")');
    await page.waitForURL('**/owners/new');
    
    // Fill only firstName to trigger validation on other required fields
    await page.fill('input#firstName', 'Test');
    await page.fill('input#lastName', ''); // Clear required field
    
    // Try to submit form
    await page.click('button[type="submit"]:has-text("Owner")');
    
    // Should display validation error for lastName
    await expect(page.locator('.invalid-feedback')).toBeVisible();
  });

  test('should add pet to owner', async ({ page }) => {
    // Click on first owner
    await page.click('table tbody tr:first-child a');
    await page.waitForURL('**/owners/**');
    
    // Click Add New Pet button
    await page.click('a:has-text("Add New Pet")');
    await page.waitForURL('**/pets/new');
    
    // Should show pet form
    await expect(page.locator('h2:has-text("Pet")')).toBeVisible();
    
    // Fill in pet details
    await page.fill('input#name', 'TestPet');
    await page.selectOption('select#typeId', { index: 0 }); // Select first pet type
    
    // Fill birth date
    const today = new Date().toISOString().split('T')[0];
    await page.fill('input#birthDate', today);
    
    // Submit form
    await page.click('button[type="submit"]:has-text("Pet")');
    await page.waitForURL('**/owners/**');
    
    // Should show new pet in owner details
    await expect(page.locator('text=TestPet')).toBeVisible();
  });
});
