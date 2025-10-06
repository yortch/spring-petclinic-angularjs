package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetTest {

    private Pet pet;

    @Mock
    private PetType mockPetType;

    @Mock
    private Owner mockOwner;

    @Mock
    private Visit mockVisit1;

    @Mock
    private Visit mockVisit2;

    @Mock
    private Visit mockVisit3;

    @BeforeEach
    void setUp() {
        pet = new Pet();
    }

    @Test
    void testSetAndGetBirthDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = sdf.parse("2020-01-15");

        pet.setBirthDate(birthDate);

        assertEquals(birthDate, pet.getBirthDate());
    }

    @Test
    void testGetBirthDateWhenNull() {
        assertNull(pet.getBirthDate());
    }

    @Test
    void testSetAndGetType() {
        pet.setType(mockPetType);

        assertEquals(mockPetType, pet.getType());
    }

    @Test
    void testGetTypeWhenNull() {
        assertNull(pet.getType());
    }

    @Test
    void testSetAndGetOwner() {
        // setOwner is protected, use addPet on Owner instead
        Owner realOwner = new Owner();
        realOwner.addPet(pet);

        assertEquals(realOwner, pet.getOwner());
    }

    @Test
    void testGetOwnerWhenNull() {
        assertNull(pet.getOwner());
    }

    @Test
    void testSetVisitsInternal() {
        Set<Visit> visits = new HashSet<>();
        visits.add(mockVisit1);
        visits.add(mockVisit2);

        pet.setVisitsInternal(visits);

        assertEquals(visits, pet.getVisitsInternal());
    }

    @Test
    void testGetVisitsInternalLazyInitialization() {
        Set<Visit> visits = pet.getVisitsInternal();

        assertNotNull(visits);
        assertTrue(visits.isEmpty());
    }

    @Test
    void testGetVisitsInternalReturnsExistingSet() {
        Set<Visit> visits = new HashSet<>();
        visits.add(mockVisit1);
        pet.setVisitsInternal(visits);

        Set<Visit> retrievedVisits = pet.getVisitsInternal();

        assertSame(visits, retrievedVisits);
        assertEquals(1, retrievedVisits.size());
    }

    @Test
    void testGetVisitsInternalMultipleCalls() {
        Set<Visit> visits1 = pet.getVisitsInternal();
        Set<Visit> visits2 = pet.getVisitsInternal();

        assertSame(visits1, visits2);
    }

    @Test
    void testGetVisitsReturnsEmptyList() {
        List<Visit> visits = pet.getVisits();

        assertNotNull(visits);
        assertTrue(visits.isEmpty());
    }

    @Test
    void testGetVisitsReturnsSortedList() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = sdf.parse("2023-01-15");
        Date date2 = sdf.parse("2023-01-10");
        Date date3 = sdf.parse("2023-01-20");

        when(mockVisit1.getDate()).thenReturn(date1);
        when(mockVisit2.getDate()).thenReturn(date2);
        when(mockVisit3.getDate()).thenReturn(date3);

        Set<Visit> visits = new HashSet<>();
        visits.add(mockVisit1);
        visits.add(mockVisit2);
        visits.add(mockVisit3);
        pet.setVisitsInternal(visits);

        List<Visit> sortedVisits = pet.getVisits();

        assertEquals(3, sortedVisits.size());
        // Visits are sorted descending by date (newest first)
        assertEquals(mockVisit3, sortedVisits.get(0)); // 2023-01-20 (newest)
        assertEquals(mockVisit1, sortedVisits.get(1)); // 2023-01-15
        assertEquals(mockVisit2, sortedVisits.get(2)); // 2023-01-10 (oldest)
    }

    @Test
    void testGetVisitsReturnsUnmodifiableList() {
        pet.addVisit(mockVisit1);
        List<Visit> visits = pet.getVisits();

        assertThrows(UnsupportedOperationException.class, () -> {
            visits.add(mockVisit2);
        });
    }

    @Test
    void testAddVisit() {
        pet.addVisit(mockVisit1);

        Set<Visit> visits = pet.getVisitsInternal();
        assertTrue(visits.contains(mockVisit1));
        verify(mockVisit1).setPet(pet);
    }

    @Test
    void testAddMultipleVisits() {
        pet.addVisit(mockVisit1);
        pet.addVisit(mockVisit2);
        pet.addVisit(mockVisit3);

        Set<Visit> visits = pet.getVisitsInternal();
        assertEquals(3, visits.size());
        assertTrue(visits.contains(mockVisit1));
        assertTrue(visits.contains(mockVisit2));
        assertTrue(visits.contains(mockVisit3));
        verify(mockVisit1).setPet(pet);
        verify(mockVisit2).setPet(pet);
        verify(mockVisit3).setPet(pet);
    }

    @Test
    void testAddVisitSetsPetReference() {
        pet.addVisit(mockVisit1);

        verify(mockVisit1, times(1)).setPet(pet);
    }

    @Test
    void testInheritedNameMethods() {
        String name = "Fluffy";
        pet.setName(name);

        assertEquals(name, pet.getName());
    }

    @Test
    void testInheritedIdMethods() {
        Integer id = 123;
        pet.setId(id);

        assertEquals(id, pet.getId());
    }

    @Test
    void testSetBirthDateToNull() {
        pet.setBirthDate(new Date());
        pet.setBirthDate(null);

        assertNull(pet.getBirthDate());
    }

    @Test
    void testSetTypeToNull() {
        pet.setType(mockPetType);
        pet.setType(null);

        assertNull(pet.getType());
    }

    @Test
    void testSetVisitsInternalToNull() {
        Set<Visit> visits = new HashSet<>();
        visits.add(mockVisit1);
        pet.setVisitsInternal(visits);
        pet.setVisitsInternal(null);

        // getVisitsInternal() initializes if null
        Set<Visit> retrievedVisits = pet.getVisitsInternal();
        assertNotNull(retrievedVisits);
        assertEquals(0, retrievedVisits.size());
    }
}