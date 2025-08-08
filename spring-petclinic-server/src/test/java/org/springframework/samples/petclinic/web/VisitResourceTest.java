package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.ClinicService;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitResourceTest {

    @Mock
    private ClinicService clinicService;

    private VisitResource visitResource;

    @BeforeEach
    void setUp() {
        visitResource = new VisitResource(clinicService);
    }

    @Test
    void create_AddsVisitAndSaves() {
        int petId = 5;
        Pet pet = new Pet();
        when(clinicService.findPetById(petId)).thenReturn(pet);

        Visit visit = new Visit();
        visit.setDescription("annual checkup");

        visitResource.create(visit, petId);

        assertNotNull(pet.getVisits());
        assertEquals(1, pet.getVisits().size());
        assertTrue(pet.getVisits().contains(visit));
        verify(clinicService, times(1)).findPetById(petId);
        verify(clinicService, times(1)).saveVisit(visit);
        verifyNoMoreInteractions(clinicService);
    }

    @Test
    void visits_ReturnsVisitsForPet() {
        int petId = 7;
        Pet pet = new Pet();
        Visit v1 = new Visit();
        v1.setDescription("dentist");
        Visit v2 = new Visit();
        v2.setDescription("grooming");
        pet.addVisit(v1);
        pet.addVisit(v2);

        when(clinicService.findPetById(petId)).thenReturn(pet);

        Object result = visitResource.visits(petId);

        assertNotNull(result);
        assertTrue(result instanceof Collection);
        Collection<?> visits = (Collection<?>) result;
        assertEquals(2, visits.size());
        assertTrue(visits.contains(v1));
        assertTrue(visits.contains(v2));
        verify(clinicService, times(1)).findPetById(petId);
        verifyNoMoreInteractions(clinicService);
    }

    @Test
    void visits_ReturnsEmptyWhenNoVisits() {
        int petId = 9;
        Pet pet = new Pet();
        when(clinicService.findPetById(petId)).thenReturn(pet);

        Object result = visitResource.visits(petId);

        assertNotNull(result);
        assertTrue(result instanceof Collection);
        Collection<?> visits = (Collection<?>) result;
        assertTrue(visits.isEmpty());
        verify(clinicService, times(1)).findPetById(petId);
        verifyNoMoreInteractions(clinicService);
    }
}