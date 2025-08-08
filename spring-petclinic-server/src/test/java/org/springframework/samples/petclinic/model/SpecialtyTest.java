package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import javax.persistence.Entity;
import javax.persistence.Table;

import static org.junit.jupiter.api.Assertions.*;

class SpecialtyTest {

    @Test
    @DisplayName("Specialty is annotated with @Entity")
    void entityAnnotationPresent() {
        assertTrue(Specialty.class.isAnnotationPresent(Entity.class));
    }

    @Test
    @DisplayName("Specialty has @Table(name = \"specialties\")")
    void tableAnnotationAndName() {
        Table table = Specialty.class.getAnnotation(Table.class);
        assertNotNull(table);
        assertEquals("specialties", table.name());
    }

    @Test
    @DisplayName("Specialty extends NamedEntity")
    void isSubclassOfNamedEntity() {
        assertTrue(NamedEntity.class.isAssignableFrom(Specialty.class));
    }

    @Test
    @DisplayName("Specialty has a working no-arg constructor")
    void canInstantiate() {
        Specialty specialty = new Specialty();
        assertNotNull(specialty);
    }
}