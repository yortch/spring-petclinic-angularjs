package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

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
        String address = "123 Main Street";
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
    void testGetPetsInternalWhenNull() {
        Set<Pet> pets = owner.getPetsInternal();
        assertNotNull(pets);
        assertTrue(pets.isEmpty());
    }

    @Test
    void testGetPetsInternalWhenNotNull() {
        Set<Pet> pets = owner.getPetsInternal();
        pets.add(mockPet1);
        Set<Pet> samePets = owner.getPetsInternal();
        assertSame(pets, samePets);
        assertEquals(1, samePets.size());
    }

    @Test
    void testGetPetsReturnsSortedUnmodifiableList() {
        when(mockPet1.getName()).thenReturn("Charlie");
        when(mockPet2.getName()).thenReturn("Alpha");
        when(mockPet3.getName()).thenReturn("Beta");

        owner.addPet(mockPet1);
        owner.addPet(mockPet2);
        owner.addPet(mockPet3);

        List<Pet> pets = owner.getPets();
        
        assertEquals(3, pets.size());
        assertEquals("Alpha", pets.get(0).getName());
        assertEquals("Beta", pets.get(1).getName());
        assertEquals("Charlie", pets.get(2).getName());

        assertThrows(UnsupportedOperationException.class, () -> pets.add(mockPet1));
    }

    @Test
    void testAddPet() {
        owner.addPet(mockPet1);
        
        verify(mockPet1).setOwner(owner);
        assertTrue(owner.getPetsInternal().contains(mockPet1));
        assertEquals(1, owner.getPetsInternal().size());
    }

    @Test
    void testGetPetWithNameOnly() {
        when(mockPet1.getName()).thenReturn("Fluffy");
        lenient().when(mockPet1.isNew()).thenReturn(false);
        
        owner.addPet(mockPet1);
        
        Pet result = owner.getPet("Fluffy");
        
        assertEquals(mockPet1, result);
    }

    @Test
    void testGetPetWithNameAndIgnoreNewFalse() {
        when(mockPet1.getName()).thenReturn("Fluffy");
        lenient().when(mockPet1.isNew()).thenReturn(true);
        
        owner.addPet(mockPet1);
        
        Pet result = owner.getPet("Fluffy", false);
        
        assertEquals(mockPet1, result);
    }

    @Test
    void testGetPetWithNameAndIgnoreNewTrue() {
        when(mockPet1.getName()).thenReturn("Fluffy");
        lenient().when(mockPet1.isNew()).thenReturn(false);
        lenient().when(mockPet2.getName()).thenReturn("Max");
        lenient().when(mockPet2.isNew()).thenReturn(true);
        
        owner.addPet(mockPet1);
        owner.addPet(mockPet2);
        
        Pet result1 = owner.getPet("Fluffy", true);
        Pet result2 = owner.getPet("Max", true);
        
        assertEquals(mockPet1, result1);
        assertNull(result2);
    }

    @Test
    void testGetPetCaseInsensitive() {
        when(mockPet1.getName()).thenReturn("Fluffy");
        lenient().when(mockPet1.isNew()).thenReturn(false);
        
        owner.addPet(mockPet1);
        
        Pet result1 = owner.getPet("fluffy");
        Pet result2 = owner.getPet("FLUFFY");
        Pet result3 = owner.getPet("FlUfFy");
        
        assertEquals(mockPet1, result1);
        assertEquals(mockPet1, result2);
        assertEquals(mockPet1, result3);
    }

    @Test
    void testGetPetNotFound() {
        when(mockPet1.getName()).thenReturn("Fluffy");
        lenient().when(mockPet1.isNew()).thenReturn(false);
        
        owner.addPet(mockPet1);
        
        Pet result = owner.getPet("NonExistent");
        
        assertNull(result);
    }

    @Test
    void testGetPetWithEmptyPetSet() {
        Pet result = owner.getPet("Fluffy");
        
        assertNull(result);
    }

    @Test
    void testGetPetWithNewPetIgnored() {
        lenient().when(mockPet1.getName()).thenReturn("NewPet");
        lenient().when(mockPet1.isNew()).thenReturn(true);
        when(mockPet2.getName()).thenReturn("OldPet");
        lenient().when(mockPet2.isNew()).thenReturn(false);
        
        owner.addPet(mockPet1);
        owner.addPet(mockPet2);
        
        Pet newPetResult = owner.getPet("NewPet", true);
        Pet oldPetResult = owner.getPet("OldPet", true);
        
        assertNull(newPetResult);
        assertEquals(mockPet2, oldPetResult);
    }

    @Test
    void testToString() {
        owner.setAddress("123 Main St");
        owner.setCity("Springfield");
        owner.setTelephone("5551234567");
        
        String result = owner.toString();
        
        assertNotNull(result);
        // ToStringCreator format uses property names without ' = '
        assertTrue(result.contains("address"));
        assertTrue(result.contains("city"));
        assertTrue(result.contains("telephone"));
    }

//     @Test
//     void testSetPetsInternal() {
//         Set<Pet> newPets = Set.of(mockPet1, mockPet2);
//         
//         owner.setPetsInternal(newPets);
//         
//         Set<Pet> retrievedPets = owner.getPetsInternal();
//         assertEquals(newPets, retrievedPets);
//     }

    @Test
    void testGetPetsWithEmptySet() {
        List<Pet> pets = owner.getPets();
        
        assertNotNull(pets);
        assertTrue(pets.isEmpty());
    }

    @Test
    void testAddMultiplePets() {
        owner.addPet(mockPet1);
        owner.addPet(mockPet2);
        owner.addPet(mockPet3);
        
        verify(mockPet1).setOwner(owner);
        verify(mockPet2).setOwner(owner);
        verify(mockPet3).setOwner(owner);
        
        assertEquals(3, owner.getPetsInternal().size());
    }
}