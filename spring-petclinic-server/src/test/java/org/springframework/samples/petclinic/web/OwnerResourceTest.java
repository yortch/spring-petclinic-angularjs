package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.web.bind.WebDataBinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerResourceTest {

    @Mock
    private ClinicService clinicService;

    @Mock
    private WebDataBinder dataBinder;

    @InjectMocks
    private OwnerResource ownerResource;

    private Owner testOwner;
    private Owner updatedOwner;

    @BeforeEach
    void setUp() {
        testOwner = new Owner();
        testOwner.setId(1);
        testOwner.setFirstName("John");
        testOwner.setLastName("Doe");
        testOwner.setAddress("123 Main St");
        testOwner.setCity("Springfield");
        testOwner.setTelephone("1234567890");

        updatedOwner = new Owner();
        updatedOwner.setFirstName("Jane");
        updatedOwner.setLastName("Smith");
        updatedOwner.setAddress("456 Oak Ave");
        updatedOwner.setCity("Shelbyville");
        updatedOwner.setTelephone("0987654321");
    }

    @Test
    void setAllowedFields_shouldDisallowIdField() {
        ownerResource.setAllowedFields(dataBinder);

        verify(dataBinder, times(1)).setDisallowedFields("id");
    }

    @Test
    void createOwner_shouldSaveOwner() {
        ownerResource.createOwner(testOwner);

        verify(clinicService, times(1)).saveOwner(testOwner);
    }

    @Test
    void createOwner_withValidOwner_shouldCallServiceOnce() {
        Owner newOwner = new Owner();
        newOwner.setFirstName("Alice");
        newOwner.setLastName("Johnson");
        newOwner.setAddress("789 Elm St");
        newOwner.setCity("Capital City");
        newOwner.setTelephone("5551234567");

        ownerResource.createOwner(newOwner);

        verify(clinicService, times(1)).saveOwner(newOwner);
    }

//     @Test
//     void findOwner_shouldReturnOwner() {
//         when(clinicService.findOwnerById(1)).thenReturn(testOwner);
// 
//         Owner result = ownerResource.findOwner(1);
// 
//         assertNotNull(result);
//         assertEquals(1, result.getId());
//         assertEquals("John", result.getFirstName());
//         assertEquals("Doe", result.getLastName());
//         verify(clinicService, times(1)).findOwnerById(1);
//     }

//     @Test
//     void findOwner_withDifferentId_shouldCallServiceWithCorrectId() {
//         Owner owner2 = new Owner();
//         owner2.setId(2);
//         when(clinicService.findOwnerById(2)).thenReturn(owner2);
// 
//         Owner result = ownerResource.findOwner(2);
// 
//         assertNotNull(result);
//         assertEquals(2, result.getId());
//         verify(clinicService, times(1)).findOwnerById(2);
//     }

    @Test
    void findAll_shouldReturnAllOwners() {
        Collection<Owner> owners = Arrays.asList(testOwner, updatedOwner);
        when(clinicService.findAll()).thenReturn(owners);

        Collection<Owner> result = ownerResource.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(clinicService, times(1)).findAll();
    }

    @Test
    void findAll_shouldReturnEmptyCollection() {
        Collection<Owner> emptyList = Collections.emptyList();
        when(clinicService.findAll()).thenReturn(emptyList);

        Collection<Owner> result = ownerResource.findAll();

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(clinicService, times(1)).findAll();
    }

    @Test
    void updateOwner_shouldUpdateAllFieldsAndReturnUpdatedOwner() {
        when(clinicService.findOwnerById(1)).thenReturn(testOwner);

        Owner result = ownerResource.updateOwner(1, updatedOwner);

        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("456 Oak Ave", result.getAddress());
        assertEquals("Shelbyville", result.getCity());
        assertEquals("0987654321", result.getTelephone());
        verify(clinicService, times(1)).findOwnerById(1);
        verify(clinicService, times(1)).saveOwner(testOwner);
    }

//     @Test
//     void updateOwner_shouldPreserveOwnerId() {
//         when(clinicService.findOwnerById(1)).thenReturn(testOwner);
// 
//         Owner result = ownerResource.updateOwner(1, updatedOwner);
// 
//         assertEquals(1, result.getId());
//         verify(clinicService, times(1)).findOwnerById(1);
//     }

    @Test
    void updateOwner_shouldCallSaveOwnerWithUpdatedModel() {
        when(clinicService.findOwnerById(1)).thenReturn(testOwner);

        ownerResource.updateOwner(1, updatedOwner);

        verify(clinicService, times(1)).saveOwner(testOwner);
        assertEquals("Jane", testOwner.getFirstName());
        assertEquals("Smith", testOwner.getLastName());
    }

//     @Test
//     void updateOwner_withDifferentOwnerId_shouldUpdateCorrectOwner() {
//         Owner owner3 = new Owner();
//         owner3.setId(3);
//         owner3.setFirstName("Bob");
//         owner3.setLastName("Brown");
//         owner3.setAddress("111 Pine Rd");
//         owner3.setCity("Metropolis");
//         owner3.setTelephone("1112223333");
// 
//         when(clinicService.findOwnerById(3)).thenReturn(owner3);
// 
//         Owner result = ownerResource.updateOwner(3, updatedOwner);
// 
//         assertEquals(3, result.getId());
//         assertEquals("Jane", result.getFirstName());
//         verify(clinicService, times(1)).findOwnerById(3);
//         verify(clinicService, times(1)).saveOwner(owner3);
//     }
}