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
    void testSetNameWithValidString() {
        String testName = "TestName";
        namedEntity.setName(testName);
        assertEquals(testName, namedEntity.getName());
    }

    @Test
    void testSetNameWithNull() {
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
    void testSetNameWithWhitespace() {
        String whitespaceName = "   ";
        namedEntity.setName(whitespaceName);
        assertEquals(whitespaceName, namedEntity.getName());
    }

    @Test
    void testGetNameWhenSet() {
        String testName = "John Doe";
        namedEntity.setName(testName);
        assertEquals(testName, namedEntity.getName());
    }

    @Test
    void testGetNameWhenNotSet() {
        assertNull(namedEntity.getName());
    }

    @Test
    void testToStringWithValidName() {
        String testName = "EntityName";
        namedEntity.setName(testName);
        assertEquals(testName, namedEntity.toString());
    }

    @Test
    void testToStringWhenNameIsNull() {
        assertNull(namedEntity.toString());
    }

    @Test
    void testSetNameOverwritesPreviousValue() {
        String firstName = "FirstName";
        String secondName = "SecondName";
        namedEntity.setName(firstName);
        namedEntity.setName(secondName);
        assertEquals(secondName, namedEntity.getName());
    }

    @Test
    void testToStringReflectsNameChanges() {
        String initialName = "Initial";
        String updatedName = "Updated";
        namedEntity.setName(initialName);
        assertEquals(initialName, namedEntity.toString());
        namedEntity.setName(updatedName);
        assertEquals(updatedName, namedEntity.toString());
    }

    @Test
    void testSetNameWithSpecialCharacters() {
        String specialName = "Test@#$%^&*()";
        namedEntity.setName(specialName);
        assertEquals(specialName, namedEntity.getName());
    }

    @Test
    void testSetNameWithUnicodeCharacters() {
        String unicodeName = "Tëst Nämé 测试";
        namedEntity.setName(unicodeName);
        assertEquals(unicodeName, namedEntity.getName());
    }
}