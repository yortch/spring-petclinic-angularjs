package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class OwnerTest {

    private Owner owner;

    @BeforeEach
    void setUp() {
        owner = new Owner();
    }

    @Test
    void gettersAndSetters_roundTrip() {
        owner.setAddress("123 Main St");
        owner.setCity("Springfield");
        owner.setTelephone("1234567890");

        assertEquals("123 Main St", owner.getAddress());
        assertEquals("Springfield", owner.getCity());
        assertEquals("1234567890", owner.getTelephone());
    }

    @Test
    void getPetsInternal_initializesWhenNull() {
        Set<Pet> internal = owner.getPetsInternal();
        assertNotNull(internal);
        assertTrue(internal.isEmpty());
    }

    @Test
    void addPet_addsToOwnerAndSetsBackReference() {
        Pet pet = new Pet();
        pet.setName("Rex");

        owner.addPet(pet);

        assertEquals(1, owner.getPets().size());
        assertTrue(owner.getPetsInternal().contains(pet));
        assertSame(owner, pet.getOwner());
    }

    @Test
    void getPets_sortedCaseInsensitiveAndUnmodifiable() {
        Pet p1 = new Pet();
        p1.setName("Beta");
        Pet p2 = new Pet();
        p2.setName("alpha");

        owner.addPet(p1);
        owner.addPet(p2);

        List<Pet> pets = owner.getPets();
        assertEquals(2, pets.size());
        assertEquals("alpha", pets.get(0).getName());
        assertEquals("Beta", pets.get(1).getName());

        assertThrows(UnsupportedOperationException.class, () -> pets.add(new Pet()));
    }

    @Test
    void getPet_byName_caseInsensitive_matchWhenIgnoreNewFalse() {
        Pet pet = new Pet();
        pet.setName("Fluffy");
        owner.addPet(pet);

        Pet found1 = owner.getPet("fluffy");
        Pet found2 = owner.getPet("FLUFFY", false);

        assertSame(pet, found1);
        assertSame(pet, found2);
    }

    @Test
    void getPet_returnsNullWhenIgnoreNewTrueAndOnlyNewPets() {
        Pet pet = new Pet();
        pet.setName("Buddy"); // new pet (id == null)
        owner.addPet(pet);

        Pet found = owner.getPet("Buddy", true);
        assertNull(found);
    }

    @Test
    void getPet_returnsNullWhenNoMatch() {
        Pet pet = new Pet();
        pet.setName("Max");
        owner.addPet(pet);

        Pet found = owner.getPet("Unknown");
        assertNull(found);
    }

    @Test
    void getPet_nullName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> owner.getPet(null));
        assertThrows(NullPointerException.class, () -> owner.getPet(null, false));
    }

    @Test
    void toString_containsCoreFields() {
        owner.setFirstName("Jane");
        owner.setLastName("Doe");
        owner.setAddress("742 Evergreen Terrace");
        owner.setCity("Springfield");
        owner.setTelephone("5551234567");

        String s = owner.toString();
        assertTrue(s.contains("firstName"));
        assertTrue(s.contains("lastName"));
        assertTrue(s.contains("address"));
        assertTrue(s.contains("city"));
        assertTrue(s.contains("telephone"));
        assertTrue(s.contains("Jane"));
        assertTrue(s.contains("Doe"));
        assertTrue(s.contains("742 Evergreen Terrace"));
        assertTrue(s.contains("Springfield"));
        assertTrue(s.contains("5551234567"));
    }

    @Test
    void setPetsInternal_replacesInternalSet() {
        Pet p = new Pet();
        p.setName("Kitty");
        Set<Pet> custom = new HashSet<>();
        custom.add(p);

        owner.setPetsInternal(custom);

        assertTrue(owner.getPetsInternal().contains(p));
        assertEquals(1, owner.getPets().size());
    }
}