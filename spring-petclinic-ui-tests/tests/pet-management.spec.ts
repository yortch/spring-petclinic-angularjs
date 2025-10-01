import { test, expect } from '@playwright/test';
import { OwnerListPage } from '../page-objects/owner-list-page';
import { OwnerDetailsPage } from '../page-objects/owner-details-page';
import { PetFormPage } from '../page-objects/pet-form-page';

test.describe('Pet Management Tests', () => {
  test('should navigate to add new pet form from owner details', async ({ page }) => {
    const ownerListPage = new OwnerListPage(page);
    const ownerDetailsPage = new OwnerDetailsPage(page);
    const petFormPage = new PetFormPage(page);
    
    // Navigate to owners list and select first owner
    await ownerListPage.goto('#!/owners');
    await ownerListPage.waitForLoad();
    
    const firstOwnerLink = page.locator('tbody tr:first-child a').first();
    await firstOwnerLink.click();
    await page.waitForURL('**/#!/owners/details/*');
    await page.waitForLoadState('networkidle');
    
    // Click "Add New Pet" button
    await ownerDetailsPage.clickAddPet();
    
    // Should navigate to new pet form
    await page.waitForURL('**/#!/owners/*/new-pet');
    expect(page.url()).toContain('/new-pet');
    
    // Check form elements are visible
    expect(await petFormPage.isFormVisible()).toBe(true);
    expect(await petFormPage.getHeadingText()).toContain('Pet');
  });

  test('should display available pet types in dropdown', async ({ page }) => {
    const ownerListPage = new OwnerListPage(page);
    const ownerDetailsPage = new OwnerDetailsPage(page);
    const petFormPage = new PetFormPage(page);
    
    // Navigate to new pet form via owner details
    await ownerListPage.goto('#!/owners');
    await ownerListPage.waitForLoad();
    
    const firstOwnerLink = page.locator('tbody tr:first-child a').first();
    await firstOwnerLink.click();
    await ownerDetailsPage.clickAddPet();
    
    await page.waitForURL('**/#!/owners/*/new-pet');
    
    // Check pet types are available
    const petTypes = await petFormPage.getPetTypes();
    expect(petTypes.length).toBeGreaterThan(0);
    
    // Expected pet types based on the PRD
    const expectedTypes = ['dog', 'cat', 'bird', 'hamster', 'snake', 'lizard'];
    const typesLower = petTypes.map(type => type.toLowerCase());
    
    expectedTypes.forEach(expectedType => {
      expect(typesLower).toContain(expectedType);
    });
  });

  test('should create new pet successfully', async ({ page }) => {
    const ownerListPage = new OwnerListPage(page);
    const ownerDetailsPage = new OwnerDetailsPage(page);
    const petFormPage = new PetFormPage(page);
    
    // Navigate to new pet form
    await ownerListPage.goto('#!/owners');
    await ownerListPage.waitForLoad();
    
    const firstOwnerLink = page.locator('tbody tr:first-child a').first();
    await firstOwnerLink.click();
    
    // Get initial pets count
    const initialPets = await ownerDetailsPage.getPetsList();
    const initialCount = initialPets.length;
    console.log(`Initial pets count: ${initialCount}, pets: ${JSON.stringify(initialPets.map(p => p.name))}`);

    await ownerDetailsPage.clickAddPet();
    await page.waitForURL('**/#!/owners/*/new-pet');

    // Fill pet form
    const testPet = {
      name: `TestPet${Date.now()}`,
      birthDate: '2023-01-15',
      type: 'dog'
    };

    await petFormPage.fillPetForm(testPet);
    
    // Check for any validation errors before submitting
    const errors = await petFormPage.getValidationErrors();
    console.log('Pre-submission validation errors:', errors);
    
    await petFormPage.submitForm();

    // Wait for navigation with more time and check for errors
    try {
      await page.waitForURL('**/#!/owners/details/*', { timeout: 15000 });
    } catch (error) {
      console.log('Navigation timeout, checking for validation errors...');
      const postErrors = await petFormPage.getValidationErrors();
      console.log('Post-submission validation errors:', postErrors);
      throw error;
    }
    
    // Should return to owner details
    expect(page.url()).not.toContain('/new-pet');

    // Verify pet was added
    const currentPets = await ownerDetailsPage.getPetsList();
    console.log(`Final pets count: ${currentPets.length}, pets: ${JSON.stringify(currentPets.map(p => p.name))}`);
    expect(currentPets.length).toBeGreaterThanOrEqual(initialCount + 1);

    // Check if our test pet is in the list
    const petNames = currentPets.map(pet => pet.name);
    expect(petNames).toContain(testPet.name);
  });

  test('should validate required fields in pet form', async ({ page }) => {
    const ownerListPage = new OwnerListPage(page);
    const ownerDetailsPage = new OwnerDetailsPage(page);
    const petFormPage = new PetFormPage(page);
    
    // Navigate to new pet form
    await ownerListPage.goto('#!/owners');
    await ownerListPage.waitForLoad();
    
    const firstOwnerLink = page.locator('tbody tr:first-child a').first();
    await firstOwnerLink.click();
    await ownerDetailsPage.clickAddPet();
    
    await page.waitForURL('**/#!/owners/*/new-pet');
    
    // Try to submit empty form
    await petFormPage.submitForm(false);  // Use HTML5 validation
    
    // Should stay on the same page (form validation should prevent submission)
    const currentUrl = page.url();
    expect(currentUrl).toContain('/new-pet');
  });

  test('should edit existing pet', async ({ page }) => {
    const ownerListPage = new OwnerListPage(page);
    const ownerDetailsPage = new OwnerDetailsPage(page);
    const petFormPage = new PetFormPage(page);
    
    // Navigate to owner details
    await ownerListPage.goto('#!/owners');
    await ownerListPage.waitForLoad();
    
    const firstOwnerLink = page.locator('tbody tr:first-child a').first();
    await firstOwnerLink.click();
    
    // Get pets list and click on first pet
    const pets = await ownerDetailsPage.getPetsList();
    
    if (pets.length > 0) {
      const firstPet = pets[0];
      await ownerDetailsPage.clickPet(firstPet.name || '');
      
      // Should navigate to pet edit form
      await page.waitForURL('**/#!/owners/*/pets/*');
      expect(page.url()).toContain('/pets/');
      
      // Form should be pre-filled
      expect(await petFormPage.isFormVisible()).toBe(true);
      expect(await petFormPage.getHeadingText()).toContain('Pet');
      
      // Modify pet name
      const currentName = await petFormPage.petNameInput.inputValue();
      const newName = currentName + '-Updated';
      await petFormPage.petNameInput.fill(newName);
      
      // Submit form
      await petFormPage.submitForm();
      
      // Should return to owner details
      await page.waitForURL('**/#!/owners/details/*');
      expect(page.url()).not.toContain('/pets/');
      
      // Verify the change was saved
      const updatedPets = await ownerDetailsPage.getPetsList();
      const petNames = updatedPets.map(pet => pet.name);
      expect(petNames).toContain(newName);
    } else {
      // If no pets exist, create one first
      await ownerDetailsPage.clickAddPet();
      await page.waitForURL('**/#!/owners/*/new-pet');
      
      const testPet = {
        name: 'TestEditPet',
        birthDate: '2023-01-01',
        type: 'cat'
      };
      
      await petFormPage.fillPetForm(testPet);
      await petFormPage.submitForm();
      
      // Wait for navigation back to owner details (should be at #!/owners/details/{id})
      await page.waitForURL('**/#!/owners/details/*', { timeout: 10000 });
      await ownerDetailsPage.clickPet(testPet.name);
      
      await page.waitForURL('**/#!/owners/*/pets/*');
      
      // Edit the pet using the form page method
      const editedPet = {
        name: testPet.name + '-Edited',
        birthDate: '2023-01-01',  // Keep same date
        type: 'cat'  // Keep same type
      };
      await petFormPage.fillPetForm(editedPet);
      await petFormPage.submitForm();
      
      // Verify edit was successful - wait for return to owner details
      await page.waitForURL('**/#!/owners/details/*', { timeout: 10000 });
      const pets = await ownerDetailsPage.getPetsList();
      const petNames = pets.map(pet => pet.name);
      expect(petNames).toContain(testPet.name + '-Edited');
    }
  });

  test('should associate pet with correct owner', async ({ page }) => {
    const ownerListPage = new OwnerListPage(page);
    const ownerDetailsPage = new OwnerDetailsPage(page);
    const petFormPage = new PetFormPage(page);
    
    // Navigate to specific owner
    await ownerListPage.goto('#!/owners');
    await ownerListPage.waitForLoad();
    
    const firstOwnerLink = page.locator('tbody tr:first-child a').first();
    await firstOwnerLink.click();
    
    // Get owner name for verification
    const ownerName = await ownerDetailsPage.getOwnerName();
    
    // Add a new pet
    await ownerDetailsPage.clickAddPet();
    await page.waitForURL('**/#!/owners/*/new-pet');
    
    const testPet = {
      name: `OwnerAssocTest${Date.now()}`,
      birthDate: '2023-06-01',
      type: 'bird'
    };
    
    await petFormPage.fillPetForm(testPet);
    await petFormPage.submitForm();
    
    // Should return to same owner's details page
    await page.waitForURL('**/#!/owners/details/*');
    
    // Verify we're still on the same owner's page
    const currentOwnerName = await ownerDetailsPage.getOwnerName();
    expect(currentOwnerName).toBe(ownerName);
    
    // Verify pet is associated with this owner
    const pets = await ownerDetailsPage.getPetsList();
    const petNames = pets.map(pet => pet.name);
    expect(petNames).toContain(testPet.name);
  });

  test('should handle navigation back from pet form', async ({ page }) => {
    const ownerListPage = new OwnerListPage(page);
    const ownerDetailsPage = new OwnerDetailsPage(page);
    const petFormPage = new PetFormPage(page);
    
    // Navigate to owner details and then to pet form
    await ownerListPage.goto('#!/owners');
    await ownerListPage.waitForLoad();
    
    const firstOwnerLink = page.locator('tbody tr:first-child a').first();
    await firstOwnerLink.click();
    
    // Get current URL to verify we return to the same place
    const ownerDetailsUrl = page.url();
    
    await ownerDetailsPage.clickAddPet();
    await page.waitForURL('**/#!/owners/*/new-pet');
    
    // Fill form partially
    await petFormPage.petNameInput.fill('NavigationTest');
    
    // Navigate back using browser back button
    await page.goBack();
    
    // Should return to owner details
    await page.waitForURL('**/#!/owners/details/*');
    expect(page.url()).toContain('#!/owners/details/');
    expect(await ownerDetailsPage.ownerInfo.isVisible()).toBe(true);
  });
});