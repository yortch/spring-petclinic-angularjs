package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.ClinicService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VisitResourceTest {

    @Mock
    private ClinicService clinicService;

    @Mock
    private Pet pet;

    @Mock
    private Visit visit;

    private VisitResource visitResource;

    @BeforeEach
    void setUp() {
        visitResource = new VisitResource(clinicService);
    }

    @Test
    void testConstructor() {
        VisitResource resource = new VisitResource(clinicService);
        assertNotNull(resource);
    }

    @Test
    void testCreateWithValidVisitAndPetId() {
        int petId = 1;
        when(clinicService.findPetById(petId)).thenReturn(pet);

        visitResource.create(visit, petId);

        verify(clinicService, times(1)).findPetById(petId);
        verify(pet, times(1)).addVisit(visit);
        verify(clinicService, times(1)).saveVisit(visit);
    }

    @Test
    void testCreateWithDifferentPetId() {
        int petId = 42;
        when(clinicService.findPetById(petId)).thenReturn(pet);

        visitResource.create(visit, petId);

        verify(clinicService, times(1)).findPetById(petId);
        verify(pet, times(1)).addVisit(visit);
        verify(clinicService, times(1)).saveVisit(visit);
    }

//     @Test
//     void testVisitsReturnsVisitsCollection() {
//         int petId = 1;
//         Set<Visit> expectedVisits = new HashSet<>();
//         expectedVisits.add(new Visit());
//         when(clinicService.findPetById(petId)).thenReturn(pet);
//         when(pet.getVisits()).thenReturn(expectedVisits);
// 
//         Object result = visitResource.visits(petId);
// 
//         assertSame(expectedVisits, result);
//         verify(clinicService, times(1)).findPetById(petId);
//         verify(pet, times(1)).getVisits();
//     }

//     @Test
//     void testVisitsWithEmptyVisitsCollection() {
//         int petId = 1;
//         Set<Visit> emptyVisits = new HashSet<>();
//         when(clinicService.findPetById(petId)).thenReturn(pet);
//         when(pet.getVisits()).thenReturn(emptyVisits);
// 
//         Object result = visitResource.visits(petId);
// 
//         assertNotNull(result);
//         assertEquals(emptyVisits, result);
//         verify(clinicService, times(1)).findPetById(petId);
//     }

//     @Test
//     void testVisitsWithDifferentPetId() {
//         int petId = 99;
//         Set<Visit> visits = new HashSet<>();
//         when(clinicService.findPetById(petId)).thenReturn(pet);
//         when(pet.getVisits()).thenReturn(visits);
// 
//         Object result = visitResource.visits(petId);
// 
//         assertSame(visits, result);
//         verify(clinicService, times(1)).findPetById(petId);
//     }

//     @Test
//     void testVisitsWithMultipleVisits() {
//         int petId = 1;
//         Set<Visit> visits = new HashSet<>();
//         Visit visit1 = new Visit();
//         Visit visit2 = new Visit();
//         visits.add(visit1);
//         visits.add(visit2);
//         when(clinicService.findPetById(petId)).thenReturn(pet);
//         when(pet.getVisits()).thenReturn(visits);
// 
//         Object result = visitResource.visits(petId);
// 
//         assertEquals(visits, result);
//         assertEquals(2, ((Set<Visit>) result).size());
//         verify(clinicService, times(1)).findPetById(petId);
//     }
}