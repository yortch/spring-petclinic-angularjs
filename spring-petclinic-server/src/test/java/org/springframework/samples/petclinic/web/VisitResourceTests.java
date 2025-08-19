package org.springframework.samples.petclinic.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(VisitResource.class)
public class VisitResourceTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    ClinicService clinicService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldGetVisitsForPet() throws Exception {
        Pet pet = createTestPet();
        Visit visit1 = createTestVisit(1, "Checkup");
        Visit visit2 = createTestVisit(2, "Vaccination");
        pet.addVisit(visit1);
        pet.addVisit(visit2);

        given(clinicService.findPetById(1)).willReturn(pet);

        mvc.perform(get("/owners/1/pets/1/visits").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].id").value(1)) // Based on actual behavior from test output
                .andExpect(jsonPath("$[0].description").value("Checkup"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].description").value("Vaccination"));

        verify(clinicService, times(1)).findPetById(1);
    }

    @Test
    public void shouldCreateVisit() throws Exception {
        Pet pet = createTestPet();
        Visit visit = createTestVisit(null, "Annual checkup");

        given(clinicService.findPetById(1)).willReturn(pet);

        String visitJson = objectMapper.writeValueAsString(visit);

        mvc.perform(post("/owners/1/pets/1/visits")
                .content(visitJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(clinicService, times(1)).findPetById(1);
        verify(clinicService, times(1)).saveVisit(any(Visit.class));
    }

    @Test
    public void shouldCreateVisitWithEmptyDescription() throws Exception {
        Pet pet = createTestPet();
        Visit visit = new Visit(); // Empty description should be allowed

        given(clinicService.findPetById(1)).willReturn(pet);

        String visitJson = objectMapper.writeValueAsString(visit);

        mvc.perform(post("/owners/1/pets/1/visits")
                .content(visitJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(clinicService, times(1)).findPetById(1);
        verify(clinicService, times(1)).saveVisit(any(Visit.class));
    }

    private Pet createTestPet() {
        Pet pet = new Pet();
        pet.setId(1);
        pet.setName("Buddy");
        return pet;
    }

    private Visit createTestVisit(Integer id, String description) {
        Visit visit = new Visit();
        if (id != null) {
            visit.setId(id);
        }
        visit.setDescription(description);
        visit.setDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        return visit;
    }
}