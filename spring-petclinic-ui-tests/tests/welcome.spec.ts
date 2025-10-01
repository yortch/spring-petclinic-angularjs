import { test, expect } from '@playwright/test';
import { WelcomePage } from '../page-objects/welcome-page';

test.describe('Welcome Page Tests', () => {
  test('should display welcome message and navigate to different sections', async ({ page }) => {
    const welcomePage = new WelcomePage(page);
    
    await welcomePage.goto('/');
    await welcomePage.waitForLoad();
    
    // Verify welcome content is displayed
    expect(await welcomePage.isWelcomeVisible()).toBe(true);
    expect(await welcomePage.isPetsImageVisible()).toBe(true);
    
    // Test navigation to veterinarians
    await welcomePage.clickVeterinarians();
    await page.waitForURL('**/#!/vets');
    expect(page.url()).toContain('#!/vets');
    
    // Navigate back to home
    await welcomePage.clickHome();
    await page.waitForURL('**/#!/welcome');
    expect(await welcomePage.isWelcomeVisible()).toBe(true);
  });

  test('should expand owners dropdown and show options', async ({ page }) => {
    const welcomePage = new WelcomePage(page);
    
    await welcomePage.goto('/');
    await welcomePage.waitForLoad();
    
    // Expand owners dropdown
    await welcomePage.expandOwnersDropdown();
    
    // Check dropdown options are visible
    const allOwnersLink = page.locator('a[ui-sref="owners"]:has-text("All")');
    const registerOwnerLink = page.locator('a[ui-sref="ownerNew"]:has-text("Register")');
    
    expect(await allOwnersLink.isVisible()).toBe(true);
    expect(await registerOwnerLink.isVisible()).toBe(true);
  });

  test('should navigate to owners list from dropdown', async ({ page }) => {
    const welcomePage = new WelcomePage(page);
    
    await welcomePage.goto('/');
    await welcomePage.waitForLoad();
    
    // Click "All" in owners dropdown
    await welcomePage.clickOwnersAll();
    
    // Should navigate to owners list
    await page.waitForURL('**/#!/owners');
    expect(page.url()).toContain('#!/owners');
    
    // Should show owners page content
    const ownersHeading = page.locator('h2:has-text("Owners")');
    expect(await ownersHeading.isVisible()).toBe(true);
  });

  test('should navigate to owner registration from dropdown', async ({ page }) => {
    const welcomePage = new WelcomePage(page);
    
    await welcomePage.goto('/');
    await welcomePage.waitForLoad();
    
    // Click "Register" in owners dropdown
    await welcomePage.clickOwnersRegister();
    
    // Should navigate to new owner form
    await page.waitForURL('**/#!/owners/new');
    expect(page.url()).toContain('#!/owners/new');
    
    // Should show new owner form
    const newOwnerHeading = page.locator('h2:has-text("Owner")');
    expect(await newOwnerHeading.isVisible()).toBe(true);
  });

  test('should maintain responsive design elements', async ({ page }) => {
    const welcomePage = new WelcomePage(page);
    
    await welcomePage.goto('/');
    await welcomePage.waitForLoad();
    
    // Check that Bootstrap responsive elements are present
    const navbar = page.locator('.navbar');
    const container = page.locator('.container-fluid');
    
    expect(await navbar.isVisible()).toBe(true);
    expect(await container.isVisible()).toBe(true);
    
    // Check navbar toggle button exists (for mobile responsiveness)
    const navbarToggle = page.locator('.navbar-toggle');
    expect(await navbarToggle.count()).toBeGreaterThan(0);
  });
});