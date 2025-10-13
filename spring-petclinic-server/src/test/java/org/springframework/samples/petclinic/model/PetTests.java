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
package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link Pet}
 *
 * @author Test Generator
 */
public class PetTests {

    private Pet pet;
    private Owner owner;
    private PetType petType;

    @BeforeEach
    public void setup() {
        pet = new Pet();
        pet.setName("Leo");
        pet.setBirthDate(new Date());
        
        petType = new PetType();
        petType.setId(1);
        petType.setName("cat");
        pet.setType(petType);
        
        owner = new Owner();
        owner.setFirstName("George");
        owner.setLastName("Franklin");
    }

    @Test
    public void testSetAndGetBirthDate() {
        Date birthDate = new Date();
        pet.setBirthDate(birthDate);
        assertThat(pet.getBirthDate()).isEqualTo(birthDate);
    }

    @Test
    public void testSetAndGetType() {
        PetType dogType = new PetType();
        dogType.setName("dog");
        
        pet.setType(dogType);
        
        assertThat(pet.getType()).isEqualTo(dogType);
        assertThat(pet.getType().getName()).isEqualTo("dog");
    }

    @Test
    public void testGetOwner() {
        owner.addPet(pet);
        
        assertThat(pet.getOwner()).isEqualTo(owner);
    }

    @Test
    public void testAddVisit() {
        Visit visit = new Visit();
        visit.setDate(new Date());
        visit.setDescription("rabies shot");
        
        pet.addVisit(visit);
        
        assertThat(pet.getVisits()).hasSize(1);
        assertThat(pet.getVisits()).contains(visit);
        assertThat(visit.getPet()).isEqualTo(pet);
    }

    @Test
    public void testAddMultipleVisits() {
        Visit visit1 = new Visit();
        visit1.setDate(new Date(System.currentTimeMillis() - 86400000)); // yesterday
        visit1.setDescription("checkup");
        
        Visit visit2 = new Visit();
        visit2.setDate(new Date());
        visit2.setDescription("rabies shot");
        
        pet.addVisit(visit1);
        pet.addVisit(visit2);
        
        List<Visit> visits = pet.getVisits();
        
        assertThat(visits).hasSize(2);
        // Visits should be sorted by date (ascending in the model - oldest first)
        assertThat(visits.get(0).getDescription()).isEqualTo("rabies shot");
    }

    @Test
    public void testGetVisitsReturnsEmptyWhenNoVisits() {
        assertThat(pet.getVisits()).isEmpty();
    }
}
