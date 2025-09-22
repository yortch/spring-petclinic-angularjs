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

import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link Visit}.
 *
 * Tests visit entity functionality including date management, description handling,
 * and pet association as specified in the PRD for visit management and medical records.
 */
public class VisitTest {

    private static final Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void constructor_should_setCurrentDate_when_created() {
        // Arrange
        long beforeCreation = System.currentTimeMillis();

        // Act
        Visit visit = new Visit();
        
        long afterCreation = System.currentTimeMillis();

        // Assert
        assertThat(visit.getDate()).isNotNull();
        assertThat(visit.getDate().getTime()).isBetween(beforeCreation, afterCreation);
    }

    @Test
    public void getDate_should_returnDate_when_dateIsSet() {
        // Arrange
        Visit visit = new Visit();
        Date expectedDate = new Date(System.currentTimeMillis() - 86400000); // Yesterday

        // Act
        visit.setDate(expectedDate);

        // Assert
        assertThat(visit.getDate()).isEqualTo(expectedDate);
    }

    @Test
    public void setDate_should_allowNull_when_nullProvided() {
        // Arrange
        Visit visit = new Visit();

        // Act
        visit.setDate(null);

        // Assert
        assertThat(visit.getDate()).isNull();
    }

    @Test
    public void getDescription_should_returnNull_when_notSet() {
        // Arrange
        Visit visit = new Visit();

        // Act & Assert
        assertThat(visit.getDescription()).isNull();
    }

    @Test
    public void setDescription_should_storeDescription_when_validDescriptionProvided() {
        // Arrange
        Visit visit = new Visit();
        String expectedDescription = "Routine checkup - all normal";

        // Act
        visit.setDescription(expectedDescription);

        // Assert
        assertThat(visit.getDescription()).isEqualTo(expectedDescription);
    }

    @Test
    public void setDescription_should_allowEmptyString_when_emptyStringProvided() {
        // Arrange
        Visit visit = new Visit();

        // Act
        visit.setDescription("");

        // Assert
        assertThat(visit.getDescription()).isEmpty();
    }

    @Test
    public void setDescription_should_allowNull_when_nullProvided() {
        // Arrange
        Visit visit = new Visit();
        visit.setDescription("Initial description");

        // Act
        visit.setDescription(null);

        // Assert
        assertThat(visit.getDescription()).isNull();
    }

    @Test
    public void getPet_should_returnNull_when_notSet() {
        // Arrange
        Visit visit = new Visit();

        // Act & Assert
        assertThat(visit.getPet()).isNull();
    }

    @Test
    public void setPet_should_storePet_when_validPetProvided() {
        // Arrange
        Visit visit = new Visit();
        Pet expectedPet = new Pet();
        expectedPet.setName("Fluffy");

        // Act
        visit.setPet(expectedPet);

        // Assert
        assertThat(visit.getPet()).isEqualTo(expectedPet);
    }

    @Test
    public void setPet_should_allowNull_when_nullProvided() {
        // Arrange
        Visit visit = new Visit();
        Pet pet = new Pet();
        pet.setName("Fluffy");
        visit.setPet(pet);

        // Act
        visit.setPet(null);

        // Assert
        assertThat(visit.getPet()).isNull();
    }

    @Test
    public void visit_should_inheritBaseEntityFunctionality_when_created() {
        // Arrange
        Visit visit = new Visit();

        // Act & Assert
        assertThat(visit.getId()).isNull();
        assertThat(visit.isNew()).isTrue();
        
        visit.setId(1);
        assertThat(visit.getId()).isEqualTo(1);
        assertThat(visit.isNew()).isFalse();
    }

    @Test
    public void validation_should_pass_when_descriptionIsWithinSizeLimit() {
        // Arrange
        Visit visit = new Visit();
        String validDescription = "A".repeat(1000); // Well within 8192 limit
        visit.setDescription(validDescription);

        // Act
        Set<ConstraintViolation<Visit>> violations = validator.validate(visit);

        // Assert
        assertThat(violations).isEmpty();
    }

