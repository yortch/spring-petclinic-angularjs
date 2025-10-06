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
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    void setAllowedFields_shouldSetIdAsDisallowedField() {
        ownerResource.setAllowedFields(dataBinder);
        
        verify(dataBinder, times(1)).setDisallowedFields("id");
    }

    @Test
    void createOwner_shouldCallSaveOwner() {
        ownerResource.createOwner(testOwner);
        
        verify(clinicService, times(1)).saveOwner(testOwner);
    }

    @Test
    void createOwner_shouldHandleValidOwner() {
        Owner newOwner = new Owner();
        newOwner.setFirstName("Test");
        newOwner.setLastName("Owner");
        newOwner.setAddress("Test Address");
        newOwner.setCity("Test City");
        newOwner.setTelephone("1111111111");

        ownerResource.createOwner(newOwner);
        
        verify(clinicService, times(1)).saveOwner(eq(newOwner));
    }

    @Test
    void findOwner_shouldReturnOwnerById() {
        when(clinicService.findOwnerById(1)).thenReturn(testOwner);
        
        Owner result = ownerResource.findOwner(1);
        
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getLastName()).isEqualTo("Doe");
        verify(clinicService, times(1)).findOwnerById(1);
    }

    @Test
    void findOwner_shouldCallFindOwnerByIdWithCorrectParameter() {
        when(clinicService.findOwnerById(anyInt())).thenReturn(testOwner);
        
        ownerResource.findOwner(42);
        
        verify(clinicService, times(1)).findOwnerById(42);
    }

    @Test
    void findAll_shouldReturnAllOwners() {
        List<Owner> owners = new ArrayList<>();
        owners.add(testOwner);
        
        Owner owner2 = new Owner();
        owner2.setId(2);
        owner2.setFirstName("Alice");
        owner2.setLastName("Johnson");
        owners.add(owner2);
        
        when(clinicService.findAll()).thenReturn(owners);
        
        Collection<Owner> result = ownerResource.findAll();
        
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).contains(testOwner, owner2);
        verify(clinicService, times(1)).findAll();
    }

    @Test
    void findAll_shouldReturnEmptyCollectionWhenNoOwners() {
        List<Owner> emptyList = new ArrayList<>();
        when(clinicService.findAll()).thenReturn(emptyList);
        
        Collection<Owner> result = ownerResource.findAll();
        
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(clinicService, times(1)).findAll();
    }

    @Test
    void updateOwner_shouldUpdateAllOwnerFields() {
        when(clinicService.findOwnerById(1)).thenReturn(testOwner);
        
        Owner result = ownerResource.updateOwner(1, updatedOwner);
        
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("Jane");
        assertThat(result.getLastName()).isEqualTo("Smith");
        assertThat(result.getAddress()).isEqualTo("456 Oak Ave");
        assertThat(result.getCity()).isEqualTo("Shelbyville");
        assertThat(result.getTelephone()).isEqualTo("0987654321");
        verify(clinicService, times(1)).findOwnerById(1);
        verify(clinicService, times(1)).saveOwner(testOwner);
    }

    @Test
    void updateOwner_shouldReturnUpdatedOwner() {
        when(clinicService.findOwnerById(1)).thenReturn(testOwner);
        
        Owner result = ownerResource.updateOwner(1, updatedOwner);
        
        assertThat(result).isSameAs(testOwner);
    }

    @Test
    void updateOwner_shouldCallSaveOwnerAfterUpdate() {
        when(clinicService.findOwnerById(1)).thenReturn(testOwner);
        
        ownerResource.updateOwner(1, updatedOwner);
        
        verify(clinicService, times(1)).saveOwner(any(Owner.class));
    }

    @Test
    void updateOwner_shouldPreserveOwnerId() {
        when(clinicService.findOwnerById(1)).thenReturn(testOwner);
        
        Owner result = ownerResource.updateOwner(1, updatedOwner);
        
        assertThat(result.getId()).isEqualTo(1);
    }

    @Test
    void updateOwner_shouldUpdateWithDifferentOwnerId() {
        Owner existingOwner = new Owner();
        existingOwner.setId(5);
        existingOwner.setFirstName("Original");
        existingOwner.setLastName("Name");
        
        when(clinicService.findOwnerById(5)).thenReturn(existingOwner);
        
        Owner result = ownerResource.updateOwner(5, updatedOwner);
        
        assertThat(result.getId()).isEqualTo(5);
        assertThat(result.getFirstName()).isEqualTo("Jane");
        verify(clinicService, times(1)).findOwnerById(5);
    }
}