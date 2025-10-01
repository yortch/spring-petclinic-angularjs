import { test, expect } from '@playwright/test';
import { BasePage } from '../page-objects/base-page';
import { OwnerListPage } from '../page-objects/owner-list-page';
import { OwnerFormPage } from '../page-objects/owner-form-page';
import { OwnerDetailsPage } from '../page-objects/owner-details-page';

test.describe('Owner Management Tests', () => {
  test('should display owners list correctly', async ({ page }) => {
    const ownerListPage = new OwnerListPage(page);
    
    await ownerListPage.goto('#!/owners');
    await ownerListPage.waitForLoad();
    
    // Check page elements are visible
    expect(await ownerListPage.ownersHeading.isVisible()).toBe(true);
    expect(await ownerListPage.searchInput.isVisible()).toBe(true);
    expect(await ownerListPage.ownersTable.isVisible()).toBe(true);
    
    // Check that table has data (sample data should exist)
    const ownerRows = await ownerListPage.getOwnerRows();
    expect(await ownerRows.count()).toBeGreaterThan(0);
  });

  test('should filter owners using search', async ({ page }) => {
    const ownerListPage = new OwnerListPage(page);
    
    await ownerListPage.goto('#!/owners');
    await ownerListPage.waitForLoad();
    
    // Get initial count of owners
    const initialRows = await ownerListPage.getOwnerRows();
    const initialCount = await initialRows.count();
    
    // Search for a specific owner
    await ownerListPage.searchOwner('George');
    await page.waitForTimeout(500); // Wait for filter to apply
    
    // Check filtered results
    const filteredRows = await ownerListPage.getOwnerRows();
    const filteredCount = await filteredRows.count();
    
    expect(filteredCount).toBeLessThanOrEqual(initialCount);
    
    // Clear search
    await ownerListPage.searchOwner('');
    await page.waitForTimeout(500);
    
    const clearedRows = await ownerListPage.getOwnerRows();
    expect(await clearedRows.count()).toBe(initialCount);
  });

  test('should navigate to owner details', async ({ page }) => {
    const ownerListPage = new OwnerListPage(page);
    const ownerDetailsPage = new OwnerDetailsPage(page);
    
    await ownerListPage.goto('#!/owners');
    await ownerListPage.waitForLoad();
    
    // Click on first owner in the list
    const firstOwnerLink = page.locator('tbody tr:first-child a').first();
    await firstOwnerLink.click();
    
    // Should navigate to owner details
    await page.waitForURL('**/#!/owners/details/*');
    await page.waitForLoadState('networkidle');
    expect(page.url()).toContain('#!/owners/details/');
    
    // Check owner details page elements are loaded
    await page.waitForSelector('h2:has-text(\"Owner Information\")', { timeout: 5000 });
    expect(await ownerDetailsPage.editOwnerButton.isVisible()).toBe(true);
    expect(await ownerDetailsPage.addPetButton.isVisible()).toBe(true);
  });

  test('should create new owner successfully', async ({ page }) => {
    const ownerFormPage = new OwnerFormPage(page);
    
    await ownerFormPage.goto('#!/owners/new');
    await ownerFormPage.waitForLoad();
    
    // Check form is visible
    expect(await ownerFormPage.isFormVisible()).toBe(true);
    
    // Fill form with test data
    const testOwner = {
      firstName: `Test${Date.now()}`,
      lastName: 'Owner',
      address: '123 Test Street',
      city: 'Test City',
      telephone: '1234567890'
    };
    
    await ownerFormPage.fillOwnerForm(testOwner);
    await ownerFormPage.submitForm();
    
    // Should redirect to owners list page (not details)
    await page.waitForURL('**/#!/owners', { timeout: 10000 });
    
    // Verify owner was created by searching for it in the list
    const ownerListPage = new OwnerListPage(page);
    await ownerListPage.searchOwner(testOwner.firstName);
    await page.waitForTimeout(500); // Wait for filter to apply
    
    // Check that the new owner appears in the results
    const rows = await ownerListPage.getOwnerRows();
    const rowCount = await rows.count();
    expect(rowCount).toBeGreaterThan(0);
    
    // Verify the owner details by clicking on the first result
    const firstOwnerLink = page.locator('tbody tr:first-child a').first();
    await firstOwnerLink.click();
    
    await page.waitForURL('**/#!/owners/details/*');
    const ownerDetailsPage = new OwnerDetailsPage(page);
    const ownerName = await ownerDetailsPage.getOwnerName();
    expect(ownerName).toContain(testOwner.firstName);
    expect(ownerName).toContain(testOwner.lastName);
  });

  test('should validate required fields in owner form', async ({ page }) => {
    const ownerFormPage = new OwnerFormPage(page);
    
    await ownerFormPage.goto('#!/owners/new');
    await ownerFormPage.waitForLoad();
    
    // Try to submit empty form
    await ownerFormPage.submitForm();
    
    // Check for validation errors or that form doesn't submit
    // The form should either show validation errors or stay on the same page
    const currentUrl = page.url();
    expect(currentUrl).toContain('#!/owners/new');
  });

  test('should edit existing owner', async ({ page }) => {
    const ownerListPage = new OwnerListPage(page);
    const ownerDetailsPage = new OwnerDetailsPage(page);
    const ownerFormPage = new OwnerFormPage(page);
    
    // Navigate to owners list and select first owner
    await ownerListPage.goto('#!/owners');
    await ownerListPage.waitForLoad();
    
    const firstOwnerLink = page.locator('tbody tr:first-child a').first();
    await firstOwnerLink.click();
    
    // Click edit button
    await ownerDetailsPage.clickEditOwner();
    
    // Should navigate to edit form
    await page.waitForURL('**/#!/owners/*/edit');
    expect(page.url()).toContain('/edit');
    
    // Form should be pre-filled
    expect(await ownerFormPage.isFormVisible()).toBe(true);
    
    // Modify a field
    const currentAddress = await ownerFormPage.addressInput.inputValue();
    const newAddress = currentAddress + ' - Updated';
    await ownerFormPage.addressInput.fill(newAddress);
    
    // Submit form
    await ownerFormPage.submitForm();
    
    // Should return to owner details
    await page.waitForURL('**/#!/owners/details/*');
    expect(page.url()).not.toContain('/edit');
    
    // Verify the change was saved
    const ownerDetails = await ownerDetailsPage.getOwnerDetails();
    expect(ownerDetails['Address']).toContain('- Updated');
  });

  test('should handle navigation between owner pages', async ({ page }) => {
    const basePage = new BasePage(page);
    const ownerListPage = new OwnerListPage(page);
    const ownerFormPage = new OwnerFormPage(page);
    
    // Start at owners list
    await ownerListPage.goto('#!/owners');
    await ownerListPage.waitForLoad();
    
    // Navigate to new owner form via navigation
    await basePage.clickOwnersRegister();
    await page.waitForURL('**/#!/owners/new');
    
    // Navigate back to list using browser back or direct navigation
    await basePage.clickOwnersAll();
    await page.waitForURL('**/#!/owners');
    
    // Should be back at owners list
    expect(await ownerListPage.ownersHeading.isVisible()).toBe(true);
  });
});