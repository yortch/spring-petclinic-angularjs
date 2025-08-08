package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NamedEntityTest {

    @Test
    void defaultNameIsNull() {
        NamedEntity entity = new NamedEntity();
        assertNull(entity.getName());
    }

    @Test
    void setAndGetName_regularString() {
        NamedEntity entity = new NamedEntity();
        entity.setName("Fido");
        assertEquals("Fido", entity.getName());
    }

    @Test
    void setAndGetName_emptyString() {
        NamedEntity entity = new NamedEntity();
        entity.setName("");
        assertEquals("", entity.getName());
    }

    @Test
    void setAndGetName_unicodeString() {
        NamedEntity entity = new NamedEntity();
        String value = "Český 🐶";
        entity.setName(value);
        assertEquals(value, entity.getName());
    }

    @Test
    void toString_withNameReturnsSame() {
        NamedEntity entity = new NamedEntity();
        entity.setName("Buddy");
        assertEquals("Buddy", entity.toString());
    }

    @Test
    void toString_whenNameIsNullReturnsNull() {
        NamedEntity entity = new NamedEntity();
        assertNull(entity.toString());
    }
}