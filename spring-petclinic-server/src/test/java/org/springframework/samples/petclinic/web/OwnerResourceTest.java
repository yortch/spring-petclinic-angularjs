package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.web.bind.WebDataBinder;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerResourceTest {

    @Mock
    private ClinicService clinicService;

    @InjectMocks
    private OwnerResource ownerResource;

    @Test
    void setAllowedFields_disallowsId() {
        WebDataBinder binder = new WebDataBinder(new Owner());

        ownerResource.setAllowedFields(binder);

        String[] disallowed = binder.getDisallowedFields();
        assertNotNull(disallowed);
        assertTrue(Arrays.asList(disallowed).contains("id"));
    }

    @Test
    void createOwner_callsServiceSave() {
        Owner owner = new Owner();

        ownerResource.createOwner(owner);

        verify(clinicService, times(1)).saveOwner(owner);
        verifyNoMoreInteractions(clinicService);
    }

    @Test
    void findOwner_returnsOwnerFromService() {
        int ownerId = 5;
        Owner owner = new Owner();
        when(clinicService.findOwnerById(ownerId)).thenReturn(owner);

        Owner result = ownerResource.findOwner(ownerId);

        assertSame(owner, result);
        verify(clinicService).findOwnerById(ownerId);
        verifyNoMoreInteractions(clinicService);
    }

    @Test
    void findAll_returnsListFromService() {
        Owner o1 = new Owner();
        Owner o2 = new Owner();
        Collection<Owner> expected = Arrays.asList(o1, o2);
        when(clinicService.findAll()).thenReturn(expected);

        Collection<Owner> result = ownerResource.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(o1));
        assertTrue(result.contains(o2));
        verify(clinicService).findAll();
        verifyNoMoreInteractions(clinicService);
    }

    @Test
    void updateOwner_copiesFieldsAndSaves() {
        int ownerId = 10;
        Owner existing = new Owner();
        existing.setFirstName("OldFirst");
        existing.setLastName("OldLast");
        existing.setCity("OldCity");
        existing.setAddress("OldAddr");
        existing.setTelephone("000");

        Owner request = new Owner();
        request.setFirstName("NewFirst");
        request.setLastName("NewLast");
        request.setCity("NewCity");
        request.setAddress("NewAddr");
        request.setTelephone("111");

        when(clinicService.findOwnerById(ownerId)).thenReturn(existing);

        Owner updated = ownerResource.updateOwner(ownerId, request);

        assertSame(existing, updated);
        assertEquals("NewFirst", updated.getFirstName());
        assertEquals("NewLast", updated.getLastName());
        assertEquals("NewCity", updated.getCity());
        assertEquals("NewAddr", updated.getAddress());
        assertEquals("111", updated.getTelephone());
        verify(clinicService).findOwnerById(ownerId);
        verify(clinicService).saveOwner(existing);
        verifyNoMoreInteractions(clinicService);
    }
}