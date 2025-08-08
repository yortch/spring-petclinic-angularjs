package org.springframework.samples.petclinic.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PetclinicPropertiesTest {

    @Test
    void defaultDatabaseIsNull() {
        PetclinicProperties props = new PetclinicProperties();
        assertNull(props.getDatabase());
    }

    @Test
    void setAndGetDatabase_hsqldb() {
        PetclinicProperties props = new PetclinicProperties();
        props.setDatabase("hsqldb");
        assertEquals("hsqldb", props.getDatabase());
    }

    @Test
    void setDatabaseToNull() {
        PetclinicProperties props = new PetclinicProperties();
        props.setDatabase(null);
        assertNull(props.getDatabase());
    }

    @Test
    void setDatabaseToEmptyString() {
        PetclinicProperties props = new PetclinicProperties();
        props.setDatabase("");
        assertEquals("", props.getDatabase());
    }
}