package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VisitTest {

    @Mock
    private Pet mockPet;

    private Visit visit;

    @BeforeEach
    void setUp() {
        visit = new Visit();
    }

    @Test
    void testConstructor_ShouldInitializeDateToCurrentDate() {
        Visit newVisit = new Visit();
        assertNotNull(newVisit.getDate());
        assertTrue(Math.abs(new Date().getTime() - newVisit.getDate().getTime()) < 1000);
    }

    @Test
    void testGetDate_ShouldReturnDate() {
        assertNotNull(visit.getDate());
    }

    @Test
    void testSetDate_WithValidDate_ShouldSetDate() {
        Date testDate = new Date(System.currentTimeMillis() - 86400000);
        visit.setDate(testDate);
        assertEquals(testDate, visit.getDate());
    }

    @Test
    void testSetDate_WithNull_ShouldSetNull() {
        visit.setDate(null);
        assertNull(visit.getDate());
    }

    @Test
    void testGetDescription_InitialValue_ShouldBeNull() {
        assertNull(visit.getDescription());
    }

    @Test
    void testSetDescription_WithValidString_ShouldSetDescription() {
        String description = "Annual checkup";
        visit.setDescription(description);
        assertEquals(description, visit.getDescription());
    }

    @Test
    void testSetDescription_WithNull_ShouldSetNull() {
        visit.setDescription("Some description");
        visit.setDescription(null);
        assertNull(visit.getDescription());
    }

    @Test
    void testSetDescription_WithEmptyString_ShouldSetEmptyString() {
        visit.setDescription("");
        assertEquals("", visit.getDescription());
    }

    @Test
    void testSetDescription_WithLargeString_ShouldSetLargeString() {
        StringBuilder largeDescription = new StringBuilder();
        for (int i = 0; i < 8192; i++) {
            largeDescription.append("a");
        }
        String description = largeDescription.toString();
        visit.setDescription(description);
        assertEquals(description, visit.getDescription());
        assertEquals(8192, visit.getDescription().length());
    }

    @Test
    void testGetPet_InitialValue_ShouldBeNull() {
        assertNull(visit.getPet());
    }

    @Test
    void testSetPet_WithValidPet_ShouldSetPet() {
        visit.setPet(mockPet);
        assertEquals(mockPet, visit.getPet());
    }

    @Test
    void testSetPet_WithNull_ShouldSetNull() {
        visit.setPet(mockPet);
        visit.setPet(null);
        assertNull(visit.getPet());
    }

    @Test
    void testVisit_AllPropertiesTogether_ShouldWorkCorrectly() {
        Date testDate = new Date();
        String description = "Regular checkup";
        
        visit.setDate(testDate);
        visit.setDescription(description);
        visit.setPet(mockPet);
        
        assertEquals(testDate, visit.getDate());
        assertEquals(description, visit.getDescription());
        assertEquals(mockPet, visit.getPet());
    }

    @Test
    void testVisit_MultipleUpdates_ShouldRetainLatestValues() {
        Date date1 = new Date(System.currentTimeMillis() - 86400000);
        Date date2 = new Date(System.currentTimeMillis() - 172800000);
        
        visit.setDate(date1);
        visit.setDescription("First description");
        visit.setPet(mockPet);
        
        visit.setDate(date2);
        visit.setDescription("Second description");
        
        assertEquals(date2, visit.getDate());
        assertEquals("Second description", visit.getDescription());
        assertEquals(mockPet, visit.getPet());
    }
}