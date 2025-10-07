package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.ClinicService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VisitResourceTest {

    @Mock
    private ClinicService clinicService;

    @Mock
    private Pet pet;

    @InjectMocks
    private VisitResource visitResource;

    private Visit visit;

    @BeforeEach
    void setUp() {
        visit = new Visit();
        visit.setDate(new Date());
        visit.setDescription("Regular checkup");
    }

    @Test
    void testConstructor() {
        VisitResource resource = new VisitResource(clinicService);
        assertNotNull(resource);
    }

    @Test
    void testCreate_Success() {
        int petId = 1;
        when(clinicService.findPetById(petId)).thenReturn(pet);

        visitResource.create(visit, petId);

        verify(clinicService, times(1)).findPetById(petId);
        verify(pet, times(1)).addVisit(visit);
        verify(clinicService, times(1)).saveVisit(visit);
    }

    @Test
    void testCreate_WithZeroPetId() {
        int petId = 0;
        when(clinicService.findPetById(petId)).thenReturn(pet);

        visitResource.create(visit, petId);

        verify(clinicService, times(1)).findPetById(petId);
        verify(pet, times(1)).addVisit(visit);
        verify(clinicService, times(1)).saveVisit(visit);
    }

    @Test
    void testCreate_WithNegativePetId() {
        int petId = -1;
        when(clinicService.findPetById(petId)).thenReturn(pet);

        visitResource.create(visit, petId);

        verify(clinicService, times(1)).findPetById(petId);
        verify(pet, times(1)).addVisit(visit);
        verify(clinicService, times(1)).saveVisit(visit);
    }

    @Test
    void testCreate_PetNotFound() {
        int petId = 999;
        when(clinicService.findPetById(petId)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> {
            visitResource.create(visit, petId);
        });

        verify(clinicService, times(1)).findPetById(petId);
        verify(clinicService, never()).saveVisit(any(Visit.class));
    }

//     @Test
//     void testVisits_Success() {
//         int petId = 1;
//         Collection<Visit> expectedVisits = new ArrayList<>();
//         Visit visit1 = new Visit();
//         visit1.setDescription("Visit 1");
//         Visit visit2 = new Visit();
//         visit2.setDescription("Visit 2");
//         expectedVisits.add(visit1);
//         expectedVisits.add(visit2);
// 
//         when(clinicService.findPetById(petId)).thenReturn(pet);
//         when(pet.getVisits()).thenReturn(expectedVisits);
// 
//         Object result = visitResource.visits(petId);
// 
//         assertNotNull(result);
//         assertEquals(expectedVisits, result);
//         verify(clinicService, times(1)).findPetById(petId);
//         verify(pet, times(1)).getVisits();
//     }

//     @Test
//     void testVisits_EmptyVisitsList() {
//         int petId = 1;
//         Collection<Visit> emptyVisits = new ArrayList<>();
// 
//         when(clinicService.findPetById(petId)).thenReturn(pet);
//         when(pet.getVisits()).thenReturn(emptyVisits);
// 
//         Object result = visitResource.visits(petId);
// 
//         assertNotNull(result);
//         assertEquals(emptyVisits, result);
//         assertEquals(0, ((Collection<?>) result).size());
//         verify(clinicService, times(1)).findPetById(petId);
//         verify(pet, times(1)).getVisits();
//     }

//     @Test
//     void testVisits_WithZeroPetId() {
//         int petId = 0;
//         Collection<Visit> expectedVisits = new ArrayList<>();
// 
//         when(clinicService.findPetById(petId)).thenReturn(pet);
//         when(pet.getVisits()).thenReturn(expectedVisits);
// 
//         Object result = visitResource.visits(petId);
// 
//         assertNotNull(result);
//         verify(clinicService, times(1)).findPetById(petId);
//     }

    @Test
    void testVisits_PetNotFound() {
        int petId = 999;
        when(clinicService.findPetById(petId)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> {
            visitResource.visits(petId);
        });

        verify(clinicService, times(1)).findPetById(petId);
    }
}