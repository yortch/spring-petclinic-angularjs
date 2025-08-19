package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class VetTests {

    @Test
    public void shouldCreateVetWithProperties() {
        Vet vet = new Vet();
        vet.setFirstName("James");
        vet.setLastName("Carter");

        assertThat(vet.getFirstName()).isEqualTo("James");
        assertThat(vet.getLastName()).isEqualTo("Carter");
        assertThat(vet.getNrOfSpecialties()).isEqualTo(0);
    }

    @Test
    public void shouldAddSpecialtyToVet() {
        Vet vet = new Vet();
        vet.setFirstName("Linda");
        vet.setLastName("Douglas");

        Specialty surgery = new Specialty();
        surgery.setName("surgery");
        
        Specialty dentistry = new Specialty();
        dentistry.setName("dentistry");

        vet.addSpecialty(surgery);
        vet.addSpecialty(dentistry);

        assertThat(vet.getNrOfSpecialties()).isEqualTo(2);
        assertThat(vet.getSpecialties()).hasSize(2);
        assertThat(vet.getSpecialties()).extracting("name").containsExactly("dentistry", "surgery");
    }

    @Test
    public void shouldSortSpecialtiesByName() {
        Vet vet = new Vet();
        
        Specialty surgery = new Specialty();
        surgery.setName("surgery");
        
        Specialty dentistry = new Specialty();
        dentistry.setName("dentistry");
        
        Specialty radiology = new Specialty();
        radiology.setName("radiology");

        // Add in random order
        vet.addSpecialty(surgery);
        vet.addSpecialty(radiology);
        vet.addSpecialty(dentistry);

        // Should be sorted alphabetically
        assertThat(vet.getSpecialties()).hasSize(3);
        assertThat(vet.getSpecialties()).extracting("name").containsExactly("dentistry", "radiology", "surgery");
    }

    @Test
    public void shouldHandleVetWithNoSpecialties() {
        Vet vet = new Vet();
        vet.setFirstName("General");
        vet.setLastName("Practitioner");

        assertThat(vet.getNrOfSpecialties()).isEqualTo(0);
        assertThat(vet.getSpecialties()).isEmpty();
    }
}