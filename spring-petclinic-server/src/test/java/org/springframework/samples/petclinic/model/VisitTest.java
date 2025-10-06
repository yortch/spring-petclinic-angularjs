package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class VisitTest {

    private Visit visit;

    @BeforeEach
    void setUp() {
        visit = new Visit();
    }

    @Test
    void testConstructor_ShouldInitializeDateToCurrentDate() {
        Visit newVisit = new Visit();
        
        assertNotNull(newVisit.getDate());
        assertTrue(newVisit.getDate().getTime() <= System.currentTimeMillis());
    }

    @Test
    void testGetDate_ShouldReturnDate() {
        Date expectedDate = new Date();
        visit.setDate(expectedDate);
        
        Date actualDate = visit.getDate();
        
        assertEquals(expectedDate, actualDate);
    }

    @Test
    void testSetDate_ShouldSetDate() {
        Date expectedDate = new Date(System.currentTimeMillis() + 86400000);
        
        visit.setDate(expectedDate);
        
        assertEquals(expectedDate, visit.getDate());
    }

    @Test
    void testSetDate_WithNull_ShouldSetNullDate() {
        visit.setDate(null);
        
        assertNull(visit.getDate());
    }

    @Test
    void testGetDescription_ShouldReturnDescription() {
        String expectedDescription = "Annual checkup";
        visit.setDescription(expectedDescription);
        
        String actualDescription = visit.getDescription();
        
        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    void testSetDescription_ShouldSetDescription() {
        String expectedDescription = "Vaccination appointment";
        
        visit.setDescription(expectedDescription);
        
        assertEquals(expectedDescription, visit.getDescription());
    }

    @Test
    void testSetDescription_WithNull_ShouldSetNullDescription() {
        visit.setDescription("Initial description");
        
        visit.setDescription(null);
        
        assertNull(visit.getDescription());
    }

    @Test
    void testSetDescription_WithEmptyString_ShouldSetEmptyDescription() {
        visit.setDescription("");
        
        assertEquals("", visit.getDescription());
    }

    @Test
    void testSetDescription_WithMaxLength_ShouldSetDescription() {
        StringBuilder longDescription = new StringBuilder();
        for (int i = 0; i < 8192; i++) {
            longDescription.append("a");
        }
        String expectedDescription = longDescription.toString();
        
        visit.setDescription(expectedDescription);
        
        assertEquals(expectedDescription, visit.getDescription());
        assertEquals(8192, visit.getDescription().length());
    }

    @Test
    void testGetPet_ShouldReturnPet() {
        Pet expectedPet = new Pet();
        visit.setPet(expectedPet);
        
        Pet actualPet = visit.getPet();
        
        assertEquals(expectedPet, actualPet);
    }

    @Test
    void testSetPet_ShouldSetPet() {
        Pet expectedPet = new Pet();
        expectedPet.setName("Fluffy");
        
        visit.setPet(expectedPet);
        
        assertEquals(expectedPet, visit.getPet());
        assertEquals("Fluffy", visit.getPet().getName());
    }

    @Test
    void testSetPet_WithNull_ShouldSetNullPet() {
        Pet pet = new Pet();
        visit.setPet(pet);
        
        visit.setPet(null);
        
        assertNull(visit.getPet());
    }

    @Test
    void testDateGetterSetter_ShouldMaintainState() {
        Date date1 = new Date(1234567890000L);
        Date date2 = new Date(9876543210000L);
        
        visit.setDate(date1);
        assertEquals(date1, visit.getDate());
        
        visit.setDate(date2);
        assertEquals(date2, visit.getDate());
    }

    @Test
    void testDescriptionGetterSetter_ShouldMaintainState() {
        visit.setDescription("First description");
        assertEquals("First description", visit.getDescription());
        
        visit.setDescription("Second description");
        assertEquals("Second description", visit.getDescription());
    }

    @Test
    void testPetGetterSetter_ShouldMaintainState() {
        Pet pet1 = new Pet();
        pet1.setName("Pet1");
        Pet pet2 = new Pet();
        pet2.setName("Pet2");
        
        visit.setPet(pet1);
        assertEquals(pet1, visit.getPet());
        
        visit.setPet(pet2);
        assertEquals(pet2, visit.getPet());
    }
}