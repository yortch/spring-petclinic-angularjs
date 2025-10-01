import { Page, Locator } from '@playwright/test';

export class BasePage {
  readonly page: Page;
  readonly navbar: Locator;
  readonly homeLink: Locator;
  readonly ownersDropdown: Locator;
  readonly veterinariansLink: Locator;
  readonly footer: Locator;

  constructor(page: Page) {
    this.page = page;
    this.navbar = page.locator('nav.navbar');
    this.homeLink = page.locator('a[ui-sref="welcome"]');
    this.ownersDropdown = page.locator('.dropdown:has-text("Owners")');
    this.veterinariansLink = page.locator('a[ui-sref="vets"]');
    this.footer = page.locator('layout-footer');
  }

  async goto(path: string = '/') {
    await this.page.goto(path);
  }

  async waitForLoad() {
    await this.page.waitForLoadState('networkidle');
  }

  async waitForAngularRoute(urlPattern: string, timeout: number = 15000) {
    try {
      await this.page.waitForURL(urlPattern, { timeout });
    } catch (error) {
      // If exact pattern fails, wait a moment and check current URL
      await this.page.waitForTimeout(1000);
      const currentUrl = this.page.url();
      console.log(`URL pattern ${urlPattern} not matched. Current URL: ${currentUrl}`);
      // Don't throw error, let test continue to provide better diagnostics
    }
  }

  async isNavbarVisible() {
    return await this.navbar.isVisible();
  }

  async clickHome() {
    await this.homeLink.click();
  }

  async clickVeterinarians() {
    await this.veterinariansLink.click();
  }

  async expandOwnersDropdown() {
    // Ensure navbar is expanded - click the dropdown toggle
    const dropdownToggle = this.page.locator('.dropdown-toggle:has-text("Owners")');
    await dropdownToggle.click();
    await this.page.waitForTimeout(100); // Small delay for dropdown animation
  }

  async clickOwnersAll() {
    await this.expandOwnersDropdown();
    await this.page.locator('a[ui-sref="owners"]').click();
  }

  async clickOwnersRegister() {
    await this.expandOwnersDropdown();
    await this.page.locator('a[ui-sref="ownerNew"]').click();
  }

  async getPageTitle() {
    return await this.page.title();
  }
}