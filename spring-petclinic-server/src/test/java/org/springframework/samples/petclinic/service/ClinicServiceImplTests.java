package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.*;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClinicServiceImplTests {

    @Mock
    private PetRepository petRepository;

    @Mock
    private VetRepository vetRepository;

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private VisitRepository visitRepository;

    @InjectMocks
    private ClinicServiceImpl clinicService;

    @Test
    public void shouldFindPetTypes() {
        // Given
        PetType cat = new PetType();
        cat.setId(1);
        cat.setName("cat");
        PetType dog = new PetType();
        dog.setId(2);
        dog.setName("dog");
        List<PetType> petTypes = Arrays.asList(cat, dog);

        given(petRepository.findPetTypes()).willReturn(petTypes);

        // When
        Collection<PetType> result = clinicService.findPetTypes();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(cat, dog);
        verify(petRepository, times(1)).findPetTypes();
    }

    @Test
    public void shouldFindOwnerById() {
        // Given
        Owner owner = createTestOwner(1, "George", "Franklin");
        given(ownerRepository.findById(1)).willReturn(Optional.of(owner));

        // When
        Owner result = clinicService.findOwnerById(1);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getFirstName()).isEqualTo("George");
        assertThat(result.getLastName()).isEqualTo("Franklin");
        verify(ownerRepository, times(1)).findById(1);
    }

    @Test
    public void shouldFindAllOwners() {
        // Given
        Owner owner1 = createTestOwner(1, "George", "Franklin");
        Owner owner2 = createTestOwner(2, "Betty", "Davis");
        List<Owner> owners = Arrays.asList(owner1, owner2);

        given(ownerRepository.findAll()).willReturn(owners);

        // When
        Collection<Owner> result = clinicService.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(owner1, owner2);
        verify(ownerRepository, times(1)).findAll();
    }

    @Test
    public void shouldSaveOwner() {
        // Given
        Owner owner = createTestOwner(null, "John", "Doe");

        // When
        clinicService.saveOwner(owner);

        // Then
        verify(ownerRepository, times(1)).save(owner);
    }

    @Test
    public void shouldFindPetById() {
        // Given
        Pet pet = createTestPet(1, "Buddy");
        given(petRepository.findById(1)).willReturn(pet);

        // When
        Pet result = clinicService.findPetById(1);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo("Buddy");
        verify(petRepository, times(1)).findById(1);
    }

    @Test
    public void shouldSavePet() {
        // Given
        Pet pet = createTestPet(null, "Max");

        // When
        clinicService.savePet(pet);

        // Then
        verify(petRepository, times(1)).save(pet);
    }

    @Test
    public void shouldFindVets() {
        // Given
        Vet vet1 = createTestVet(1, "James", "Carter");
        Vet vet2 = createTestVet(2, "Helen", "Leary");
        List<Vet> vets = Arrays.asList(vet1, vet2);

        given(vetRepository.findAll()).willReturn((Collection) vets);

        // When
        Collection<Vet> result = clinicService.findVets();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(vet1, vet2);
        verify(vetRepository, times(1)).findAll();
    }

    @Test
    public void shouldSaveVisit() {
        // Given
        Visit visit = createTestVisit(null, "Annual checkup");

        // When
        clinicService.saveVisit(visit);

        // Then
        verify(visitRepository, times(1)).save(visit);
    }

    private Owner createTestOwner(Integer id, String firstName, String lastName) {
        Owner owner = new Owner();
        if (id != null) {
            owner.setId(id);
        }
        owner.setFirstName(firstName);
        owner.setLastName(lastName);
        owner.setAddress("110 W. Liberty St.");
        owner.setCity("Madison");
        owner.setTelephone("6085551023");
        return owner;
    }

    private Pet createTestPet(Integer id, String name) {
        Pet pet = new Pet();
        if (id != null) {
            pet.setId(id);
        }
        pet.setName(name);
        return pet;
    }

    private Vet createTestVet(Integer id, String firstName, String lastName) {
        Vet vet = new Vet();
        if (id != null) {
            vet.setId(id);
        }
        vet.setFirstName(firstName);
        vet.setLastName(lastName);
        return vet;
    }

    private Visit createTestVisit(Integer id, String description) {
        Visit visit = new Visit();
        if (id != null) {
            visit.setId(id);
        }
        visit.setDescription(description);
        return visit;
    }
}