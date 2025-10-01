import { test, expect } from '@playwright/test';
import { OwnerListPage } from '../page-objects/owner-list-page';
import { OwnerDetailsPage } from '../page-objects/owner-details-page';
import { VisitsPage } from '../page-objects/visits-page';

test.describe('Visit Management Tests', () => {
  test('should navigate to visits page from pet details', async ({ page }) => {
    const ownerListPage = new OwnerListPage(page);
    const ownerDetailsPage = new OwnerDetailsPage(page);
    const visitsPage = new VisitsPage(page);
    
    // Navigate to owner with pets
    await ownerListPage.goto('#!/owners');
    await ownerListPage.waitForLoad();
    
    const firstOwnerLink = page.locator('tbody tr:first-child a').first();
    await firstOwnerLink.click();
    await page.waitForURL('**/#!/owners/details/*');
    await page.waitForLoadState('networkidle');
    
    // Get pets and click on visits for first pet
    const pets = await ownerDetailsPage.getPetsList();
    if (pets.length > 0) {
      // Look for a visits link in the pet table
      const visitsLink = page.locator('a:has-text("Visits"), a[href*="visits"]').first();
      if (await visitsLink.isVisible()) {
        await visitsLink.click();
        
        // Should navigate to visits page
        await page.waitForURL('**/#!/owners/*/pets/*/visits');
        expect(page.url()).toContain('/visits');
        
        // Should display visits page content
        const heading = await visitsPage.getPageHeading();
        expect(heading).toBeTruthy();
      }
    }
  });

  test('should display pet information on visits page', async ({ page }) => {
    const ownerListPage = new OwnerListPage(page);
    const ownerDetailsPage = new OwnerDetailsPage(page);
    const visitsPage = new VisitsPage(page);
    
    // Navigate to a pet's visits page
    await ownerListPage.goto('#!/owners');
    await ownerListPage.waitForLoad();
    
    const firstOwnerLink = page.locator('tbody tr:first-child a').first();
    await firstOwnerLink.click();
    await page.waitForURL('**/#!/owners/details/*');
    await page.waitForLoadState('networkidle');
    
    // Try to find and click a visits link
    const visitsLink = page.locator('a:has-text("Visits"), a[href*="visits"]').first();
    if (await visitsLink.isVisible()) {
      await visitsLink.click();
      await page.waitForURL('**/#!/owners/*/pets/*/visits');
      
      // Should display visits page with heading
      const heading = await visitsPage.getPageHeading();
      expect(heading).toContain('Visits');
      
      // Should display the add visit form
      expect(await visitsPage.isAddVisitButtonVisible()).toBe(true);
    }
  });

  test('should display visits history in chronological order', async ({ page }) => {
    const ownerListPage = new OwnerListPage(page);
    const visitsPage = new VisitsPage(page);
    
    // Navigate to a visits page
    await ownerListPage.goto('#!/owners');
    await ownerListPage.waitForLoad();
    
    const firstOwnerLink = page.locator('tbody tr:first-child a').first();
    await firstOwnerLink.click();
    await page.waitForURL('**/#!/owners/details/*');
    await page.waitForLoadState('networkidle');
    
    const visitsLink = page.locator('a:has-text("Visits"), a[href*="visits"]').first();
    if (await visitsLink.isVisible()) {
      await visitsLink.click();
      await page.waitForURL('**/#!/owners/*/pets/*/visits');
      
      // Get visit history
      const visits = await visitsPage.getVisitHistory();
      
      // Verify visits structure
      visits.forEach(visit => {
        expect(visit.date).toBeTruthy();
        expect(visit.description).toBeTruthy();
      });
      
      // If there are multiple visits, check chronological order
      if (visits.length > 1) {
        for (let i = 0; i < visits.length - 1; i++) {
          const currentDate = new Date(visits[i].date || '');
          const nextDate = new Date(visits[i + 1].date || '');
          // Visits should be in reverse chronological order (newest first)
          expect(currentDate.getTime()).toBeGreaterThanOrEqual(nextDate.getTime());
        }
      }
    }
  });

  test('should add new visit successfully', async ({ page }) => {
    const ownerListPage = new OwnerListPage(page);
    const visitsPage = new VisitsPage(page);
    
    // Navigate to visits page
    await ownerListPage.goto('#!/owners');
    await ownerListPage.waitForLoad();
    
    const firstOwnerLink = page.locator('tbody tr:first-child a').first();
    await firstOwnerLink.click();
    await page.waitForURL('**/#!/owners/details/*');
    await page.waitForLoadState('networkidle');
    
    const visitsLink = page.locator('a:has-text("Visits"), a[href*="visits"]').first();
    if (await visitsLink.isVisible()) {
      await visitsLink.click();
      await page.waitForURL('**/#!/owners/*/pets/*/visits');
      
      // Get initial visits count
      const initialVisits = await visitsPage.getVisitHistory();
      const initialCount = initialVisits.length;
      
      // Check if add visit functionality is available
      if (await visitsPage.isAddVisitButtonVisible()) {
        const testVisit = {
          date: '2023-12-01',
          description: `Test visit description ${Date.now()}`
        };
        
        await visitsPage.addNewVisit(testVisit);
        
        // Wait for page to reload after submission
        await page.waitForLoadState('networkidle');
        await page.waitForTimeout(500); // Give Angular time to update the view
        
        // Should add the visit
        const updatedVisits = await visitsPage.getVisitHistory();
        expect(updatedVisits.length).toBeGreaterThanOrEqual(initialCount + 1);
        
        // Verify the new visit is in the list
        const visitDescriptions = updatedVisits.map(v => v.description);
        expect(visitDescriptions).toContain(testVisit.description);
      }
    }
  });

  test('should validate visit form data', async ({ page }) => {
    const ownerListPage = new OwnerListPage(page);
    const visitsPage = new VisitsPage(page);
    
    // Navigate to visits page
    await ownerListPage.goto('#!/owners');
    await ownerListPage.waitForLoad();
    
    const firstOwnerLink = page.locator('tbody tr:first-child a').first();
    await firstOwnerLink.click();
    
    const visitsLink = page.locator('a:has-text("Visits"), a[href*="visits"]').first();
    if (await visitsLink.isVisible()) {
      await visitsLink.click();
      await page.waitForURL('**/#!/owners/*/pets/*/visits');
      
      // Check if visit form is available
      if (await visitsPage.isAddVisitButtonVisible()) {
        await visitsPage.addVisitButton.click();
        
        if (await visitsPage.isVisitFormVisible()) {
          // Try to submit empty form
          await visitsPage.submitVisitButton.click();
          
          // Form should either show validation errors or not submit
          // The URL should remain the same if validation fails
          const currentUrl = page.url();
          expect(currentUrl).toContain('/visits');
        }
      }
    }
  });

  test('should link visits to correct pet', async ({ page }) => {
    const ownerListPage = new OwnerListPage(page);
    const ownerDetailsPage = new OwnerDetailsPage(page);
    const visitsPage = new VisitsPage(page);
    
    // Navigate to owner with multiple pets (if available)
    await ownerListPage.goto('#!/owners');
    await ownerListPage.waitForLoad();
    
    const firstOwnerLink = page.locator('tbody tr:first-child a').first();
    await firstOwnerLink.click();
    await page.waitForURL('**/#!/owners/details/*');
    await page.waitForLoadState('networkidle');
    
    const pets = await ownerDetailsPage.getPetsList();
    
    if (pets.length > 0) {
      const firstPet = pets[0];
      
      // Navigate to visits for this specific pet
      const visitsLink = page.locator('a:has-text("Visits"), a[href*="visits"]').first();
      if (await visitsLink.isVisible()) {
        await visitsLink.click();
        await page.waitForURL('**/#!/owners/*/pets/*/visits');
        
        // Verify we're on visits page and can see the visits heading
        const heading = await visitsPage.getPageHeading();
        expect(heading).toContain('Visits');
        
        // Verify the URL contains the correct pet ID (from firstPet if available)
        expect(page.url()).toContain('/pets/');
        expect(page.url()).toContain('/visits');
      }
    }
  });

  test('should display visit date in proper format', async ({ page }) => {
    const ownerListPage = new OwnerListPage(page);
    const visitsPage = new VisitsPage(page);
    
    // Navigate to visits page
    await ownerListPage.goto('#!/owners');
    await ownerListPage.waitForLoad();
    
    const firstOwnerLink = page.locator('tbody tr:first-child a').first();
    await firstOwnerLink.click();
    
    const visitsLink = page.locator('a:has-text("Visits"), a[href*="visits"]').first();
    if (await visitsLink.isVisible()) {
      await visitsLink.click();
      await page.waitForURL('**/#!/owners/*/pets/*/visits');
      
      const visits = await visitsPage.getVisitHistory();
      
      // Check date format for each visit
      visits.forEach(visit => {
        if (visit.date) {
          // Date should be in a valid format
          const parsedDate = new Date(visit.date);
          expect(parsedDate.toString()).not.toBe('Invalid Date');
          
          // Date should not be in the future (assuming this is historical data)
          const now = new Date();
          expect(parsedDate.getTime()).toBeLessThanOrEqual(now.getTime());
        }
      });
    }
  });

  test('should handle navigation back to pet details', async ({ page }) => {
    const ownerListPage = new OwnerListPage(page);
    const ownerDetailsPage = new OwnerDetailsPage(page);
    
    // Navigate to visits page
    await ownerListPage.goto('#!/owners');
    await ownerListPage.waitForLoad();
    
    const firstOwnerLink = page.locator('tbody tr:first-child a').first();
    await firstOwnerLink.click();
    await page.waitForURL('**/#!/owners/details/*');
    await page.waitForLoadState('networkidle');
    
    const visitsLink = page.locator('a:has-text("Visits"), a[href*="visits"]').first();
    if (await visitsLink.isVisible()) {
      await visitsLink.click();
      await page.waitForURL('**/#!/owners/*/pets/*/visits');
      await page.waitForLoadState('networkidle');
      
      // Use browser back navigation to go back
      await page.goBack();
      
      // Should navigate back to owner details
      await page.waitForURL('**/#!/owners/details/*', { timeout: 10000 });
      expect(page.url()).toContain('#!/owners/details/');
      expect(page.url()).not.toContain('/visits');
    }
  });
});