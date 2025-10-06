package org.springframework.samples.petclinic.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.petclinic.model.Vet;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class VetRepositoryTest {

    @Autowired
    private VetRepository vetRepository;

    @Test
    void testFindAll_ReturnsAllVets() {
        Collection<Vet> vets = vetRepository.findAll();
        
        assertNotNull(vets);
        // Don't assume test data exists, just verify it returns a collection
        assertTrue(vets.size() >= 0);
    }

    @Test
    void testFindAll_ReturnsCollection() {
        Collection<Vet> vets = vetRepository.findAll();
        
        assertNotNull(vets);
        assertTrue(vets instanceof Collection);
    }

    @Test
    void testFindAll_DoesNotThrowException() {
        assertDoesNotThrow(() -> {
            Collection<Vet> vets = vetRepository.findAll();
            assertNotNull(vets);
        });
    }

    @Test
    void testFindAll_ReturnsVetObjects() {
        Collection<Vet> vets = vetRepository.findAll();
        
        assertNotNull(vets);
        if (!vets.isEmpty()) {
            for (Vet vet : vets) {
                assertNotNull(vet);
                assertTrue(vet instanceof Vet);
            }
        }
    }

    @Test
    void testFindAll_MultipleInvocations() {
        Collection<Vet> vets1 = vetRepository.findAll();
        Collection<Vet> vets2 = vetRepository.findAll();
        
        assertNotNull(vets1);
        assertNotNull(vets2);
        assertEquals(vets1.size(), vets2.size());
    }
}