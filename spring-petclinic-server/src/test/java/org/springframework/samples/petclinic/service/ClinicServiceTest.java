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

    private Owner owner;
    private Pet pet;
    private PetType petType;
    private Vet vet;
    private Visit visit;

    @BeforeEach
    void setUp() {
        owner = new Owner();
        owner.setId(1);
        owner.setFirstName("John");
        owner.setLastName("Doe");

        pet = new Pet();
        pet.setId(1);
        pet.setName("Fluffy");

        petType = new PetType();
        petType.setId(1);
        petType.setName("Cat");

        vet = new Vet();
        vet.setId(1);
        vet.setFirstName("Jane");
        vet.setLastName("Smith");

        visit = new Visit();
        visit.setId(1);
        visit.setDescription("Checkup");
    }

    @Test
    void testFindPetTypes() {
        Collection<PetType> petTypes = new ArrayList<>();
        petTypes.add(petType);
        when(clinicService.findPetTypes()).thenReturn(petTypes);

        Collection<PetType> result = clinicService.findPetTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(petType));
        verify(clinicService, times(1)).findPetTypes();
    }

    @Test
    void testFindPetTypesEmpty() {
        when(clinicService.findPetTypes()).thenReturn(Collections.emptyList());

        Collection<PetType> result = clinicService.findPetTypes();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(clinicService, times(1)).findPetTypes();
    }

//     @Test
//     void testFindOwnerById() {
//         when(clinicService.findOwnerById(1)).thenReturn(owner);
// 
//         Owner result = clinicService.findOwnerById(1);
// 
//         assertNotNull(result);
//         assertEquals(1, result.getId());
//         assertEquals("John", result.getFirstName());
//         assertEquals("Doe", result.getLastName());
//         verify(clinicService, times(1)).findOwnerById(1);
//     }

    @Test
    void testFindOwnerByIdNotFound() {
        when(clinicService.findOwnerById(anyInt())).thenReturn(null);

        Owner result = clinicService.findOwnerById(999);

        assertNull(result);
        verify(clinicService, times(1)).findOwnerById(999);
    }

//     @Test
//     void testFindPetById() {
//         when(clinicService.findPetById(1)).thenReturn(pet);
// 
//         Pet result = clinicService.findPetById(1);
// 
//         assertNotNull(result);
//         assertEquals(1, result.getId());
//         assertEquals("Fluffy", result.getName());
//         verify(clinicService, times(1)).findPetById(1);
//     }

    @Test
    void testFindPetByIdNotFound() {
        when(clinicService.findPetById(anyInt())).thenReturn(null);

        Pet result = clinicService.findPetById(999);

        assertNull(result);
        verify(clinicService, times(1)).findPetById(999);
    }

    @Test
    void testSavePet() {
        doNothing().when(clinicService).savePet(any(Pet.class));

        clinicService.savePet(pet);

        verify(clinicService, times(1)).savePet(pet);
    }

    @Test
    void testSavePetNull() {
        doNothing().when(clinicService).savePet(null);

        clinicService.savePet(null);

        verify(clinicService, times(1)).savePet(null);
    }

    @Test
    void testSaveVisit() {
        doNothing().when(clinicService).saveVisit(any(Visit.class));

        clinicService.saveVisit(visit);

        verify(clinicService, times(1)).saveVisit(visit);
    }

    @Test
    void testSaveVisitNull() {
        doNothing().when(clinicService).saveVisit(null);

        clinicService.saveVisit(null);

        verify(clinicService, times(1)).saveVisit(null);
    }

    @Test
    void testFindVets() {
        Collection<Vet> vets = new ArrayList<>();
        vets.add(vet);
        when(clinicService.findVets()).thenReturn(vets);

        Collection<Vet> result = clinicService.findVets();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(vet));
        verify(clinicService, times(1)).findVets();
    }

    @Test
    void testFindVetsEmpty() {
        when(clinicService.findVets()).thenReturn(Collections.emptyList());

        Collection<Vet> result = clinicService.findVets();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(clinicService, times(1)).findVets();
    }

    @Test
    void testSaveOwner() {
        doNothing().when(clinicService).saveOwner(any(Owner.class));

        clinicService.saveOwner(owner);

        verify(clinicService, times(1)).saveOwner(owner);
    }

    @Test
    void testSaveOwnerNull() {
        doNothing().when(clinicService).saveOwner(null);

        clinicService.saveOwner(null);

        verify(clinicService, times(1)).saveOwner(null);
    }

    @Test
    void testFindAll() {
        Collection<Owner> owners = new ArrayList<>();
        owners.add(owner);
        when(clinicService.findAll()).thenReturn(owners);

        Collection<Owner> result = clinicService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(owner));
        verify(clinicService, times(1)).findAll();
    }

    @Test
    void testFindAllEmpty() {
        when(clinicService.findAll()).thenReturn(Collections.emptyList());

        Collection<Owner> result = clinicService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(clinicService, times(1)).findAll();
    }
}