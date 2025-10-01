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

import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link Vet}.
 *
 * Tests veterinarian entity functionality including name management and specialty
 * associations as specified in the PRD for veterinarian directory management.
 */
public class VetTest {

    private static final Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void getSpecialties_should_returnEmptyList_when_noSpecialtiesAdded() {
        // Arrange
        Vet vet = new Vet();

        // Act
        List<Specialty> specialties = vet.getSpecialties();

        // Assert
        assertThat(specialties).isEmpty();
        assertThat(specialties).isNotNull();
    }

    @Test
    public void addSpecialty_should_addSpecialtyToVet_when_validSpecialtyProvided() {
        // Arrange
        Vet vet = new Vet();
        Specialty specialty = new Specialty();
        specialty.setName("Surgery");

        // Act
        vet.addSpecialty(specialty);

        // Assert
        assertThat(vet.getSpecialties()).hasSize(1);
        assertThat(vet.getSpecialties().getFirst()).isEqualTo(specialty);
        assertThat(vet.getNrOfSpecialties()).isEqualTo(1);
    }

    @Test
    public void addSpecialty_should_addMultipleSpecialties_when_multipleSpecialtiesProvided() {
        // Arrange
        Vet vet = new Vet();
        
        Specialty surgery = new Specialty();
        surgery.setName("Surgery");
        
        Specialty dentistry = new Specialty();
        dentistry.setName("Dentistry");

        // Act
        vet.addSpecialty(surgery);
        vet.addSpecialty(dentistry);

        // Assert
        assertThat(vet.getSpecialties()).hasSize(2);
        assertThat(vet.getNrOfSpecialties()).isEqualTo(2);
        assertThat(vet.getSpecialties()).containsExactly(dentistry, surgery); // Sorted alphabetically
    }

    @Test
    public void getSpecialties_should_returnSortedList_when_specialtiesHaveDifferentNames() {
        // Arrange
        Vet vet = new Vet();
        
        Specialty zooSpecialty = new Specialty();
        zooSpecialty.setName("Zoo Medicine");
        
        Specialty anesthesia = new Specialty();
        anesthesia.setName("Anesthesia");
        
        Specialty internal = new Specialty();
        internal.setName("Internal Medicine");

        // Act
        vet.addSpecialty(zooSpecialty);
        vet.addSpecialty(anesthesia);
        vet.addSpecialty(internal);

        // Assert
        List<Specialty> specialties = vet.getSpecialties();
        assertThat(specialties).hasSize(3);
        assertThat(specialties.getFirst().getName()).isEqualTo("Anesthesia");
        assertThat(specialties.get(1).getName()).isEqualTo("Internal Medicine");
        assertThat(specialties.get(2).getName()).isEqualTo("Zoo Medicine");
    }

    @Test
    public void getSpecialties_should_returnUnmodifiableList_when_called() {
        // Arrange
        Vet vet = new Vet();
        Specialty specialty = new Specialty();
        specialty.setName("Surgery");
        vet.addSpecialty(specialty);

        // Act
        List<Specialty> specialties = vet.getSpecialties();

        // Assert
        assertThat(specialties).hasSize(1);
        
        // This should not affect the vet's specialties
        try {
            specialties.clear();
            assertThat(vet.getSpecialties()).hasSize(1); // Original list should remain unchanged
        } catch (UnsupportedOperationException e) {
            // This is expected for unmodifiable lists
            assertThat(true).isTrue();
        }
    }

    @Test
    public void getNrOfSpecialties_should_returnZero_when_noSpecialties() {
        // Arrange
        Vet vet = new Vet();

        // Act & Assert
        assertThat(vet.getNrOfSpecialties()).isEqualTo(0);
    }

    @Test
    public void getNrOfSpecialties_should_returnCorrectCount_when_specialtiesAdded() {
        // Arrange
        Vet vet = new Vet();
        
        Specialty cardiology = new Specialty();
        cardiology.setName("Cardiology");
        
        Specialty dermatology = new Specialty();
        dermatology.setName("Dermatology");
        
        Specialty oncology = new Specialty();
        oncology.setName("Oncology");

        // Act
        vet.addSpecialty(cardiology);
        assertThat(vet.getNrOfSpecialties()).isEqualTo(1);
        
        vet.addSpecialty(dermatology);
        assertThat(vet.getNrOfSpecialties()).isEqualTo(2);
        
        vet.addSpecialty(oncology);

        // Assert
        assertThat(vet.getNrOfSpecialties()).isEqualTo(3);
    }

