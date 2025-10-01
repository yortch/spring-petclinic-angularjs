import { test, expect } from '@playwright/test';
import { WelcomePage } from '../page-objects/welcome-page';

test.describe('Application Loading Tests', () => {
  test('should load the application successfully', async ({ page }) => {
    const welcomePage = new WelcomePage(page);
    
    // Navigate to the application
    await welcomePage.goto('/');
    await welcomePage.waitForLoad();
    
    // Check page title
    const title = await welcomePage.getPageTitle();
    expect(title).toContain('PetClinic');
    
    // Check that main navigation is visible
    expect(await welcomePage.isNavbarVisible()).toBe(true);
    
    // Check that the application content is visible
    expect(await welcomePage.isWelcomeVisible()).toBe(true);
  });

  test('should display navigation elements correctly', async ({ page }) => {
    const welcomePage = new WelcomePage(page);
    
    await welcomePage.goto('/');
    await welcomePage.waitForLoad();
    
    // Check navigation elements are visible
    expect(await welcomePage.homeLink.isVisible()).toBe(true);
    expect(await welcomePage.ownersDropdown.isVisible()).toBe(true);
    expect(await welcomePage.veterinariansLink.isVisible()).toBe(true);
  });

  test('should load welcome page content', async ({ page }) => {
    const welcomePage = new WelcomePage(page);
    
    await welcomePage.goto('/');
    await welcomePage.waitForLoad();
    
    // Check welcome page specific content
    expect(await welcomePage.isWelcomeVisible()).toBe(true);
    expect(await welcomePage.isPetsImageVisible()).toBe(true);
  });

  test('should handle AngularJS routing correctly', async ({ page }) => {
    const welcomePage = new WelcomePage(page);
    
    // Test direct navigation to welcome route
    await welcomePage.goto('#!/welcome');
    await welcomePage.waitForLoad();
    
    expect(await welcomePage.isWelcomeVisible()).toBe(true);
    
    // Test that URL contains AngularJS hash routing
    const url = page.url();
    expect(url).toContain('#!/welcome');
  });
});