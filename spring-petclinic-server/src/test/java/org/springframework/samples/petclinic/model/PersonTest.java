package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PersonTest {

    private Person person;

    @BeforeEach
    void setUp() {
        person = new TestPerson();
    }

    @Test
    void testSetFirstName_SetsValue() {
        String firstName = "John";
        person.setFirstName(firstName);
        assertEquals(firstName, person.getFirstName());
    }

    @Test
    void testGetFirstName_WhenSet_ReturnsValue() {
        String firstName = "Jane";
        person.setFirstName(firstName);
        String result = person.getFirstName();
        assertEquals(firstName, result);
    }

    @Test
    void testSetLastName_SetsValue() {
        String lastName = "Doe";
        person.setLastName(lastName);
        assertEquals(lastName, person.getLastName());
    }

    @Test
    void testGetLastName_WhenSet_ReturnsValue() {
        String lastName = "Smith";
        person.setLastName(lastName);
        String result = person.getLastName();
        assertEquals(lastName, result);
    }

    @Test
    void testSetFirstName_WithNull_AcceptsNull() {
        person.setFirstName(null);
        assertNull(person.getFirstName());
    }

    @Test
    void testSetLastName_WithNull_AcceptsNull() {
        person.setLastName(null);
        assertNull(person.getLastName());
    }

    @Test
    void testSetFirstName_WithEmptyString_AcceptsEmptyString() {
        String emptyString = "";
        person.setFirstName(emptyString);
        assertEquals(emptyString, person.getFirstName());
    }

    @Test
    void testSetLastName_WithEmptyString_AcceptsEmptyString() {
        String emptyString = "";
        person.setLastName(emptyString);
        assertEquals(emptyString, person.getLastName());
    }

    @Test
    void testSetFirstName_WithWhitespace_AcceptsWhitespace() {
        String whitespace = "   ";
        person.setFirstName(whitespace);
        assertEquals(whitespace, person.getFirstName());
    }

    @Test
    void testSetLastName_WithWhitespace_AcceptsWhitespace() {
        String whitespace = "   ";
        person.setLastName(whitespace);
        assertEquals(whitespace, person.getLastName());
    }

    @Test
    void testFirstNameAndLastName_IndependentProperties() {
        String firstName = "Alice";
        String lastName = "Johnson";
        person.setFirstName(firstName);
        person.setLastName(lastName);
        assertEquals(firstName, person.getFirstName());
        assertEquals(lastName, person.getLastName());
    }

    private static class TestPerson extends Person {
    }
}