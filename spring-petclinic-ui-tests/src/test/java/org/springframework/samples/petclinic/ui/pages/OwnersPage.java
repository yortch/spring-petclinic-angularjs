package org.springframework.samples.petclinic.ui.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;

/**
 * Page Object Model for the Owners List Page
 */
public class OwnersPage {
    private final Page page;
    
    // Locators
    private final Locator ownersHeading;
    private final Locator searchFilter;
    private final Locator ownersTable;
    private final Locator ownerRows;
    private final Locator addOwnerButton;
    private final Locator noOwnersMessage;

    public OwnersPage(Page page) {
        this.page = page;
        this.ownersHeading = page.locator("h2:has-text('Owners')");
        this.searchFilter = page.locator("input[placeholder='Search Filter']");
        this.ownersTable = page.locator("table.table-striped");
        this.ownerRows = page.locator("table.table-striped tbody tr");
        this.addOwnerButton = page.locator("a:has-text('Add Owner')");
        this.noOwnersMessage = page.locator("text=No owners found");
    }

    public boolean isDisplayed() {
        return ownersHeading.isVisible();
    }

    public void searchOwners(String searchTerm) {
        searchFilter.fill(searchTerm);
    }

    public int getOwnerCount() {
        return ownerRows.count();
    }

    public boolean isOwnerDisplayed(String ownerName) {
        return page.locator("td a:has-text('" + ownerName + "')").isVisible();
    }

    public void clickOwner(String ownerName) {
        page.locator("td a:has-text('" + ownerName + "')").click();
    }

    public boolean isTableDisplayed() {
        return ownersTable.isVisible();
    }

    public String getFirstOwnerName() {
        return ownerRows.first().locator("td a").first().textContent();
    }

    public void clickAddOwner() {
        addOwnerButton.click();
    }

    public boolean hasNoOwnersMessage() {
        return noOwnersMessage.isVisible();
    }

    /**
     * Get owner information from a specific row
     */
    public OwnerInfo getOwnerInfo(int rowIndex) {
        Locator row = ownerRows.nth(rowIndex);
        String name = row.locator("td").nth(0).textContent().trim();
        String address = row.locator("td").nth(1).textContent().trim();
        String city = row.locator("td").nth(2).textContent().trim();
        String telephone = row.locator("td").nth(3).textContent().trim();
        String pets = row.locator("td").nth(4).textContent().trim();
        
        return new OwnerInfo(name, address, city, telephone, pets);
    }

    public static class OwnerInfo {
        public final String name;
        public final String address;
        public final String city;
        public final String telephone;
        public final String pets;

        public OwnerInfo(String name, String address, String city, String telephone, String pets) {
            this.name = name;
            this.address = address;
            this.city = city;
            this.telephone = telephone;
            this.pets = pets;
        }
    }
}