package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    private Person person;

    @BeforeEach
    void setUp() {
        person = new Person() {};
    }

    @Test
    void testSetAndGetFirstName() {
        String firstName = "John";
        person.setFirstName(firstName);
        assertEquals(firstName, person.getFirstName());
    }

    @Test
    void testSetAndGetLastName() {
        String lastName = "Doe";
        person.setLastName(lastName);
        assertEquals(lastName, person.getLastName());
    }

    @Test
    void testSetFirstNameWithNull() {
        person.setFirstName(null);
        assertNull(person.getFirstName());
    }

    @Test
    void testSetLastNameWithNull() {
        person.setLastName(null);
        assertNull(person.getLastName());
    }

    @Test
    void testSetFirstNameWithEmptyString() {
        String emptyFirstName = "";
        person.setFirstName(emptyFirstName);
        assertEquals(emptyFirstName, person.getFirstName());
    }

    @Test
    void testSetLastNameWithEmptyString() {
        String emptyLastName = "";
        person.setLastName(emptyLastName);
        assertEquals(emptyLastName, person.getLastName());
    }

    @Test
    void testFirstNameInitiallyNull() {
        Person newPerson = new Person() {};
        assertNull(newPerson.getFirstName());
    }

    @Test
    void testLastNameInitiallyNull() {
        Person newPerson = new Person() {};
        assertNull(newPerson.getLastName());
    }

    @Test
    void testSetFirstNameMultipleTimes() {
        person.setFirstName("John");
        person.setFirstName("Jane");
        assertEquals("Jane", person.getFirstName());
    }

    @Test
    void testSetLastNameMultipleTimes() {
        person.setLastName("Doe");
        person.setLastName("Smith");
        assertEquals("Smith", person.getLastName());
    }
}