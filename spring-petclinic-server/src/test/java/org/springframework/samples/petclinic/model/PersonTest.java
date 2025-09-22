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
 * Unit tests for {@link Person}.
 *
 * Tests person entity functionality including name management
 * which is fundamental to both Owner and Veterinarian entities as required by PRD.
 */
public class PersonTest {

    private static class TestPerson extends Person {
        // Test implementation of Person
    }

    @Test
    public void getFirstName_should_returnNull_when_notSet() {
        // Arrange
        TestPerson person = new TestPerson();

        // Act & Assert
        assertThat(person.getFirstName()).isNull();
    }

    @Test
    public void setFirstName_should_storeFirstName_when_validNameProvided() {
        // Arrange
        TestPerson person = new TestPerson();
        String expectedFirstName = "John";

        // Act
        person.setFirstName(expectedFirstName);

        // Assert
        assertThat(person.getFirstName()).isEqualTo(expectedFirstName);
    }

    @Test
    public void getLastName_should_returnNull_when_notSet() {
        // Arrange
        TestPerson person = new TestPerson();

        // Act & Assert
        assertThat(person.getLastName()).isNull();
    }

    @Test
    public void setLastName_should_storeLastName_when_validNameProvided() {
        // Arrange
        TestPerson person = new TestPerson();
        String expectedLastName = "Doe";

        // Act
        person.setLastName(expectedLastName);

        // Assert
        assertThat(person.getLastName()).isEqualTo(expectedLastName);
    }

    @Test
    public void setFirstName_should_allowEmptyString_when_emptyStringProvided() {
        // Arrange
        TestPerson person = new TestPerson();

        // Act
        person.setFirstName("");

        // Assert
        assertThat(person.getFirstName()).isEmpty();
    }

    @Test
    public void setLastName_should_allowEmptyString_when_emptyStringProvided() {
        // Arrange
        TestPerson person = new TestPerson();

        // Act
        person.setLastName("");

        // Assert
        assertThat(person.getLastName()).isEmpty();
    }

    @Test
    public void setFirstName_should_allowNull_when_nullProvided() {
        // Arrange
        TestPerson person = new TestPerson();
        person.setFirstName("Initial");

        // Act
        person.setFirstName(null);

        // Assert
        assertThat(person.getFirstName()).isNull();
    }

    @Test
    public void setLastName_should_allowNull_when_nullProvided() {
        // Arrange
        TestPerson person = new TestPerson();
        person.setLastName("Initial");

        // Act
        person.setLastName(null);

        // Assert
        assertThat(person.getLastName()).isNull();
    }

    @Test
    public void person_should_inheritBaseEntityFunctionality_when_created() {
        // Arrange
        TestPerson person = new TestPerson();

        // Act & Assert
        assertThat(person.getId()).isNull();
        assertThat(person.isNew()).isTrue();
        
        person.setId(1);
        assertThat(person.getId()).isEqualTo(1);
        assertThat(person.isNew()).isFalse();
    }
}