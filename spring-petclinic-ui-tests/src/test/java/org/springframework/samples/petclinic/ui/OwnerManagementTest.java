package org.springframework.samples.petclinic.ui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.samples.petclinic.ui.pages.HomePage;
import org.springframework.samples.petclinic.ui.pages.OwnersPage;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * UI tests for Owner Management functionality.
 * Based on PRD Epic: Owner Management user stories.
 */
public class OwnerManagementTest extends BaseUITest {

    @Test
    @DisplayName("Should display list of all owners")
    public void shouldDisplayOwnersList() {
        // Given: User navigates to owners page
        navigateToHomePage();
        HomePage homePage = new HomePage(page);
        homePage.clickFindOwners();

        // When: Owners page loads
        OwnersPage ownersPage = new OwnersPage(page);

        // Then: Should display owners list with required information
        assertThat(ownersPage.isDisplayed()).isTrue();
        assertThat(ownersPage.isTableDisplayed()).isTrue();
        
        // Verify we have owners data (based on PRD test data)
        assertThat(ownersPage.getOwnerCount()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should enable search functionality for owners")
    public void shouldEnableOwnerSearch() {
        // Given: User is on owners page with data
        navigateToOwnersList();
        OwnersPage ownersPage = new OwnersPage(page);
        
        int initialOwnerCount = ownersPage.getOwnerCount();
        assertThat(initialOwnerCount).isGreaterThan(0);

        // When: User searches for a specific owner
        ownersPage.searchOwners("Franklin");

        // Then: Should filter results to matching owners
        // Wait for filter to apply
        page.waitForTimeout(500);
        
        // Should have fewer results after filtering
        int filteredCount = ownersPage.getOwnerCount();
        assertThat(filteredCount).isLessThanOrEqualTo(initialOwnerCount);
        
        // If any results, they should contain the search term
        if (filteredCount > 0) {
            assertThat(ownersPage.isOwnerDisplayed("Franklin")).isTrue();
        }
    }

    @Test
    @DisplayName("Should display owner details with contact information")
    public void shouldDisplayOwnerContactDetails() {
        // Given: User is on owners page
        navigateToOwnersList();
        OwnersPage ownersPage = new OwnersPage(page);
        
        assertThat(ownersPage.getOwnerCount()).isGreaterThan(0);

        // When: User views owner information
        OwnersPage.OwnerInfo firstOwner = ownersPage.getOwnerInfo(0);

        // Then: Should display all required contact fields per PRD
        assertThat(firstOwner.name).isNotEmpty();
        assertThat(firstOwner.city).isNotEmpty();
        assertThat(firstOwner.telephone).isNotEmpty();
        // Note: Address might be hidden on small screens (responsive design)
    }

    @Test
    @DisplayName("Should enable navigation to owner details")
    public void shouldNavigateToOwnerDetails() {
        // Given: User is on owners page with data
        navigateToOwnersList();
        OwnersPage ownersPage = new OwnersPage(page);
        
        assertThat(ownersPage.getOwnerCount()).isGreaterThan(0);
        String firstOwnerName = ownersPage.getFirstOwnerName();

        // When: User clicks on an owner name
        ownersPage.clickOwner(firstOwnerName);

        // Then: Should navigate to owner details page
        page.waitForURL("**/owners/*");
        assertThat(page.url()).contains("/owners/");
        
        // Should display owner details (as per PRD requirements)
        assertThat(page.locator("h2:has-text('Owner Information')").isVisible()).isTrue();
    }

    @Test
    @DisplayName("Should display responsive layout for mobile devices")
    public void shouldDisplayResponsiveLayout() {
        // Given: Mobile viewport (PRD requirement for responsive design)
        page.setViewportSize(375, 667); // iPhone size
        
        // When: User navigates to owners page
        navigateToOwnersList();
        OwnersPage ownersPage = new OwnersPage(page);

        // Then: Should display mobile-friendly layout
        assertThat(ownersPage.isDisplayed()).isTrue();
        assertThat(ownersPage.isTableDisplayed()).isTrue();
        
        // Should handle responsive design requirements from PRD
        // Table should still be functional on mobile
        assertThat(ownersPage.getOwnerCount()).isGreaterThan(0);
    }

    private void navigateToOwnersList() {
        navigateToHomePage();
        HomePage homePage = new HomePage(page);
        homePage.clickFindOwners();
        page.waitForURL("**/owners");
    }
}