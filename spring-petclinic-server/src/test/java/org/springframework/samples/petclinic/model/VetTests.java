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
package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link Vet}
 *
 * @author Test Generator
 */
public class VetTests {

    private Vet vet;

    @BeforeEach
    public void setup() {
        vet = new Vet();
        vet.setFirstName("James");
        vet.setLastName("Carter");
    }

    @Test
    public void testGetNrOfSpecialtiesWithNoSpecialties() {
        assertThat(vet.getNrOfSpecialties()).isEqualTo(0);
    }

    @Test
    public void testGetNrOfSpecialtiesWithOneSpecialty() {
        Specialty specialty = new Specialty();
        specialty.setName("radiology");
        vet.addSpecialty(specialty);
        
        assertThat(vet.getNrOfSpecialties()).isEqualTo(1);
    }

    @Test
    public void testGetNrOfSpecialtiesWithMultipleSpecialties() {
        Specialty radiology = new Specialty();
        radiology.setName("radiology");
        
        Specialty surgery = new Specialty();
        surgery.setName("surgery");
        
        vet.addSpecialty(radiology);
        vet.addSpecialty(surgery);
        
        assertThat(vet.getNrOfSpecialties()).isEqualTo(2);
    }

    @Test
    public void testAddSpecialty() {
        Specialty specialty = new Specialty();
        specialty.setName("dentistry");
        
        vet.addSpecialty(specialty);
        
        assertThat(vet.getSpecialties()).hasSize(1);
        assertThat(vet.getSpecialties()).contains(specialty);
    }

    @Test
    public void testGetSpecialtiesReturnsEmptySet() {
        assertThat(vet.getSpecialties()).isNotNull();
        assertThat(vet.getSpecialties()).isEmpty();
    }

    @Test
    public void testGetSpecialtiesSorted() {
        Specialty radiology = new Specialty();
        radiology.setName("radiology");
        
        Specialty dentistry = new Specialty();
        dentistry.setName("dentistry");
        
        Specialty surgery = new Specialty();
        surgery.setName("surgery");
        
        vet.addSpecialty(surgery);
        vet.addSpecialty(radiology);
        vet.addSpecialty(dentistry);
        
        assertThat(vet.getSpecialties()).hasSize(3);
        // Specialties should be sorted by name
        assertThat(vet.getSpecialtiesInternal()).containsExactlyInAnyOrder(radiology, dentistry, surgery);
    }
}
