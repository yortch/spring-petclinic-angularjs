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

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link Owner}.
 *
 * Tests owner entity functionality including contact information management,
 * pet associations, and validation constraints as specified in the PRD.
 * Validates functional requirements for owner management operations.
 */
public class OwnerTest {

    private static final Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void getAddress_should_returnNull_when_notSet() {
        // Arrange
        Owner owner = new Owner();

        // Act & Assert
        assertThat(owner.getAddress()).isNull();
    }

    @Test
    public void setAddress_should_storeAddress_when_validAddressProvided() {
        // Arrange
        Owner owner = new Owner();
        String expectedAddress = "123 Main Street";

        // Act
        owner.setAddress(expectedAddress);

        // Assert
        assertThat(owner.getAddress()).isEqualTo(expectedAddress);
    }

    @Test
    public void getCity_should_returnNull_when_notSet() {
        // Arrange
        Owner owner = new Owner();

        // Act & Assert
        assertThat(owner.getCity()).isNull();
    }

    @Test
    public void setCity_should_storeCity_when_validCityProvided() {
        // Arrange
        Owner owner = new Owner();
        String expectedCity = "Springfield";

        // Act
        owner.setCity(expectedCity);

        // Assert
        assertThat(owner.getCity()).isEqualTo(expectedCity);
    }

    @Test
    public void getTelephone_should_returnNull_when_notSet() {
        // Arrange
        Owner owner = new Owner();

        // Act & Assert
        assertThat(owner.getTelephone()).isNull();
    }

    @Test
    public void setTelephone_should_storeTelephone_when_validTelephoneProvided() {
        // Arrange
        Owner owner = new Owner();
        String expectedTelephone = "1234567890";

        // Act
        owner.setTelephone(expectedTelephone);

        // Assert
        assertThat(owner.getTelephone()).isEqualTo(expectedTelephone);
    }

    @Test
    public void getPets_should_returnEmptyList_when_noPetsAdded() {
        // Arrange
        Owner owner = new Owner();

        // Act
        List<Pet> pets = owner.getPets();

        // Assert
        assertThat(pets).isEmpty();
        assertThat(pets).isNotNull();
    }

    @Test
    public void addPet_should_addPetToOwner_when_validPetProvided() {
        // Arrange
        Owner owner = new Owner();
        Pet pet = new Pet();
        pet.setName("Fluffy");

        // Act
        owner.addPet(pet);

        // Assert
        assertThat(owner.getPets()).hasSize(1);
        assertThat(owner.getPets().getFirst()).isEqualTo(pet);
        assertThat(pet.getOwner()).isEqualTo(owner);
    }

    @Test
    public void addPet_should_addMultiplePets_when_multiplePetsProvided() {
        // Arrange
        Owner owner = new Owner();
        Pet pet1 = new Pet();
        pet1.setName("Fluffy");
        Pet pet2 = new Pet();
        pet2.setName("Buddy");

        // Act
        owner.addPet(pet1);
        owner.addPet(pet2);

        // Assert
        assertThat(owner.getPets()).hasSize(2);
        assertThat(owner.getPets()).containsExactly(pet2, pet1); // Sorted alphabetically
    }

    @Test
    public void getPets_should_returnSortedList_when_petsHaveDifferentNames() {
        // Arrange
        Owner owner = new Owner();
        Pet petZ = new Pet();
        petZ.setName("Zebra");
        Pet petA = new Pet();
        petA.setName("Alpha");
        Pet petM = new Pet();
        petM.setName("Max");

        // Act
        owner.addPet(petZ);
        owner.addPet(petA);
        owner.addPet(petM);

        // Assert
        List<Pet> pets = owner.getPets();
        assertThat(pets).hasSize(3);
        assertThat(pets.getFirst().getName()).isEqualTo("Alpha");
        assertThat(pets.get(1).getName()).isEqualTo("Max");
        assertThat(pets.get(2).getName()).isEqualTo("Zebra");
    }

