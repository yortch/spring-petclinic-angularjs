package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class PetTests {

    @Test
    public void shouldCreatePetWithProperties() {
        Pet pet = new Pet();
        pet.setName("Buddy");
        
        Date birthDate = Date.from(LocalDate.of(2020, 1, 15).atStartOfDay(ZoneId.systemDefault()).toInstant());
        pet.setBirthDate(birthDate);

        PetType petType = new PetType();
        petType.setName("dog");
        pet.setType(petType);

        assertThat(pet.getName()).isEqualTo("Buddy");
        assertThat(pet.getBirthDate()).isEqualTo(birthDate);
        assertThat(pet.getType().getName()).isEqualTo("dog");
    }

    @Test
    public void shouldAddVisitToPet() {
        Pet pet = new Pet();
        pet.setName("Buddy");

        Visit visit = new Visit();
        visit.setDescription("Annual checkup");

        pet.addVisit(visit);

        assertThat(pet.getVisits()).hasSize(1);
        assertThat(pet.getVisits().get(0).getDescription()).isEqualTo("Annual checkup");
        assertThat(visit.getPet()).isEqualTo(pet);
    }

    @Test
    public void shouldSortVisitsByDateDescending() {
        Pet pet = new Pet();
        pet.setName("Buddy");

        Visit oldVisit = new Visit();
        oldVisit.setDescription("Old checkup");
        oldVisit.setDate(Date.from(LocalDate.of(2020, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        Visit recentVisit = new Visit();
        recentVisit.setDescription("Recent checkup");
        recentVisit.setDate(Date.from(LocalDate.of(2023, 6, 15).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        pet.addVisit(oldVisit);
        pet.addVisit(recentVisit);

        // Visits should be sorted by date descending (most recent first)
        assertThat(pet.getVisits()).hasSize(2);
        assertThat(pet.getVisits().get(0).getDescription()).isEqualTo("Recent checkup");
        assertThat(pet.getVisits().get(1).getDescription()).isEqualTo("Old checkup");
    }

    @Test
    public void shouldSetOwnerRelationship() {
        Owner owner = new Owner();
        owner.setFirstName("George");
        owner.setLastName("Franklin");

        Pet pet = new Pet();
        pet.setName("Buddy");

        owner.addPet(pet);

        assertThat(pet.getOwner()).isEqualTo(owner);
        assertThat(owner.getPets()).contains(pet);
    }
}