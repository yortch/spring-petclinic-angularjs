/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.*;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

/**
 * Test class for {@link ClinicServiceImpl}
 * Tests the service layer with mocked repositories
 *
 * @author Test Generator
 */
@ExtendWith(MockitoExtension.class)
public class ClinicServiceImplTests {

    @Mock
    private PetRepository petRepository;

    @Mock
    private VetRepository vetRepository;

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private VisitRepository visitRepository;

    private ClinicServiceImpl clinicService;

    private Owner testOwner;
    private Pet testPet;
    private Visit testVisit;
    private Vet testVet;
    private PetType testPetType;

    @BeforeEach
    public void setup() {
        clinicService = new ClinicServiceImpl(petRepository, vetRepository, ownerRepository, visitRepository);

        // Setup test owner
        testOwner = new Owner();
        testOwner.setId(1);
        testOwner.setFirstName("George");
        testOwner.setLastName("Franklin");
        testOwner.setAddress("110 W. Liberty St.");
        testOwner.setCity("Madison");
        testOwner.setTelephone("6085551023");

        // Setup test pet
        testPet = new Pet();
        testPet.setId(1);
        testPet.setName("Leo");
        testPet.setBirthDate(new Date());
        
        testPetType = new PetType();
        testPetType.setId(1);
        testPetType.setName("cat");
        testPet.setType(testPetType);
        
        testOwner.addPet(testPet);

        // Setup test visit
        testVisit = new Visit();
        testVisit.setId(1);
        testVisit.setDate(new Date());
        testVisit.setDescription("rabies shot");
        testVisit.setPet(testPet);

        // Setup test vet
        testVet = new Vet();
        testVet.setId(1);
        testVet.setFirstName("James");
        testVet.setLastName("Carter");
    }

    @Test
    public void shouldFindOwnerById() {
        // given
        given(ownerRepository.findById(1)).willReturn(Optional.of(testOwner));

        // when
        Owner found = clinicService.findOwnerById(1);

        // then
        assertThat(found).isNotNull();
        assertThat(found.getFirstName()).isEqualTo("George");
        assertThat(found.getLastName()).isEqualTo("Franklin");
        assertThat(found.getAddress()).isEqualTo("110 W. Liberty St.");
        verify(ownerRepository, times(1)).findById(1);
    }

    @Test
    public void shouldFindAllOwners() {
        // given
        List<Owner> owners = Arrays.asList(testOwner);
        given(ownerRepository.findAll()).willReturn(owners);

        // when
        Collection<Owner> found = clinicService.findAll();

        // then
        assertThat(found).isNotNull();
        assertThat(found).hasSize(1);
        assertThat(found).contains(testOwner);
        verify(ownerRepository, times(1)).findAll();
    }

    @Test
    public void shouldSaveOwner() {
        // given
        Owner newOwner = new Owner();
        newOwner.setFirstName("Betty");
        newOwner.setLastName("Davis");
        newOwner.setAddress("638 Cardinal Ave.");
        newOwner.setCity("Sun Prairie");
        newOwner.setTelephone("6085551749");

        // when
        clinicService.saveOwner(newOwner);

        // then
        verify(ownerRepository, times(1)).save(newOwner);
    }

    @Test
    public void shouldFindPetById() {
        // given
        given(petRepository.findById(1)).willReturn(testPet);

        // when
        Pet found = clinicService.findPetById(1);

        // then
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Leo");
        assertThat(found.getType().getName()).isEqualTo("cat");
        verify(petRepository, times(1)).findById(1);
    }

    @Test
    public void shouldSavePet() {
        // given
        Pet newPet = new Pet();
        newPet.setName("Max");
        newPet.setBirthDate(new Date());
        newPet.setType(testPetType);

        // when
        clinicService.savePet(newPet);

        // then
        verify(petRepository, times(1)).save(newPet);
    }

    @Test
    public void shouldFindPetTypes() {
        // given
        List<PetType> petTypes = Arrays.asList(testPetType);
        given(petRepository.findPetTypes()).willReturn(petTypes);

        // when
        Collection<PetType> found = clinicService.findPetTypes();

        // then
        assertThat(found).isNotNull();
        assertThat(found).hasSize(1);
        assertThat(found).contains(testPetType);
        verify(petRepository, times(1)).findPetTypes();
    }

    @Test
    public void shouldSaveVisit() {
        // given
        Visit newVisit = new Visit();
        newVisit.setDate(new Date());
        newVisit.setDescription("neutered");
        newVisit.setPet(testPet);

        // when
        clinicService.saveVisit(newVisit);

        // then
        verify(visitRepository, times(1)).save(newVisit);
    }

    @Test
    public void shouldFindVets() {
        // given
        Collection<Vet> vets = Arrays.asList(testVet);
        given(vetRepository.findAll()).willReturn(vets);

        // when
        Collection<Vet> found = clinicService.findVets();

        // then
        assertThat(found).isNotNull();
        assertThat(found).hasSize(1);
        assertThat(found).contains(testVet);
        verify(vetRepository, times(1)).findAll();
    }
}
