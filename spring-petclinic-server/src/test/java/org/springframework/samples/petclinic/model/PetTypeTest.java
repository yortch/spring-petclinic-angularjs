/*
 * Copyright 2002-2013 the original author or authors.
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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link PetType}.
 *
 * Tests pet type functionality including name management and validation
 * as specified in the PRD for supporting multiple pet types (dog, cat, bird, etc.).
 */
public class PetTypeTest {

    @Test
    public void getName_should_returnNull_when_notSet() {
        // Arrange
        PetType petType = new PetType();

        // Act & Assert
        assertThat(petType.getName()).isNull();
    }

    @Test
    public void setName_should_storeName_when_validNameProvided() {
        // Arrange
        PetType petType = new PetType();
        String expectedName = "Dog";

        // Act
        petType.setName(expectedName);

        // Assert
        assertThat(petType.getName()).isEqualTo(expectedName);
    }

    @Test
    public void toString_should_returnName_when_nameIsSet() {
        // Arrange
        PetType petType = new PetType();
        String expectedName = "Cat";
        petType.setName(expectedName);

        // Act
        String result = petType.toString();

        // Assert
        assertThat(result).isEqualTo(expectedName);
    }

    @Test
    public void toString_should_returnNull_when_nameIsNull() {
        // Arrange
        PetType petType = new PetType();

        // Act
        String result = petType.toString();

        // Assert
        assertThat(result).isNull();
    }

    @Test
    public void petType_should_inheritBaseEntityFunctionality_when_created() {
        // Arrange
        PetType petType = new PetType();

        // Act & Assert
        assertThat(petType.getId()).isNull();
        assertThat(petType.isNew()).isTrue();
        
        petType.setId(1);
        assertThat(petType.getId()).isEqualTo(1);
        assertThat(petType.isNew()).isFalse();
    }

    @Test
    public void petType_should_supportNameUpdate_when_nameChanges() {
        // Arrange
        PetType petType = new PetType();
        String originalName = "Puppy";
        String newName = "Dog";

        petType.setName(originalName);

        // Act
        petType.setName(newName);

        // Assert
        assertThat(petType.getName()).isEqualTo(newName);
        assertThat(petType.getName()).isNotEqualTo(originalName);
    }

    @Test
    public void petType_should_allowEmptyName_when_emptyStringProvided() {
        // Arrange
        PetType petType = new PetType();

        // Act
        petType.setName("");

        // Assert
        assertThat(petType.getName()).isEmpty();
    }

    @Test
    public void petType_should_allowNullName_when_nullProvided() {
        // Arrange
        PetType petType = new PetType();
        petType.setName("Initial");

        // Act
        petType.setName(null);

        // Assert
        assertThat(petType.getName()).isNull();
    }

    /**
     * Parameterized test for common pet types as mentioned in PRD:
     * "Support multiple pet types (minimum: dog, cat, bird, hamster, snake, lizard)"
     */
    @Nested
    public class CommonPetTypesTest {

        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][]{
                    {"dog"},
                    {"cat"},
                    {"bird"},
                    {"hamster"},
                    {"snake"},
                    {"lizard"},
                    {"Dog"},
                    {"Cat"},
                    {"Bird"},
                    {"Hamster"},
                    {"Snake"},
                    {"Lizard"}
            });
        }
        public String petTypeName;

        @MethodSource("data")
        @ParameterizedTest(name = "petTypeName={0}")
        public void petType_should_supportCommonPetTypes_when_validTypeProvided(String petTypeName) {
            initCommonPetTypesTest(petTypeName);
            // Arrange
            PetType petType = new PetType();

            // Act
            petType.setName(petTypeName);

            // Assert
            assertThat(petType.getName()).isEqualTo(petTypeName);
            assertThat(petType.toString()).isEqualTo(petTypeName);
        }

        public void initCommonPetTypesTest(String petTypeName) {
            this.petTypeName = petTypeName;
        }
    }

    @Test
    public void petType_should_supportPetAssociation_when_usedWithPet() {
        // Arrange
        PetType dogType = new PetType();
        dogType.setName("Dog");
        dogType.setId(1);

        Pet pet = new Pet();
        pet.setName("Buddy");

        // Act
        pet.setType(dogType);

        // Assert
        assertThat(pet.getType()).isEqualTo(dogType);
        assertThat(pet.getType().getName()).isEqualTo("Dog");
    }

    @Test
    public void petType_should_supportMultiplePetAssociations_when_sharedAcrossPets() {
        // Arrange
        PetType catType = new PetType();
        catType.setName("Cat");
        catType.setId(2);

        Pet cat1 = new Pet();
        cat1.setName("Whiskers");
        
        Pet cat2 = new Pet();
        cat2.setName("Mittens");

        // Act
        cat1.setType(catType);
        cat2.setType(catType);

        // Assert
        assertThat(cat1.getType()).isEqualTo(catType);
        assertThat(cat2.getType()).isEqualTo(catType);
        assertThat(cat1.getType().getName()).isEqualTo("Cat");
        assertThat(cat2.getType().getName()).isEqualTo("Cat");
    }
}