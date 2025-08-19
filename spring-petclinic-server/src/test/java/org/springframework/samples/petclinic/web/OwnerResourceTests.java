package org.springframework.samples.petclinic.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collection;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(OwnerResource.class)
public class OwnerResourceTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    ClinicService clinicService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldGetOwnersList() throws Exception {
        Owner owner1 = createTestOwner(1, "George", "Franklin");
        Owner owner2 = createTestOwner(2, "Betty", "Davis");
        Collection<Owner> owners = Arrays.asList(owner1, owner2);

        given(clinicService.findAll()).willReturn(owners);

        mvc.perform(get("/owners/list").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("George"))
                .andExpect(jsonPath("$[0].lastName").value("Franklin"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].firstName").value("Betty"))
                .andExpect(jsonPath("$[1].lastName").value("Davis"));

        verify(clinicService, times(1)).findAll();
    }

    @Test
    public void shouldGetOwnerById() throws Exception {
        Owner owner = createTestOwner(1, "George", "Franklin");

        given(clinicService.findOwnerById(1)).willReturn(owner);

        mvc.perform(get("/owners/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("George"))
                .andExpect(jsonPath("$.lastName").value("Franklin"))
                .andExpect(jsonPath("$.address").value("110 W. Liberty St."))
                .andExpect(jsonPath("$.city").value("Madison"))
                .andExpect(jsonPath("$.telephone").value("6085551023"));

        verify(clinicService, times(1)).findOwnerById(1);
    }

    @Test
    public void shouldCreateOwner() throws Exception {
        Owner owner = createTestOwner(null, "John", "Doe");
        String ownerJson = objectMapper.writeValueAsString(owner);

        mvc.perform(post("/owners")
                .content(ownerJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(clinicService, times(1)).saveOwner(any(Owner.class));
    }

    @Test
    public void shouldUpdateOwner() throws Exception {
        Owner existingOwner = createTestOwner(1, "George", "Franklin");
        Owner updatedOwner = createTestOwner(1, "George", "Washington");
        updatedOwner.setAddress("123 Main St.");
        updatedOwner.setCity("Springfield");
        updatedOwner.setTelephone("1234567890");

        given(clinicService.findOwnerById(1)).willReturn(existingOwner);

        String ownerJson = objectMapper.writeValueAsString(updatedOwner);

        mvc.perform(put("/owners/1")
                .content(ownerJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("George"))
                .andExpect(jsonPath("$.lastName").value("Washington"))
                .andExpect(jsonPath("$.address").value("123 Main St."))
                .andExpect(jsonPath("$.city").value("Springfield"))
                .andExpect(jsonPath("$.telephone").value("1234567890"));

        verify(clinicService, times(1)).findOwnerById(1);
        verify(clinicService, times(1)).saveOwner(any(Owner.class));
    }

    @Test
    public void shouldHandleInvalidOwnerCreation() throws Exception {
        Owner invalidOwner = new Owner();
        // Missing required fields to trigger validation errors
        String ownerJson = objectMapper.writeValueAsString(invalidOwner);

        mvc.perform(post("/owners")
                .content(ownerJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(clinicService, never()).saveOwner(any(Owner.class));
    }

    private Owner createTestOwner(Integer id, String firstName, String lastName) {
        Owner owner = new Owner();
        if (id != null) {
            owner.setId(id);
        }
        owner.setFirstName(firstName);
        owner.setLastName(lastName);
        owner.setAddress("110 W. Liberty St.");
        owner.setCity("Madison");
        owner.setTelephone("6085551023");
        return owner;
    }
}