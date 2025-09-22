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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link Specialty}.
 *
 * Tests specialty entity functionality including name management
 * as specified in the PRD for veterinarian specialties management.
 */
public class SpecialtyTest {

    @Test
    public void getName_should_returnNull_when_notSet() {
        // Arrange
        Specialty specialty = new Specialty();

        // Act & Assert
        assertThat(specialty.getName()).isNull();
    }

    @Test
    public void setName_should_storeName_when_validNameProvided() {
        // Arrange
        Specialty specialty = new Specialty();
        String expectedName = "Dentistry";

        // Act
        specialty.setName(expectedName);

        // Assert
        assertThat(specialty.getName()).isEqualTo(expectedName);
    }

    @Test
    public void toString_should_returnName_when_nameIsSet() {
        // Arrange
        Specialty specialty = new Specialty();
        String expectedName = "Surgery";
        specialty.setName(expectedName);

        // Act
        String result = specialty.toString();

        // Assert
        assertThat(result).isEqualTo(expectedName);
    }

    @Test
    public void toString_should_returnNull_when_nameIsNull() {
        // Arrange
        Specialty specialty = new Specialty();

        // Act
        String result = specialty.toString();

        // Assert
        assertThat(result).isNull();
    }

    @Test
    public void specialty_should_inheritBaseEntityFunctionality_when_created() {
        // Arrange
        Specialty specialty = new Specialty();

        // Act & Assert
        assertThat(specialty.getId()).isNull();
        assertThat(specialty.isNew()).isTrue();
        
        specialty.setId(1);
        assertThat(specialty.getId()).isEqualTo(1);
        assertThat(specialty.isNew()).isFalse();
    }

    @Test
    public void specialty_should_supportNameUpdate_when_nameChanges() {
        // Arrange
        Specialty specialty = new Specialty();
        String originalName = "General";
        String newName = "Cardiology";

        specialty.setName(originalName);

        // Act
        specialty.setName(newName);

        // Assert
        assertThat(specialty.getName()).isEqualTo(newName);
        assertThat(specialty.getName()).isNotEqualTo(originalName);
    }

    @Test
    public void specialty_should_allowEmptyName_when_emptyStringProvided() {
        // Arrange
        Specialty specialty = new Specialty();

        // Act
        specialty.setName("");

        // Assert
        assertThat(specialty.getName()).isEmpty();
    }

    @Test
    public void specialty_should_allowNullName_when_nullProvided() {
        // Arrange
        Specialty specialty = new Specialty();
        specialty.setName("Initial");

        // Act
        specialty.setName(null);

        // Assert
        assertThat(specialty.getName()).isNull();
    }

    @Test
    public void specialty_should_supportCommonVeterinarySpecialties_when_created() {
        // Arrange & Act
        Specialty dentistry = new Specialty();
        dentistry.setName("Dentistry");
        
        Specialty surgery = new Specialty();
        surgery.setName("Surgery");
        
        Specialty cardiology = new Specialty();
        cardiology.setName("Cardiology");
        
        Specialty dermatology = new Specialty();
        dermatology.setName("Dermatology");

        // Assert
        assertThat(dentistry.getName()).isEqualTo("Dentistry");
        assertThat(surgery.getName()).isEqualTo("Surgery");
        assertThat(cardiology.getName()).isEqualTo("Cardiology");
        assertThat(dermatology.getName()).isEqualTo("Dermatology");
    }

    @Test
    public void specialty_should_supportVetAssociation_when_usedWithVet() {
        // Arrange
        Specialty surgery = new Specialty();
        surgery.setName("Surgery");
        surgery.setId(1);

        Vet vet = new Vet();
        vet.setFirstName("Dr. Jane");
        vet.setLastName("Smith");

        // Act
        vet.addSpecialty(surgery);

        // Assert
        assertThat(vet.getSpecialties()).hasSize(1);
        assertThat(vet.getSpecialties()).contains(surgery);
        assertThat(vet.getNrOfSpecialties()).isEqualTo(1);
    }
}