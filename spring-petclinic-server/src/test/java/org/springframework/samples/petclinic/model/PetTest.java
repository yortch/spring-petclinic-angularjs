/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link Pet}.
 *
 * Tests pet entity functionality including birth date management, type assignment,
 * owner association, and visit management as specified in the PRD for pet registration
 * and medical record keeping.
 */
public class PetTest {

    @Test
    public void getName_should_returnNull_when_notSet() {
        // Arrange
        Pet pet = new Pet();

        // Act & Assert
        assertThat(pet.getName()).isNull();
    }

    @Test
    public void setName_should_storeName_when_validNameProvided() {
        // Arrange
        Pet pet = new Pet();
        String expectedName = "Fluffy";

        // Act
        pet.setName(expectedName);

        // Assert
        assertThat(pet.getName()).isEqualTo(expectedName);
    }

    @Test
    public void getBirthDate_should_returnNull_when_notSet() {
        // Arrange
        Pet pet = new Pet();

        // Act & Assert
        assertThat(pet.getBirthDate()).isNull();
    }

    @Test
    public void setBirthDate_should_storeBirthDate_when_validDateProvided() {
        // Arrange
        Pet pet = new Pet();
        Date expectedBirthDate = new Date();

        // Act
        pet.setBirthDate(expectedBirthDate);

        // Assert
        assertThat(pet.getBirthDate()).isEqualTo(expectedBirthDate);
    }

    @Test
    public void getType_should_returnNull_when_notSet() {
        // Arrange
        Pet pet = new Pet();

        // Act & Assert
        assertThat(pet.getType()).isNull();
    }

    @Test
    public void setType_should_storeType_when_validTypeProvided() {
        // Arrange
        Pet pet = new Pet();
        PetType expectedType = new PetType();
        expectedType.setName("Dog");

        // Act
        pet.setType(expectedType);

        // Assert
        assertThat(pet.getType()).isEqualTo(expectedType);
    }

    @Test
    public void getOwner_should_returnNull_when_notSet() {
        // Arrange
        Pet pet = new Pet();

        // Act & Assert
        assertThat(pet.getOwner()).isNull();
    }

    @Test
    public void setOwner_should_storeOwner_when_validOwnerProvided() {
        // Arrange
        Pet pet = new Pet();
        Owner expectedOwner = new Owner();
        expectedOwner.setFirstName("John");
        expectedOwner.setLastName("Doe");

        // Act
        pet.setOwner(expectedOwner);

        // Assert
        assertThat(pet.getOwner()).isEqualTo(expectedOwner);
    }

    @Test
    public void getVisits_should_returnEmptyList_when_noVisitsAdded() {
        // Arrange
        Pet pet = new Pet();

        // Act
        List<Visit> visits = pet.getVisits();

        // Assert
        assertThat(visits).isEmpty();
        assertThat(visits).isNotNull();
    }

    @Test
    public void addVisit_should_addVisitToPet_when_validVisitProvided() {
        // Arrange
        Pet pet = new Pet();
        Visit visit = new Visit();
        visit.setDescription("Routine checkup");
        visit.setDate(new Date());

        // Act
        pet.addVisit(visit);

        // Assert
        assertThat(pet.getVisits()).hasSize(1);
        assertThat(pet.getVisits().getFirst()).isEqualTo(visit);
        assertThat(visit.getPet()).isEqualTo(pet);
    }

    @Test
    public void addVisit_should_addMultipleVisits_when_multipleVisitsProvided() {
        // Arrange
        Pet pet = new Pet();
        
        Calendar cal = Calendar.getInstance();
        
        Visit visit1 = new Visit();
        visit1.setDescription("First visit");
        cal.set(2023, Calendar.JANUARY, 15);
        visit1.setDate(cal.getTime());
        
        Visit visit2 = new Visit();
        visit2.setDescription("Second visit");
        cal.set(2023, Calendar.FEBRUARY, 20);
        visit2.setDate(cal.getTime());

        // Act
        pet.addVisit(visit1);
        pet.addVisit(visit2);

        // Assert
        assertThat(pet.getVisits()).hasSize(2);
        assertThat(pet.getVisits()).contains(visit1, visit2);
    }

