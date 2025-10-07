package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.validation.Errors;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetValidatorTest {

    private PetValidator petValidator;

    @Mock
    private Pet pet;

    @Mock
    private Owner owner;

    @Mock
    private PetType petType;

    @Mock
    private Errors errors;

    @BeforeEach
    void setUp() {
        petValidator = new PetValidator();
    }

//     @Test
//     void test_ValidateWithNullName() {
//         when(pet.getName()).thenReturn(null);
//         when(pet.getBirthDate()).thenReturn(LocalDate.now());
// 
//         petValidator.validate(pet, errors);
// 
//         verify(errors).rejectValue("name", "required", "required");
//     }

//     @Test
//     void test_ValidateWithEmptyName() {
//         when(pet.getName()).thenReturn("");
//         when(pet.getBirthDate()).thenReturn(LocalDate.now());
// 
//         petValidator.validate(pet, errors);
// 
//         verify(errors).rejectValue("name", "required", "required");
//     }

//     @Test
//     void test_ValidateWithWhitespaceName() {
//         when(pet.getName()).thenReturn("   ");
//         when(pet.getBirthDate()).thenReturn(LocalDate.now());
// 
//         petValidator.validate(pet, errors);
// 
//         verify(errors, never()).rejectValue(eq("name"), eq("required"), any());
//     }

//     @Test
//     void test_ValidateWithDuplicateNameForNewPet() {
//         String petName = "Fluffy";
//         Pet existingPet = mock(Pet.class);
// 
//         when(pet.getName()).thenReturn(petName);
//         when(pet.isNew()).thenReturn(true);
//         when(pet.getOwner()).thenReturn(owner);
//         when(owner.getPet(petName, true)).thenReturn(existingPet);
//         when(pet.getType()).thenReturn(petType);
//         when(pet.getBirthDate()).thenReturn(LocalDate.now());
// 
//         petValidator.validate(pet, errors);
// 
//         verify(errors).rejectValue("name", "duplicate", "already exists");
//     }

//     @Test
//     void test_ValidateWithUniqueNameForNewPet() {
//         String petName = "Fluffy";
// 
//         when(pet.getName()).thenReturn(petName);
//         when(pet.isNew()).thenReturn(true);
//         when(pet.getOwner()).thenReturn(owner);
//         when(owner.getPet(petName, true)).thenReturn(null);
//         when(pet.getType()).thenReturn(petType);
//         when(pet.getBirthDate()).thenReturn(LocalDate.now());
// 
//         petValidator.validate(pet, errors);
// 
//         verify(errors, never()).rejectValue(eq("name"), eq("duplicate"), any());
//         verify(errors, never()).rejectValue(eq("name"), eq("required"), any());
//     }

//     @Test
//     void test_ValidateWithNameForExistingPet() {
//         String petName = "Fluffy";
// 
//         when(pet.getName()).thenReturn(petName);
//         when(pet.isNew()).thenReturn(false);
//         when(pet.getBirthDate()).thenReturn(LocalDate.now());
// 
//         petValidator.validate(pet, errors);
// 
//         verify(pet, never()).getOwner();
//         verify(errors, never()).rejectValue(eq("name"), eq("duplicate"), any());
//     }

//     @Test
//     void test_ValidateWithNullTypeForNewPet() {
//         when(pet.getName()).thenReturn("Fluffy");
//         when(pet.isNew()).thenReturn(true);
//         when(pet.getOwner()).thenReturn(owner);
//         when(owner.getPet(any(), eq(true))).thenReturn(null);
//         when(pet.getType()).thenReturn(null);
//         when(pet.getBirthDate()).thenReturn(LocalDate.now());
// 
//         petValidator.validate(pet, errors);
// 
//         verify(errors).rejectValue("type", "required", "required");
//     }

//     @Test
//     void test_ValidateWithTypeForNewPet() {
//         when(pet.getName()).thenReturn("Fluffy");
//         when(pet.isNew()).thenReturn(true);
//         when(pet.getOwner()).thenReturn(owner);
//         when(owner.getPet(any(), eq(true))).thenReturn(null);
//         when(pet.getType()).thenReturn(petType);
//         when(pet.getBirthDate()).thenReturn(LocalDate.now());
// 
//         petValidator.validate(pet, errors);
// 
//         verify(errors, never()).rejectValue(eq("type"), eq("required"), any());
//     }

//     @Test
//     void test_ValidateWithNullTypeForExistingPet() {
//         when(pet.getName()).thenReturn("Fluffy");
//         when(pet.isNew()).thenReturn(false);
//         when(pet.getType()).thenReturn(null);
//         when(pet.getBirthDate()).thenReturn(LocalDate.now());
// 
//         petValidator.validate(pet, errors);
// 
//         verify(errors, never()).rejectValue(eq("type"), eq("required"), any());
//     }

    @Test
    void test_ValidateWithNullBirthDate() {
        when(pet.getName()).thenReturn("Fluffy");
        when(pet.isNew()).thenReturn(false);
        when(pet.getBirthDate()).thenReturn(null);

        petValidator.validate(pet, errors);

        verify(errors).rejectValue("birthDate", "required", "required");
    }

//     @Test
//     void test_ValidateWithValidBirthDate() {
//         when(pet.getName()).thenReturn("Fluffy");
//         when(pet.isNew()).thenReturn(false);
//         when(pet.getBirthDate()).thenReturn(LocalDate.now());
// 
//         petValidator.validate(pet, errors);
// 
//         verify(errors, never()).rejectValue(eq("birthDate"), eq("required"), any());
//     }

//     @Test
//     void test_ValidateWithAllValidFields() {
//         when(pet.getName()).thenReturn("Fluffy");
//         when(pet.isNew()).thenReturn(true);
//         when(pet.getOwner()).thenReturn(owner);
//         when(owner.getPet("Fluffy", true)).thenReturn(null);
//         when(pet.getType()).thenReturn(petType);
//         when(pet.getBirthDate()).thenReturn(LocalDate.now());
// 
//         petValidator.validate(pet, errors);
// 
//         verify(errors, never()).rejectValue(any(), any(), any());
//     }

    @Test
    void test_ValidateWithMultipleValidationErrors() {
        when(pet.getName()).thenReturn("");
        when(pet.isNew()).thenReturn(true);
        when(pet.getType()).thenReturn(null);
        when(pet.getBirthDate()).thenReturn(null);

        petValidator.validate(pet, errors);

        verify(errors).rejectValue("name", "required", "required");
        verify(errors).rejectValue("type", "required", "required");
        verify(errors).rejectValue("birthDate", "required", "required");
    }
}