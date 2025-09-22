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
package org.springframework.samples.petclinic.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.*;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;

import java.util.*;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ClinicServiceImpl}.
 *
 * Tests business operations including owner management, pet registration, visit recording,
 * and veterinarian directory functionality as specified in the PRD requirements.
 */
@RunWith(MockitoJUnitRunner.class)
public class ClinicServiceImplTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private VetRepository vetRepository;

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private VisitRepository visitRepository;

    private ClinicServiceImpl clinicService;

    @Before
    public void setUp() {
        clinicService = new ClinicServiceImpl(petRepository, vetRepository, ownerRepository, visitRepository);
    }

    // ===============================
    // Pet Type Management Tests
    // ===============================

    @Test
    public void findPetTypes_should_returnAllPetTypes_when_repositoryHasData() {
        // Arrange
        List<PetType> expectedTypes = Arrays.asList(
            createPetType(1, "dog"),
            createPetType(2, "cat"),
            createPetType(3, "bird")
        );
        when(petRepository.findPetTypes()).thenReturn(expectedTypes);

        // Act
        Collection<PetType> result = clinicService.findPetTypes();

        // Assert
        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyElementsOf(expectedTypes);
        verify(petRepository, times(1)).findPetTypes();
    }

    @Test
    public void findPetTypes_should_returnEmptyCollection_when_noPetTypesExist() {
        // Arrange
        when(petRepository.findPetTypes()).thenReturn(Collections.emptyList());

        // Act
        Collection<PetType> result = clinicService.findPetTypes();

        // Assert
        assertThat(result).isEmpty();
        verify(petRepository, times(1)).findPetTypes();
    }

    @Test
    public void findPetTypes_should_propagateException_when_repositoryThrowsException() {
        // Arrange
        when(petRepository.findPetTypes()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThatThrownBy(() -> clinicService.findPetTypes())
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Database error");
        verify(petRepository, times(1)).findPetTypes();
    }

    // ===============================
    // Owner Management Tests
    // ===============================

    @Test
    public void findOwnerById_should_returnOwner_when_ownerExists() {
        // Arrange
        Owner expectedOwner = createValidOwner(1, "John", "Doe");
        when(ownerRepository.findById(1)).thenReturn(Optional.of(expectedOwner));

        // Act
        Owner result = clinicService.findOwnerById(1);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getLastName()).isEqualTo("Doe");
        verify(ownerRepository, times(1)).findById(1);
    }

    @Test
    public void findOwnerById_should_throwException_when_ownerNotFound() {
        // Arrange
        when(ownerRepository.findById(99)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> clinicService.findOwnerById(99))
            .isInstanceOf(NoSuchElementException.class);
        verify(ownerRepository, times(1)).findById(99);
    }

    @Test
    public void findAll_should_returnAllOwners_when_ownersExist() {
        // Arrange
        List<Owner> expectedOwners = Arrays.asList(
            createValidOwner(1, "John", "Doe"),
            createValidOwner(2, "Jane", "Smith"),
            createValidOwner(3, "Bob", "Johnson")
        );
        when(ownerRepository.findAll()).thenReturn(expectedOwners);

        // Act
        Collection<Owner> result = clinicService.findAll();

        // Assert
        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyElementsOf(expectedOwners);
        verify(ownerRepository, times(1)).findAll();
    }

    @Test
    public void saveOwner_should_delegateToRepository_when_validOwnerProvided() {
        // Arrange
        Owner owner = createValidOwner(null, "New", "Owner");
        
        // Act
        clinicService.saveOwner(owner);

        // Assert
        verify(ownerRepository, times(1)).save(owner);
    }

    @Test
    public void saveOwner_should_propagateException_when_repositoryFails() {
        // Arrange
        Owner owner = createValidOwner(null, "New", "Owner");
        doThrow(new DataAccessException("Save failed") {}).when(ownerRepository).save(owner);

        // Act & Assert
        assertThatThrownBy(() -> clinicService.saveOwner(owner))
            .isInstanceOf(DataAccessException.class)
            .hasMessage("Save failed");
        verify(ownerRepository, times(1)).save(owner);
    }

    // ===============================
    // Pet Management Tests
    // ===============================

    @Test
    public void findPetById_should_returnPet_when_petExists() {
        // Arrange
        Pet expectedPet = createValidPet(1, "Fluffy");
        when(petRepository.findById(1)).thenReturn(expectedPet);

        // Act
        Pet result = clinicService.findPetById(1);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo("Fluffy");
        verify(petRepository, times(1)).findById(1);
    }

    @Test
    public void findPetById_should_returnNull_when_petNotFound() {
        // Arrange
        when(petRepository.findById(99)).thenReturn(null);

        // Act
        Pet result = clinicService.findPetById(99);

        // Assert
        assertThat(result).isNull();
        verify(petRepository, times(1)).findById(99);
    }

    @Test
    public void savePet_should_delegateToRepository_when_validPetProvided() {
        // Arrange
        Pet pet = createValidPet(null, "New Pet");
        
        // Act
        clinicService.savePet(pet);

        // Assert
        verify(petRepository, times(1)).save(pet);
    }

    @Test
    public void savePet_should_supportPetRegistrationScenario_when_newPetAdded() {
        // Arrange - PRD: Pet registration functionality
        PetType dogType = createPetType(1, "dog");
        
        Pet newPet = new Pet();
        newPet.setName("Max");
        newPet.setBirthDate(new Date(System.currentTimeMillis() - 365 * 24 * 60 * 60 * 1000L)); // 1 year ago
        newPet.setType(dogType);
        // Can't use setOwner as it's protected, would be set by owner.addPet(pet) in real code

        // Act
        clinicService.savePet(newPet);

        // Assert
        verify(petRepository, times(1)).save(newPet);
        // Verify that pet contains all required registration information
        assertThat(newPet.getName()).isEqualTo("Max");
        assertThat(newPet.getType()).isEqualTo(dogType);
        assertThat(newPet.getBirthDate()).isNotNull();
    }

    // ===============================
    // Visit Management Tests  
    // ===============================

    @Test
    public void saveVisit_should_delegateToRepository_when_validVisitProvided() {
        // Arrange
        Visit visit = createValidVisit(1, "Routine checkup");
        
        // Act
        clinicService.saveVisit(visit);

        // Assert
        verify(visitRepository, times(1)).save(visit);
    }

    @Test
    public void saveVisit_should_supportVisitRecordingScenario_when_newVisitCreated() {
        // Arrange - PRD: Visit recording functionality
        Pet pet = createValidPet(1, "Fluffy");
        
        Visit visit = new Visit();
        visit.setDate(new Date());
        visit.setDescription("Annual vaccination and health check. Pet appears healthy.");
        visit.setPet(pet);

        // Act
        clinicService.saveVisit(visit);

        // Assert
        verify(visitRepository, times(1)).save(visit);
        // Verify visit contains all required medical record information
        assertThat(visit.getDate()).isNotNull();
        assertThat(visit.getDescription()).isNotEmpty();
        assertThat(visit.getPet()).isEqualTo(pet);
    }

    @Test
    public void saveVisit_should_propagateException_when_repositoryFails() {
        // Arrange
        Visit visit = createValidVisit(1, "Test visit");
        doThrow(new DataAccessException("Visit save failed") {}).when(visitRepository).save(visit);

        // Act & Assert
        assertThatThrownBy(() -> clinicService.saveVisit(visit))
            .isInstanceOf(DataAccessException.class)
            .hasMessage("Visit save failed");
        verify(visitRepository, times(1)).save(visit);
    }

    // ===============================
    // Veterinarian Directory Tests
    // ===============================

    @Test
    public void findVets_should_returnAllVets_when_vetsExist() {
        // Arrange
        List<Vet> expectedVets = Arrays.asList(
            createValidVet(1, "Dr. Sarah", "Johnson"),
            createValidVet(2, "Dr. Michael", "Brown"),
            createValidVet(3, "Dr. Emily", "Davis")
        );
        when(vetRepository.findAll()).thenReturn(expectedVets);

        // Act
        Collection<Vet> result = clinicService.findVets();

        // Assert
        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyElementsOf(expectedVets);
        verify(vetRepository, times(1)).findAll();
    }

    @Test
    public void findVets_should_returnEmptyCollection_when_noVetsExist() {
        // Arrange
        when(vetRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        Collection<Vet> result = clinicService.findVets();

        // Assert
        assertThat(result).isEmpty();
        verify(vetRepository, times(1)).findAll();
    }

    @Test
    public void findVets_should_supportVeterinaryDirectoryScenario_when_searchingVets() {
        // Arrange - PRD: Veterinarian directory functionality
        Vet generalVet = createValidVet(1, "Dr. Sarah", "Johnson");
        
        Vet specialist = createValidVet(2, "Dr. Michael", "Brown");
        Specialty surgery = new Specialty();
        surgery.setName("Surgery");
        specialist.addSpecialty(surgery);

        List<Vet> vets = Arrays.asList(generalVet, specialist);
        when(vetRepository.findAll()).thenReturn(vets);

        // Act
        Collection<Vet> result = clinicService.findVets();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).contains(generalVet, specialist);
        
        // Verify directory provides both general practitioners and specialists
        boolean hasGeneralVet = result.stream().anyMatch(v -> v.getNrOfSpecialties() == 0);
        boolean hasSpecialist = result.stream().anyMatch(v -> v.getNrOfSpecialties() > 0);
        
        assertThat(hasGeneralVet).isTrue();
        assertThat(hasSpecialist).isTrue();
        verify(vetRepository, times(1)).findAll();
    }

    // ===============================
    // Integration Scenario Tests
    // ===============================

    @Test
    public void clinicService_should_supportCompletePatientJourney_when_usedInSequence() {
        // Arrange - Complete patient journey scenario from PRD
        Owner owner = createValidOwner(1, "John", "Doe");
        Pet pet = createValidPet(1, "Max");
        // Note: Pet.setOwner is protected, so in real code owner.addPet(pet) would be used
        Visit visit = createValidVisit(1, "Initial consultation");
        visit.setPet(pet);

        when(ownerRepository.findById(1)).thenReturn(Optional.of(owner));
        when(petRepository.findById(1)).thenReturn(pet);

        // Act - Simulate complete patient journey
        // 1. Find owner (for appointment scheduling)
        Owner foundOwner = clinicService.findOwnerById(1);
        
        // 2. Register new pet
        clinicService.savePet(pet);
        
        // 3. Find pet for visit
        Pet foundPet = clinicService.findPetById(1);
        
        // 4. Record visit
        clinicService.saveVisit(visit);

        // Assert
        assertThat(foundOwner).isEqualTo(owner);
        assertThat(foundPet).isEqualTo(pet);
        
        verify(ownerRepository, times(1)).findById(1);
        verify(petRepository, times(1)).save(pet);
        verify(petRepository, times(1)).findById(1);
        verify(visitRepository, times(1)).save(visit);
    }

    @Test
    public void clinicService_should_supportVeterinaryWorkflow_when_handlingAppointments() {
        // Arrange - Veterinary workflow scenario
        List<Vet> availableVets = Arrays.asList(
            createValidVet(1, "Dr. Sarah", "Johnson"),
            createSpecialistVet(2, "Dr. Michael", "Brown", "Surgery")
        );
        when(vetRepository.findAll()).thenReturn(availableVets);

        Pet pet = createValidPet(1, "Fluffy");
        when(petRepository.findById(1)).thenReturn(pet);

        Visit surgicalConsult = new Visit();
        surgicalConsult.setDate(new Date());
        surgicalConsult.setDescription("Surgical consultation for tumor removal");
        surgicalConsult.setPet(pet);

        // Act
        // 1. Get veterinary directory for appointment assignment
        Collection<Vet> vets = clinicService.findVets();
        
        // 2. Find pet for medical records
        Pet foundPet = clinicService.findPetById(1);
        
        // 3. Record specialist visit
        clinicService.saveVisit(surgicalConsult);

        // Assert
        assertThat(vets).hasSize(2);
        assertThat(foundPet).isEqualTo(pet);
        
        // Verify specialist is available for surgical cases
        boolean hasSurgicalSpecialist = vets.stream()
            .anyMatch(v -> v.getSpecialties().stream()
                .anyMatch(s -> s.getName().equals("Surgery")));
        assertThat(hasSurgicalSpecialist).isTrue();
        
        verify(vetRepository, times(1)).findAll();
        verify(petRepository, times(1)).findById(1);
        verify(visitRepository, times(1)).save(surgicalConsult);
    }

    // ===============================
    // Helper Methods
    // ===============================

    private static Owner createValidOwner(Integer id, String firstName, String lastName) {
        Owner owner = new Owner();
        owner.setId(id);
        owner.setFirstName(firstName);
        owner.setLastName(lastName);
        owner.setAddress("123 Main St");
        owner.setCity("Springfield");
        owner.setTelephone("5551234567");
        return owner;
    }

    private static Pet createValidPet(Integer id, String name) {
        Pet pet = new Pet();
        pet.setId(id);
        pet.setName(name);
        pet.setBirthDate(new Date(System.currentTimeMillis() - 365 * 24 * 60 * 60 * 1000L)); // 1 year ago
        
        PetType type = new PetType();
        type.setId(1);
        type.setName("dog");
        pet.setType(type);
        
        return pet;
    }

    private static Visit createValidVisit(Integer id, String description) {
        Visit visit = new Visit();
        visit.setId(id);
        visit.setDate(new Date());
        visit.setDescription(description);
        return visit;
    }

    private static Vet createValidVet(Integer id, String firstName, String lastName) {
        Vet vet = new Vet();
        vet.setId(id);
        vet.setFirstName(firstName);
        vet.setLastName(lastName);
        return vet;
    }

    private static Vet createSpecialistVet(Integer id, String firstName, String lastName, String specialtyName) {
        Vet vet = createValidVet(id, firstName, lastName);
        Specialty specialty = new Specialty();
        specialty.setName(specialtyName);
        vet.addSpecialty(specialty);
        return vet;
    }

    private static PetType createPetType(Integer id, String name) {
        PetType type = new PetType();
        type.setId(id);
        type.setName(name);
        return type;
    }
}