    @Test
    public void vet_should_inheritPersonFunctionality_when_created() {
        // Arrange
        Vet vet = new Vet();

        // Act & Assert
        assertThat(vet.getFirstName()).isNull();
        assertThat(vet.getLastName()).isNull();
        
        vet.setFirstName("John");
        vet.setLastName("Doe");
        
        assertThat(vet.getFirstName()).isEqualTo("John");
        assertThat(vet.getLastName()).isEqualTo("Doe");
    }

    @Test
    public void vet_should_inheritBaseEntityFunctionality_when_created() {
        // Arrange
        Vet vet = new Vet();

        // Act & Assert
        assertThat(vet.getId()).isNull();
        assertThat(vet.isNew()).isTrue();
        
        vet.setId(1);
        assertThat(vet.getId()).isEqualTo(1);
        assertThat(vet.isNew()).isFalse();
    }

    @Test
    public void validation_should_fail_when_firstNameIsEmpty() {
        // Arrange
        Vet vet = createValidVet();
        vet.setFirstName("");

        // Act
        Set<ConstraintViolation<Vet>> violations = validator.validate(vet);

        // Assert
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("firstName");
    }

    @Test
    public void validation_should_fail_when_lastNameIsEmpty() {
        // Arrange
        Vet vet = createValidVet();
        vet.setLastName("");

        // Act
        Set<ConstraintViolation<Vet>> violations = validator.validate(vet);

        // Assert
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("lastName");
    }

    @Test
    public void validation_should_pass_when_allRequiredFieldsAreValid() {
        // Arrange
        Vet vet = createValidVet();

        // Act
        Set<ConstraintViolation<Vet>> violations = validator.validate(vet);

        // Assert
        assertThat(violations).isEmpty();
    }

    @Test
    public void vet_should_supportVeterinaryDirectoryScenario_when_usedAsSpecified() {
        // Arrange
        Vet generalVet = new Vet();
        generalVet.setFirstName("Dr. Sarah");
        generalVet.setLastName("Johnson");
        generalVet.setId(1);

        Vet specialist = new Vet();
        specialist.setFirstName("Dr. Michael");
        specialist.setLastName("Brown");
        specialist.setId(2);

        Specialty surgery = new Specialty();
        surgery.setName("Surgery");
        
        Specialty cardiology = new Specialty();
        cardiology.setName("Cardiology");

        // Act
        specialist.addSpecialty(surgery);
        specialist.addSpecialty(cardiology);

        // Assert
        // General vet has no specialties
        assertThat(generalVet.getNrOfSpecialties()).isEqualTo(0);
        assertThat(generalVet.getSpecialties()).isEmpty();

        // Specialist vet has multiple specialties
        assertThat(specialist.getNrOfSpecialties()).isEqualTo(2);
        assertThat(specialist.getSpecialties()).hasSize(2);
        assertThat(specialist.getSpecialties()).containsExactly(cardiology, surgery); // Sorted
    }

    @Test
    public void vet_should_supportSpecialtyRouting_when_usedForCaseAssignment() {
        // Arrange
        Vet surgicalVet = new Vet();
        surgicalVet.setFirstName("Dr. Emily");
        surgicalVet.setLastName("Davis");

        Specialty surgery = new Specialty();
        surgery.setName("Surgery");
        
        Specialty emergency = new Specialty();
        emergency.setName("Emergency Medicine");

        surgicalVet.addSpecialty(surgery);
        surgicalVet.addSpecialty(emergency);

        // Act & Assert
        boolean canHandleSurgery = surgicalVet.getSpecialties().stream()
            .anyMatch(s -> s.getName().equals("Surgery"));
        
        boolean canHandleEmergency = surgicalVet.getSpecialties().stream()
            .anyMatch(s -> s.getName().equals("Emergency Medicine"));
        
        boolean canHandleDentistry = surgicalVet.getSpecialties().stream()
            .anyMatch(s -> s.getName().equals("Dentistry"));

        assertThat(canHandleSurgery).isTrue();
        assertThat(canHandleEmergency).isTrue();
        assertThat(canHandleDentistry).isFalse();
    }

    @Test
    public void addSpecialty_should_preventDuplicates_when_sameSpecialtyAddedTwice() {
        // Arrange
        Vet vet = new Vet();
        Specialty surgery = new Specialty();
        surgery.setName("Surgery");
        surgery.setId(1); // Same ID to simulate same specialty

        // Act
        vet.addSpecialty(surgery);
        vet.addSpecialty(surgery); // Adding same specialty again

        // Assert
        // Sets naturally prevent duplicates
        assertThat(vet.getNrOfSpecialties()).isEqualTo(1);
        assertThat(vet.getSpecialties()).hasSize(1);
    }

    private static Vet createValidVet() {
        Vet vet = new Vet();
        vet.setFirstName("John");
        vet.setLastName("Doe");
        return vet;
    }
}