    @Test
    public void validation_should_fail_when_descriptionExceedsSizeLimit() {
        // Arrange
        Visit visit = new Visit();
        String invalidDescription = "A".repeat(8193); // Exceeds 8192 limit
        visit.setDescription(invalidDescription);

        // Act
        Set<ConstraintViolation<Visit>> violations = validator.validate(visit);

        // Assert
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("description");
    }

    @Test
    public void validation_should_pass_when_descriptionIsEmpty() {
        // Arrange
        Visit visit = new Visit();
        visit.setDescription("");

        // Act
        Set<ConstraintViolation<Visit>> violations = validator.validate(visit);

        // Assert
        assertThat(violations).isEmpty();
    }

    @Test
    public void validation_should_pass_when_descriptionIsNull() {
        // Arrange
        Visit visit = new Visit();
        visit.setDescription(null);

        // Act
        Set<ConstraintViolation<Visit>> violations = validator.validate(visit);

        // Assert
        assertThat(violations).isEmpty();
    }

    @Test
    public void validation_should_pass_when_descriptionIsAtMaximumLength() {
        // Arrange
        Visit visit = new Visit();
        String maxLengthDescription = "A".repeat(8192); // Exactly at limit
        visit.setDescription(maxLengthDescription);

        // Act
        Set<ConstraintViolation<Visit>> violations = validator.validate(visit);

        // Assert
        assertThat(violations).isEmpty();
    }

    @Test
    public void visit_should_supportCompleteLifecycle_when_allPropertiesSet() {
        // Arrange
        Visit visit = new Visit();
        Date visitDate = new Date();
        String description = "Annual vaccination and health check";
        
        Pet pet = new Pet();
        pet.setName("Max");
        pet.setId(1);

        // Act
        visit.setDate(visitDate);
        visit.setDescription(description);
        visit.setPet(pet);

        // Assert
        assertThat(visit.getDate()).isEqualTo(visitDate);
        assertThat(visit.getDescription()).isEqualTo(description);
        assertThat(visit.getPet()).isEqualTo(pet);
    }

    @Test
    public void visit_should_supportMedicalRecordScenario_when_usedForPetHistory() {
        // Arrange
        Pet pet = new Pet();
        pet.setName("Buddy");
        
        Visit checkupVisit = new Visit();
        checkupVisit.setDescription("Routine annual checkup - healthy");
        checkupVisit.setPet(pet);
        
        Visit vaccinationVisit = new Visit();
        vaccinationVisit.setDescription("Rabies vaccination administered");
        vaccinationVisit.setPet(pet);

        // Act
        pet.addVisit(checkupVisit);
        pet.addVisit(vaccinationVisit);

        // Assert
        assertThat(pet.getVisits()).hasSize(2);
        assertThat(pet.getVisits()).contains(checkupVisit, vaccinationVisit);
        assertThat(checkupVisit.getPet()).isEqualTo(pet);
        assertThat(vaccinationVisit.getPet()).isEqualTo(pet);
    }

    @Test
    public void visit_should_supportDateUpdate_when_rescheduled() {
        // Arrange
        Visit visit = new Visit();
        Date originalDate = new Date(System.currentTimeMillis() - 86400000); // Yesterday
        Date newDate = new Date(); // Today

        visit.setDate(originalDate);

        // Act
        visit.setDate(newDate);

        // Assert
        assertThat(visit.getDate()).isEqualTo(newDate);
        assertThat(visit.getDate()).isNotEqualTo(originalDate);
    }

    @Test
    public void visit_should_supportDescriptionUpdate_when_additionalNotesAdded() {
        // Arrange
        Visit visit = new Visit();
        String initialDescription = "Routine checkup";
        String updatedDescription = "Routine checkup - found minor skin irritation, prescribed cream";

        visit.setDescription(initialDescription);

        // Act
        visit.setDescription(updatedDescription);

        // Assert
        assertThat(visit.getDescription()).isEqualTo(updatedDescription);
        assertThat(visit.getDescription()).isNotEqualTo(initialDescription);
    }
}