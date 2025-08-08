package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    void findPetTypes_returnsCollectionFromRepository() {
        PetType type = new PetType();
        when(petRepository.findPetTypes()).thenReturn(Collections.singletonList(type));

        Collection<PetType> result = clinicService.findPetTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(type));
        verify(petRepository).findPetTypes();
        verifyNoMoreInteractions(petRepository, vetRepository, ownerRepository, visitRepository);
    }

    @Test
    void findOwnerById_returnsOwner_whenPresent() {
        Owner owner = new Owner();
        when(ownerRepository.findById(1)).thenReturn(Optional.of(owner));

        Owner result = clinicService.findOwnerById(1);

        assertSame(owner, result);
        verify(ownerRepository).findById(1);
        verifyNoMoreInteractions(ownerRepository, petRepository, vetRepository, visitRepository);
    }

    @Test
    void findOwnerById_throwsNoSuchElementException_whenAbsent() {
        when(ownerRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> clinicService.findOwnerById(2));
        verify(ownerRepository).findById(2);
        verifyNoMoreInteractions(ownerRepository, petRepository, vetRepository, visitRepository);
    }

    @Test
    void findOwnerById_propagatesDataAccessException_whenRepositoryThrows() {
        when(ownerRepository.findById(3)).thenThrow(new DataRetrievalFailureException("boom"));

        DataRetrievalFailureException ex = assertThrows(DataRetrievalFailureException.class,
                () -> clinicService.findOwnerById(3));
        assertEquals("boom", ex.getMessage());
        verify(ownerRepository).findById(3);
        verifyNoMoreInteractions(ownerRepository, petRepository, vetRepository, visitRepository);
    }

    @Test
    void findAll_returnsOwnersFromRepository() {
        Owner o1 = new Owner();
        Owner o2 = new Owner();
        when(ownerRepository.findAll()).thenReturn(Arrays.asList(o1, o2));

        Collection<Owner> result = clinicService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(o1));
        assertTrue(result.contains(o2));
        verify(ownerRepository).findAll();
        verifyNoMoreInteractions(ownerRepository, petRepository, vetRepository, visitRepository);
    }

    @Test
    void saveOwner_delegatesToRepository() {
        Owner owner = new Owner();

        clinicService.saveOwner(owner);

        verify(ownerRepository).save(owner);
        verifyNoMoreInteractions(ownerRepository, petRepository, vetRepository, visitRepository);
    }

    @Test
    void saveVisit_delegatesToRepository() {
        Visit visit = new Visit();

        clinicService.saveVisit(visit);

        verify(visitRepository).save(visit);
        verifyNoMoreInteractions(visitRepository, ownerRepository, petRepository, vetRepository);
    }

    @Test
    void findPetById_returnsPetFromRepository() {
        Pet pet = new Pet();
        when(petRepository.findById(10)).thenReturn(pet);

        Pet result = clinicService.findPetById(10);

        assertSame(pet, result);
        verify(petRepository).findById(10);
        verifyNoMoreInteractions(petRepository, ownerRepository, vetRepository, visitRepository);
    }

    @Test
    void savePet_delegatesToRepository() {
        Pet pet = new Pet();

        clinicService.savePet(pet);

        verify(petRepository).save(pet);
        verifyNoMoreInteractions(petRepository, ownerRepository, vetRepository, visitRepository);
    }

    @Test
    void findVets_returnsVetsFromRepository() {
        Vet vet = new Vet();
        when(vetRepository.findAll()).thenReturn(Collections.singletonList(vet));

        Collection<Vet> result = clinicService.findVets();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(vet));
        verify(vetRepository).findAll();
        verifyNoMoreInteractions(vetRepository, ownerRepository, petRepository, visitRepository);
    }
}