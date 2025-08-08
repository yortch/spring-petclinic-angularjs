package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VetsTest {

    private Vets vets;

    @BeforeEach
    void setUp() {
        this.vets = new Vets();
    }

    @Test
    void getVetList_returnsNonNullEmptyListOnFirstCall() {
        List<Vet> list = vets.getVetList();
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    void getVetList_returnsSameInstanceOnSubsequentCalls() {
        List<Vet> first = vets.getVetList(); // initializes internal list (vets == null branch)
        List<Vet> second = vets.getVetList(); // returns existing list (vets != null branch)
        assertSame(first, second);
    }

    @Test
    void getVetList_allowsAddingElementsAndPersists() {
        List<Vet> list = vets.getVetList();
        int initialSize = list.size();

        Vet vet = new Vet();
        list.add(vet);

        assertEquals(initialSize + 1, list.size());
        assertTrue(list.contains(vet));

        // Verify persistence across subsequent calls (same instance and contents)
        List<Vet> again = vets.getVetList();
        assertSame(list, again);
        assertTrue(again.contains(vet));
    }
}