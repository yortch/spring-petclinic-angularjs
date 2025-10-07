package org.springframework.samples.petclinic.ui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.samples.petclinic.ui.pages.HomePage;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * UI tests for the PetClinic Home Page functionality.
 * Based on PRD requirements for user navigation and welcome interface.
 */
public class HomePageTest extends BaseUITest {

    @Test
    @DisplayName("Home page should display welcome message and navigation")
    public void shouldDisplayWelcomePageWithNavigation() {
        // Given: User navigates to the application
        navigateToHomePage();
        HomePage homePage = new HomePage(page);

        // When: Page loads
        // Then: Welcome message and navigation should be visible
        assertThat(homePage.isDisplayed()).isTrue();
        assertThat(homePage.getPageTitle()).contains("PetClinic");
        assertThat(homePage.isPetClinicImageDisplayed()).isTrue();
    }

    @Test
    @DisplayName("User should be able to navigate to Find Owners page")
    public void shouldNavigateToFindOwners() {
        // Given: User is on home page
        navigateToHomePage();
        HomePage homePage = new HomePage(page);

        // When: User clicks on Find Owners link
        homePage.clickFindOwners();

        // Then: Should navigate to owners page
        page.waitForURL("**/owners");
        assertThat(page.url()).contains("owners");
    }

    @Test
    @DisplayName("User should be able to navigate to Veterinarians page")
    public void shouldNavigateToVeterinarians() {
        // Given: User is on home page
        navigateToHomePage();
        HomePage homePage = new HomePage(page);

        // When: User clicks on Veterinarians link
        homePage.clickVeterinarians();

        // Then: Should navigate to vets page
        page.waitForURL("**/vets");
        assertThat(page.url()).contains("vets");
    }

    @Test
    @DisplayName("Application should be responsive and load within acceptable time")
    public void shouldLoadWithinAcceptableTime() {
        // Given: Application performance requirements from PRD
        long startTime = System.currentTimeMillis();

        // When: User navigates to home page
        navigateToHomePage();
        HomePage homePage = new HomePage(page);

        // Then: Page should load within 3 seconds (PRD requirement)
        long loadTime = System.currentTimeMillis() - startTime;
        assertThat(loadTime).isLessThan(3000);
        assertThat(homePage.isDisplayed()).isTrue();
    }
}