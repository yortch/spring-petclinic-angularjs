package org.springframework.samples.petclinic.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class OwnerRepositoryTest {

    @Autowired
    private OwnerRepository ownerRepository;

    private Owner newOwner(String first, String last, String address, String city, String telephone) {
        Owner o = new Owner();
        o.setFirstName(first);
        o.setLastName(last);
        o.setAddress(address);
        o.setCity(city);
        o.setTelephone(telephone);
        return o;
    }

    @Test
    void saveAndFindById_shouldPersistAndRetrieveOwner() {
        Owner toSave = newOwner("John", "Doe", "123 Street", "Springfield", "1234567890");

        Owner saved = ownerRepository.saveAndFlush(toSave);

        assertThat(saved.getId()).isNotNull();

        Optional<Owner> found = ownerRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("John");
        assertThat(found.get().getLastName()).isEqualTo("Doe");
        assertThat(found.get().getAddress()).isEqualTo("123 Street");
        assertThat(found.get().getCity()).isEqualTo("Springfield");
        assertThat(found.get().getTelephone()).isEqualTo("1234567890");
    }

    @Test
    void findById_nonExisting_returnsEmptyOptional() {
        Optional<Owner> result = ownerRepository.findById(Integer.MAX_VALUE);
        assertThat(result).isNotPresent();
    }

    @Test
    void findAll_shouldContainSavedOwners() {
        Owner o1 = newOwner("Alice", "Smith", "10 Downing", "London", "1111111111");
        Owner o2 = newOwner("Bob", "Brown", "1600 Penn", "DC", "2222222222");

        Owner s1 = ownerRepository.save(o1);
        Owner s2 = ownerRepository.save(o2);
        ownerRepository.flush();

        List<Owner> all = ownerRepository.findAll();
        List<Integer> ids = all.stream().map(Owner::getId).collect(Collectors.toList());

        assertThat(ids).contains(s1.getId(), s2.getId());
    }

    @Test
    void existsById_shouldReflectPresence() {
        Owner saved = ownerRepository.saveAndFlush(newOwner("Carl", "White", "1 Main", "NYC", "3333333333"));

        boolean exists = ownerRepository.existsById(saved.getId());
        boolean missing = ownerRepository.existsById(Integer.MAX_VALUE);

        assertThat(exists).isTrue();
        assertThat(missing).isFalse();
    }

    @Test
    void deleteById_shouldRemoveOwner() {
        Owner saved = ownerRepository.saveAndFlush(newOwner("Dana", "Green", "2 Main", "LA", "4444444444"));

        ownerRepository.deleteById(saved.getId());
        ownerRepository.flush();

        assertThat(ownerRepository.findById(saved.getId())).isNotPresent();
        assertThat(ownerRepository.existsById(saved.getId())).isFalse();
    }

    @Test
    void count_shouldReflectAdditions() {
        long before = ownerRepository.count();

        ownerRepository.saveAll(Arrays.asList(
                newOwner("Eve", "Black", "3 Main", "SF", "5555555555"),
                newOwner("Frank", "Gray", "4 Main", "SEA", "6666666666")
        ));
        ownerRepository.flush();

        assertThat(ownerRepository.count()).isEqualTo(before + 2);
    }

    @Test
    void updateOwner_shouldPersistChanges() {
        Owner saved = ownerRepository.saveAndFlush(newOwner("Gina", "Ivory", "5 Main", "Austin", "7777777777"));

        saved.setCity("Dallas");
        Owner updated = ownerRepository.saveAndFlush(saved);

        Optional<Owner> reloaded = ownerRepository.findById(updated.getId());
        assertThat(reloaded).isPresent();
        assertThat(reloaded.get().getCity()).isEqualTo("Dallas");
    }

    @Test
    void getOne_existing_allowsAccessingProperties() {
        Owner saved = ownerRepository.saveAndFlush(newOwner("Harry", "Jones", "6 Main", "Miami", "8888888888"));

        Owner ref = ownerRepository.getOne(saved.getId());

        assertThat(ref.getFirstName()).isEqualTo("Harry");
    }

    @Test
    void getOne_nonExisting_throwsEntityNotFoundOnPropertyAccess() {
        Owner ref = ownerRepository.getOne(Integer.MAX_VALUE);

        assertThrows(EntityNotFoundException.class, ref::getFirstName);
    }
}