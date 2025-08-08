package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class VetTest {

    private Vet vet;

    @BeforeEach
    void setUp() {
        this.vet = new Vet();
    }

    @Test
    void getSpecialtiesInternal_initializes_whenNull() {
        // triggers specialties == null branch
        Set<Specialty> internal = vet.getSpecialtiesInternal();
        assertNotNull(internal);
        assertTrue(internal.isEmpty());
    }

    @Test
    void setSpecialtiesInternal_overrides_and_getSpecialtiesInternal_returns_same_instance() {
        Set<Specialty> preset = new HashSet<>();
        Specialty s = new Specialty();
        preset.add(s);

        vet.setSpecialtiesInternal(preset);
        Set<Specialty> internal = vet.getSpecialtiesInternal();

        assertSame(preset, internal);
        assertTrue(internal.contains(s));
    }

    @Test
    void getSpecialties_empty_unmodifiable() {
        List<Specialty> list = vet.getSpecialties();
        assertNotNull(list);
        assertTrue(list.isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> list.add(new Specialty()));
    }

    @Test
    void getSpecialties_sorted_case_insensitive_and_unmodifiable() {
        Specialty s1 = new Specialty();
        s1.setName("surgery");
        Specialty s2 = new Specialty();
        s2.setName("Anesthesia");

        // set in arbitrary order
        Set<Specialty> set = new HashSet<>();
        set.add(s1);
        set.add(s2);
        vet.setSpecialtiesInternal(set);

        List<Specialty> sorted = vet.getSpecialties();
        assertEquals(2, sorted.size());
        // Expect "Anesthesia" before "surgery" (ascending, ignore case)
        assertEquals("Anesthesia", sorted.get(0).getName());
        assertEquals("surgery", sorted.get(1).getName());

        assertThrows(UnsupportedOperationException.class, () -> sorted.remove(0));
    }

    @Test
    void addSpecialty_and_getNrOfSpecialties_with_duplicates() {
        Specialty a = new Specialty();
        a.setName("A");
        vet.addSpecialty(a);
        vet.addSpecialty(a); // same instance duplicate
        assertEquals(1, vet.getNrOfSpecialties());

        Specialty a2 = new Specialty();
        a2.setName("A"); // same name, different instance -> should count as separate in Set
        vet.addSpecialty(a2);
        assertEquals(2, vet.getNrOfSpecialties());
    }
}