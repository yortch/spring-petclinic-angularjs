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
        specialty1.setName("Dentistry");
        
        specialty2 = new Specialty();
        specialty2.setName("Surgery");
        
        specialty3 = new Specialty();
        specialty3.setName("Cardiology");
    }

    @Test
    void testGetSpecialtiesInternal_WhenNull_InitializesHashSet() {
        Set<Specialty> result = vet.getSpecialtiesInternal();
        
        assertNotNull(result);
        assertTrue(result instanceof HashSet);
        assertEquals(0, result.size());
    }

    @Test
    void testGetSpecialtiesInternal_WhenNotNull_ReturnsSameInstance() {
        Set<Specialty> initialSet = new HashSet<>();
        initialSet.add(specialty1);
        vet.setSpecialtiesInternal(initialSet);
        
        Set<Specialty> result = vet.getSpecialtiesInternal();
        
        assertSame(initialSet, result);
        assertEquals(1, result.size());
        assertTrue(result.contains(specialty1));
    }

    @Test
    void testSetSpecialtiesInternal_SetsSpecialties() {
        Set<Specialty> specialties = new HashSet<>();
        specialties.add(specialty1);
        specialties.add(specialty2);
        
        vet.setSpecialtiesInternal(specialties);
        
        Set<Specialty> result = vet.getSpecialtiesInternal();
        assertEquals(2, result.size());
        assertTrue(result.contains(specialty1));
        assertTrue(result.contains(specialty2));
    }

    @Test
    void testGetSpecialties_ReturnsEmptyList_WhenNoSpecialties() {
        List<Specialty> result = vet.getSpecialties();
        
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testGetSpecialties_ReturnsSortedList() {
        vet.addSpecialty(specialty2);
        vet.addSpecialty(specialty1);
        vet.addSpecialty(specialty3);
        
        List<Specialty> result = vet.getSpecialties();
        
        assertEquals(3, result.size());
        assertEquals("Cardiology", result.get(0).getName());
        assertEquals("Dentistry", result.get(1).getName());
        assertEquals("Surgery", result.get(2).getName());
    }

    @Test
    void testGetSpecialties_ReturnsUnmodifiableList() {
        vet.addSpecialty(specialty1);
        
        List<Specialty> result = vet.getSpecialties();
        
        assertThrows(UnsupportedOperationException.class, () -> {
            result.add(specialty2);
        });
    }

    @Test
    void testGetSpecialties_ReturnsUnmodifiableList_OnRemove() {
        vet.addSpecialty(specialty1);
        
        List<Specialty> result = vet.getSpecialties();
        
        assertThrows(UnsupportedOperationException.class, () -> {
            result.remove(0);
        });
    }

    @Test
    void testGetNrOfSpecialties_ReturnsZero_WhenNoSpecialties() {
        int result = vet.getNrOfSpecialties();
        
        assertEquals(0, result);
    }

    @Test
    void testGetNrOfSpecialties_ReturnsCorrectCount() {
        vet.addSpecialty(specialty1);
        vet.addSpecialty(specialty2);
        
        int result = vet.getNrOfSpecialties();
        
        assertEquals(2, result);
    }

    @Test
    void testGetNrOfSpecialties_ReturnsCorrectCount_AfterSetSpecialties() {
        Set<Specialty> specialties = new HashSet<>();
        specialties.add(specialty1);
        specialties.add(specialty2);
        specialties.add(specialty3);
        vet.setSpecialtiesInternal(specialties);
        
        int result = vet.getNrOfSpecialties();
        
        assertEquals(3, result);
    }

    @Test
    void testAddSpecialty_AddsToSet() {
        vet.addSpecialty(specialty1);
        
        assertEquals(1, vet.getNrOfSpecialties());
        assertTrue(vet.getSpecialtiesInternal().contains(specialty1));
    }

    @Test
    void testAddSpecialty_InitializesSet_WhenNull() {
        vet.addSpecialty(specialty1);
        
        assertNotNull(vet.getSpecialtiesInternal());
        assertEquals(1, vet.getNrOfSpecialties());
    }

    @Test
    void testAddSpecialty_DoesNotAddDuplicate() {
        vet.addSpecialty(specialty1);
        vet.addSpecialty(specialty1);
        
        assertEquals(1, vet.getNrOfSpecialties());
    }

    @Test
    void testAddSpecialty_MultipleSpecialties() {
        vet.addSpecialty(specialty1);
        vet.addSpecialty(specialty2);
        vet.addSpecialty(specialty3);
        
        assertEquals(3, vet.getNrOfSpecialties());
        List<Specialty> specialties = vet.getSpecialties();
        assertEquals(3, specialties.size());
    }

    @Test
    void testSetSpecialtiesInternal_WithNull() {
        vet.addSpecialty(specialty1);
        vet.setSpecialtiesInternal(null);
        
        Set<Specialty> result = vet.getSpecialtiesInternal();
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testGetSpecialties_CreatesNewListEachTime() {
        vet.addSpecialty(specialty1);
        
        List<Specialty> result1 = vet.getSpecialties();
        List<Specialty> result2 = vet.getSpecialties();
        
        assertNotSame(result1, result2);
        assertEquals(result1, result2);
    }
}