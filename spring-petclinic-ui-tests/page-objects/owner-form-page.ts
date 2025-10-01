import { Page, Locator } from '@playwright/test';
import { BasePage } from './base-page';

export class OwnerFormPage extends BasePage {
  readonly firstNameInput: Locator;
  readonly lastNameInput: Locator;
  readonly addressInput: Locator;
  readonly cityInput: Locator;
  readonly telephoneInput: Locator;
  readonly submitButton: Locator;
  readonly formErrors: Locator;

  constructor(page: Page) {
    super(page);
    this.firstNameInput = page.locator('input[ng-model="$ctrl.owner.firstName"]');
    this.lastNameInput = page.locator('input[ng-model="$ctrl.owner.lastName"]');
    this.addressInput = page.locator('input[ng-model="$ctrl.owner.address"]');
    this.cityInput = page.locator('input[ng-model="$ctrl.owner.city"]');
    this.telephoneInput = page.locator('input[ng-model="$ctrl.owner.telephone"]');
    this.submitButton = page.locator('button[type="submit"]');
    this.formErrors = page.locator('.help-block, .alert-danger');
  }

  async fillOwnerForm(owner: {
    firstName: string;
    lastName: string;
    address: string;
    city: string;
    telephone: string;
  }) {
    await this.firstNameInput.fill(owner.firstName);
    await this.lastNameInput.fill(owner.lastName);
    await this.addressInput.fill(owner.address);
    await this.cityInput.fill(owner.city);
    await this.telephoneInput.fill(owner.telephone);
  }

  async submitForm() {
    await this.submitButton.click();
  }

  async getValidationErrors() {
    return await this.formErrors.allTextContents();
  }

  async isFormVisible() {
    return await this.firstNameInput.isVisible();
  }
}