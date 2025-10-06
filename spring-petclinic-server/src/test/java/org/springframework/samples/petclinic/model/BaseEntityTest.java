package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseEntityTest {

    private BaseEntity baseEntity;

    @BeforeEach
    void setUp() {
        baseEntity = new BaseEntity();
    }

//     @Test
//     void testSetIdWithPositiveValue() {
//         baseEntity.setId(10);
//         assertEquals(10, baseEntity.getId());
//     }

//     @Test
//     void testSetIdWithNegativeValue() {
//         baseEntity.setId(-5);
//         assertEquals(-5, baseEntity.getId());
//     }

//     @Test
//     void testSetIdWithZero() {
//         baseEntity.setId(0);
//         assertEquals(0, baseEntity.getId());
//     }

    @Test
    void testSetIdWithNull() {
        baseEntity.setId(100);
        baseEntity.setId(null);
        assertNull(baseEntity.getId());
    }

    @Test
    void testGetIdWhenNotSet() {
        assertNull(baseEntity.getId());
    }

//     @Test
//     void testGetIdAfterSetting() {
//         baseEntity.setId(42);
//         assertEquals(42, baseEntity.getId());
//     }

    @Test
    void testIsNewReturnsTrueWhenIdIsNull() {
        assertTrue(baseEntity.isNew());
    }

    @Test
    void testIsNewReturnsFalseWhenIdIsPositive() {
        baseEntity.setId(1);
        assertFalse(baseEntity.isNew());
    }

    @Test
    void testIsNewReturnsFalseWhenIdIsZero() {
        baseEntity.setId(0);
        assertFalse(baseEntity.isNew());
    }

    @Test
    void testIsNewReturnsFalseWhenIdIsNegative() {
        baseEntity.setId(-1);
        assertFalse(baseEntity.isNew());
    }

    @Test
    void testIsNewAfterSettingIdToNull() {
        baseEntity.setId(5);
        baseEntity.setId(null);
        assertTrue(baseEntity.isNew());
    }
}