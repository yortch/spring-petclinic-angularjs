package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.Test;

import javax.persistence.Entity;
import javax.persistence.Table;

import static org.junit.jupiter.api.Assertions.*;

public class PetTypeTest {

    @Test
    void defaultState_isNewAndNulls() {
        PetType type = new PetType();
        assertTrue(type.isNew());
        assertNull(type.getId());
        assertNull(type.getName());
    }

//     @Test
//     void setId_makesNotNew() {
//         PetType type = new PetType();
//         type.setId(1);
//         assertEquals(1, type.getId());
//         assertFalse(type.isNew());
//     }

    @Test
    void setName_persistsValue() {
        PetType type = new PetType();
        type.setName("cat");
        assertEquals("cat", type.getName());
    }

    @Test
    void jpaAnnotations_presentOnClass() {
        Entity entity = PetType.class.getAnnotation(Entity.class);
        assertNotNull(entity);

        Table table = PetType.class.getAnnotation(Table.class);
        assertNotNull(table);
        assertEquals("types", table.name());
    }
}