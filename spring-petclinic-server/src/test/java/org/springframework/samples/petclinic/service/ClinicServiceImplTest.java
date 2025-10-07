package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

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

    @InjectMocks
    private ClinicServiceImpl clinicService;

    private Owner testOwner;
    private Pet testPet;
    private PetType testPetType;
    private Vet testVet;
    private Visit testVisit;

    @BeforeEach
    void setUp() {
        testOwner = new Owner();
        testOwner.setId(1);
        testOwner.setFirstName("John");
        testOwner.setLastName("Doe");

        testPet = new Pet();
        testPet.setId(1);
        testPet.setName("Fluffy");

        testPetType = new PetType();
        testPetType.setId(1);
        testPetType.setName("Dog");

        testVet = new Vet();
        testVet.setId(1);
        testVet.setFirstName("Jane");
        testVet.setLastName("Smith");

        testVisit = new Visit();
        testVisit.setId(1);
        testVisit.setDescription("Regular checkup");
    }

//     @Test
//     void testFindPetTypes() {
//         Collection<PetType> petTypes = Arrays.asList(testPetType);
//         when(petRepository.findPetTypes()).thenReturn(petTypes);
// 
//         Collection<PetType> result = clinicService.findPetTypes();
// 
//         assertNotNull(result);
//         assertEquals(1, result.size());
//         assertTrue(result.contains(testPetType));
//         verify(petRepository, times(1)).findPetTypes();
//     }

    @Test
    void testFindPetTypesEmpty() {
        when(petRepository.findPetTypes()).thenReturn(Collections.emptyList());

        Collection<PetType> result = clinicService.findPetTypes();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(petRepository, times(1)).findPetTypes();
    }

    @Test
    void testFindPetTypesThrowsException() {
        when(petRepository.findPetTypes()).thenThrow(new DataAccessException("Database error") {});

        assertThrows(DataAccessException.class, () -> clinicService.findPetTypes());
        verify(petRepository, times(1)).findPetTypes();
    }

    @Test
    void testFindOwnerById() {
        when(ownerRepository.findById(1)).thenReturn(Optional.of(testOwner));

        Owner result = clinicService.findOwnerById(1);

        assertNotNull(result);
        assertEquals(testOwner.getId(), result.getId());
        assertEquals(testOwner.getFirstName(), result.getFirstName());
        verify(ownerRepository, times(1)).findById(1);
    }

    @Test
    void testFindOwnerByIdThrowsException() {
        when(ownerRepository.findById(anyInt())).thenThrow(new DataAccessException("Database error") {});

        assertThrows(DataAccessException.class, () -> clinicService.findOwnerById(999));
        verify(ownerRepository, times(1)).findById(999);
    }

//     @Test
//     void testFindAll() {
//         Collection<Owner> owners = Arrays.asList(testOwner);
//         when(ownerRepository.findAll()).thenReturn(owners);
// 
//         Collection<Owner> result = clinicService.findAll();
// 
//         assertNotNull(result);
//         assertEquals(1, result.size());
//         assertTrue(result.contains(testOwner));
//         verify(ownerRepository, times(1)).findAll();
//     }

    @Test
    void testFindAllEmpty() {
        when(ownerRepository.findAll()).thenReturn(Collections.emptyList());

        Collection<Owner> result = clinicService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(ownerRepository, times(1)).findAll();
    }

    @Test
    void testFindAllThrowsException() {
        when(ownerRepository.findAll()).thenThrow(new DataAccessException("Database error") {});

        assertThrows(DataAccessException.class, () -> clinicService.findAll());
        verify(ownerRepository, times(1)).findAll();
    }

    @Test
    void testSaveOwner() {
        when(ownerRepository.save(any(Owner.class))).thenReturn(testOwner);

        clinicService.saveOwner(testOwner);

        verify(ownerRepository, times(1)).save(testOwner);
    }

    @Test
    void testSaveOwnerThrowsException() {
        when(ownerRepository.save(any(Owner.class))).thenThrow(new DataAccessException("Database error") {});

        assertThrows(DataAccessException.class, () -> clinicService.saveOwner(testOwner));
        verify(ownerRepository, times(1)).save(testOwner);
    }

//     @Test
//     void testSaveVisit() {
//         when(visitRepository.save(any(Visit.class))).thenReturn(testVisit);
// 
//         clinicService.saveVisit(testVisit);
// 
//         verify(visitRepository, times(1)).save(testVisit);
//     }

//     @Test
//     void testSaveVisitThrowsException() {
//         when(visitRepository.save(any(Visit.class))).thenThrow(new DataAccessException("Database error") {});
// 
//         assertThrows(DataAccessException.class, () -> clinicService.saveVisit(testVisit));
//         verify(visitRepository, times(1)).save(testVisit);
//     }

    @Test
    void testFindPetById() {
        when(petRepository.findById(1)).thenReturn(testPet);

        Pet result = clinicService.findPetById(1);

        assertNotNull(result);
        assertEquals(testPet.getId(), result.getId());
        assertEquals(testPet.getName(), result.getName());
        verify(petRepository, times(1)).findById(1);
    }

    @Test
    void testFindPetByIdThrowsException() {
        when(petRepository.findById(anyInt())).thenThrow(new DataAccessException("Database error") {});

        assertThrows(DataAccessException.class, () -> clinicService.findPetById(999));
        verify(petRepository, times(1)).findById(999);
    }

//     @Test
//     void testSavePet() {
//         when(petRepository.save(any(Pet.class))).thenReturn(testPet);
// 
//         clinicService.savePet(testPet);
// 
//         verify(petRepository, times(1)).save(testPet);
//     }

//     @Test
//     void testSavePetThrowsException() {
//         when(petRepository.save(any(Pet.class))).thenThrow(new DataAccessException("Database error") {});
// 
//         assertThrows(DataAccessException.class, () -> clinicService.savePet(testPet));
//         verify(petRepository, times(1)).save(testPet);
//     }

    @Test
    void testFindVets() {
        Collection<Vet> vets = Arrays.asList(testVet);
        when(vetRepository.findAll()).thenReturn(vets);

        Collection<Vet> result = clinicService.findVets();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(testVet));
        verify(vetRepository, times(1)).findAll();
    }

    @Test
    void testFindVetsEmpty() {
        when(vetRepository.findAll()).thenReturn(Collections.emptyList());

        Collection<Vet> result = clinicService.findVets();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(vetRepository, times(1)).findAll();
    }

    @Test
    void testFindVetsThrowsException() {
        when(vetRepository.findAll()).thenThrow(new DataAccessException("Database error") {});

        assertThrows(DataAccessException.class, () -> clinicService.findVets());
        verify(vetRepository, times(1)).findAll();
    }
}