    @Test
    public void getPet_should_returnPet_when_petNameExists() {
        // Arrange
        Owner owner = new Owner();
        Pet pet = new Pet();
        pet.setName("Fluffy");
        owner.addPet(pet);

        // Act
        Pet foundPet = owner.getPet("Fluffy");

        // Assert
        assertThat(foundPet).isEqualTo(pet);
    }

    @Test
    public void getPet_should_returnPet_when_petNameExistsIgnoreCase() {
        // Arrange
        Owner owner = new Owner();
        Pet pet = new Pet();
        pet.setName("Fluffy");
        owner.addPet(pet);

        // Act
        Pet foundPet = owner.getPet("FLUFFY");

        // Assert
        assertThat(foundPet).isEqualTo(pet);
    }

    @Test
    public void getPet_should_returnNull_when_petNameDoesNotExist() {
        // Arrange
        Owner owner = new Owner();
        Pet pet = new Pet();
        pet.setName("Fluffy");
        owner.addPet(pet);

        // Act
        Pet foundPet = owner.getPet("Nonexistent");

        // Assert
        assertThat(foundPet).isNull();
    }

    @Test
    public void getPet_should_returnNull_when_petIsNewAndIgnoreNewIsTrue() {
        // Arrange
        Owner owner = new Owner();
        Pet pet = new Pet();
        pet.setName("Fluffy");
        // Pet is new (id is null)
        owner.addPet(pet);

        // Act
        Pet foundPet = owner.getPet("Fluffy", true);

        // Assert
        assertThat(foundPet).isNull();
    }

    @Test
    public void getPet_should_returnPet_when_petIsNewAndIgnoreNewIsFalse() {
        // Arrange
        Owner owner = new Owner();
        Pet pet = new Pet();
        pet.setName("Fluffy");
        owner.addPet(pet);

        // Act
        Pet foundPet = owner.getPet("Fluffy", false);

        // Assert
        assertThat(foundPet).isEqualTo(pet);
    }

    @Test
    public void toString_should_includeAllProperties_when_called() {
        // Arrange
        Owner owner = new Owner();
        owner.setId(1);
        owner.setFirstName("John");
        owner.setLastName("Doe");
        owner.setAddress("123 Main St");
        owner.setCity("Springfield");
        owner.setTelephone("1234567890");

        // Act
        String result = owner.toString();

        // Assert
        assertThat(result).contains("id = 1");
        assertThat(result).contains("firstName = 'John'");
        assertThat(result).contains("lastName = 'Doe'");
        assertThat(result).contains("address = '123 Main St'");
        assertThat(result).contains("city = 'Springfield'");
        assertThat(result).contains("telephone = '1234567890'");
    }

    // Validation tests as per PRD requirements

    @Test
    public void validation_should_fail_when_addressIsEmpty() {
        // Arrange
        Owner owner = createValidOwner();
        owner.setAddress("");

        // Act
        Set<ConstraintViolation<Owner>> violations = validator.validate(owner);

        // Assert
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("address");
    }

    @Test
    public void validation_should_fail_when_addressIsNull() {
        // Arrange
        Owner owner = createValidOwner();
        owner.setAddress(null);

        // Act
        Set<ConstraintViolation<Owner>> violations = validator.validate(owner);

        // Assert
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("address");
    }

    @Test
    public void validation_should_fail_when_cityIsEmpty() {
        // Arrange
        Owner owner = createValidOwner();
        owner.setCity("");

        // Act
        Set<ConstraintViolation<Owner>> violations = validator.validate(owner);

        // Assert
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("city");
    }

    @Test
    public void validation_should_fail_when_cityIsNull() {
        // Arrange
        Owner owner = createValidOwner();
        owner.setCity(null);

        // Act
        Set<ConstraintViolation<Owner>> violations = validator.validate(owner);

        // Assert
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("city");
    }

    @Test
    public void validation_should_fail_when_telephoneIsEmpty() {
        // Arrange
        Owner owner = createValidOwner();
        owner.setTelephone("");

        // Act
        Set<ConstraintViolation<Owner>> violations = validator.validate(owner);

        // Assert - Empty telephone violates both @NotEmpty and @Digits
        assertThat(violations).hasSize(2);
        assertThat(violations).extracting(v -> v.getPropertyPath().toString()).contains("telephone");
    }

