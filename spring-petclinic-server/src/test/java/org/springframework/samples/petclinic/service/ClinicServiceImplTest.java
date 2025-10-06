package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClinicServiceImplTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private VetRepository vetRepository;

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private VisitRepository visitRepository;

    private ClinicServiceImpl clinicService;

    @BeforeEach
    void setUp() {
        clinicService = new ClinicServiceImpl(petRepository, vetRepository, ownerRepository, visitRepository);
    }

//     @Test
//     void findPetTypes_shouldReturnPetTypes() {
//         Collection<PetType> expectedPetTypes = new ArrayList<>();
//         PetType petType1 = new PetType();
//         petType1.setId(1);
//         petType1.setName("dog");
//         PetType petType2 = new PetType();
//         petType2.setId(2);
//         petType2.setName("cat");
//         expectedPetTypes.add(petType1);
//         expectedPetTypes.add(petType2);
// 
//         when(petRepository.findPetTypes()).thenReturn(expectedPetTypes);
// 
//         Collection<PetType> actualPetTypes = clinicService.findPetTypes();
// 
//         assertThat(actualPetTypes).isEqualTo(expectedPetTypes);
//         assertThat(actualPetTypes).hasSize(2);
//         verify(petRepository).findPetTypes();
//     }

    @Test
    void findOwnerById_shouldReturnOwner() {
        Owner expectedOwner = new Owner();
        expectedOwner.setId(1);
        expectedOwner.setFirstName("John");
        expectedOwner.setLastName("Doe");

        when(ownerRepository.findById(1)).thenReturn(Optional.of(expectedOwner));

        Owner actualOwner = clinicService.findOwnerById(1);

        assertThat(actualOwner).isEqualTo(expectedOwner);
        assertThat(actualOwner.getId()).isEqualTo(1);
        verify(ownerRepository).findById(1);
    }

//     @Test
//     void findAll_shouldReturnAllOwners() {
//         Collection<Owner> expectedOwners = new ArrayList<>();
//         Owner owner1 = new Owner();
//         owner1.setId(1);
//         owner1.setFirstName("John");
//         Owner owner2 = new Owner();
//         owner2.setId(2);
//         owner2.setFirstName("Jane");
//         expectedOwners.add(owner1);
//         expectedOwners.add(owner2);
// 
//         when(ownerRepository.findAll()).thenReturn(expectedOwners);
// 
//         Collection<Owner> actualOwners = clinicService.findAll();
// 
//         assertThat(actualOwners).isEqualTo(expectedOwners);
//         assertThat(actualOwners).hasSize(2);
//         verify(ownerRepository).findAll();
//     }

    @Test
    void saveOwner_shouldSaveOwner() {
        Owner owner = new Owner();
        owner.setId(1);
        owner.setFirstName("John");
        owner.setLastName("Doe");

        when(ownerRepository.save(owner)).thenReturn(owner);

        clinicService.saveOwner(owner);

        verify(ownerRepository).save(owner);
    }

//     @Test
//     void saveVisit_shouldSaveVisit() {
//         Visit visit = new Visit();
//         visit.setId(1);
//         visit.setDescription("Regular checkup");
// 
//         when(visitRepository.save(visit)).thenReturn(visit);
// 
//         clinicService.saveVisit(visit);
// 
//         verify(visitRepository).save(visit);
//     }

    @Test
    void findPetById_shouldReturnPet() {
        Pet expectedPet = new Pet();
        expectedPet.setId(1);
        expectedPet.setName("Max");

        when(petRepository.findById(1)).thenReturn(expectedPet);

        Pet actualPet = clinicService.findPetById(1);

        assertThat(actualPet).isEqualTo(expectedPet);
        assertThat(actualPet.getId()).isEqualTo(1);
        verify(petRepository).findById(1);
    }

//     @Test
//     void savePet_shouldSavePet() {
//         Pet pet = new Pet();
//         pet.setId(1);
//         pet.setName("Max");
// 
//         when(petRepository.save(pet)).thenReturn(pet);
// 
//         clinicService.savePet(pet);
// 
//         verify(petRepository).save(pet);
//     }

    @Test
    void findVets_shouldReturnVets() {
        Collection<Vet> expectedVets = new ArrayList<>();
        Vet vet1 = new Vet();
        vet1.setId(1);
        vet1.setFirstName("James");
        vet1.setLastName("Carter");
        Vet vet2 = new Vet();
        vet2.setId(2);
        vet2.setFirstName("Helen");
        vet2.setLastName("Leary");
        expectedVets.add(vet1);
        expectedVets.add(vet2);

        when(vetRepository.findAll()).thenReturn(expectedVets);

        Collection<Vet> actualVets = clinicService.findVets();

        assertThat(actualVets).isEqualTo(expectedVets);
        assertThat(actualVets).hasSize(2);
        verify(vetRepository).findAll();
    }
}