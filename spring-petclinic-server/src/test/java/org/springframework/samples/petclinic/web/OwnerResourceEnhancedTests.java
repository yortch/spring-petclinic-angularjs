/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collection;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for {@link OwnerResource}
 *
 * @author Test Generator
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(OwnerResource.class)
public class OwnerResourceEnhancedTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClinicService clinicService;

    private Owner owner1;
    private Owner owner2;

    @BeforeEach
    public void setup() {
        owner1 = new Owner();
        owner1.setId(1);
        owner1.setFirstName("George");
        owner1.setLastName("Franklin");
        owner1.setAddress("110 W. Liberty St.");
        owner1.setCity("Madison");
        owner1.setTelephone("6085551023");

        owner2 = new Owner();
        owner2.setId(2);
        owner2.setFirstName("Betty");
        owner2.setLastName("Davis");
        owner2.setAddress("638 Cardinal Ave.");
        owner2.setCity("Sun Prairie");
        owner2.setTelephone("6085551749");
    }

    @Test
    public void shouldGetOwnerById() throws Exception {
        given(clinicService.findOwnerById(1)).willReturn(owner1);

        mvc.perform(get("/owners/1").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.firstName").value("George"))
            .andExpect(jsonPath("$.lastName").value("Franklin"))
            .andExpect(jsonPath("$.address").value("110 W. Liberty St."))
            .andExpect(jsonPath("$.city").value("Madison"))
            .andExpect(jsonPath("$.telephone").value("6085551023"));
    }

    @Test
    public void shouldGetAllOwners() throws Exception {
        Collection<Owner> owners = Arrays.asList(owner1, owner2);
        given(clinicService.findAll()).willReturn(owners);

        mvc.perform(get("/owners/list").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].firstName").value("George"))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].firstName").value("Betty"));
    }

    @Test
    public void shouldCreateOwner() throws Exception {
        Owner newOwner = new Owner();
        newOwner.setFirstName("Sam");
        newOwner.setLastName("Schultz");
        newOwner.setAddress("4, Evans Street");
        newOwner.setCity("Wollongong");
        newOwner.setTelephone("4444444444");

        mvc.perform(post("/owners")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(newOwner)))
            .andExpect(status().isCreated());

        verify(clinicService).saveOwner(any(Owner.class));
    }

    @Test
    public void shouldUpdateOwner() throws Exception {
        given(clinicService.findOwnerById(1)).willReturn(owner1);

        Owner updatedOwner = new Owner();
        updatedOwner.setFirstName("George");
        updatedOwner.setLastName("Franklin Updated");
        updatedOwner.setAddress("111 W. Liberty St.");
        updatedOwner.setCity("Madison");
        updatedOwner.setTelephone("6085551024");

        mvc.perform(put("/owners/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedOwner)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.lastName").value("Franklin Updated"))
            .andExpect(jsonPath("$.address").value("111 W. Liberty St."))
            .andExpect(jsonPath("$.telephone").value("6085551024"));

        verify(clinicService).saveOwner(any(Owner.class));
    }

    @Test
    public void shouldRejectInvalidOwnerCreation() throws Exception {
        Owner invalidOwner = new Owner();
        invalidOwner.setFirstName(""); // Invalid: empty first name
        invalidOwner.setLastName("");  // Invalid: empty last name

        mvc.perform(post("/owners")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidOwner)))
            .andExpect(status().isBadRequest());
    }
}
