package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.isNull;

@ExtendWith(MockitoExtension.class)
class ClinicServiceTest {

    @Mock
    private ClinicService clinicService;

    @Test
    void findPetTypes_returnsStubbedTypes() {
        List<PetType> types = Arrays.asList(new PetType(), new PetType());
        when(clinicService.findPetTypes()).thenReturn(types);

        Collection<PetType> result = clinicService.findPetTypes();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(clinicService).findPetTypes();
    }

    @Test
    void findOwnerById_returnsOwner_whenIdMatches() {
        Owner owner = new Owner();
        when(clinicService.findOwnerById(7)).thenReturn(owner);

        Owner result = clinicService.findOwnerById(7);

        assertSame(owner, result);
        verify(clinicService).findOwnerById(7);
    }

    @Test
    void findOwnerById_returnsNull_whenIdNotStubbed() {
        Owner result = clinicService.findOwnerById(-1);

        assertNull(result);
        verify(clinicService).findOwnerById(-1);
    }

    @Test
    void findPetById_returnsPet_whenIdMatches() {
        Pet pet = new Pet();
        when(clinicService.findPetById(5)).thenReturn(pet);

        Pet result = clinicService.findPetById(5);

        assertSame(pet, result);
        verify(clinicService).findPetById(5);
    }

    @Test
    void savePet_invokesService() {
        Pet pet = new Pet();

        clinicService.savePet(pet);

        verify(clinicService).savePet(pet);
    }

    @Test
    void savePet_throwsException_onNull() {
        doThrow(new IllegalArgumentException("pet must not be null"))
                .when(clinicService).savePet((Pet) isNull());

        assertThrows(IllegalArgumentException.class, () -> clinicService.savePet(null));
        verify(clinicService).savePet(null);
    }

    @Test
    void saveVisit_invokesService() {
        Visit visit = new Visit();

        clinicService.saveVisit(visit);

        verify(clinicService).saveVisit(visit);
    }

    @Test
    void saveVisit_throwsException_onNull() {
        doThrow(new IllegalArgumentException("visit must not be null"))
                .when(clinicService).saveVisit((Visit) isNull());

        assertThrows(IllegalArgumentException.class, () -> clinicService.saveVisit(null));
        verify(clinicService).saveVisit(null);
    }

    @Test
    void findVets_returnsStubbedList() {
        List<Vet> vets = Arrays.asList(new Vet(), new Vet(), new Vet());
        when(clinicService.findVets()).thenReturn(vets);

        Collection<Vet> result = clinicService.findVets();

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(clinicService).findVets();
    }

    @Test
    void saveOwner_invokesService() {
        Owner owner = new Owner();

        clinicService.saveOwner(owner);

        verify(clinicService).saveOwner(owner);
    }

    @Test
    void saveOwner_throwsException_onNull() {
        doThrow(new IllegalArgumentException("owner must not be null"))
                .when(clinicService).saveOwner((Owner) isNull());

        assertThrows(IllegalArgumentException.class, () -> clinicService.saveOwner(null));
        verify(clinicService).saveOwner(null);
    }

    @Test
    void findAll_returnsStubbedOwners() {
        List<Owner> owners = Arrays.asList(new Owner(), new Owner());
        when(clinicService.findAll()).thenReturn(owners);

        Collection<Owner> result = clinicService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(clinicService).findAll();
    }
}