package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerTest {

    private Owner owner;

    @Mock
    private Pet mockPet1;

    @Mock
    private Pet mockPet2;

    @Mock
    private Pet mockPet3;

    @BeforeEach
    void setUp() {
        owner = new Owner();
    }

    @Test
    void testGetAndSetAddress() {
        String address = "123 Main St";
        owner.setAddress(address);
        assertEquals(address, owner.getAddress());
    }

    @Test
    void testGetAndSetCity() {
        String city = "New York";
        owner.setCity(city);
        assertEquals(city, owner.getCity());
    }

    @Test
    void testGetAndSetTelephone() {
        String telephone = "1234567890";
        owner.setTelephone(telephone);
        assertEquals(telephone, owner.getTelephone());
    }

    @Test
    void testSetPetsInternal() {
        Set<Pet> pets = new HashSet<>();
        pets.add(mockPet1);
        owner.setPetsInternal(pets);
        assertEquals(pets, owner.getPetsInternal());
    }

    @Test
    void testGetPetsInternal_WhenNull_InitializesEmptySet() {
        Set<Pet> pets = owner.getPetsInternal();
        assertNotNull(pets);
        assertTrue(pets.isEmpty());
    }

    @Test
    void testGetPetsInternal_WhenNotNull_ReturnsSameSet() {
        Set<Pet> pets = new HashSet<>();
        pets.add(mockPet1);
        owner.setPetsInternal(pets);
        Set<Pet> retrievedPets = owner.getPetsInternal();
        assertEquals(pets, retrievedPets);
    }

    @Test
    void testGetPets_ReturnsUnmodifiableList() {
        Pet pet1 = new Pet();
        pet1.setName("Fluffy");
        owner.addPet(pet1);

        List<Pet> pets = owner.getPets();
        assertNotNull(pets);
        assertThrows(UnsupportedOperationException.class, () -> pets.add(mockPet2));
    }

    @Test
    void testGetPets_ReturnsSortedList() {
        Pet pet1 = new Pet();
        pet1.setName("Zebra");
        Pet pet2 = new Pet();
        pet2.setName("Alpha");
        Pet pet3 = new Pet();
        pet3.setName("Beta");

        owner.addPet(pet1);
        owner.addPet(pet2);
        owner.addPet(pet3);

        List<Pet> pets = owner.getPets();
        assertEquals(3, pets.size());
        assertEquals("Alpha", pets.get(0).getName());
        assertEquals("Beta", pets.get(1).getName());
        assertEquals("Zebra", pets.get(2).getName());
    }

    @Test
    void testGetPets_WhenEmpty_ReturnsEmptyList() {
        List<Pet> pets = owner.getPets();
        assertNotNull(pets);
        assertTrue(pets.isEmpty());
    }

    @Test
    void testAddPet_SetsBidirectionalRelationship() {
        Pet pet = new Pet();
        pet.setName("Buddy");

        owner.addPet(pet);

        assertTrue(owner.getPetsInternal().contains(pet));
        assertEquals(owner, pet.getOwner());
    }

    @Test
    void testAddPet_MultiplesPets() {
        Pet pet1 = new Pet();
        pet1.setName("Pet1");
        Pet pet2 = new Pet();
        pet2.setName("Pet2");

        owner.addPet(pet1);
        owner.addPet(pet2);

        assertEquals(2, owner.getPetsInternal().size());
        assertEquals(owner, pet1.getOwner());
        assertEquals(owner, pet2.getOwner());
    }

    @Test
    void testGetPet_String_Found() {
        Pet pet = new Pet();
        pet.setName("Fluffy");
        owner.addPet(pet);

        Pet foundPet = owner.getPet("Fluffy");
        assertNotNull(foundPet);
        assertEquals(pet, foundPet);
    }

    @Test
    void testGetPet_String_NotFound() {
        Pet pet = new Pet();
        pet.setName("Fluffy");
        owner.addPet(pet);

        Pet foundPet = owner.getPet("NonExistent");
        assertNull(foundPet);
    }

    @Test
    void testGetPet_String_CaseInsensitive() {
        Pet pet = new Pet();
        pet.setName("Fluffy");
        owner.addPet(pet);

        Pet foundPet1 = owner.getPet("fluffy");
        Pet foundPet2 = owner.getPet("FLUFFY");
        Pet foundPet3 = owner.getPet("FlUfFy");

        assertNotNull(foundPet1);
        assertNotNull(foundPet2);
        assertNotNull(foundPet3);
        assertEquals(pet, foundPet1);
        assertEquals(pet, foundPet2);
        assertEquals(pet, foundPet3);
    }

    @Test
    void testGetPet_String_EmptyPets() {
        Pet foundPet = owner.getPet("Fluffy");
        assertNull(foundPet);
    }

    @Test
    void testGetPet_StringBoolean_IgnoreNewFalse_FoundNewPet() {
        Pet newPet = new Pet();
        newPet.setName("NewPet");
        owner.addPet(newPet);

        Pet foundPet = owner.getPet("NewPet", false);
        assertNotNull(foundPet);
        assertEquals(newPet, foundPet);
    }

    @Test
    void testGetPet_StringBoolean_IgnoreNewTrue_SkipsNewPet() {
        Pet newPet = new Pet();
        newPet.setName("NewPet");
        owner.addPet(newPet);

        Pet foundPet = owner.getPet("NewPet", true);
        assertNull(foundPet);
    }

    @Test
    void testGetPet_StringBoolean_IgnoreNewTrue_FindsExistingPet() {
        when(mockPet1.isNew()).thenReturn(false);
        when(mockPet1.getName()).thenReturn("ExistingPet");
        owner.getPetsInternal().add(mockPet1);

        Pet foundPet = owner.getPet("ExistingPet", true);
        assertNotNull(foundPet);
        assertEquals(mockPet1, foundPet);
    }

    @Test
    void testGetPet_StringBoolean_IgnoreNewFalse_FindsExistingPet() {
        when(mockPet1.getName()).thenReturn("ExistingPet");
        owner.getPetsInternal().add(mockPet1);

        Pet foundPet = owner.getPet("ExistingPet", false);
        assertNotNull(foundPet);
        assertEquals(mockPet1, foundPet);
        
        verify(mockPet1).getName();
    }

    @Test
    void testGetPet_StringBoolean_CaseInsensitive() {
        when(mockPet1.isNew()).thenReturn(false);
        when(mockPet1.getName()).thenReturn("TestPet");
        owner.getPetsInternal().add(mockPet1);

        Pet foundPet = owner.getPet("TESTPET", true);
        assertNotNull(foundPet);
        assertEquals(mockPet1, foundPet);
    }

    @Test
    void testGetPet_StringBoolean_NotFound() {
        when(mockPet1.getName()).thenReturn("Pet1");
        owner.getPetsInternal().add(mockPet1);

        Pet foundPet = owner.getPet("Pet2", false);
        assertNull(foundPet);
        
        verify(mockPet1).getName();
    }

    @Test
    void testGetPet_StringBoolean_MixedNewAndExisting() {
        when(mockPet1.isNew()).thenReturn(true);
        when(mockPet2.isNew()).thenReturn(false);
        when(mockPet2.getName()).thenReturn("OldPet");

        owner.getPetsInternal().add(mockPet1);
        owner.getPetsInternal().add(mockPet2);

        Pet foundPet2 = owner.getPet("OldPet", true);

        assertNotNull(foundPet2);
        assertEquals(mockPet2, foundPet2);
    }

    @Test
    void testToString() {
        owner.setId(1);
        owner.setFirstName("John");
        owner.setLastName("Doe");
        owner.setAddress("123 Main St");
        owner.setCity("New York");
        owner.setTelephone("1234567890");

        String result = owner.toString();

        assertNotNull(result);
        assertTrue(result.contains("id"));
        assertTrue(result.contains("1"));
        assertTrue(result.contains("lastName"));
        assertTrue(result.contains("Doe"));
        assertTrue(result.contains("firstName"));
        assertTrue(result.contains("John"));
        assertTrue(result.contains("address"));
        assertTrue(result.contains("123 Main St"));
        assertTrue(result.contains("city"));
        assertTrue(result.contains("New York"));
        assertTrue(result.contains("telephone"));
        assertTrue(result.contains("1234567890"));
    }

    @Test
    void testToString_WithNewEntity() {
        owner.setFirstName("Jane");
        owner.setLastName("Smith");
        owner.setAddress("456 Oak Ave");
        owner.setCity("Boston");
        owner.setTelephone("9876543210");

        String result = owner.toString();

        assertNotNull(result);
        assertTrue(result.contains("new"));
        assertTrue(result.contains("true"));
        assertTrue(result.contains("lastName"));
        assertTrue(result.contains("Smith"));
        assertTrue(result.contains("firstName"));
        assertTrue(result.contains("Jane"));
    }
}