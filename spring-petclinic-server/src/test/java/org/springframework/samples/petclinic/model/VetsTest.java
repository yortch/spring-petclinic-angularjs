package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class VetsTest {

    private Vets vets;

    @BeforeEach
    void setUp() {
        vets = new Vets();
    }

    @Test
    void testGetVetList_WhenNull_InitializesNewArrayList() {
        List<Vet> vetList = vets.getVetList();
        
        assertThat(vetList).isNotNull();
        assertThat(vetList).isEmpty();
    }

    @Test
    void testGetVetList_WhenNotNull_ReturnsSameInstance() {
        List<Vet> firstCall = vets.getVetList();
        List<Vet> secondCall = vets.getVetList();
        
        assertThat(firstCall).isSameAs(secondCall);
    }

    @Test
    void testGetVetList_ReturnsEmptyList_Initially() {
        List<Vet> vetList = vets.getVetList();
        
        assertThat(vetList).hasSize(0);
    }

    @Test
    void testGetVetList_AllowsModification() {
        List<Vet> vetList = vets.getVetList();
        Vet vet = new Vet();
        vet.setFirstName("John");
        vet.setLastName("Doe");
        
        vetList.add(vet);
        
        assertThat(vets.getVetList()).hasSize(1);
        assertThat(vets.getVetList().get(0)).isEqualTo(vet);
    }

    @Test
    void testGetVetList_MultipleCalls_MaintainsState() {
        List<Vet> vetList = vets.getVetList();
        Vet vet1 = new Vet();
        vet1.setFirstName("Jane");
        vet1.setLastName("Smith");
        vetList.add(vet1);
        
        List<Vet> secondRetrieval = vets.getVetList();
        
        assertThat(secondRetrieval).hasSize(1);
        assertThat(secondRetrieval).contains(vet1);
    }
}