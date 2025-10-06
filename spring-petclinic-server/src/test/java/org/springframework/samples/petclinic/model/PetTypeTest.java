package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import javax.persistence.Entity;
import javax.persistence.Table;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PetType Entity Tests")
class PetTypeTest {

    private PetType petType;

    @BeforeEach
    void setUp() {
        petType = new PetType();
    }

    @Test
    @DisplayName("Should create PetType instance")
    void testPetTypeCreation() {
        assertNotNull(petType);
    }

    @Test
    @DisplayName("Should be annotated with @Entity")
    void testEntityAnnotation() {
        assertTrue(petType.getClass().isAnnotationPresent(Entity.class));
    }

    @Test
    @DisplayName("Should be annotated with @Table and have correct name")
    void testTableAnnotation() {
        assertTrue(petType.getClass().isAnnotationPresent(Table.class));
        Table tableAnnotation = petType.getClass().getAnnotation(Table.class);
        assertEquals("types", tableAnnotation.name());
    }

    @Test
    @DisplayName("Should extend NamedEntity")
    void testInheritance() {
        assertTrue(petType instanceof NamedEntity);
    }

    @Test
    @DisplayName("Should set and get name from NamedEntity")
    void testSetAndGetName() {
        String expectedName = "Dog";
        petType.setName(expectedName);
        assertEquals(expectedName, petType.getName());
    }

    @Test
    @DisplayName("Should handle null name")
    void testNullName() {
        petType.setName(null);
        assertNull(petType.getName());
    }

    @Test
    @DisplayName("Should handle empty name")
    void testEmptyName() {
        String emptyName = "";
        petType.setName(emptyName);
        assertEquals(emptyName, petType.getName());
    }

    @Test
    @DisplayName("Should create multiple instances independently")
    void testMultipleInstances() {
        PetType petType1 = new PetType();
        PetType petType2 = new PetType();
        
        petType1.setName("Cat");
        petType2.setName("Dog");
        
        assertNotEquals(petType1.getName(), petType2.getName());
    }

    @Test
    @DisplayName("Should have toString method from parent")
    void testToString() {
        petType.setName("Hamster");
        assertNotNull(petType.toString());
    }

    @Test
    @DisplayName("Should be a valid JPA entity class")
    void testJpaEntityValidity() {
        assertNotNull(petType.getClass().getAnnotation(Entity.class));
        assertNotNull(petType.getClass().getAnnotation(Table.class));
        assertTrue(NamedEntity.class.isAssignableFrom(petType.getClass()));
    }
}