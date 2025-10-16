package org.springframework.samples.petclinic.ui.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;

/**
 * Page Object Model for the PetClinic Home Page
 */
public class HomePage {
    private final Page page;
    
    // Locators
    private final Locator welcomeHeading;
    private final Locator petClinicImage;
    private final Locator findOwnersLink;
    private final Locator veterinariansLink;
    private final Locator errorMessage;

    public HomePage(Page page) {
        this.page = page;
        this.welcomeHeading = page.locator("h1:has-text('Welcome to Petclinic')");
        this.petClinicImage = page.locator("img[alt='pets logo']");
        this.findOwnersLink = page.locator("a[href*='owners']");
        this.veterinariansLink = page.locator("a[href*='vets']");
        this.errorMessage = page.locator(".alert-danger");
    }

    public boolean isDisplayed() {
        return welcomeHeading.isVisible();
    }

    public boolean isPetClinicImageDisplayed() {
        return petClinicImage.isVisible();
    }

    public void clickFindOwners() {
        findOwnersLink.first().click();
    }

    public void clickVeterinarians() {
        veterinariansLink.first().click();
    }

    public String getPageTitle() {
        return page.title();
    }

    public boolean hasErrorMessage() {
        return errorMessage.isVisible();
    }

    public String getErrorMessage() {
        return errorMessage.textContent();
    }
}