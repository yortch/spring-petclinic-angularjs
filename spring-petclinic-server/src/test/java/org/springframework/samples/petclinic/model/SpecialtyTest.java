package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpecialtyTest {

    private Specialty specialty;

    @BeforeEach
    void setUp() {
        specialty = new Specialty();
    }

    @Test
    void testSpecialtyCreation() {
        assertNotNull(specialty);
    }

    @Test
    void testSetAndGetName() {
        String specialtyName = "Dentistry";
        specialty.setName(specialtyName);
        assertEquals(specialtyName, specialty.getName());
    }

    @Test
    void testSetNameWithNull() {
        specialty.setName(null);
        assertNull(specialty.getName());
    }

    @Test
    void testSetNameWithEmptyString() {
        String emptyName = "";
        specialty.setName(emptyName);
        assertEquals(emptyName, specialty.getName());
    }

    @Test
    void testSetNameWithWhitespace() {
        String whitespaceName = "   ";
        specialty.setName(whitespaceName);
        assertEquals(whitespaceName, specialty.getName());
    }

    @Test
    void testSetAndGetId() {
        Integer id = 1;
        specialty.setId(id);
        assertEquals(id, specialty.getId());
    }

    @Test
    void testSetIdWithNull() {
        specialty.setId(null);
        assertNull(specialty.getId());
    }

    @Test
    void testIsNew() {
        assertTrue(specialty.isNew());
        specialty.setId(1);
        assertFalse(specialty.isNew());
    }

    @Test
    void testToString() {
        specialty.setName("Surgery");
        String result = specialty.toString();
        assertNotNull(result);
    }

    @Test
    void testToStringWithNullName() {
        specialty.setName(null);
        String result = specialty.toString();
        assertNotNull(result);
    }

    @Test
    void testMultipleSpecialties() {
        Specialty specialty1 = new Specialty();
        specialty1.setName("Radiology");
        specialty1.setId(1);

        Specialty specialty2 = new Specialty();
        specialty2.setName("Surgery");
        specialty2.setId(2);

        assertNotEquals(specialty1.getName(), specialty2.getName());
        assertNotEquals(specialty1.getId(), specialty2.getId());
    }

    @Test
    void testNameUpdate() {
        String initialName = "Cardiology";
        String updatedName = "Neurology";
        
        specialty.setName(initialName);
        assertEquals(initialName, specialty.getName());
        
        specialty.setName(updatedName);
        assertEquals(updatedName, specialty.getName());
    }

    @Test
    void testIdUpdate() {
        Integer initialId = 1;
        Integer updatedId = 2;
        
        specialty.setId(initialId);
        assertEquals(initialId, specialty.getId());
        
        specialty.setId(updatedId);
        assertEquals(updatedId, specialty.getId());
    }

    @Test
    void testSpecialtyWithLongName() {
        String longName = "Veterinary Ophthalmology and Advanced Surgical Procedures";
        specialty.setName(longName);
        assertEquals(longName, specialty.getName());
    }

    @Test
    void testSpecialtyWithSpecialCharacters() {
        String nameWithSpecialChars = "Surgery & Dentistry";
        specialty.setName(nameWithSpecialChars);
        assertEquals(nameWithSpecialChars, specialty.getName());
    }

    @Test
    void testSpecialtyWithNumbers() {
        String nameWithNumbers = "Type 1 Diabetes Care";
        specialty.setName(nameWithNumbers);
        assertEquals(nameWithNumbers, specialty.getName());
    }
}