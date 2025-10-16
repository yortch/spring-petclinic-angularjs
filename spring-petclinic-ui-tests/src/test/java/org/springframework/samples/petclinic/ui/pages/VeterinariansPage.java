package org.springframework.samples.petclinic.ui.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;

/**
 * Page Object Model for the Veterinarians Page
 */
public class VeterinariansPage {
    private final Page page;
    
    // Locators
    private final Locator vetsHeading;
    private final Locator vetsTable;
    private final Locator vetRows;

    public VeterinariansPage(Page page) {
        this.page = page;
        this.vetsHeading = page.locator("h2:has-text('Veterinarians')");
        this.vetsTable = page.locator("table.table-striped");
        this.vetRows = page.locator("table.table-striped tbody tr");
    }

    public boolean isDisplayed() {
        return vetsHeading.isVisible();
    }

    public int getVetCount() {
        return vetRows.count();
    }

    public boolean isVetDisplayed(String vetName) {
        return page.locator("td:has-text('" + vetName + "')").isVisible();
    }

    public boolean isTableDisplayed() {
        return vetsTable.isVisible();
    }

    /**
     * Get veterinarian information from a specific row
     */
    public VetInfo getVetInfo(int rowIndex) {
        Locator row = vetRows.nth(rowIndex);
        String name = row.locator("td").nth(0).textContent().trim();
        String specialties = row.locator("td").nth(1).textContent().trim();
        
        return new VetInfo(name, specialties);
    }

    public static class VetInfo {
        public final String name;
        public final String specialties;

        public VetInfo(String name, String specialties) {
            this.name = name;
            this.specialties = specialties;
        }
    }
}