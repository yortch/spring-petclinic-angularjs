import { Page, Locator } from '@playwright/test';
import { BasePage } from './base-page';

export class VetListPage extends BasePage {
  readonly vetsHeading: Locator;
  readonly vetsTable: Locator;
  readonly nameColumn: Locator;
  readonly specialtiesColumn: Locator;

  constructor(page: Page) {
    super(page);
    this.vetsHeading = page.locator('h2:has-text("Veterinarians")');
    this.vetsTable = page.locator('table.table-striped');
    this.nameColumn = page.locator('th:has-text("Name")');
    this.specialtiesColumn = page.locator('th:has-text("Specialties")');
  }

  async getVeterinarianRows() {
    return this.vetsTable.locator('tbody tr');
  }

  async getVeterinarians() {
    const vets = [];
    const vetRows = await this.getVeterinarianRows();
    const count = await vetRows.count();
    
    for (let i = 0; i < count; i++) {
      const row = vetRows.nth(i);
      const cells = row.locator('td');
      
      const name = await cells.nth(0).textContent();
      const specialties = await cells.nth(1).textContent();
      
      vets.push({
        name: name?.trim(),
        specialties: specialties?.trim()
      });
    }
    
    return vets;
  }

  async isVetVisible(firstName: string, lastName: string) {
    const vetRow = this.page.locator(`tr:has-text("${firstName} ${lastName}")`);
    return await vetRow.isVisible();
  }

  async getVetSpecialties(firstName: string, lastName: string) {
    const vetRow = this.page.locator(`tr:has-text("${firstName} ${lastName}")`);
    const specialtiesCell = vetRow.locator('td').nth(1);
    return await specialtiesCell.textContent();
  }

  async isTableHeaderVisible() {
    return await this.nameColumn.isVisible() && await this.specialtiesColumn.isVisible();
  }
}