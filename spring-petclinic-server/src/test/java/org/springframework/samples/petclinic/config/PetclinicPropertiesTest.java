package org.springframework.samples.petclinic.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PetclinicPropertiesTest {

    private PetclinicProperties petclinicProperties;

    @BeforeEach
    void setUp() {
        petclinicProperties = new PetclinicProperties();
    }

    @Test
    void getDatabaseShouldReturnNullByDefault() {
        assertNull(petclinicProperties.getDatabase());
    }

    @Test
    void setDatabaseShouldSetDatabaseValue() {
        String expectedDatabase = "hsqldb";
        petclinicProperties.setDatabase(expectedDatabase);
        assertEquals(expectedDatabase, petclinicProperties.getDatabase());
    }

    @Test
    void setDatabaseShouldAcceptMysqlValue() {
        String expectedDatabase = "mysql";
        petclinicProperties.setDatabase(expectedDatabase);
        assertEquals(expectedDatabase, petclinicProperties.getDatabase());
    }

    @Test
    void setDatabaseShouldAcceptPostgresqlValue() {
        String expectedDatabase = "postgresql";
        petclinicProperties.setDatabase(expectedDatabase);
        assertEquals(expectedDatabase, petclinicProperties.getDatabase());
    }

    @Test
    void setDatabaseShouldAcceptNullValue() {
        petclinicProperties.setDatabase("hsqldb");
        petclinicProperties.setDatabase(null);
        assertNull(petclinicProperties.getDatabase());
    }

    @Test
    void setDatabaseShouldAcceptEmptyString() {
        String expectedDatabase = "";
        petclinicProperties.setDatabase(expectedDatabase);
        assertEquals(expectedDatabase, petclinicProperties.getDatabase());
    }

    @Test
    void setDatabaseShouldOverridePreviousValue() {
        petclinicProperties.setDatabase("hsqldb");
        String expectedDatabase = "mysql";
        petclinicProperties.setDatabase(expectedDatabase);
        assertEquals(expectedDatabase, petclinicProperties.getDatabase());
    }

    @Test
    void getDatabaseShouldReturnLastSetValue() {
        petclinicProperties.setDatabase("hsqldb");
        petclinicProperties.setDatabase("mysql");
        String expectedDatabase = "postgresql";
        petclinicProperties.setDatabase(expectedDatabase);
        assertEquals(expectedDatabase, petclinicProperties.getDatabase());
    }
}