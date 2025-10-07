package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class OwnerTests {

    @Test
    public void shouldCreateOwnerWithProperties() {
        Owner owner = new Owner();
        owner.setFirstName("George");
        owner.setLastName("Franklin");
        owner.setAddress("110 W. Liberty St.");
        owner.setCity("Madison");
        owner.setTelephone("6085551023");

        assertThat(owner.getFirstName()).isEqualTo("George");
        assertThat(owner.getLastName()).isEqualTo("Franklin");
        assertThat(owner.getAddress()).isEqualTo("110 W. Liberty St.");
        assertThat(owner.getCity()).isEqualTo("Madison");
        assertThat(owner.getTelephone()).isEqualTo("6085551023");
    }

    @Test
    public void shouldAddPetToOwner() {
        Owner owner = new Owner();
        Pet pet = new Pet();
        pet.setName("Buddy");

        owner.addPet(pet);

        assertThat(owner.getPets()).hasSize(1);
        assertThat(owner.getPets().get(0).getName()).isEqualTo("Buddy");
        assertThat(pet.getOwner()).isEqualTo(owner);
    }

    @Test
    public void shouldFindPetByName() {
        Owner owner = new Owner();
        Pet pet1 = new Pet();
        pet1.setName("Buddy");
        Pet pet2 = new Pet();
        pet2.setName("Max");

        owner.addPet(pet1);
        owner.addPet(pet2);

        Pet foundPet = owner.getPet("buddy"); // Case insensitive
        assertThat(foundPet).isNotNull();
        assertThat(foundPet.getName()).isEqualTo("Buddy");

        Pet notFoundPet = owner.getPet("Charlie");
        assertThat(notFoundPet).isNull();
    }

    @Test
    public void shouldFindPetByNameIgnoreNew() {
        Owner owner = new Owner();
        Pet existingPet = new Pet();
        existingPet.setId(1);
        existingPet.setName("Buddy");
        
        Pet newPet = new Pet(); // No ID, so it's new
        newPet.setName("Buddy");

        owner.addPet(existingPet);
        owner.addPet(newPet);

        Pet foundPet = owner.getPet("Buddy", true); // Ignore new pets
        assertThat(foundPet).isNotNull();
        assertThat(foundPet.getId()).isEqualTo(1);
    }

    @Test
    public void shouldSortPetsByName() {
        Owner owner = new Owner();
        Pet petZ = new Pet();
        petZ.setName("Zorro");
        Pet petA = new Pet();
        petA.setName("Apollo");
        Pet petM = new Pet();
        petM.setName("Max");

        owner.addPet(petZ);
        owner.addPet(petA);
        owner.addPet(petM);

        assertThat(owner.getPets()).hasSize(3);
        assertThat(owner.getPets().get(0).getName()).isEqualTo("Apollo");
        assertThat(owner.getPets().get(1).getName()).isEqualTo("Max");
        assertThat(owner.getPets().get(2).getName()).isEqualTo("Zorro");
    }

    @Test
    public void shouldGenerateToString() {
        Owner owner = new Owner();
        owner.setId(1);
        owner.setFirstName("George");
        owner.setLastName("Franklin");
        owner.setAddress("110 W. Liberty St.");
        owner.setCity("Madison");
        owner.setTelephone("6085551023");

        String toString = owner.toString();
        assertThat(toString).contains("George");
        assertThat(toString).contains("Franklin");
        assertThat(toString).contains("Madison");
        assertThat(toString).contains("6085551023");
    }
}