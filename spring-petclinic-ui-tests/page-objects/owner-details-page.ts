import { Page, Locator } from '@playwright/test';
import { BasePage } from './base-page';

export class OwnerDetailsPage extends BasePage {
  readonly ownerInfo: Locator;
  readonly editOwnerButton: Locator;
  readonly addPetButton: Locator;
  readonly petsTable: Locator;

  constructor(page: Page) {
    super(page);
    this.ownerInfo = page.locator('h2:has-text("Owner Information")').locator('..').locator('table.table-striped').first();
    this.editOwnerButton = page.locator('a:has-text("Edit Owner")');
    this.addPetButton = page.locator('a:has-text("Add New Pet")');
    this.petsTable = page.locator('h2:has-text("Pets and Visits")').locator('..').locator('table');
  }

  async getOwnerName() {
    // The owner name is in the first table row, second column
    // Try the most specific selector for owner information table
    const ownerNameCell = this.page.locator('h2:has-text("Owner Information") + table.table-striped tr:has-text("Name") td').nth(1);
    if (await ownerNameCell.count() > 0) {
      const nameValue = await ownerNameCell.textContent();
      if (nameValue && nameValue.trim() !== '') {
        return nameValue.trim();
      }
    }
    
    // Fallback: try using the b tag
    const nameCell = this.page.locator('h2:has-text("Owner Information") + table.table-striped tr:has-text("Name") td b');
    if (await nameCell.count() > 0) {
      const nameValue = await nameCell.textContent();
      if (nameValue && nameValue.trim() !== '') {
        return nameValue.trim();
      }
    }
    
    // Last resort: use the getOwnerDetails method
    const details = await this.getOwnerDetails();
    if (details['Name']) {
      return details['Name'];
    }
    
    // Fallback: return empty string
    return '';
  }

  async getOwnerDetails() {
    const details: { [key: string]: string } = {};
    // Only parse the first table (owner information), not the pets/visits table
    const ownerTable = this.page.locator('h2:has-text("Owner Information")').locator('..').locator('table.table-striped').first();
    const rows = ownerTable.locator('tr');
    const count = await rows.count();
    
    for (let i = 0; i < count; i++) {
      const row = rows.nth(i);
      const th = row.locator('th');
      const td = row.locator('td');
      
      if (await th.count() > 0 && await td.count() > 0) {
        const thCount = await th.count();
        const tdCount = await td.count();
        
        // Only process rows with exactly one th and at least one td
        if (thCount === 1 && tdCount >= 1) {
          const key = await th.first().textContent();
          const value = await td.first().textContent();
          if (key && value && !value.includes('Edit Owner') && !value.includes('Add New Pet')) {
            details[key.trim()] = value.trim();
          }
        }
      }
    }
    
    return details;
  }

  async clickEditOwner() {
    await this.editOwnerButton.click();
  }

  async clickAddPet() {
    await this.addPetButton.click();
  }

  async getPetsList() {
    const pets = [];
    try {
      // Look for pets in the second table (under "Pets and Visits" heading)
      // Each pet is in a table row with a definition list (dl.dl-horizontal)
      const petRows = this.page.locator('h2:has-text("Pets and Visits")').locator('..').locator('table tr');
      const count = await petRows.count();
      
      for (let i = 0; i < count; i++) {
        const row = petRows.nth(i);
        
        // Look for definition list in this row
        const dl = row.locator('dl.dl-horizontal');
        if (await dl.count() > 0) {
          // Get pet name from the link in dd element following dt:has-text("Name")
          const nameElement = dl.locator('dt:has-text("Name")').locator('..').locator('dd').first();
          // Get pet type from dd element following dt:has-text("Type")  
          const typeElement = dl.locator('dt:has-text("Type")').locator('..').locator('dd').last();
          
          if (await nameElement.count() > 0 && await typeElement.count() > 0) {
            const petName = await nameElement.textContent();
            const petType = await typeElement.textContent();
            if (petName && petType) {
              pets.push({ name: petName.trim(), type: petType.trim() });
            }
          }
        }
      }
    } catch (error) {
      console.log('Error reading pets list:', error);
    }
    
    return pets;
  }

  async clickPet(petName: string) {
    const petLink = this.page.locator(`a:has-text("${petName}")`);
    await petLink.click();
  }
}