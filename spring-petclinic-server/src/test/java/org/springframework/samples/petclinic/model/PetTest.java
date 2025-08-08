package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PetTest {

    private static Date dateOf(int year, int monthZeroBased, int day) {
        Calendar cal = new GregorianCalendar(year, monthZeroBased, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    @Test
    void setAndGetBirthDate() {
        Pet pet = new Pet();
        Date d = dateOf(2020, Calendar.JANUARY, 15);
        pet.setBirthDate(d);
        assertEquals(d, pet.getBirthDate());
    }

    @Test
    void setAndGetType() {
        Pet pet = new Pet();
        PetType type = new PetType();
        type.setName("cat");
        pet.setType(type);
        assertSame(type, pet.getType());
    }

    @Test
    void setAndGetOwner_protectedSetterAccessibleInSamePackage() {
        Pet pet = new Pet();
        Owner owner = new Owner();
        owner.setFirstName("John");
        owner.setLastName("Doe");
        pet.setOwner(owner);
        assertSame(owner, pet.getOwner());
    }

    @Test
    void getVisitsInternal_initializesWhenNull_andCachesInstance() {
        Pet pet = new Pet();
        // first call initializes
        Set<Visit> first = pet.getVisitsInternal();
        assertNotNull(first);
        assertTrue(first.isEmpty());
        // second call returns same instance
        Set<Visit> second = pet.getVisitsInternal();
        assertSame(first, second);
    }

    @Test
    void setVisitsInternal_replacesSet() {
        Pet pet = new Pet();
        Set<Visit> custom = new HashSet<>();
        pet.setVisitsInternal(custom);
        assertSame(custom, pet.getVisitsInternal());

        // Ensure getVisits reflects content and remains sorted
        Visit v1 = new Visit();
        v1.setDate(dateOf(2020, Calendar.JANUARY, 10));
        Visit v2 = new Visit();
        v2.setDate(dateOf(2020, Calendar.JANUARY, 5));
        // add directly into the custom set to verify replacement worked
        custom.add(v1);
        custom.add(v2);

        List<Visit> visits = pet.getVisits();
        assertEquals(2, visits.size());
        assertTrue(!visits.get(0).getDate().after(visits.get(1).getDate()));
    }

    @Test
    void addVisit_addsToInternalSet_andSetsBackReference() {
        Pet pet = new Pet();
        Visit visit = new Visit();
        visit.setDate(dateOf(2021, Calendar.MARCH, 3));

        pet.addVisit(visit);

        Set<Visit> internal = pet.getVisitsInternal();
        assertTrue(internal.contains(visit));
        assertSame(pet, visit.getPet());
    }

    @Test
    void getVisits_returnsEmptyUnmodifiableListWhenNoVisits() {
        Pet pet = new Pet();
        List<Visit> visits = pet.getVisits();
        assertNotNull(visits);
        assertTrue(visits.isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> visits.add(new Visit()));
    }

    @Test
    void getVisits_returnsSortedAscendingByDate_andIsUnmodifiable() {
        Pet pet = new Pet();

        Visit later = new Visit();
        later.setDate(dateOf(2022, Calendar.JANUARY, 10));
        Visit earlier = new Visit();
        earlier.setDate(dateOf(2022, Calendar.JANUARY, 1));

        // add in reverse order to ensure sorting happens
        pet.addVisit(later);
        pet.addVisit(earlier);

        List<Visit> sorted = pet.getVisits();
        assertEquals(2, sorted.size());
        assertEquals(earlier, sorted.get(0));
        assertEquals(later, sorted.get(1));

        assertThrows(UnsupportedOperationException.class, () -> sorted.remove(0));
    }
}