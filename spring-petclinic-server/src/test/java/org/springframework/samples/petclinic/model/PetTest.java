package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PetTest {

    private Pet pet;

    @Mock
    private PetType petType;

    @Mock
    private Owner owner;

    @Mock
    private Visit visit1;

    @Mock
    private Visit visit2;

    @BeforeEach
    void setUp() {
        pet = new Pet();
    }

    @Test
    void testSetBirthDate() {
        Date birthDate = new Date();
        pet.setBirthDate(birthDate);
        assertEquals(birthDate, pet.getBirthDate());
    }

    @Test
    void testGetBirthDate() {
        assertNull(pet.getBirthDate());
        Date birthDate = new Date();
        pet.setBirthDate(birthDate);
        assertEquals(birthDate, pet.getBirthDate());
    }

    @Test
    void testSetBirthDateWithNull() {
        pet.setBirthDate(null);
        assertNull(pet.getBirthDate());
    }

    @Test
    void testSetType() {
        pet.setType(petType);
        assertEquals(petType, pet.getType());
    }

    @Test
    void testGetType() {
        assertNull(pet.getType());
        pet.setType(petType);
        assertEquals(petType, pet.getType());
    }

    @Test
    void testSetTypeWithNull() {
        pet.setType(null);
        assertNull(pet.getType());
    }

    @Test
    void testSetOwner() {
        pet.setOwner(owner);
        assertEquals(owner, pet.getOwner());
    }

    @Test
    void testGetOwner() {
        assertNull(pet.getOwner());
        pet.setOwner(owner);
        assertEquals(owner, pet.getOwner());
    }

    @Test
    void testSetOwnerWithNull() {
        pet.setOwner(null);
        assertNull(pet.getOwner());
    }

    @Test
    void testGetVisitsInternal_WhenNull_InitializesNewHashSet() {
        Set<Visit> visits = pet.getVisitsInternal();
        assertNotNull(visits);
        assertTrue(visits.isEmpty());
        assertTrue(visits instanceof HashSet);
    }

    @Test
    void testGetVisitsInternal_WhenNotNull_ReturnsSameInstance() {
        Set<Visit> firstCall = pet.getVisitsInternal();
        Set<Visit> secondCall = pet.getVisitsInternal();
        assertSame(firstCall, secondCall);
    }

    @Test
    void testSetVisitsInternal() {
        Set<Visit> visits = new HashSet<>();
        visits.add(visit1);
        pet.setVisitsInternal(visits);
        assertEquals(visits, pet.getVisitsInternal());
    }

    @Test
    void testSetVisitsInternalWithNull() {
        pet.setVisitsInternal(null);
        assertNull(pet.getVisitsInternal());
    }

    @Test
    void testGetVisits_WhenEmpty_ReturnsEmptyUnmodifiableList() {
        List<Visit> visits = pet.getVisits();
        assertNotNull(visits);
        assertTrue(visits.isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> visits.add(visit1));
    }

    @Test
    void testGetVisits_ReturnsSortedList() {
        Calendar cal = Calendar.getInstance();
        cal.set(2023, Calendar.JANUARY, 15);
        Date laterDate = cal.getTime();
        
        cal.set(2023, Calendar.JANUARY, 10);
        Date earlierDate = cal.getTime();

        Visit earlyVisit = new Visit();
        earlyVisit.setDate(earlierDate);

        Visit lateVisit = new Visit();
        lateVisit.setDate(laterDate);

        pet.addVisit(lateVisit);
        pet.addVisit(earlyVisit);

        List<Visit> visits = pet.getVisits();
        assertEquals(2, visits.size());
        assertEquals(earlierDate, visits.get(0).getDate());
        assertEquals(laterDate, visits.get(1).getDate());
    }

    @Test
    void testGetVisits_ReturnsUnmodifiableList() {
        pet.addVisit(visit1);
        List<Visit> visits = pet.getVisits();
        assertThrows(UnsupportedOperationException.class, () -> visits.add(visit2));
    }

    @Test
    void testAddVisit() {
        Visit visit = new Visit();
        pet.addVisit(visit);
        
        Set<Visit> internalVisits = pet.getVisitsInternal();
        assertTrue(internalVisits.contains(visit));
        assertEquals(pet, visit.getPet());
    }

    @Test
    void testAddVisit_MultipleVisits() {
        Visit visit1 = new Visit();
        Visit visit2 = new Visit();
        
        pet.addVisit(visit1);
        pet.addVisit(visit2);
        
        Set<Visit> internalVisits = pet.getVisitsInternal();
        assertEquals(2, internalVisits.size());
        assertTrue(internalVisits.contains(visit1));
        assertTrue(internalVisits.contains(visit2));
        assertEquals(pet, visit1.getPet());
        assertEquals(pet, visit2.getPet());
    }

    @Test
    void testAddVisit_DuplicateVisit() {
        Visit visit = new Visit();
        pet.addVisit(visit);
        pet.addVisit(visit);
        
        Set<Visit> internalVisits = pet.getVisitsInternal();
        assertEquals(1, internalVisits.size());
    }

    @Test
    void testInheritedNameProperty() {
        String petName = "Fluffy";
        pet.setName(petName);
        assertEquals(petName, pet.getName());
    }

    @Test
    void testInheritedIdProperty() {
        Integer id = 123;
        pet.setId(id);
        assertEquals(id, pet.getId());
    }

    @Test
    void testInheritedIsNew_WhenIdIsNull() {
        pet.setId(null);
        assertTrue(pet.isNew());
    }

    @Test
    void testInheritedIsNew_WhenIdIsNotNull() {
        pet.setId(1);
        assertFalse(pet.isNew());
    }

    @Test
    void testCompleteObjectCreation() {
        Date birthDate = new Date();
        String petName = "Max";
        Integer id = 1;

        pet.setId(id);
        pet.setName(petName);
        pet.setBirthDate(birthDate);
        pet.setType(petType);
        pet.setOwner(owner);

        assertEquals(id, pet.getId());
        assertEquals(petName, pet.getName());
        assertEquals(birthDate, pet.getBirthDate());
        assertEquals(petType, pet.getType());
        assertEquals(owner, pet.getOwner());
        assertFalse(pet.isNew());
    }
}