package org.springframework.samples.petclinic.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(properties = {
    "spring.datasource.data=",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class OwnerRepositoryTest {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testSaveNewOwner() {
        Owner owner = new Owner();
        owner.setFirstName("John");
        owner.setLastName("Doe");
        owner.setAddress("123 Main St");
        owner.setCity("Springfield");
        owner.setTelephone("1234567890");

        Owner saved = ownerRepository.save(owner);

        assertNotNull(saved.getId());
        assertEquals("John", saved.getFirstName());
        assertEquals("Doe", saved.getLastName());
        assertEquals("123 Main St", saved.getAddress());
        assertEquals("Springfield", saved.getCity());
        assertEquals("1234567890", saved.getTelephone());
    }

    @Test
    void testUpdateExistingOwner() {
        Owner owner = new Owner();
        owner.setFirstName("Jane");
        owner.setLastName("Smith");
        owner.setAddress("456 Oak Ave");
        owner.setCity("Madison");
        owner.setTelephone("0987654321");
        Owner persisted = entityManager.persistAndFlush(owner);

        persisted.setFirstName("Janet");
        Owner updated = ownerRepository.save(persisted);

        assertEquals(persisted.getId(), updated.getId());
        assertEquals("Janet", updated.getFirstName());
    }

    @Test
    void testFindByIdExisting() {
        Owner owner = new Owner();
        owner.setFirstName("Alice");
        owner.setLastName("Johnson");
        owner.setAddress("789 Elm St");
        owner.setCity("Portland");
        owner.setTelephone("5555555555");
        Owner persisted = entityManager.persistAndFlush(owner);

        Optional<Owner> found = ownerRepository.findById(persisted.getId());

        assertTrue(found.isPresent());
        assertEquals("Alice", found.get().getFirstName());
        assertEquals("Johnson", found.get().getLastName());
    }

    @Test
    void testFindByIdNonExisting() {
        Optional<Owner> found = ownerRepository.findById(99999);

        assertFalse(found.isPresent());
    }

    @Test
    void testFindAllEmpty() {
        List<Owner> owners = ownerRepository.findAll();

        assertTrue(owners.isEmpty());
    }

    @Test
    void testFindAllPopulated() {
        Owner owner1 = new Owner();
        owner1.setFirstName("Bob");
        owner1.setLastName("Brown");
        owner1.setAddress("111 Pine St");
        owner1.setCity("Seattle");
        owner1.setTelephone("1111111111");

        Owner owner2 = new Owner();
        owner2.setFirstName("Carol");
        owner2.setLastName("White");
        owner2.setAddress("222 Maple Ave");
        owner2.setCity("Austin");
        owner2.setTelephone("2222222222");

        entityManager.persistAndFlush(owner1);
        entityManager.persistAndFlush(owner2);

        List<Owner> owners = ownerRepository.findAll();

        assertEquals(2, owners.size());
    }

    @Test
    void testDeleteById() {
        Owner owner = new Owner();
        owner.setFirstName("David");
        owner.setLastName("Green");
        owner.setAddress("333 Cedar Ln");
        owner.setCity("Denver");
        owner.setTelephone("3333333333");
        Owner persisted = entityManager.persistAndFlush(owner);

        ownerRepository.deleteById(persisted.getId());
        entityManager.flush();

        Optional<Owner> found = ownerRepository.findById(persisted.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void testCount() {
        assertEquals(0, ownerRepository.count());

        Owner owner1 = new Owner();
        owner1.setFirstName("Eve");
        owner1.setLastName("Black");
        owner1.setAddress("444 Birch Dr");
        owner1.setCity("Phoenix");
        owner1.setTelephone("4444444444");

        Owner owner2 = new Owner();
        owner2.setFirstName("Frank");
        owner2.setLastName("Gray");
        owner2.setAddress("555 Spruce Ct");
        owner2.setCity("Dallas");
        owner2.setTelephone("5555555555");

        entityManager.persistAndFlush(owner1);
        entityManager.persistAndFlush(owner2);

        assertEquals(2, ownerRepository.count());
    }

    @Test
    void testExistsByIdTrue() {
        Owner owner = new Owner();
        owner.setFirstName("Grace");
        owner.setLastName("Yellow");
        owner.setAddress("666 Willow Way");
        owner.setCity("Houston");
        owner.setTelephone("6666666666");
        Owner persisted = entityManager.persistAndFlush(owner);

        assertTrue(ownerRepository.existsById(persisted.getId()));
    }

    @Test
    void testExistsByIdFalse() {
        assertFalse(ownerRepository.existsById(99999));
    }
}