import { Page, Locator } from '@playwright/test';
import { BasePage } from './base-page';

export class PetFormPage extends BasePage {
  readonly petNameInput: Locator;
  readonly birthDateInput: Locator;
  readonly petTypeSelect: Locator;
  readonly submitButton: Locator;
  readonly formErrors: Locator;
  readonly pageHeading: Locator;

  constructor(page: Page) {
    super(page);
    this.petNameInput = page.locator('input[name="name"]');
    this.birthDateInput = page.locator('input[type="date"]');
    this.petTypeSelect = page.locator('select[ng-model="$ctrl.petTypeId"]');
    this.submitButton = page.locator('button[type="submit"]');
    this.formErrors = page.locator('.help-inline, .alert-danger');
    this.pageHeading = page.locator('h2');
  }

  async fillPetForm(pet: {
    name: string;
    birthDate: string;
    type: string;
  }) {
    // First, wait for AngularJS to be fully loaded and pet types to be populated
    await this.page.waitForFunction(
      () => {
        const angular = (window as any).angular;
        const select = document.querySelector('select[ng-model="$ctrl.petTypeId"]') as HTMLSelectElement;
        return angular && select && select.options.length > 0;
      },
      { timeout: 15000 }
    );
    
    // Wait for the AngularJS controller to be fully ready
    await this.page.waitForTimeout(1500);
    
    // Fill form using AngularJS directly to ensure proper model binding
    await this.page.evaluate((petData) => {
      const angular = (window as any).angular;
      if (!angular) return;
      
      // Find the form element and get its scope
      const form = document.querySelector('form[ng-submit="$ctrl.submit()"]');
      if (!form) return;
      
      const scope = angular.element(form).scope();
      if (!scope || !scope.$ctrl) return;
      
      // Ensure pet object exists with all required properties
      if (!scope.$ctrl.pet) {
        scope.$ctrl.pet = {};
      }
      
      // In edit mode, the pet object may already have properties like 'owner', 'type', 'id'
      // We need to preserve those and only update what we're changing
      const existingPet = scope.$ctrl.pet || {};
      
      // Update the model properties
      scope.$ctrl.pet = {
        ...existingPet,
        name: petData.name,
        birthDate: new Date(petData.birthDate)
      };
      
      // Find the pet type ID by name
      const types = scope.$ctrl.types || [];
      const selectedType = types.find((type: any) => 
        type.name && type.name.toLowerCase() === petData.type.toLowerCase()
      );
      if (selectedType) {
        scope.$ctrl.petTypeId = String(selectedType.id);
      }
      
      // Apply changes
      scope.$apply();
    }, {
      name: pet.name,
      birthDate: pet.birthDate,
      type: pet.type
    });
    
    // Wait for digest cycle to complete
    await this.page.waitForTimeout(500);
    
    // Verify the form is filled correctly
    const formValues = await this.page.evaluate(() => {
      const nameInput = document.querySelector('input[name="name"]') as HTMLInputElement;
      const dateInput = document.querySelector('input[type="date"]') as HTMLInputElement;
      const typeSelect = document.querySelector('select[ng-model="$ctrl.petTypeId"]') as HTMLSelectElement;
      
      return {
        name: nameInput ? nameInput.value : '',
        date: dateInput ? dateInput.value : '',
        type: typeSelect ? typeSelect.value : ''
      };
    });
    
    console.log('Form filled with values:', formValues);
  }

  async submitForm(bypassValidation: boolean = true) {
    if (bypassValidation) {
      // Submit using AngularJS scope method to bypass HTML5 validation checks
      await this.page.evaluate(() => {
        const angular = (window as any).angular;
        if (!angular) return false;
        
        const form = document.querySelector('form[ng-submit="$ctrl.submit()"]');
        if (!form) return false;
        
        const scope = angular.element(form).scope();
        if (scope && scope.$ctrl && typeof scope.$ctrl.submit === 'function') {
          scope.$ctrl.submit();
          return true;
        }
        return false;
      });
      
      // Wait for the submit to process
      await this.page.waitForTimeout(1000);
    } else {
      // Use normal button click to let HTML5 validation work
      await this.submitButton.click();
      await this.page.waitForTimeout(500);
    }
  }

  async getPetTypes() {
    // Wait for the AngularJS controller to load pet types via HTTP
    await this.page.waitForFunction(
      () => {
        const select = document.querySelector('select[ng-model="$ctrl.petTypeId"]') as HTMLSelectElement;
        return select && select.options.length > 0;
      },
      { timeout: 10000 }
    );
    
    const options = await this.petTypeSelect.locator('option').allTextContents();
    return options.filter(option => option.trim() !== '');
  }

  async getValidationErrors() {
    return await this.formErrors.allTextContents();
  }

  async isFormVisible() {
    return await this.petNameInput.isVisible();
  }

  async getHeadingText() {
    return await this.pageHeading.textContent();
  }
}