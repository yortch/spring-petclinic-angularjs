package org.springframework.samples.petclinic.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Vet;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VetRepositoryTest {

    @Mock
    private VetRepository vetRepository;

    @Test
    @DisplayName("findAll returns non-empty collection")
    void findAll_returnsVets() {
        Vet v1 = new Vet();
        v1.setFirstName("Alice");
        v1.setLastName("Smith");
        Vet v2 = new Vet();
        v2.setFirstName("Bob");
        v2.setLastName("Jones");

        when(vetRepository.findAll()).thenReturn(Arrays.asList(v1, v2));

        Collection<Vet> result = vetRepository.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(v1));
        assertTrue(result.contains(v2));
        verify(vetRepository, times(1)).findAll();
        verifyNoMoreInteractions(vetRepository);
    }

    @Test
    @DisplayName("findAll returns empty collection")
    void findAll_returnsEmpty() {
        when(vetRepository.findAll()).thenReturn(Collections.emptyList());

        Collection<Vet> result = vetRepository.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(vetRepository, times(1)).findAll();
        verifyNoMoreInteractions(vetRepository);
    }

    @Test
    @DisplayName("findAll propagates DataAccessException")
    void findAll_throwsDataAccessException() {
        when(vetRepository.findAll()).thenThrow(new DataAccessException("failure") {});

        assertThrows(DataAccessException.class, () -> vetRepository.findAll());
        verify(vetRepository, times(1)).findAll();
        verifyNoMoreInteractions(vetRepository);
    }
}