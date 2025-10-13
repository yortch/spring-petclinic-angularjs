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

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link Owner}
 *
 * @author Test Generator
 */
public class OwnerTests {

    private Owner owner;
    private Pet pet1;
    private Pet pet2;

    @Before
    public void setup() {
        owner = new Owner();
        owner.setFirstName("George");
        owner.setLastName("Franklin");
        owner.setAddress("110 W. Liberty St.");
        owner.setCity("Madison");
        owner.setTelephone("6085551023");

        PetType catType = new PetType();
        catType.setName("cat");

        pet1 = new Pet();
        pet1.setName("Leo");
        pet1.setBirthDate(new Date());
        pet1.setType(catType);

        pet2 = new Pet();
        pet2.setName("Basil");
        pet2.setBirthDate(new Date());
        pet2.setType(catType);
    }

    @Test
    public void testSetAndGetAddress() {
        String address = "123 Main Street";
        owner.setAddress(address);
        assertThat(owner.getAddress()).isEqualTo(address);
    }

    @Test
    public void testSetAndGetCity() {
        String city = "Springfield";
        owner.setCity(city);
        assertThat(owner.getCity()).isEqualTo(city);
    }

    @Test
    public void testSetAndGetTelephone() {
        String telephone = "1234567890";
        owner.setTelephone(telephone);
        assertThat(owner.getTelephone()).isEqualTo(telephone);
    }

    @Test
    public void testAddPet() {
        assertThat(owner.getPets()).isEmpty();
        
        owner.addPet(pet1);
        
        assertThat(owner.getPets()).hasSize(1);
        assertThat(owner.getPets()).contains(pet1);
        assertThat(pet1.getOwner()).isEqualTo(owner);
    }

    @Test
    public void testAddMultiplePets() {
        owner.addPet(pet1);
        owner.addPet(pet2);
        
        assertThat(owner.getPets()).hasSize(2);
        assertThat(owner.getPets()).contains(pet1, pet2);
    }

    @Test
    public void testGetPets() {
        owner.addPet(pet1);
        owner.addPet(pet2);
        
        List<Pet> pets = owner.getPets();
        
        assertThat(pets).isNotNull();
        assertThat(pets).hasSize(2);
        // Pets should be sorted by name
        assertThat(pets.get(0).getName()).isEqualTo("Basil");
        assertThat(pets.get(1).getName()).isEqualTo("Leo");
    }

    @Test
    public void testGetPetByName() {
        owner.addPet(pet1);
        owner.addPet(pet2);
        
        Pet found = owner.getPet("Leo");
        
        assertThat(found).isNotNull();
        assertThat(found).isEqualTo(pet1);
        assertThat(found.getName()).isEqualTo("Leo");
    }

    @Test
    public void testGetPetByNameCaseInsensitive() {
        owner.addPet(pet1);
        
        Pet found = owner.getPet("LEO");
        
        assertThat(found).isNotNull();
        assertThat(found).isEqualTo(pet1);
    }

    @Test
    public void testGetPetByNameNotFound() {
        owner.addPet(pet1);
        
        Pet found = owner.getPet("NonExistent");
        
        assertThat(found).isNull();
    }

    @Test
    public void testGetPetIgnoreNew() {
        Pet newPet = new Pet();
        newPet.setName("NewPet");
        newPet.setBirthDate(new Date());
        PetType dogType = new PetType();
        dogType.setName("dog");
        newPet.setType(dogType);
        
        owner.addPet(pet1);
        owner.addPet(newPet);
        
        // Should find existing pet
        Pet found = owner.getPet("Leo", false);
        assertThat(found).isNotNull();
        
        // Should not find new pet when ignoreNew is true
        Pet notFound = owner.getPet("NewPet", true);
        assertThat(notFound).isNull();
    }

    @Test
    public void testToString() {
        owner.setId(1);
        String str = owner.toString();
        
        assertThat(str).contains("George");
        assertThat(str).contains("Franklin");
        assertThat(str).contains("110 W. Liberty St.");
        assertThat(str).contains("Madison");
        assertThat(str).contains("6085551023");
    }
}
