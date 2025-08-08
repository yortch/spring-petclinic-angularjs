package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class VisitTest {

    @Test
    void constructor_initializesDateNotNull() {
        Visit visit = new Visit();
        assertNotNull(visit.getDate());
    }

    @Test
    void getAndSetDate_roundTrip() {
        Visit visit = new Visit();
        Date d = new Date(123456789L);
        visit.setDate(d);
        assertSame(d, visit.getDate());
    }

    @Test
    void setDate_allowsNull() {
        Visit visit = new Visit();
        visit.setDate(null);
        assertNull(visit.getDate());
    }

    @Test
    void getAndSetDescription_roundTrip() {
        Visit visit = new Visit();
        String desc = "Annual checkup and vaccination.";
        visit.setDescription(desc);
        assertEquals(desc, visit.getDescription());
    }

    @Test
    void setDescription_allowsNull() {
        Visit visit = new Visit();
        visit.setDescription(null);
        assertNull(visit.getDescription());
    }

    @Test
    void getAndSetPet_roundTrip() {
        Visit visit = new Visit();
        Pet pet = new Pet();
        visit.setPet(pet);
        assertSame(pet, visit.getPet());
    }

    @Test
    void setPet_allowsNull() {
        Visit visit = new Visit();
        visit.setPet(null);
        assertNull(visit.getPet());
    }
}