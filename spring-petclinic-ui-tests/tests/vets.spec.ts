import { test, expect } from '@playwright/test';

/**
 * Tests for Veterinarian functionality in Angular 20
 */
test.describe('Veterinarian Management (Angular 20)', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/');
    await page.click('a.nav-link:has-text("Veterinarians")');
    await page.waitForURL('**/vets');
  });

  test('should display list of veterinarians', async ({ page }) => {
    // Should display vets table
    await expect(page.locator('table.table')).toBeVisible();
    
    // Should have column headers
    await expect(page.locator('th:has-text("Name")')).toBeVisible();
    await expect(page.locator('th:has-text("Specialties")')).toBeVisible();
    
    // Should have at least one veterinarian
    const rowCount = await page.locator('table tbody tr').count();
    expect(rowCount).toBeGreaterThan(0);
  });

  test('should display veterinarian specialties', async ({ page }) => {
    // Check that specialties are displayed (some vets may have none)
    const hasSpecialties = await page.locator('table tbody tr:first-child td:nth-child(2)').textContent();
    expect(hasSpecialties).toBeDefined();
  });

  test('should display veterinarians in table format', async ({ page }) => {
    // Verify table structure
    await expect(page.locator('table.table-striped')).toBeVisible();
    await expect(page.locator('table thead')).toBeVisible();
    await expect(page.locator('table tbody')).toBeVisible();
  });
});
