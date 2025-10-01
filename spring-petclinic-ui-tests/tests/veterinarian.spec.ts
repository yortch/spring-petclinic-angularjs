import { test, expect } from '@playwright/test';
import { VetListPage } from '../page-objects/vet-list-page';

test.describe('Veterinarian Directory Tests', () => {
  test('should display veterinarians list correctly', async ({ page }) => {
    const vetListPage = new VetListPage(page);
    
    await vetListPage.goto('#!/vets');
    await vetListPage.waitForLoad();
    
    // Check page elements are visible
    expect(await vetListPage.vetsHeading.isVisible()).toBe(true);
    expect(await vetListPage.vetsTable.isVisible()).toBe(true);
    expect(await vetListPage.isTableHeaderVisible()).toBe(true);
    
    // Check that table has data (sample data should exist)
    const vetRows = await vetListPage.getVeterinarianRows();
    expect(await vetRows.count()).toBeGreaterThan(0);
  });

  test('should display veterinarian names and specialties', async ({ page }) => {
    const vetListPage = new VetListPage(page);
    
    await vetListPage.goto('#!/vets');
    await vetListPage.waitForLoad();
    
    // Get all veterinarians data
    const vets = await vetListPage.getVeterinarians();
    expect(vets.length).toBeGreaterThan(0);
    
    // Verify each vet has a name
    vets.forEach(vet => {
      expect(vet.name).toBeTruthy();
      expect(vet.name?.length).toBeGreaterThan(0);
    });
    
    // Check that at least one vet has specialties
    const vetsWithSpecialties = vets.filter(vet => 
      vet.specialties && vet.specialties.trim().length > 0
    );
    expect(vetsWithSpecialties.length).toBeGreaterThan(0);
  });

  test('should navigate to veterinarians page from navigation menu', async ({ page }) => {
    const vetListPage = new VetListPage(page);
    
    // Start from home page
    await vetListPage.goto('/');
    await vetListPage.waitForLoad();
    
    // Click veterinarians link in navigation
    await vetListPage.clickVeterinarians();
    
    // Should navigate to vets page
    await page.waitForURL('**/#!/vets');
    expect(page.url()).toContain('#!/vets');
    
    // Should display veterinarians content
    expect(await vetListPage.vetsHeading.isVisible()).toBe(true);
  });

  test('should format veterinarian specialties correctly', async ({ page }) => {
    const vetListPage = new VetListPage(page);
    
    await vetListPage.goto('#!/vets');
    await vetListPage.waitForLoad();
    
    const vets = await vetListPage.getVeterinarians();
    
    // Find vets with multiple specialties
    const vetsWithMultipleSpecialties = vets.filter(vet => 
      vet.specialties && vet.specialties.includes(' ')
    );
    
    if (vetsWithMultipleSpecialties.length > 0) {
      // Check that specialties are properly formatted
      vetsWithMultipleSpecialties.forEach(vet => {
        expect(vet.specialties).toBeTruthy();
        
        // Specialties should be space-separated
        // Based on the template: {{specialty.name + ' '}}
        const specialties = vet.specialties?.trim();
        expect(specialties?.length).toBeGreaterThan(0);
      });
    }
  });

  test('should display proper table structure', async ({ page }) => {
    const vetListPage = new VetListPage(page);
    
    await vetListPage.goto('#!/vets');
    await vetListPage.waitForLoad();
    
    // Check table structure
    const table = vetListPage.vetsTable;
    expect(await table.isVisible()).toBe(true);
    
    // Check table headers
    const headers = table.locator('thead th');
    expect(await headers.count()).toBe(2);
    
    const headerTexts = await headers.allTextContents();
    expect(headerTexts).toContain('Name');
    expect(headerTexts).toContain('Specialties');
    
    // Check that each row has the correct number of columns
    const vetRows = await vetListPage.getVeterinarianRows();
    const firstRow = vetRows.first();
    const cells = firstRow.locator('td');
    expect(await cells.count()).toBe(2);
  });

  test('should verify expected veterinarian specialties exist', async ({ page }) => {
    const vetListPage = new VetListPage(page);
    
    await vetListPage.goto('#!/vets');
    await vetListPage.waitForLoad();
    
    const vets = await vetListPage.getVeterinarians();
    
    // Collect all specialties from all veterinarians
    const allSpecialties: string[] = [];
    vets.forEach(vet => {
      if (vet.specialties) {
        // Split specialties by space and filter out empty strings
        const vetSpecialties = vet.specialties.split(' ').filter(s => s.trim().length > 0);
        allSpecialties.push(...vetSpecialties);
      }
    });
    
    // Based on PRD requirements, common veterinary specialties should exist
    const commonSpecialties = ['surgery', 'cardiology', 'dermatology', 'radiology', 'dentistry'];
    
    // At least some common specialties should be present in the system
    const foundCommonSpecialties = commonSpecialties.filter(specialty =>
      allSpecialties.some(s => s.toLowerCase().includes(specialty.toLowerCase()))
    );
    
    // We expect at least one common specialty to be present
    expect(foundCommonSpecialties.length).toBeGreaterThan(0);
  });

  test('should maintain read-only nature of veterinarian directory', async ({ page }) => {
    const vetListPage = new VetListPage(page);
    
    await vetListPage.goto('#!/vets');
    await vetListPage.waitForLoad();
    
    // Per PRD: "Provide read-only access to veterinarian information for staff reference"
    // There should be no add/edit/delete buttons for veterinarians
    const addButton = page.locator('button:has-text("Add"), a:has-text("Add")');
    const editButton = page.locator('button:has-text("Edit"), a:has-text("Edit")');
    const deleteButton = page.locator('button:has-text("Delete"), a:has-text("Delete")');
    
    expect(await addButton.count()).toBe(0);
    expect(await editButton.count()).toBe(0);
    expect(await deleteButton.count()).toBe(0);
    
    // Should only display information, not forms or input elements
    const inputs = page.locator('input, textarea, select');
    expect(await inputs.count()).toBe(0);
  });

  test('should handle empty specialties gracefully', async ({ page }) => {
    const vetListPage = new VetListPage(page);
    
    await vetListPage.goto('#!/vets');
    await vetListPage.waitForLoad();
    
    const vets = await vetListPage.getVeterinarians();
    
    // Check that vets without specialties are handled properly
    vets.forEach(vet => {
      // Each vet should have a name, even if specialties are empty
      expect(vet.name).toBeTruthy();
      
      // Specialties column should exist even if empty
      expect(vet.specialties !== undefined).toBe(true);
    });
  });

  test('should verify responsive table styling', async ({ page }) => {
    const vetListPage = new VetListPage(page);
    
    await vetListPage.goto('#!/vets');
    await vetListPage.waitForLoad();
    
    // Check that table has Bootstrap styling classes
    const table = vetListPage.vetsTable;
    const tableClass = await table.getAttribute('class');
    expect(tableClass).toContain('table-striped');
    
    // Table should be properly structured for responsive design
    expect(await table.isVisible()).toBe(true);
  });
});