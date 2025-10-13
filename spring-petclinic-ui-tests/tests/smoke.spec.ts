import { test, expect } from '@playwright/test';

/**
 * Basic smoke test to verify the Pet Clinic application loads correctly
 */
test.describe('Pet Clinic - Smoke Tests', () => {
  test('should load the welcome page', async ({ page }) => {
    await page.goto('/');
    
    // Check that the page title is correct
    await expect(page).toHaveTitle(/PetClinic/);
    
    // Check that the navigation bar is present
    await expect(page.locator('nav')).toBeVisible();
    
    // Check that the home link is present
    await expect(page.locator('a:has-text("Home")')).toBeVisible();
    
    // Check that the Find Owners link is present
    await expect(page.locator('a:has-text("Find owners")')).toBeVisible();
    
    // Check that the Veterinarians link is present
    await expect(page.locator('a:has-text("Veterinarians")')).toBeVisible();
    
    // Check for the welcome message or header
    await expect(page.locator('h1, h2')).toContainText(/Welcome/i);
  });

  test('should navigate to different pages', async ({ page }) => {
    await page.goto('/');
    
    // Navigate to Find Owners
    await page.click('text=Find owners');
    await page.waitForLoadState('networkidle');
    await expect(page.url()).toContain('owners');
    
    // Navigate to Veterinarians
    await page.click('text=Veterinarians');
    await page.waitForLoadState('networkidle');
    await expect(page.url()).toContain('vets');
    
    // Navigate back to Home
    await page.click('text=Home');
    await page.waitForLoadState('networkidle');
    await expect(page.url()).toContain('welcome');
  });

  test('should display footer information', async ({ page }) => {
    await page.goto('/');
    
    // Check that footer is present
    const footer = page.locator('footer');
    await expect(footer).toBeVisible();
  });
});