    @Test
    public void validation_should_fail_when_telephoneIsNull() {
        // Arrange
        Owner owner = createValidOwner();
        owner.setTelephone(null);

        // Act
        Set<ConstraintViolation<Owner>> violations = validator.validate(owner);

        // Assert
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("telephone");
    }

    @Test
    public void validation_should_fail_when_telephoneExceedsTenDigits() {
        // Arrange
        Owner owner = createValidOwner();
        owner.setTelephone("12345678901"); // 11 digits

        // Act
        Set<ConstraintViolation<Owner>> violations = validator.validate(owner);

        // Assert
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("telephone");
    }

    @Test
    public void validation_should_pass_when_telephoneIsValidTenDigits() {
        // Arrange
        Owner owner = createValidOwner();
        owner.setTelephone("1234567890");

        // Act
        Set<ConstraintViolation<Owner>> violations = validator.validate(owner);

        // Assert
        assertThat(violations).isEmpty();
    }

    @Test
    public void validation_should_pass_when_telephoneIsValidLessThanTenDigits() {
        // Arrange
        Owner owner = createValidOwner();
        owner.setTelephone("123456");

        // Act
        Set<ConstraintViolation<Owner>> violations = validator.validate(owner);

        // Assert
        assertThat(violations).isEmpty();
    }

    @Test
    public void validation_should_fail_when_firstNameIsEmpty() {
        // Arrange
        Owner owner = createValidOwner();
        owner.setFirstName("");

        // Act
        Set<ConstraintViolation<Owner>> violations = validator.validate(owner);

        // Assert
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("firstName");
    }

    @Test
    public void validation_should_fail_when_lastNameIsEmpty() {
        // Arrange
        Owner owner = createValidOwner();
        owner.setLastName("");

        // Act
        Set<ConstraintViolation<Owner>> violations = validator.validate(owner);

        // Assert
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("lastName");
    }

    @Test
    public void validation_should_pass_when_allRequiredFieldsAreValid() {
        // Arrange
        Owner owner = createValidOwner();

        // Act
        Set<ConstraintViolation<Owner>> violations = validator.validate(owner);

        // Assert
        assertThat(violations).isEmpty();
    }

    /**
     * Parameterized test for telephone validation as per PRD requirement:
     * "Validate telephone numbers to ensure proper format (10 digits maximum)"
     */
    @Nested
    public class TelephoneValidationTest {

        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][]{
                    {"1234567890", true},   // Valid: exactly 10 digits
                    {"123456789", true},    // Valid: less than 10 digits
                    {"1", true},            // Valid: single digit
                    {"12345678901", false}, // Invalid: more than 10 digits
                    {"123456789012", false}, // Invalid: 12 digits
                    {"", false},            // Invalid: empty
                    {null, false}           // Invalid: null
            });
        }
        public String telephone;
        public boolean shouldBeValid;

        @MethodSource("data")
        @ParameterizedTest(name = "telephone={0}, shouldBeValid={1}")
        public void telephoneValidation_should_validateCorrectly(String telephone, boolean shouldBeValid) {
            initTelephoneValidationTest(telephone, shouldBeValid);
            // Arrange
            Owner owner = createValidOwner();
            owner.setTelephone(telephone);

            // Act
            Set<ConstraintViolation<Owner>> violations = validator.validate(owner);

            // Assert
            if (shouldBeValid) {
                assertThat(violations).isEmpty();
            } else {
                assertThat(violations).isNotEmpty();
                assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("telephone");
            }
        }

        public void initTelephoneValidationTest(String telephone, boolean shouldBeValid) {
            this.telephone = telephone;
            this.shouldBeValid = shouldBeValid;
        }
    }

    private static Owner createValidOwner() {
        Owner owner = new Owner();
        owner.setFirstName("John");
        owner.setLastName("Doe");
        owner.setAddress("123 Main Street");
        owner.setCity("Springfield");
        owner.setTelephone("1234567890");
        return owner;
    }
}