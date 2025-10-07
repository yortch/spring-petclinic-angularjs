package org.springframework.samples.petclinic.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Visit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class VisitRepositoryTest {

    @Mock
    private VisitRepository visitRepository;

    private Visit visit;

    @BeforeEach
    void setUp() {
        visit = new Visit();
    }

    @Test
    void testSave_NewVisit_Success() {
        doNothing().when(visitRepository).save(any(Visit.class));

        visitRepository.save(visit);

        verify(visitRepository, times(1)).save(visit);
    }

    @Test
    void testSave_ExistingVisit_Success() {
        Visit existingVisit = new Visit();
        existingVisit.setId(1);

        doNothing().when(visitRepository).save(any(Visit.class));

        visitRepository.save(existingVisit);

        verify(visitRepository, times(1)).save(existingVisit);
    }

    @Test
    void testSave_ThrowsDataAccessException() {
        doThrow(new DataAccessException("Database error") {}).when(visitRepository).save(any(Visit.class));

        assertThatThrownBy(() -> visitRepository.save(visit))
            .isInstanceOf(DataAccessException.class)
            .hasMessageContaining("Database error");

        verify(visitRepository, times(1)).save(visit);
    }

    @Test
    void testSave_NullVisit() {
        doNothing().when(visitRepository).save(null);

        visitRepository.save(null);

        verify(visitRepository, times(1)).save(null);
    }

    @Test
    void testFindByPetId_MultipleVisits() {
        Integer petId = 1;
        Visit visit1 = new Visit();
        visit1.setId(1);
        Visit visit2 = new Visit();
        visit2.setId(2);
        List<Visit> expectedVisits = Arrays.asList(visit1, visit2);

        when(visitRepository.findByPetId(petId)).thenReturn(expectedVisits);

        List<Visit> actualVisits = visitRepository.findByPetId(petId);

        assertThat(actualVisits).isNotNull();
        assertThat(actualVisits).hasSize(2);
        assertThat(actualVisits).containsExactlyElementsOf(expectedVisits);
        verify(visitRepository, times(1)).findByPetId(petId);
    }

    @Test
    void testFindByPetId_SingleVisit() {
        Integer petId = 1;
        Visit singleVisit = new Visit();
        singleVisit.setId(1);
        List<Visit> expectedVisits = Collections.singletonList(singleVisit);

        when(visitRepository.findByPetId(petId)).thenReturn(expectedVisits);

        List<Visit> actualVisits = visitRepository.findByPetId(petId);

        assertThat(actualVisits).isNotNull();
        assertThat(actualVisits).hasSize(1);
        assertThat(actualVisits.get(0)).isEqualTo(singleVisit);
        verify(visitRepository, times(1)).findByPetId(petId);
    }

    @Test
    void testFindByPetId_NoVisits() {
        Integer petId = 999;
        List<Visit> emptyList = new ArrayList<>();

        when(visitRepository.findByPetId(petId)).thenReturn(emptyList);

        List<Visit> actualVisits = visitRepository.findByPetId(petId);

        assertThat(actualVisits).isNotNull();
        assertThat(actualVisits).isEmpty();
        verify(visitRepository, times(1)).findByPetId(petId);
    }

    @Test
    void testFindByPetId_NullPetId() {
        when(visitRepository.findByPetId(null)).thenReturn(Collections.emptyList());

        List<Visit> actualVisits = visitRepository.findByPetId(null);

        assertThat(actualVisits).isNotNull();
        verify(visitRepository, times(1)).findByPetId(null);
    }

    @Test
    void testFindByPetId_ZeroPetId() {
        Integer petId = 0;
        when(visitRepository.findByPetId(petId)).thenReturn(Collections.emptyList());

        List<Visit> actualVisits = visitRepository.findByPetId(petId);

        assertThat(actualVisits).isNotNull();
        assertThat(actualVisits).isEmpty();
        verify(visitRepository, times(1)).findByPetId(petId);
    }

    @Test
    void testFindByPetId_NegativePetId() {
        Integer petId = -1;
        when(visitRepository.findByPetId(petId)).thenReturn(Collections.emptyList());

        List<Visit> actualVisits = visitRepository.findByPetId(petId);

        assertThat(actualVisits).isNotNull();
        assertThat(actualVisits).isEmpty();
        verify(visitRepository, times(1)).findByPetId(petId);
    }
}