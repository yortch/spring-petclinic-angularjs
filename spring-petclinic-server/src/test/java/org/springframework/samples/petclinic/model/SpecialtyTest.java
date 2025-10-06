package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.Test;

import javax.persistence.Entity;
import javax.persistence.Table;

import static org.junit.jupiter.api.Assertions.*;

class SpecialtyTest {

    @Test
    void shouldInstantiateSpecialty() {
        Specialty specialty = new Specialty();
        assertNotNull(specialty);
    }

    @Test
    void shouldSetAndGetName() {
        Specialty specialty = new Specialty();
        String testName = "Dentistry";
        specialty.setName(testName);
        assertEquals(testName, specialty.getName());
    }

    @Test
    void shouldSetAndGetId() {
        Specialty specialty = new Specialty();
        Integer testId = 1;
        specialty.setId(testId);
        assertEquals(testId, specialty.getId());
    }

    @Test
    void shouldSetNameToNull() {
        Specialty specialty = new Specialty();
        specialty.setName("Surgery");
        specialty.setName(null);
        assertNull(specialty.getName());
    }

    @Test
    void shouldSetIdToNull() {
        Specialty specialty = new Specialty();
        specialty.setId(1);
        specialty.setId(null);
        assertNull(specialty.getId());
    }

    @Test
    void shouldHaveEntityAnnotation() {
        assertTrue(Specialty.class.isAnnotationPresent(Entity.class));
    }

    @Test
    void shouldHaveTableAnnotation() {
        assertTrue(Specialty.class.isAnnotationPresent(Table.class));
    }

    @Test
    void shouldHaveCorrectTableName() {
        Table tableAnnotation = Specialty.class.getAnnotation(Table.class);
        assertEquals("specialties", tableAnnotation.name());
    }

    @Test
    void shouldHandleEmptyName() {
        Specialty specialty = new Specialty();
        specialty.setName("");
        assertEquals("", specialty.getName());
    }

//     @Test
//     void shouldHandleLongName() {
//         Specialty specialty = new Specialty();
//         String longName = "A".repeat(255);
//         specialty.setName(longName);
//         assertEquals(longName, specialty.getName());
//     }

    @Test
    void shouldAllowMultipleSpecialtiesWithSameName() {
        Specialty specialty1 = new Specialty();
        Specialty specialty2 = new Specialty();
        String name = "Cardiology";
        specialty1.setName(name);
        specialty2.setName(name);
        assertEquals(specialty1.getName(), specialty2.getName());
    }

    @Test
    void shouldAllowDifferentIdsForDifferentInstances() {
        Specialty specialty1 = new Specialty();
        Specialty specialty2 = new Specialty();
        specialty1.setId(1);
        specialty2.setId(2);
        assertNotEquals(specialty1.getId(), specialty2.getId());
    }
}