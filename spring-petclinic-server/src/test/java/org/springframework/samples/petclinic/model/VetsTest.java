package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VetsTest {

    @Test
    void testGetVetList_WhenVetsIsNull_ShouldInitializeEmptyList() {
        Vets vets = new Vets();
        
        List<Vet> vetList = vets.getVetList();
        
        assertNotNull(vetList);
        assertTrue(vetList.isEmpty());
    }

    @Test
    void testGetVetList_WhenCalledMultipleTimes_ShouldReturnSameInstance() {
        Vets vets = new Vets();
        
        List<Vet> firstCall = vets.getVetList();
        List<Vet> secondCall = vets.getVetList();
        
        assertSame(firstCall, secondCall);
    }

    @Test
    void testGetVetList_WhenListIsModified_ShouldRetainModifications() {
        Vets vets = new Vets();
        Vet vet = new Vet();
        vet.setId(1);
        vet.setFirstName("John");
        vet.setLastName("Doe");
        
        List<Vet> vetList = vets.getVetList();
        vetList.add(vet);
        
        assertEquals(1, vets.getVetList().size());
        assertEquals(vet, vets.getVetList().get(0));
    }

    @Test
    void testGetVetList_ShouldReturnNonNullList() {
        Vets vets = new Vets();
        
        assertNotNull(vets.getVetList());
    }

    @Test
    void testGetVetList_WhenListAlreadyInitialized_ShouldNotCreateNewInstance() {
        Vets vets = new Vets();
        Vet vet1 = new Vet();
        vet1.setId(1);
        vet1.setFirstName("Jane");
        vet1.setLastName("Smith");
        
        vets.getVetList().add(vet1);
        List<Vet> vetList = vets.getVetList();
        
        assertEquals(1, vetList.size());
        assertEquals(vet1, vetList.get(0));
    }

    @Test
    void testGetVetList_ShouldReturnMutableList() {
        Vets vets = new Vets();
        Vet vet = new Vet();
        
        List<Vet> vetList = vets.getVetList();
        vetList.add(vet);
        vetList.remove(vet);
        
        assertTrue(vetList.isEmpty());
    }
}