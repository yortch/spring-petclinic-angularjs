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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link BaseEntity}.
 *
 * Tests basic functionality of the base entity including ID management
 * and new entity detection as required by the PRD for data persistence.
 */
public class BaseEntityTest {

    private static class TestEntity extends BaseEntity {
        // Test implementation of BaseEntity
    }

    @Test
    public void getId_should_returnNull_when_entityIsNew() {
        // Arrange
        TestEntity entity = new TestEntity();

        // Act & Assert
        assertThat(entity.getId()).isNull();
    }

    @Test
    public void setId_should_storeIdValue_when_validIdProvided() {
        // Arrange
        TestEntity entity = new TestEntity();
        Integer expectedId = 123;

        // Act
        entity.setId(expectedId);

        // Assert
        assertThat(entity.getId()).isEqualTo(expectedId);
    }

    @Test
    public void isNew_should_returnTrue_when_idIsNull() {
        // Arrange
        TestEntity entity = new TestEntity();

        // Act & Assert
        assertThat(entity.isNew()).isTrue();
    }

    @Test
    public void isNew_should_returnFalse_when_idIsSet() {
        // Arrange
        TestEntity entity = new TestEntity();
        entity.setId(1);

        // Act & Assert
        assertThat(entity.isNew()).isFalse();
    }

    @Test
    public void setId_should_allowNullValue_when_resettingEntity() {
        // Arrange
        TestEntity entity = new TestEntity();
        entity.setId(100);

        // Act
        entity.setId(null);

        // Assert
        assertThat(entity.getId()).isNull();
        assertThat(entity.isNew()).isTrue();
    }
}