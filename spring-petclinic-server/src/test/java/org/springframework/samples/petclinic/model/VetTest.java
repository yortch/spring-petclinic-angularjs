package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VetTest {

    private Vet vet;
    private Specialty specialty1;
    private Specialty specialty2;
    private Specialty specialty3;

    @BeforeEach
    void setUp() {
        vet = new Vet();
        specialty1 = new Specialty();
        specialty1.setName("Cardiology");
        
        specialty2 = new Specialty();
        specialty2.setName("Surgery");
        
        specialty3 = new Specialty();
        specialty3.setName("Dentistry");
    }

    @Test
    void testSetSpecialtiesInternal() {
        Set<Specialty> specialties = new HashSet<>();
        specialties.add(specialty1);
        
        vet.setSpecialtiesInternal(specialties);
        
        assertEquals(1, vet.getSpecialtiesInternal().size());
        assertTrue(vet.getSpecialtiesInternal().contains(specialty1));
    }

    @Test
    void testSetSpecialtiesInternalWithNull() {
        vet.setSpecialtiesInternal(null);
        
        // getSpecialtiesInternal() initializes the set if null
        assertNotNull(vet.getSpecialtiesInternal());
        assertEquals(0, vet.getSpecialtiesInternal().size());
    }

    @Test
    void testSetSpecialtiesInternalWithEmptySet() {
        Set<Specialty> emptySet = new HashSet<>();
        vet.setSpecialtiesInternal(emptySet);
        
        assertEquals(0, vet.getSpecialtiesInternal().size());
    }

    @Test
    void testGetSpecialtiesInternalWhenNull() {
        Set<Specialty> specialties = vet.getSpecialtiesInternal();
        
        assertNotNull(specialties);
        assertEquals(0, specialties.size());
    }

    @Test
    void testGetSpecialtiesInternalWhenAlreadyInitialized() {
        Set<Specialty> initialSet = new HashSet<>();
        initialSet.add(specialty1);
        vet.setSpecialtiesInternal(initialSet);
        
        Set<Specialty> retrievedSet = vet.getSpecialtiesInternal();
        
        assertSame(initialSet, retrievedSet);
        assertEquals(1, retrievedSet.size());
    }

    @Test
    void testGetSpecialtiesReturnsEmptyList() {
        List<Specialty> specialties = vet.getSpecialties();
        
        assertNotNull(specialties);
        assertEquals(0, specialties.size());
    }

    @Test
    void testGetSpecialtiesReturnsSortedList() {
        vet.addSpecialty(specialty2);
        vet.addSpecialty(specialty1);
        vet.addSpecialty(specialty3);
        
        List<Specialty> specialties = vet.getSpecialties();
        
        assertEquals(3, specialties.size());
        assertEquals("Cardiology", specialties.get(0).getName());
        assertEquals("Dentistry", specialties.get(1).getName());
        assertEquals("Surgery", specialties.get(2).getName());
    }

    @Test
    void testGetSpecialtiesReturnsUnmodifiableList() {
        vet.addSpecialty(specialty1);
        
        List<Specialty> specialties = vet.getSpecialties();
        
        assertThrows(UnsupportedOperationException.class, () -> {
            specialties.add(specialty2);
        });
    }

    @Test
    void testGetNrOfSpecialtiesWithNoSpecialties() {
        assertEquals(0, vet.getNrOfSpecialties());
    }

    @Test
    void testGetNrOfSpecialtiesWithMultipleSpecialties() {
        vet.addSpecialty(specialty1);
        vet.addSpecialty(specialty2);
        vet.addSpecialty(specialty3);
        
        assertEquals(3, vet.getNrOfSpecialties());
    }

    @Test
    void testAddSpecialtyToEmptySet() {
        vet.addSpecialty(specialty1);
        
        assertEquals(1, vet.getNrOfSpecialties());
        assertTrue(vet.getSpecialtiesInternal().contains(specialty1));
    }

    @Test
    void testAddMultipleSpecialties() {
        vet.addSpecialty(specialty1);
        vet.addSpecialty(specialty2);
        
        assertEquals(2, vet.getNrOfSpecialties());
        assertTrue(vet.getSpecialtiesInternal().contains(specialty1));
        assertTrue(vet.getSpecialtiesInternal().contains(specialty2));
    }

    @Test
    void testAddDuplicateSpecialty() {
        vet.addSpecialty(specialty1);
        vet.addSpecialty(specialty1);
        
        assertEquals(1, vet.getNrOfSpecialties());
    }
}