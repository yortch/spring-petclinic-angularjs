package org.springframework.samples.petclinic.ui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.samples.petclinic.ui.pages.HomePage;
import org.springframework.samples.petclinic.ui.pages.VeterinariansPage;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * UI tests for Veterinarian Management functionality.
 * Based on PRD Epic: Clinic Administration user stories.
 */
public class VeterinarianManagementTest extends BaseUITest {

    @Test
    @DisplayName("Should display list of all veterinarians")
    public void shouldDisplayVeterinariansList() {
        // Given: User navigates to veterinarians page
        navigateToHomePage();
        HomePage homePage = new HomePage(page);
        homePage.clickVeterinarians();

        // When: Veterinarians page loads
        VeterinariansPage vetsPage = new VeterinariansPage(page);

        // Then: Should display veterinarians list as per PRD requirements
        assertThat(vetsPage.isDisplayed()).isTrue();
        assertThat(vetsPage.isTableDisplayed()).isTrue();
        
        // Verify we have veterinarians data (based on PRD test data)
        assertThat(vetsPage.getVetCount()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should display veterinarian names and specialties")
    public void shouldDisplayVeterinarianDetails() {
        // Given: User is on veterinarians page
        navigateToVeterinariansPage();
        VeterinariansPage vetsPage = new VeterinariansPage(page);
        
        assertThat(vetsPage.getVetCount()).isGreaterThan(0);

        // When: User views veterinarian information
        VeterinariansPage.VetInfo firstVet = vetsPage.getVetInfo(0);

        // Then: Should display all required fields per PRD
        assertThat(firstVet.name).isNotEmpty();
        // Specialties may be empty for general practitioners
        assertThat(firstVet.specialties).isNotNull();
    }

    @Test
    @DisplayName("Should display veterinarians with multiple specialties")
    public void shouldDisplayVeterinariansWithSpecialties() {
        // Given: User is on veterinarians page
        navigateToVeterinariansPage();
        VeterinariansPage vetsPage = new VeterinariansPage(page);

        // When: Looking for vets with specialties (based on PRD test data)
        // Then: Should find veterinarians with specialties like "Douglas" with dentistry and surgery
        boolean foundVetWithSpecialties = false;
        for (int i = 0; i < vetsPage.getVetCount(); i++) {
            VeterinariansPage.VetInfo vet = vetsPage.getVetInfo(i);
            if (!vet.specialties.trim().isEmpty()) {
                foundVetWithSpecialties = true;
                break;
            }
        }
        assertThat(foundVetWithSpecialties).isTrue();
    }

    @Test
    @DisplayName("Should handle veterinarians with no specialties")
    public void shouldHandleVeterinariansWithoutSpecialties() {
        // Given: User is on veterinarians page
        navigateToVeterinariansPage();
        VeterinariansPage vetsPage = new VeterinariansPage(page);

        // When: Looking for general practitioners (no specialties)
        // Then: Should display them correctly (PRD requirement)
        boolean foundGeneralPractitioner = false;
        for (int i = 0; i < vetsPage.getVetCount(); i++) {
            VeterinariansPage.VetInfo vet = vetsPage.getVetInfo(i);
            if (vet.specialties.trim().isEmpty()) {
                foundGeneralPractitioner = true;
                assertThat(vet.name).isNotEmpty();
                break;
            }
        }
        // Based on PRD test data, there should be general practitioners
        assertThat(foundGeneralPractitioner).isTrue();
    }

    @Test
    @DisplayName("Should verify known veterinarians from PRD test data")
    public void shouldDisplayKnownVeterinarians() {
        // Given: User is on veterinarians page
        navigateToVeterinariansPage();
        VeterinariansPage vetsPage = new VeterinariansPage(page);

        // When/Then: Should display known vets from PRD test data
        assertThat(vetsPage.isVetDisplayed("James Carter")).isTrue();
        assertThat(vetsPage.isVetDisplayed("Helen Leary")).isTrue();
        
        // Linda Douglas should have both dentistry and surgery specialties
        assertThat(vetsPage.isVetDisplayed("Linda Douglas")).isTrue();
    }

    @Test
    @DisplayName("Should display responsive layout for veterinarians page")
    public void shouldDisplayResponsiveLayoutForVets() {
        // Given: Mobile viewport (PRD requirement for responsive design)
        page.setViewportSize(375, 667); // iPhone size
        
        // When: User navigates to veterinarians page
        navigateToVeterinariansPage();
        VeterinariansPage vetsPage = new VeterinariansPage(page);

        // Then: Should display mobile-friendly layout
        assertThat(vetsPage.isDisplayed()).isTrue();
        assertThat(vetsPage.isTableDisplayed()).isTrue();
        
        // Should handle responsive design requirements from PRD
        assertThat(vetsPage.getVetCount()).isGreaterThan(0);
    }

    private void navigateToVeterinariansPage() {
        navigateToHomePage();
        HomePage homePage = new HomePage(page);
        homePage.clickVeterinarians();
        page.waitForURL("**/vets");
    }
}