package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NamedEntityTest {

    private NamedEntity namedEntity;

    @BeforeEach
    void setUp() {
        namedEntity = new NamedEntity() {};
    }

    @Test
    void testGetNameInitiallyNull() {
        assertNull(namedEntity.getName());
    }

    @Test
    void testSetNameAndGetName() {
        String expectedName = "TestName";
        namedEntity.setName(expectedName);
        assertEquals(expectedName, namedEntity.getName());
    }

    @Test
    void testSetNameWithNull() {
        namedEntity.setName("InitialName");
        namedEntity.setName(null);
        assertNull(namedEntity.getName());
    }

    @Test
    void testSetNameWithEmptyString() {
        String emptyName = "";
        namedEntity.setName(emptyName);
        assertEquals(emptyName, namedEntity.getName());
    }

    @Test
    void testToStringWithNullName() {
        assertNull(namedEntity.toString());
    }

    @Test
    void testToStringWithSetName() {
        String expectedName = "TestEntity";
        namedEntity.setName(expectedName);
        assertEquals(expectedName, namedEntity.toString());
    }

    @Test
    void testToStringReturnsGetName() {
        String name = "MyEntity";
        namedEntity.setName(name);
        assertEquals(namedEntity.getName(), namedEntity.toString());
    }
}