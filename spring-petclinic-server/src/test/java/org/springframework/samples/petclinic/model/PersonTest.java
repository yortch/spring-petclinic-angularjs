package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    private Person person;

    @BeforeEach
    void setUp() {
        person = new Person();
    }

    @Test
    void defaultValues_areNull() {
        assertNull(person.getFirstName());
        assertNull(person.getLastName());
    }

    @Test
    void getFirstName_returnsValueSetBySetFirstName() {
        person.setFirstName("John");
        assertEquals("John", person.getFirstName());
    }

    @Test
    void getLastName_returnsValueSetBySetLastName() {
        person.setLastName("Doe");
        assertEquals("Doe", person.getLastName());
    }

    @Test
    void setters_allowEmptyStringsAndNulls() {
        person.setFirstName("");
        person.setLastName("");
        assertEquals("", person.getFirstName());
        assertEquals("", person.getLastName());

        person.setFirstName(null);
        person.setLastName(null);
        assertNull(person.getFirstName());
        assertNull(person.getLastName());
    }
}