import { test, expect } from '@playwright/test';

/**
 * Basic smoke test to verify the Pet Clinic Angular 20 application loads correctly
 */
test.describe('Pet Clinic - Smoke Tests (Angular 20)', () => {
  test('should load the welcome page', async ({ page }) => {
    await page.goto('/');
    
    // Check that the page title is correct
    await expect(page).toHaveTitle(/SpringPetclinicAngular/);
    
    // Check that the navigation bar is present
    await expect(page.locator('nav.navbar')).toBeVisible();
    
    // Check that the home link is present and active
    await expect(page.locator('a.nav-link:has-text("Home")')).toBeVisible();
    await expect(page.locator('a.nav-link.active:has-text("Home")')).toBeVisible();
    
    // Check that the Find Owners link is present
    await expect(page.locator('a.nav-link:has-text("Find owners")')).toBeVisible();
    
    // Check that the Veterinarians link is present
    await expect(page.locator('a.nav-link:has-text("Veterinarians")')).toBeVisible();
    
    // Check for the welcome message header
    await expect(page.locator('h1:has-text("Welcome to PetClinic")')).toBeVisible();
    
    // Check for Spring logo (use first() to avoid strict mode violation with multiple logos)
    await expect(page.locator('img[alt="Spring Logo"]').first()).toBeVisible();
    
    // Check for features section
    await expect(page.locator('h2:has-text("Features")')).toBeVisible();
  });

  test('should navigate to different pages using Angular routing', async ({ page }) => {
    await page.goto('/');
    
    // Navigate to Find Owners
    await page.click('a.nav-link:has-text("Find owners")');
    await page.waitForURL('**/owners');
    await expect(page.url()).toContain('owners');
    
    // Navigate to Veterinarians
    await page.click('a.nav-link:has-text("Veterinarians")');
    await page.waitForURL('**/vets');
    await expect(page.url()).toContain('vets');
    
    // Navigate back to Home
    await page.click('a.nav-link:has-text("Home")');
    await page.waitForURL('**/welcome');
    await expect(page.url()).toContain('welcome');
  });

  test('should display footer information', async ({ page }) => {
    await page.goto('/');
    
    // Check that footer is present with copyright
    const footer = page.locator('footer');
    await expect(footer).toBeVisible();
    await expect(footer).toContainText(/©.*Spring PetClinic/i);
  });

  test('should maintain Spring green theme colors', async ({ page }) => {
    await page.goto('/');
    
    // Check navigation bar has brown background (#34302D)
    const nav = page.locator('nav.navbar');
    await expect(nav).toHaveCSS('background-color', 'rgb(52, 48, 45)'); // #34302D
    
    // Check welcome banner has green background (#6db33f)
    const banner = page.locator('.welcome-banner');
    await expect(banner).toHaveCSS('background-color', 'rgb(109, 179, 63)'); // #6db33f
  });
});
