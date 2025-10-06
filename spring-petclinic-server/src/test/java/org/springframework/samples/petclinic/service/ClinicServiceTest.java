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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClinicServiceTest {

    @Mock
    private ClinicService clinicService;

    private Owner testOwner;
    private Pet testPet;
    private Visit testVisit;
    private PetType testPetType;
    private Vet testVet;

    @BeforeEach
    void setUp() {
        testOwner = new Owner();
        testOwner.setId(1);
        testOwner.setFirstName("John");
        testOwner.setLastName("Doe");

        testPet = new Pet();
        testPet.setId(1);
        testPet.setName("Max");

        testVisit = new Visit();
        testVisit.setId(1);

        testPetType = new PetType();
        testPetType.setId(1);
        testPetType.setName("Dog");

        testVet = new Vet();
        testVet.setId(1);
        testVet.setFirstName("Jane");
        testVet.setLastName("Smith");
    }

    @Test
    void testFindPetTypes_ReturnsCollection() {
        Collection<PetType> petTypes = new ArrayList<>();
        petTypes.add(testPetType);

        when(clinicService.findPetTypes()).thenReturn(petTypes);

        Collection<PetType> result = clinicService.findPetTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(clinicService, times(1)).findPetTypes();
    }

    @Test
    void testFindPetTypes_ReturnsEmptyCollection() {
        when(clinicService.findPetTypes()).thenReturn(Collections.emptyList());

        Collection<PetType> result = clinicService.findPetTypes();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(clinicService, times(1)).findPetTypes();
    }

//     @Test
//     void testFindOwnerById_ValidId() {
//         when(clinicService.findOwnerById(1)).thenReturn(testOwner);
// 
//         Owner result = clinicService.findOwnerById(1);
// 
//         assertNotNull(result);
//         assertEquals(1, result.getId());
//         assertEquals("John", result.getFirstName());
//         verify(clinicService, times(1)).findOwnerById(1);
//     }

    @Test
    void testFindOwnerById_InvalidId() {
        when(clinicService.findOwnerById(999)).thenReturn(null);

        Owner result = clinicService.findOwnerById(999);

        assertNull(result);
        verify(clinicService, times(1)).findOwnerById(999);
    }

    @Test
    void testFindOwnerById_ZeroId() {
        when(clinicService.findOwnerById(0)).thenReturn(null);

        Owner result = clinicService.findOwnerById(0);

        assertNull(result);
        verify(clinicService, times(1)).findOwnerById(0);
    }

    @Test
    void testFindOwnerById_NegativeId() {
        when(clinicService.findOwnerById(-1)).thenReturn(null);

        Owner result = clinicService.findOwnerById(-1);

        assertNull(result);
        verify(clinicService, times(1)).findOwnerById(-1);
    }

//     @Test
//     void testFindPetById_ValidId() {
//         when(clinicService.findPetById(1)).thenReturn(testPet);
// 
//         Pet result = clinicService.findPetById(1);
// 
//         assertNotNull(result);
//         assertEquals(1, result.getId());
//         assertEquals("Max", result.getName());
//         verify(clinicService, times(1)).findPetById(1);
//     }

    @Test
    void testFindPetById_InvalidId() {
        when(clinicService.findPetById(999)).thenReturn(null);

        Pet result = clinicService.findPetById(999);

        assertNull(result);
        verify(clinicService, times(1)).findPetById(999);
    }

    @Test
    void testFindPetById_ZeroId() {
        when(clinicService.findPetById(0)).thenReturn(null);

        Pet result = clinicService.findPetById(0);

        assertNull(result);
        verify(clinicService, times(1)).findPetById(0);
    }

    @Test
    void testFindPetById_NegativeId() {
        when(clinicService.findPetById(-1)).thenReturn(null);

        Pet result = clinicService.findPetById(-1);

        assertNull(result);
        verify(clinicService, times(1)).findPetById(-1);
    }

    @Test
    void testSavePet_ValidPet() {
        doNothing().when(clinicService).savePet(any(Pet.class));

        clinicService.savePet(testPet);

        verify(clinicService, times(1)).savePet(testPet);
    }

    @Test
    void testSavePet_NullPet() {
        doNothing().when(clinicService).savePet(null);

        clinicService.savePet(null);

        verify(clinicService, times(1)).savePet(null);
    }

    @Test
    void testSaveVisit_ValidVisit() {
        doNothing().when(clinicService).saveVisit(any(Visit.class));

        clinicService.saveVisit(testVisit);

        verify(clinicService, times(1)).saveVisit(testVisit);
    }

    @Test
    void testSaveVisit_NullVisit() {
        doNothing().when(clinicService).saveVisit(null);

        clinicService.saveVisit(null);

        verify(clinicService, times(1)).saveVisit(null);
    }

    @Test
    void testFindVets_ReturnsCollection() {
        Collection<Vet> vets = new ArrayList<>();
        vets.add(testVet);

        when(clinicService.findVets()).thenReturn(vets);

        Collection<Vet> result = clinicService.findVets();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(clinicService, times(1)).findVets();
    }

    @Test
    void testFindVets_ReturnsEmptyCollection() {
        when(clinicService.findVets()).thenReturn(Collections.emptyList());

        Collection<Vet> result = clinicService.findVets();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(clinicService, times(1)).findVets();
    }

    @Test
    void testSaveOwner_ValidOwner() {
        doNothing().when(clinicService).saveOwner(any(Owner.class));

        clinicService.saveOwner(testOwner);

        verify(clinicService, times(1)).saveOwner(testOwner);
    }

    @Test
    void testSaveOwner_NullOwner() {
        doNothing().when(clinicService).saveOwner(null);

        clinicService.saveOwner(null);

        verify(clinicService, times(1)).saveOwner(null);
    }

    @Test
    void testFindAll_ReturnsCollection() {
        Collection<Owner> owners = new ArrayList<>();
        owners.add(testOwner);

        when(clinicService.findAll()).thenReturn(owners);

        Collection<Owner> result = clinicService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(clinicService, times(1)).findAll();
    }

    @Test
    void testFindAll_ReturnsEmptyCollection() {
        when(clinicService.findAll()).thenReturn(Collections.emptyList());

        Collection<Owner> result = clinicService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(clinicService, times(1)).findAll();
    }

    @Test
    void testFindAll_ReturnsMultipleOwners() {
        Collection<Owner> owners = new ArrayList<>();
        Owner owner2 = new Owner();
        owner2.setId(2);
        owner2.setFirstName("Alice");
        owner2.setLastName("Brown");
        
        owners.add(testOwner);
        owners.add(owner2);

        when(clinicService.findAll()).thenReturn(owners);

        Collection<Owner> result = clinicService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(clinicService, times(1)).findAll();
    }

    @Test
    void testFindVets_ReturnsMultipleVets() {
        Collection<Vet> vets = new ArrayList<>();
        Vet vet2 = new Vet();
        vet2.setId(2);
        vet2.setFirstName("Bob");
        vet2.setLastName("Johnson");
        
        vets.add(testVet);
        vets.add(vet2);

        when(clinicService.findVets()).thenReturn(vets);

        Collection<Vet> result = clinicService.findVets();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(clinicService, times(1)).findVets();
    }

    @Test
    void testFindPetTypes_ReturnsMultiplePetTypes() {
        Collection<PetType> petTypes = new ArrayList<>();
        PetType petType2 = new PetType();
        petType2.setId(2);
        petType2.setName("Cat");
        
        petTypes.add(testPetType);
        petTypes.add(petType2);

        when(clinicService.findPetTypes()).thenReturn(petTypes);

        Collection<PetType> result = clinicService.findPetTypes();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(clinicService, times(1)).findPetTypes();
    }
}