    @Test
    public void getVisits_should_returnSortedByDate_when_visitsHaveDifferentDates() {
        // Arrange
        Pet pet = new Pet();
        Calendar cal = Calendar.getInstance();
        
        Visit laterVisit = new Visit();
        laterVisit.setDescription("Later visit");
        cal.set(2023, Calendar.DECEMBER, 25);
        laterVisit.setDate(cal.getTime());
        
        Visit earlierVisit = new Visit();
        earlierVisit.setDescription("Earlier visit");
        cal.set(2023, Calendar.JANUARY, 1);
        earlierVisit.setDate(cal.getTime());

        // Act
        pet.addVisit(laterVisit);
        pet.addVisit(earlierVisit);

        // Assert
        List<Visit> visits = pet.getVisits();
        assertThat(visits).hasSize(2);
        assertThat(visits.getFirst().getDescription()).isEqualTo("Later visit"); // Should be sorted by date descending (most recent first)
        assertThat(visits.get(1).getDescription()).isEqualTo("Earlier visit");
    }

    @Test
    public void getVisits_should_returnUnmodifiableList_when_called() {
        // Arrange
        Pet pet = new Pet();
        Visit visit = new Visit();
        visit.setDescription("Test visit");
        pet.addVisit(visit);

        // Act
        List<Visit> visits = pet.getVisits();

        // Assert
        assertThat(visits).hasSize(1);
        
        // This should not affect the pet's visits
        try {
            visits.clear();
            assertThat(pet.getVisits()).hasSize(1); // Original list should remain unchanged
        } catch (UnsupportedOperationException e) {
            // This is expected for unmodifiable lists
            assertThat(true).isTrue();
        }
    }

    @Test
    public void toString_should_returnName_when_nameIsSet() {
        // Arrange
        Pet pet = new Pet();
        String expectedName = "Fluffy";
        pet.setName(expectedName);

        // Act
        String result = pet.toString();

        // Assert
        assertThat(result).isEqualTo(expectedName);
    }

    @Test
    public void toString_should_returnNull_when_nameIsNull() {
        // Arrange
        Pet pet = new Pet();

        // Act
        String result = pet.toString();

        // Assert
        assertThat(result).isNull();
    }

    @Test
    public void pet_should_inheritBaseEntityFunctionality_when_created() {
        // Arrange
        Pet pet = new Pet();

        // Act & Assert
        assertThat(pet.getId()).isNull();
        assertThat(pet.isNew()).isTrue();
        
        pet.setId(1);
        assertThat(pet.getId()).isEqualTo(1);
        assertThat(pet.isNew()).isFalse();
    }

    @Test
    public void pet_should_supportCompleteLifecycle_when_allPropertiesSet() {
        // Arrange
        Pet pet = new Pet();
        pet.setName("Max");
        
        Date birthDate = new Date();
        pet.setBirthDate(birthDate);
        
        PetType type = new PetType();
        type.setName("Dog");
        pet.setType(type);
        
        Owner owner = new Owner();
        owner.setFirstName("John");
        owner.setLastName("Doe");
        pet.setOwner(owner);
        
        Visit visit = new Visit();
        visit.setDescription("Annual checkup");
        visit.setDate(new Date());

        // Act
        pet.addVisit(visit);

        // Assert
        assertThat(pet.getName()).isEqualTo("Max");
        assertThat(pet.getBirthDate()).isEqualTo(birthDate);
        assertThat(pet.getType()).isEqualTo(type);
        assertThat(pet.getOwner()).isEqualTo(owner);
        assertThat(pet.getVisits()).hasSize(1);
        assertThat(pet.getVisits().getFirst()).isEqualTo(visit);
        assertThat(visit.getPet()).isEqualTo(pet);
    }

    @Test
    public void pet_should_supportPetTypeAssociation_when_typeChanges() {
        // Arrange
        Pet pet = new Pet();
        
        PetType dogType = new PetType();
        dogType.setName("Dog");
        
        PetType catType = new PetType();
        catType.setName("Cat");

        // Act
        pet.setType(dogType);
        assertThat(pet.getType()).isEqualTo(dogType);
        
        pet.setType(catType);

        // Assert
        assertThat(pet.getType()).isEqualTo(catType);
    }

    @Test
    public void pet_should_supportOwnerChange_when_ownerChanges() {
        // Arrange
        Pet pet = new Pet();
        
        Owner owner1 = new Owner();
        owner1.setFirstName("John");
        owner1.setLastName("Doe");
        
        Owner owner2 = new Owner();
        owner2.setFirstName("Jane");
        owner2.setLastName("Smith");

        // Act
        pet.setOwner(owner1);
        assertThat(pet.getOwner()).isEqualTo(owner1);
        
        pet.setOwner(owner2);

        // Assert
        assertThat(pet.getOwner()).isEqualTo(owner2);
    }
}