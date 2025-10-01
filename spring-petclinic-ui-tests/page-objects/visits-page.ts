import { Page, Locator } from '@playwright/test';
import { BasePage } from './base-page';

export class VisitsPage extends BasePage {
  readonly visitsHeading: Locator;
  readonly addVisitButton: Locator;
  readonly visitForm: Locator;
  readonly visitDateInput: Locator;
  readonly visitDescriptionTextarea: Locator;
  readonly submitVisitButton: Locator;
  readonly visitsTable: Locator;
  readonly petInfo: Locator;

  constructor(page: Page) {
    super(page);
    this.visitsHeading = page.locator('h2');
    this.addVisitButton = page.locator('button:has-text("Add New Visit"), button:has-text("Add Visit"), a:has-text("Add Visit")');
    this.visitForm = page.locator('form');
    this.visitDateInput = page.locator('input[name="date"], input[type="date"]');
    this.visitDescriptionTextarea = page.locator('textarea[ng-model="$ctrl.desc"], textarea[name="description"]');
    this.submitVisitButton = page.locator('button[type="submit"]');
    this.visitsTable = page.locator('table');
    this.petInfo = page.locator('.table-condensed, .pet-info');
  }

  async addNewVisit(visit: {
    date: string;
    description: string;
  }) {
    await this.addVisitButton.click();
    await this.visitDateInput.fill(visit.date);
    await this.visitDescriptionTextarea.fill(visit.description);
    await this.submitVisitButton.click();
  }

  async getVisitHistory() {
    const visits = [];
    const visitRows = this.visitsTable.locator('tbody tr');
    const count = await visitRows.count();
    
    for (let i = 0; i < count; i++) {
      const row = visitRows.nth(i);
      const cells = row.locator('td');
      const cellCount = await cells.count();
      
      if (cellCount >= 2) {
        const date = await cells.nth(0).textContent();
        const description = await cells.nth(1).textContent();
        visits.push({
          date: date?.trim(),
          description: description?.trim()
        });
      }
    }
    
    return visits;
  }

  async getPetInformation() {
    const petDetails: { [key: string]: string } = {};
    const rows = this.petInfo.locator('tr');
    const count = await rows.count();
    
    for (let i = 0; i < count; i++) {
      const row = rows.nth(i);
      const cells = row.locator('td');
      const cellCount = await cells.count();
      
      if (cellCount >= 2) {
        const key = await cells.nth(0).textContent();
        const value = await cells.nth(1).textContent();
        if (key && value) {
          petDetails[key.trim()] = value.trim();
        }
      }
    }
    
    return petDetails;
  }

  async isVisitFormVisible() {
    return await this.visitForm.isVisible();
  }

  async isAddVisitButtonVisible() {
    return await this.addVisitButton.isVisible();
  }

  async getPageHeading() {
    return await this.visitsHeading.textContent();
  }
}