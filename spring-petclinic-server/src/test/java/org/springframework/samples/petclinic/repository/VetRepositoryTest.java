package org.springframework.samples.petclinic.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Vet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VetRepositoryTest {

    @Mock
    private VetRepository vetRepository;

    private Vet vet1;
    private Vet vet2;

    @BeforeEach
    void setUp() {
        vet1 = new Vet();
        vet1.setId(1);
        vet1.setFirstName("James");
        vet1.setLastName("Carter");

        vet2 = new Vet();
        vet2.setId(2);
        vet2.setFirstName("Helen");
        vet2.setLastName("Leary");
    }

    @Test
    void testFindAll_ReturnsEmptyCollection() throws DataAccessException {
        when(vetRepository.findAll()).thenReturn(Collections.emptyList());

        Collection<Vet> result = vetRepository.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(vetRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_ReturnsSingleVet() throws DataAccessException {
        Collection<Vet> expectedVets = Collections.singletonList(vet1);
        when(vetRepository.findAll()).thenReturn(expectedVets);

        Collection<Vet> result = vetRepository.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(vet1));
        verify(vetRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_ReturnsMultipleVets() throws DataAccessException {
        Collection<Vet> expectedVets = new ArrayList<>();
        expectedVets.add(vet1);
        expectedVets.add(vet2);
        when(vetRepository.findAll()).thenReturn(expectedVets);

        Collection<Vet> result = vetRepository.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(vet1));
        assertTrue(result.contains(vet2));
        verify(vetRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_ThrowsDataAccessException() throws DataAccessException {
        DataAccessException exception = new DataAccessException("Database connection failed") {};
        when(vetRepository.findAll()).thenThrow(exception);

        assertThrows(DataAccessException.class, () -> {
            vetRepository.findAll();
        });
        verify(vetRepository, times(1)).findAll();
    }
}