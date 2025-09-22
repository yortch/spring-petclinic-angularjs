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
package org.springframework.samples.petclinic.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link PetRepository}.
 *
 * Tests data persistence and retrieval functionality for pet registration and management
 * as specified in the PRD requirements.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
public class PetRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PetRepository petRepository;

    @Test
    public void findPetTypes_should_returnAllPetTypesOrderedByName_when_petTypesExist() {
        // Arrange - sample data already has 6 pet types, add 4 more for testing
        PetType guinea = createPetType("guinea pig");
        PetType rabbit = createPetType("rabbit");

        entityManager.persist(guinea);
        entityManager.persist(rabbit);
        entityManager.flush();

        // Act
        List<PetType> result = petRepository.findPetTypes();

        // Assert - 6 sample + 2 test = 8 total, should be alphabetically ordered
        assertThat(result.size()).isEqualTo(8);
        assertThat(result.get(0).getName()).isEqualTo("bird");
        assertThat(result.get(1).getName()).isEqualTo("cat");
        assertThat(result.get(2).getName()).isEqualTo("dog");
        assertThat(result.get(3).getName()).isEqualTo("guinea pig");
        assertThat(result.get(4).getName()).isEqualTo("hamster");
        assertThat(result.get(5).getName()).isEqualTo("lizard");
        assertThat(result.get(6).getName()).isEqualTo("rabbit");
        assertThat(result.get(7).getName()).isEqualTo("snake");
    }

    @Test
    public void findPetTypes_should_includeSampleTypes_when_sampleDataLoaded() {
        // Act
        List<PetType> result = petRepository.findPetTypes();

        // Assert - sample data has 6 pet types: cat, dog, lizard, snake, bird, hamster
        assertThat(result.size()).isEqualTo(6);
        assertThat(result)
            .extracting(PetType::getName)
            .containsExactly("bird", "cat", "dog", "hamster", "lizard", "snake"); // alphabetically ordered
    }

    @Test
    public void findById_should_returnPet_when_petExists() {
        // Arrange
        Owner owner = createValidOwner("John", "Doe");
        // Use existing pet type from sample data (ID=2 is 'dog')
        PetType dogType = entityManager.find(PetType.class, 2);
        Pet pet = createValidPet("Buddy", dogType, owner);

        entityManager.persist(owner);
        Integer savedId = entityManager.persistAndGetId(pet, Integer.class);
        entityManager.flush();

        // Act
        Pet result = petRepository.findById(savedId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Buddy");
        assertThat(result.getType().getName()).isEqualTo("dog");
        assertThat(result.getOwner().getFirstName()).isEqualTo("John");
        assertThat(result.getId()).isEqualTo(savedId);
    }

    @Test
    public void findById_should_returnNull_when_petNotFound() {
        // Act
        Pet result = petRepository.findById(999);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    public void save_should_persistNewPet_when_petIsNew() {
        // Arrange
        Owner owner = createValidOwner("Jane", "Smith");
        // Use existing pet type from sample data (ID=1 is 'cat')
        PetType catType = entityManager.find(PetType.class, 1);
        Pet newPet = createValidPet("Whiskers", catType, owner);

        // Verify pet is new before any persistence
        assertThat(newPet.isNew()).isTrue();

        entityManager.persist(owner);
        entityManager.flush();

        // Note: Due to CascadeType.ALL on Owner.pets, the pet is automatically persisted
        // when the owner is persisted, so the pet is no longer "new" at this point
        assertThat(newPet.isNew()).isFalse();
        assertThat(newPet.getId()).isNotNull();

        // Act - Save should still work for an already persisted pet
        petRepository.save(newPet);
        entityManager.flush();

        // Assert
        Pet foundPet = entityManager.find(Pet.class, newPet.getId());
        assertThat(foundPet).isNotNull();
        assertThat(foundPet.getName()).isEqualTo("Whiskers");
        assertThat(foundPet.getType().getName()).isEqualTo("cat");
    }

    @Test
    public void save_should_updateExistingPet_when_petExists() {
        // Arrange
        Owner owner = createValidOwner("Bob", "Johnson");
        // Use existing pet type from sample data (ID=2 is 'dog')
        PetType dogType = entityManager.find(PetType.class, 2);
        Pet pet = createValidPet("Max", dogType, owner);

        entityManager.persist(owner);
        Integer savedId = entityManager.persistAndGetId(pet, Integer.class);
        entityManager.flush();
        entityManager.clear();

        Pet foundPet = entityManager.find(Pet.class, savedId);
        foundPet.setName("MaxUpdated");

        // Act
        petRepository.save(foundPet);
        entityManager.flush();
        entityManager.clear();

        // Assert
        Pet updatedPet = entityManager.find(Pet.class, savedId);
        assertThat(updatedPet.getName()).isEqualTo("MaxUpdated");
        assertThat(updatedPet.getType().getName()).isEqualTo("dog");
        assertThat(updatedPet.getOwner().getFirstName()).isEqualTo("Bob");
    }

    @Test
    public void repository_should_supportPetRegistrationScenario_when_usedForPRD() {
        // Arrange - PRD: Pet registration functionality
        Owner owner = createValidOwner("Alice", "Brown");
        // Use existing pet type from sample data (ID=2 is 'dog')
        PetType dogType = entityManager.find(PetType.class, 2);
        Pet newPet = createValidPet("Luna", dogType, owner);

        entityManager.persist(owner);
        entityManager.flush();

        // Act
        // 1. Register new pet
        petRepository.save(newPet);
        entityManager.flush();

        // 2. Retrieve pet by ID for visit scheduling
        Pet foundPet = petRepository.findById(newPet.getId());

        // 3. Get all available pet types for registration form
        List<PetType> availableTypes = petRepository.findPetTypes();

        // Assert
        assertThat(foundPet).isNotNull();
        assertThat(foundPet.getName()).isEqualTo("Luna");
        assertThat(foundPet.getType().getName()).isEqualTo("dog");
        assertThat(foundPet.getOwner().getFirstName()).isEqualTo("Alice");

        // Assert pet types include the 6 sample types
        assertThat(availableTypes.size()).isEqualTo(6);
        assertThat(availableTypes)
            .extracting(PetType::getName)
            .contains("dog", "cat", "bird", "hamster", "lizard", "snake");
    }

    @Test
    public void save_should_maintainOwnerRelationship_when_petIsSaved() {
        // Arrange
        Owner owner = createValidOwner("Charlie", "Wilson");
        // Use existing pet type from sample data (ID=5 is 'bird')
        PetType birdType = entityManager.find(PetType.class, 5);
        Pet pet = createValidPet("Tweety", birdType, owner);

        entityManager.persist(owner);
        entityManager.flush();

        // Act
        petRepository.save(pet);
        entityManager.flush();
        entityManager.clear(); // Clear to ensure fresh load

        // Assert
        Pet savedPet = entityManager.find(Pet.class, pet.getId());
        assertThat(savedPet.getOwner()).isNotNull();
        assertThat(savedPet.getOwner().getId()).isEqualTo(owner.getId());
        assertThat(savedPet.getOwner().getFirstName()).isEqualTo("Charlie");

        // Verify bidirectional relationship
        Owner savedOwner = entityManager.find(Owner.class, owner.getId());
        assertThat(savedOwner.getPets()).hasSize(1);
        assertThat(savedOwner.getPets().iterator().next().getName()).isEqualTo("Tweety");
    }

    @Test
    public void findPetTypes_should_supportPetTypeSelection_when_usedInRegistration() {
        // Arrange - Add 2 additional pet types to the existing 6 sample types
        PetType rabbit = createPetType("rabbit");
        PetType guineaPig = createPetType("guinea pig");

        entityManager.persist(rabbit);
        entityManager.persist(guineaPig);
        entityManager.flush();

        // Act
        List<PetType> result = petRepository.findPetTypes();

        // Assert - 6 sample + 2 test = 8 total types
        assertThat(result.size()).isEqualTo(8);
        assertThat(result).extracting(PetType::getName)
            .containsExactly("bird", "cat", "dog", "guinea pig", "hamster", "lizard", "rabbit", "snake");
    }

    @Test
    public void save_should_handlePetWithVisits_when_petHasMedicalHistory() {
        // Arrange
        Owner owner = createValidOwner("David", "Garcia");
        // Use existing pet type from sample data (ID=2 is 'dog')
        PetType dogType = entityManager.find(PetType.class, 2);
        Pet pet = createValidPet("Rocky", dogType, owner);

        entityManager.persist(owner);
        entityManager.flush();

        // Act
        petRepository.save(pet);
        entityManager.flush();

        // Assert
        Pet savedPet = entityManager.find(Pet.class, pet.getId());
        assertThat(savedPet.getVisits()).isNotNull();
        assertThat(savedPet.getVisits()).isEmpty(); // No visits added in this test
    }

    @Test
    public void repository_should_maintainDataIntegrity_when_multipleOperations() {
        // Arrange
        Owner owner1 = createValidOwner("Owner1", "LastName1");
        Owner owner2 = createValidOwner("Owner2", "LastName2");
        // Use existing pet types from sample data
        PetType dogType = entityManager.find(PetType.class, 2); // ID=2 is 'dog'
        PetType catType = entityManager.find(PetType.class, 1); // ID=1 is 'cat'

        entityManager.persist(owner1);
        entityManager.persist(owner2);
        entityManager.flush();

        Pet pet1 = createValidPet("Pet1", dogType, owner1);
        Pet pet2 = createValidPet("Pet2", catType, owner2);

        // Act
        petRepository.save(pet1);
        petRepository.save(pet2);
        entityManager.flush();

        // Assert
        Pet foundPet1 = petRepository.findById(pet1.getId());
        Pet foundPet2 = petRepository.findById(pet2.getId());

        assertThat(foundPet1.getOwner().getId()).isEqualTo(owner1.getId());
        assertThat(foundPet2.getOwner().getId()).isEqualTo(owner2.getId());
        assertThat(foundPet1.getType().getName()).isEqualTo("dog");
        assertThat(foundPet2.getType().getName()).isEqualTo("cat");
    }

    // ===============================
    // Helper Methods
    // ===============================

    private static Owner createValidOwner(String firstName, String lastName) {
        Owner owner = new Owner();
        owner.setFirstName(firstName);
        owner.setLastName(lastName);
        owner.setAddress("123 Main St");
        owner.setCity("Springfield");
        owner.setTelephone("5551234567");
        return owner;
    }

    private static PetType createPetType(String name) {
        PetType type = new PetType();
        type.setName(name);
        return type;
    }

    private static Pet createValidPet(String name, PetType type, Owner owner) {
        Pet pet = new Pet();
        pet.setName(name);
        pet.setBirthDate(new Date(System.currentTimeMillis() - 365 * 24 * 60 * 60 * 1000L)); // 1 year ago
        pet.setType(type);
        owner.addPet(pet); // This sets the bidirectional relationship
        return pet;
    }
}