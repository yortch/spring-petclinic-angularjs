import { Page, Locator } from '@playwright/test';
import { BasePage } from './base-page';

export class OwnerListPage extends BasePage {
  readonly ownersHeading: Locator;
  readonly searchInput: Locator;
  readonly ownersTable: Locator;
  readonly addOwnerButton: Locator;

  constructor(page: Page) {
    super(page);
    this.ownersHeading = page.locator('h2:has-text("Owners")');
    this.searchInput = page.locator('input[placeholder="Search Filter"]');
    this.ownersTable = page.locator('table.table-striped');
    this.addOwnerButton = page.locator('a[ui-sref="ownerNew"]');
  }

  async searchOwner(searchTerm: string) {
    await this.searchInput.fill(searchTerm);
  }

  async getOwnerRows() {
    return this.ownersTable.locator('tbody tr');
  }

  async clickOwnerByName(firstName: string, lastName: string) {
    const ownerLink = this.page.locator(`a:has-text("${firstName} ${lastName}")`);
    await ownerLink.click();
  }

  async isOwnerVisible(firstName: string, lastName: string) {
    const ownerRow = this.page.locator(`tr:has-text("${firstName} ${lastName}")`);
    return await ownerRow.isVisible();
  }
}