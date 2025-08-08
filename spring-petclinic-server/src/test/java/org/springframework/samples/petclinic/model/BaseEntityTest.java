package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseEntityTest {

    private BaseEntity entity;

    @BeforeEach
    void setUp() {
        entity = new BaseEntity();
    }

    @Test
    void isNew_whenIdNull_returnsTrue() {
        assertTrue(entity.isNew());
        assertNull(entity.getId());
    }

    @Test
    void isNew_whenIdSet_returnsFalse() {
        entity.setId(1);
        assertFalse(entity.isNew());
    }

    @Test
    void getId_afterSet_returnsSame() {
        Integer id = 42;
        entity.setId(id);
        assertEquals(id, entity.getId());
    }

    @Test
    void setId_toNull_resetsIsNewTrue() {
        entity.setId(5);
        assertFalse(entity.isNew());
        entity.setId(null);
        assertTrue(entity.isNew());
        assertNull(entity.getId());
    }
}