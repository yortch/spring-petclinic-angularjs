package org.springframework.samples.petclinic.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PetclinicPropertiesTest {

    private PetclinicProperties petclinicProperties;

    @BeforeEach
    void setUp() {
        petclinicProperties = new PetclinicProperties();
    }

    @Test
    void testGetDatabaseReturnsNull_WhenNotSet() {
        assertNull(petclinicProperties.getDatabase());
    }

    @Test
    void testSetDatabaseAndGetDatabase_WithValidValue() {
        String database = "mysql";
        petclinicProperties.setDatabase(database);
        assertEquals(database, petclinicProperties.getDatabase());
    }

    @Test
    void testSetDatabase_WithNull() {
        petclinicProperties.setDatabase(null);
        assertNull(petclinicProperties.getDatabase());
    }

    @Test
    void testSetDatabase_WithEmptyString() {
        String emptyDatabase = "";
        petclinicProperties.setDatabase(emptyDatabase);
        assertEquals(emptyDatabase, petclinicProperties.getDatabase());
    }

    @Test
    void testSetDatabase_OverwritesExistingValue() {
        petclinicProperties.setDatabase("hsqldb");
        assertEquals("hsqldb", petclinicProperties.getDatabase());
        
        petclinicProperties.setDatabase("postgresql");
        assertEquals("postgresql", petclinicProperties.getDatabase());
    }

    @Test
    void testSetDatabase_WithHsqldb() {
        petclinicProperties.setDatabase("hsqldb");
        assertEquals("hsqldb", petclinicProperties.getDatabase());
    }

    @Test
    void testSetDatabase_WithMysql() {
        petclinicProperties.setDatabase("mysql");
        assertEquals("mysql", petclinicProperties.getDatabase());
    }

    @Test
    void testSetDatabase_WithPostgresql() {
        petclinicProperties.setDatabase("postgresql");
        assertEquals("postgresql", petclinicProperties.getDatabase());
    }

    @Test
    void testSetDatabase_WithWhitespace() {
        String databaseWithSpaces = " mysql ";
        petclinicProperties.setDatabase(databaseWithSpaces);
        assertEquals(databaseWithSpaces, petclinicProperties.getDatabase());
    }

    @Test
    void testSetDatabase_WithSpecialCharacters() {
        String databaseWithSpecialChars = "my-sql_2.0";
        petclinicProperties.setDatabase(databaseWithSpecialChars);
        assertEquals(databaseWithSpecialChars, petclinicProperties.getDatabase());
    }
}