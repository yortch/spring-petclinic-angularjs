import { test, expect } from '@playwright/test';
import { BasePage } from '../page-objects/base-page';
import { WelcomePage } from '../page-objects/welcome-page';
import { OwnerListPage } from '../page-objects/owner-list-page';
import { OwnerFormPage } from '../page-objects/owner-form-page';
import { VetListPage } from '../page-objects/vet-list-page';

test.describe('Navigation and Form Validation Tests', () => {
  test('should navigate correctly between all main sections', async ({ page }) => {
    const welcomePage = new WelcomePage(page);
    const ownerListPage = new OwnerListPage(page);
    const vetListPage = new VetListPage(page);
    
    // Start at welcome page
    await welcomePage.goto('#!/welcome');
    await welcomePage.waitForLoad();
    expect(await welcomePage.isWelcomeVisible()).toBe(true);
    
    // Navigate to owners list
    await welcomePage.clickOwnersAll();
    await page.waitForURL('**/#!/owners');
    await page.waitForLoadState('networkidle');
    expect(await ownerListPage.ownersHeading.isVisible()).toBe(true);
    
    // Navigate to veterinarians
    await welcomePage.clickVeterinarians();
    await page.waitForURL('**/#!/vets');
    await vetListPage.vetsHeading.waitFor({ state: 'visible', timeout: 5000 });
    expect(await vetListPage.vetsHeading.isVisible()).toBe(true);
    
    // Navigate back to home
    await welcomePage.clickHome();
    await page.waitForURL('**/#!/welcome');
    expect(await welcomePage.isWelcomeVisible()).toBe(true);
  });

  test('should maintain consistent navigation across all pages', async ({ page }) => {
    const pages = ['#!/welcome', '#!/owners', '#!/vets'];
    
    for (const pagePath of pages) {
      await page.goto(pagePath);
      await page.waitForLoadState('networkidle');
      
      // Check navigation elements are consistently visible
      expect(await page.locator('.navbar').isVisible()).toBe(true);
      expect(await page.locator('a[ui-sref="welcome"]').isVisible()).toBe(true);
      expect(await page.locator('.dropdown:has-text("Owners")').isVisible()).toBe(true);
      expect(await page.locator('a[ui-sref="vets"]').isVisible()).toBe(true);
      
      // Check footer is present
      expect(await page.locator('layout-footer').isVisible()).toBe(true);
    }
  });

  test('should validate required fields in owner form', async ({ page }) => {
    const ownerFormPage = new OwnerFormPage(page);
    
    await ownerFormPage.goto('#!/owners/new');
    await ownerFormPage.waitForLoad();
    
    // Test empty form submission
    await ownerFormPage.submitForm();
    
    // Form should either show validation errors or stay on the same page
    const currentUrl = page.url();
    expect(currentUrl).toContain('#!/owners/new');
    
    // Test with invalid data
    await ownerFormPage.fillOwnerForm({
      firstName: '',
      lastName: '',
      address: '',
      city: '',
      telephone: '12345678901' // Too many digits (> 10)
    });
    
    await ownerFormPage.submitForm();
    
    // Should still be on the form page due to validation
    expect(page.url()).toContain('#!/owners/new');
  });

  test('should validate telephone number format', async ({ page }) => {
    const ownerFormPage = new OwnerFormPage(page);
    
    await ownerFormPage.goto('#!/owners/new');
    await ownerFormPage.waitForLoad();
    
    // Test with valid data but invalid telephone
    await ownerFormPage.fillOwnerForm({
      firstName: 'Test',
      lastName: 'User',
      address: '123 Test St',
      city: 'Test City',
      telephone: '12345678901234567890' // Way too long
    });
    
    await ownerFormPage.submitForm();
    
    // Should stay on form due to telephone validation
    expect(page.url()).toContain('#!/owners/new');
  });

  test('should handle AngularJS state transitions correctly', async ({ page }) => {
    // Test direct URL navigation to various states
    const states = [
      { url: '#!/welcome', selector: 'h1:has-text("Welcome to Petclinic")' },
      { url: '#!/owners', selector: 'h2:has-text("Owners")' },
      { url: '#!/owners/new', selector: 'h2:has-text("Owner")' },
      { url: '#!/vets', selector: 'h2:has-text("Veterinarians")' }
    ];
    
    for (const state of states) {
      await page.goto(state.url);
      await page.waitForLoadState('networkidle');
      
      // Check URL is correct
      expect(page.url()).toContain(state.url);
      
      // Check state-specific content is visible
      const element = page.locator(state.selector).first();
      await element.waitFor({ state: 'visible', timeout: 5000 });
      expect(await element.isVisible()).toBe(true);
    }
  });

  test('should handle browser back/forward navigation', async ({ page }) => {
    const welcomePage = new WelcomePage(page);
    
    // Start at welcome
    await welcomePage.goto('#!/welcome');
    await welcomePage.waitForLoad();
    
    // Navigate to owners
    await welcomePage.expandOwnersDropdown();
    await page.locator('a[ui-sref="owners"]:has-text("All")').click();
    await page.waitForURL('**/#!/owners');
    
    // Navigate to veterinarians
    await welcomePage.clickVeterinarians();
    await page.waitForURL('**/#!/vets');
    
    // Use browser back button
    await page.goBack();
    await page.waitForURL('**/#!/owners');
    expect(await page.locator('h2:has-text("Owners")').isVisible()).toBe(true);
    
    // Use browser back button again
    await page.goBack();
    await page.waitForURL('**/#!/welcome');
    expect(await page.locator('h1:has-text("Welcome to Petclinic")').isVisible()).toBe(true);
    
    // Use browser forward button
    await page.goForward();
    await page.waitForURL('**/#!/owners');
    expect(await page.locator('h2:has-text("Owners")').isVisible()).toBe(true);
  });

  test('should display error messages appropriately', async ({ page }) => {
    // Navigate to a form and test error handling
    await page.goto('#!/owners/new');
    await page.waitForLoadState('networkidle');
    
    // Try to submit with partial invalid data
    await page.locator('input[name="firstName"]').fill('Test');
    await page.locator('input[name="telephone"]').fill('invalid_phone');
    
    await page.locator('button[type="submit"]').click();
    
    // Check if any error messages or validation feedback is displayed
    const errorElements = page.locator('.help-block, .alert-danger, .error, .invalid-feedback');
    const hasErrors = await errorElements.count() > 0;
    
    // Either errors should be shown or form should not submit (stay on same page)
    if (!hasErrors) {
      expect(page.url()).toContain('#!/owners/new');
    }
  });

  test('should handle invalid routes gracefully', async ({ page }) => {
    // Test navigation to non-existent routes
    await page.goto('#!/invalid-route');
    await page.waitForLoadState('networkidle');
    
    // Should either redirect to welcome or show a proper error state
    // Based on AngularJS configuration: $urlRouterProvider.otherwise('/welcome');
    await page.waitForURL('**/#!/welcome');
    expect(page.url()).toContain('#!/welcome');
  });

  test('should maintain responsive design across different screen sizes', async ({ page }) => {
    const viewports = [
      { width: 1280, height: 720 }, // Desktop
      { width: 768, height: 1024 }, // Tablet
      { width: 480, height: 854 }   // Mobile
    ];
    
    for (const viewport of viewports) {
      await page.setViewportSize(viewport);
      
      await page.goto('#!/welcome');
      await page.waitForLoadState('networkidle');
      
      // Navigation should be visible or have mobile toggle
      const navbar = page.locator('.navbar');
      expect(await navbar.isVisible()).toBe(true);
      
      // Check for mobile navbar toggle at smaller sizes
      if (viewport.width < 768) {
        const navbarToggle = page.locator('.navbar-toggle');
        expect(await navbarToggle.isVisible()).toBe(true);
      }
    }
  });

  test('should validate date formats correctly', async ({ page }) => {
    // Navigate to owner with pets to test date handling
    await page.goto('#!/owners');
    await page.waitForLoadState('networkidle');
    
    const firstOwnerLink = page.locator('tbody tr:first-child a').first();
    if (await firstOwnerLink.isVisible()) {
      await firstOwnerLink.click();
      // Wait for navigation to owner details page
      await page.waitForURL('**/#!/owners/details/*');
      await page.waitForLoadState('networkidle');
      
      // Try to add a pet with invalid date
      const addPetButton = page.locator('a:has-text("Add New Pet")');
      await addPetButton.waitFor({ state: 'visible', timeout: 5000 });
      await addPetButton.click();
      await page.waitForURL('**/#!/owners/*/new-pet');
      await page.waitForLoadState('networkidle');
      
      // Fill form with name and pet type
      await page.locator('input[ng-model="$ctrl.pet.name"]').fill('Test Pet');
      
      if (await page.locator('select[ng-model="$ctrl.petTypeId"]').isVisible()) {
        await page.locator('select[ng-model="$ctrl.petTypeId"]').selectOption('1');
      }
      
      // Set invalid date directly via AngularJS model (bypassing HTML5 validation)
      await page.evaluate(() => {
        const scope = (window as any).angular.element(document.querySelector('input[ng-model="$ctrl.pet.birthDate"]')).scope();
        scope.$ctrl.pet.birthDate = 'invalid-date';
        scope.$apply();
      });
      
      // Try to submit the form
      await page.locator('button[type="submit"]').click();
      
      // Should stay on form due to invalid date or show error
      await page.waitForTimeout(500);
      expect(page.url()).toContain('/new-pet');
    }
  });

  test('should handle concurrent navigation requests', async ({ page }) => {
    // Test rapid navigation between sections
    await page.goto('#!/welcome');
    await page.waitForLoadState('networkidle');
    
    // Rapidly navigate between sections
    await page.locator('a[ui-sref="vets"]').click();
    await page.locator('a[ui-sref="welcome"]').click();
    
    // Should handle the navigation gracefully
    await page.waitForURL('**/#!/welcome');
    expect(await page.locator('h1:has-text("Welcome to Petclinic")').isVisible()).toBe(true);
  });

  test('should maintain application state during navigation', async ({ page }) => {
    const basePage = new BasePage(page);
    
    // Navigate to owners and use search
    await page.goto('#!/owners');
    await page.waitForLoadState('networkidle');
    
    const searchInput = page.locator('input[placeholder="Search Filter"]');
    if (await searchInput.isVisible()) {
      await searchInput.fill('George');
      await page.waitForTimeout(500); // Wait for filter to apply
      
      // Navigate away and back using proper navigation methods
      await basePage.clickVeterinarians();
      await page.waitForURL('**/#!/vets');
      
      await basePage.clickOwnersAll();
      await page.waitForURL('**/#!/owners');
      
      // Search filter should be cleared (new state)
      const currentSearchValue = await searchInput.inputValue();
      expect(currentSearchValue).toBe('');
    }
  });
});