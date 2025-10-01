import { Page, Locator } from '@playwright/test';
import { BasePage } from './base-page';

export class WelcomePage extends BasePage {
  readonly welcomeHeading: Locator;
  readonly petsImage: Locator;

  constructor(page: Page) {
    super(page);
    this.welcomeHeading = page.locator('h1:has-text("Welcome to Petclinic")');
    this.petsImage = page.locator('img[alt="pets logo"]');
  }

  async isWelcomeVisible() {
    return await this.welcomeHeading.isVisible();
  }

  async isPetsImageVisible() {
    return await this.petsImage.isVisible();
  }
}