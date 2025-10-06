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

import java.util.Date;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class PetValidatorTest {

    private PetValidator petValidator;

    @Mock
    private Pet pet;

    @Mock
    private Errors errors;

    @Mock
    private Owner owner;

    @Mock
    private PetType petType;

    @BeforeEach
    void setUp() {
        petValidator = new PetValidator();
    }

    @Test
    void testValidate_NameIsNull_RejectsNameAsRequired() {
        when(pet.getName()).thenReturn(null);

        petValidator.validate(pet, errors);

        verify(errors).rejectValue("name", "required", "required");
    }

    @Test
    void testValidate_NameIsEmpty_RejectsNameAsRequired() {
        when(pet.getName()).thenReturn("");

        petValidator.validate(pet, errors);

        verify(errors).rejectValue("name", "required", "required");
    }

    @Test
    void testValidate_NewPetWithDuplicateName_RejectsNameAsDuplicate() {
        String petName = "Fluffy";
        Pet existingPet = mock(Pet.class);

        when(pet.getName()).thenReturn(petName);
        when(pet.isNew()).thenReturn(true);
        when(pet.getOwner()).thenReturn(owner);
        when(owner.getPet(petName, true)).thenReturn(existingPet);

        petValidator.validate(pet, errors);

        verify(errors).rejectValue("name", "duplicate", "already exists");
    }

    @Test
    void testValidate_NewPetWithUniqueName_DoesNotRejectName() {
        String petName = "Fluffy";

        when(pet.getName()).thenReturn(petName);
        when(pet.isNew()).thenReturn(true);
        when(pet.getOwner()).thenReturn(owner);
        when(owner.getPet(petName, true)).thenReturn(null);
        when(pet.getType()).thenReturn(petType);
        when(pet.getBirthDate()).thenReturn(new Date());

        petValidator.validate(pet, errors);

        verify(errors, never()).rejectValue(eq("name"), anyString(), anyString());
    }

    @Test
    void testValidate_ExistingPetWithDuplicateName_DoesNotRejectName() {
        String petName = "Fluffy";

        when(pet.getName()).thenReturn(petName);
        when(pet.isNew()).thenReturn(false);
        when(pet.getBirthDate()).thenReturn(new Date());

        petValidator.validate(pet, errors);

        verify(errors, never()).rejectValue(eq("name"), anyString(), anyString());
    }

    @Test
    void testValidate_NewPetWithNullType_RejectsTypeAsRequired() {
        when(pet.getName()).thenReturn("Fluffy");
        when(pet.isNew()).thenReturn(true);
        when(pet.getOwner()).thenReturn(owner);
        when(owner.getPet(anyString(), anyBoolean())).thenReturn(null);
        when(pet.getType()).thenReturn(null);

        petValidator.validate(pet, errors);

        verify(errors).rejectValue("type", "required", "required");
    }

    @Test
    void testValidate_NewPetWithNonNullType_DoesNotRejectType() {
        when(pet.getName()).thenReturn("Fluffy");
        when(pet.isNew()).thenReturn(true);
        when(pet.getOwner()).thenReturn(owner);
        when(owner.getPet(anyString(), anyBoolean())).thenReturn(null);
        when(pet.getType()).thenReturn(petType);
        when(pet.getBirthDate()).thenReturn(new Date());

        petValidator.validate(pet, errors);

        verify(errors, never()).rejectValue(eq("type"), anyString(), anyString());
    }

    @Test
    void testValidate_ExistingPetWithNullType_DoesNotRejectType() {
        when(pet.getName()).thenReturn("Fluffy");
        when(pet.isNew()).thenReturn(false);
        lenient().when(pet.getType()).thenReturn(null);
        when(pet.getBirthDate()).thenReturn(new Date());

        petValidator.validate(pet, errors);

        verify(errors, never()).rejectValue(eq("type"), anyString(), anyString());
    }

    @Test
    void testValidate_NullBirthDate_RejectsBirthDateAsRequired() {
        when(pet.getName()).thenReturn("Fluffy");
        when(pet.isNew()).thenReturn(false);
        when(pet.getBirthDate()).thenReturn(null);

        petValidator.validate(pet, errors);

        verify(errors).rejectValue("birthDate", "required", "required");
    }

    @Test
    void testValidate_NonNullBirthDate_DoesNotRejectBirthDate() {
        when(pet.getName()).thenReturn("Fluffy");
        when(pet.isNew()).thenReturn(false);
        when(pet.getBirthDate()).thenReturn(new Date());

        petValidator.validate(pet, errors);

        verify(errors, never()).rejectValue(eq("birthDate"), anyString(), anyString());
    }

    @Test
    void testValidate_ValidNewPet_NoErrors() {
        String petName = "Fluffy";

        when(pet.getName()).thenReturn(petName);
        when(pet.isNew()).thenReturn(true);
        when(pet.getOwner()).thenReturn(owner);
        when(owner.getPet(petName, true)).thenReturn(null);
        when(pet.getType()).thenReturn(petType);
        when(pet.getBirthDate()).thenReturn(new Date());

        petValidator.validate(pet, errors);

        verify(errors, never()).rejectValue(anyString(), anyString(), anyString());
    }

    @Test
    void testValidate_ValidExistingPet_NoErrors() {
        when(pet.getName()).thenReturn("Fluffy");
        when(pet.isNew()).thenReturn(false);
        when(pet.getBirthDate()).thenReturn(new Date());

        petValidator.validate(pet, errors);

        verify(errors, never()).rejectValue(anyString(), anyString(), anyString());
    }
}