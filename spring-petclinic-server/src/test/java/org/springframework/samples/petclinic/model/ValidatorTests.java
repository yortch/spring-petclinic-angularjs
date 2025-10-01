package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Locale;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.petclinic.web.PetValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Comprehensive validation tests for JSR-303 annotations and custom validators.
 * Tests both Bean Validation annotations on models and Spring custom validators.
 *
 * @author Michael Isvy
 * @author Spring PetClinic Team
 */
public class ValidatorTests {

    private Validator beanValidator;
    private PetValidator petValidator;

    @BeforeEach
    public void setUp() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        beanValidator = createValidator();
        petValidator = new PetValidator();
    }

    private Validator createValidator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        return localValidatorFactoryBean;
    }

    // =========================
    // Person/Owner JSR-303 Tests
    // =========================

    @Test
    public void shouldNotValidateWhenFirstNameEmpty() {
        Person person = new Person();
        person.setFirstName("");
        person.setLastName("smith");

        Set<ConstraintViolation<Person>> constraintViolations = beanValidator.validate(person);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Person> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("firstName");
        assertThat(violation.getMessage()).isEqualTo("must not be empty");
    }

    @Test
    public void shouldNotValidateWhenLastNameEmpty() {
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("");

        Set<ConstraintViolation<Person>> constraintViolations = beanValidator.validate(person);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Person> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("lastName");
        assertThat(violation.getMessage()).isEqualTo("must not be empty");
    }

    @Test
    public void shouldValidateValidPerson() {
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Smith");

        Set<ConstraintViolation<Person>> constraintViolations = beanValidator.validate(person);

        assertThat(constraintViolations.size()).isEqualTo(0);
    }

    @Test
    public void shouldNotValidateOwnerWhenAddressEmpty() {
        Owner owner = new Owner();
        owner.setFirstName("John");
        owner.setLastName("Smith");
        owner.setAddress("");
        owner.setCity("Springfield");
        owner.setTelephone("1234567890");

        Set<ConstraintViolation<Owner>> constraintViolations = beanValidator.validate(owner);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Owner> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("address");
        assertThat(violation.getMessage()).isEqualTo("must not be empty");
    }

    @Test
    public void shouldNotValidateOwnerWhenCityEmpty() {
        Owner owner = new Owner();
        owner.setFirstName("John");
        owner.setLastName("Smith");
        owner.setAddress("123 Main St");
        owner.setCity("");
        owner.setTelephone("1234567890");

        Set<ConstraintViolation<Owner>> constraintViolations = beanValidator.validate(owner);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Owner> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("city");
        assertThat(violation.getMessage()).isEqualTo("must not be empty");
    }

    @Test
    public void shouldNotValidateOwnerWhenTelephoneEmpty() {
        Owner owner = new Owner();
        owner.setFirstName("John");
        owner.setLastName("Smith");
        owner.setAddress("123 Main St");
        owner.setCity("Springfield");
        owner.setTelephone("");

        Set<ConstraintViolation<Owner>> constraintViolations = beanValidator.validate(owner);

        // Empty telephone triggers both @NotEmpty and @Digits validation
        assertThat(constraintViolations.size()).isEqualTo(2);
        boolean hasNotEmptyError = constraintViolations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("telephone") && 
                          v.getMessage().equals("must not be empty"));
        boolean hasDigitsError = constraintViolations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("telephone") && 
                          v.getMessage().contains("numeric value out of bounds"));
        assertThat(hasNotEmptyError).isTrue();
        assertThat(hasDigitsError).isTrue();
    }

    @Test
    public void shouldNotValidateOwnerWhenTelephoneHasInvalidDigits() {
        Owner owner = new Owner();
        owner.setFirstName("John");
        owner.setLastName("Smith");
        owner.setAddress("123 Main St");
        owner.setCity("Springfield");
        owner.setTelephone("12345678901234567890"); // More than 10 digits

        Set<ConstraintViolation<Owner>> constraintViolations = beanValidator.validate(owner);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Owner> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("telephone");
        assertThat(violation.getMessage()).contains("numeric value out of bounds");
    }

    @Test
    public void shouldValidateValidOwner() {
        Owner owner = new Owner();
        owner.setFirstName("John");
        owner.setLastName("Smith");
        owner.setAddress("123 Main St");
        owner.setCity("Springfield");
        owner.setTelephone("1234567890");

        Set<ConstraintViolation<Owner>> constraintViolations = beanValidator.validate(owner);

        assertThat(constraintViolations.size()).isEqualTo(0);
    }

    // =========================
    // Visit JSR-303 Tests
    // =========================

    @Test
    public void shouldNotValidateVisitWhenDescriptionTooLong() {
        Visit visit = new Visit();
        visit.setDate(new Date());
        
        // Create a description that exceeds 8192 characters
        StringBuilder longDescription = new StringBuilder();
        for (int i = 0; i < 8193; i++) {
            longDescription.append("a");
        }
        visit.setDescription(longDescription.toString());

        Set<ConstraintViolation<Visit>> constraintViolations = beanValidator.validate(visit);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Visit> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("description");
        assertThat(violation.getMessage()).contains("size must be between 0 and 8192");
    }

    @Test
    public void shouldValidateVisitWithValidDescription() {
        Visit visit = new Visit();
        visit.setDate(new Date());
        visit.setDescription("Regular checkup");

        Set<ConstraintViolation<Visit>> constraintViolations = beanValidator.validate(visit);

        assertThat(constraintViolations.size()).isEqualTo(0);
    }

    @Test
    public void shouldValidateVisitWithMaxLengthDescription() {
        Visit visit = new Visit();
        visit.setDate(new Date());
        
        // Create a description exactly at the limit (8192 characters)
        StringBuilder maxDescription = new StringBuilder();
        for (int i = 0; i < 8192; i++) {
            maxDescription.append("a");
        }
        visit.setDescription(maxDescription.toString());

        Set<ConstraintViolation<Visit>> constraintViolations = beanValidator.validate(visit);

        assertThat(constraintViolations.size()).isEqualTo(0);
    }

    // =========================
    // Custom PetValidator Tests
    // =========================

    @Test
    public void shouldValidateValidPetWithPetValidator() {
        Owner owner = createValidOwner();
        Pet pet = createValidPet();
        pet.setOwner(owner);

        Errors errors = new BeanPropertyBindingResult(pet, "pet");
        petValidator.validate(pet, errors);

        assertThat(errors.hasErrors()).isFalse();
    }

    @Test
    public void shouldNotValidatePetWhenNameEmpty() {
        Owner owner = createValidOwner();
        Pet pet = createValidPet();
        pet.setOwner(owner);
        pet.setName(""); // Empty name

        Errors errors = new BeanPropertyBindingResult(pet, "pet");
        petValidator.validate(pet, errors);

        assertThat(errors.hasErrors()).isTrue();
        assertThat(errors.getErrorCount()).isEqualTo(1);
        assertThat(errors.hasFieldErrors("name")).isTrue();
        if (errors.getFieldError("name") != null) {
            assertThat(errors.getFieldError("name").getCode()).isEqualTo("required");
        }
    }

    @Test
    public void shouldNotValidatePetWhenNameNull() {
        Owner owner = createValidOwner();
        Pet pet = createValidPet();
        pet.setOwner(owner);
        pet.setName(null); // Null name

        Errors errors = new BeanPropertyBindingResult(pet, "pet");
        petValidator.validate(pet, errors);

        assertThat(errors.hasErrors()).isTrue();
        assertThat(errors.getErrorCount()).isEqualTo(1);
        assertThat(errors.hasFieldErrors("name")).isTrue();
        if (errors.getFieldError("name") != null) {
            assertThat(errors.getFieldError("name").getCode()).isEqualTo("required");
        }
    }

    @Test
    public void shouldNotValidateNewPetWhenTypeNull() {
        Owner owner = createValidOwner();
        Pet pet = createValidPet();
        pet.setOwner(owner);
        pet.setId(null); // Make it a new pet
        pet.setType(null); // Null type

        Errors errors = new BeanPropertyBindingResult(pet, "pet");
        petValidator.validate(pet, errors);

        assertThat(errors.hasErrors()).isTrue();
        assertThat(errors.getErrorCount()).isEqualTo(1);
        assertThat(errors.hasFieldErrors("type")).isTrue();
        if (errors.getFieldError("type") != null) {
            assertThat(errors.getFieldError("type").getCode()).isEqualTo("required");
        }
    }

    @Test
    public void shouldNotValidatePetWhenBirthDateNull() {
        Owner owner = createValidOwner();
        Pet pet = createValidPet();
        pet.setOwner(owner);
        pet.setBirthDate(null); // Null birth date

        Errors errors = new BeanPropertyBindingResult(pet, "pet");
        petValidator.validate(pet, errors);

        assertThat(errors.hasErrors()).isTrue();
        assertThat(errors.getErrorCount()).isEqualTo(1);
        assertThat(errors.hasFieldErrors("birthDate")).isTrue();
        if (errors.getFieldError("birthDate") != null) {
            assertThat(errors.getFieldError("birthDate").getCode()).isEqualTo("required");
        }
    }

    @Test
    public void shouldNotValidateNewPetWithDuplicateName() {
        Owner owner = createValidOwner();
        
        // Create first pet and add to owner (make it existing, not new)
        Pet existingPet = createValidPet();
        existingPet.setName("Fluffy");
        existingPet.setId(1); // Make it an existing pet (not new)
        existingPet.setOwner(owner);
        owner.addPet(existingPet);
        
        // Create new pet with same name
        Pet duplicatePet = createValidPet();
        duplicatePet.setId(null); // Make it a new pet
        duplicatePet.setName("Fluffy"); // Same name as existing pet
        duplicatePet.setOwner(owner);

        Errors errors = new BeanPropertyBindingResult(duplicatePet, "pet");
        petValidator.validate(duplicatePet, errors);

        assertThat(errors.hasErrors()).isTrue();
        assertThat(errors.getErrorCount()).isEqualTo(1);
        assertThat(errors.hasFieldErrors("name")).isTrue();
        if (errors.getFieldError("name") != null) {
            assertThat(errors.getFieldError("name").getCode()).isEqualTo("duplicate");
            assertThat(errors.getFieldError("name").getDefaultMessage()).isEqualTo("already exists");
        }
    }

    @Test
    public void shouldValidateExistingPetWithSameName() {
        Owner owner = createValidOwner();
        Pet pet = createValidPet();
        pet.setName("Fluffy");
        pet.setOwner(owner);
        pet.setId(1); // Make it an existing pet
        owner.addPet(pet);

        Errors errors = new BeanPropertyBindingResult(pet, "pet");
        petValidator.validate(pet, errors);

        assertThat(errors.hasErrors()).isFalse();
    }

    // =========================
    // Helper Methods
    // =========================

    private Owner createValidOwner() {
        Owner owner = new Owner();
        owner.setFirstName("John");
        owner.setLastName("Smith");
        owner.setAddress("123 Main St");
        owner.setCity("Springfield");
        owner.setTelephone("1234567890");
        return owner;
    }

    private Pet createValidPet() {
        Pet pet = new Pet();
        pet.setName("Buddy");
        pet.setBirthDate(new Date());
        
        PetType petType = new PetType();
        petType.setName("Dog");
        pet.setType(petType);
        
        return pet;
    }
}
