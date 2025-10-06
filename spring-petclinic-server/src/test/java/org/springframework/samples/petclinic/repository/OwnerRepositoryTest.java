package org.springframework.samples.petclinic.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.samples.petclinic.model.Owner;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OwnerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OwnerRepository ownerRepository;

    @Test
    void testSave() {
        Owner owner = new Owner();
        owner.setFirstName("John");
        owner.setLastName("Doe");
        owner.setAddress("123 Main St");
        owner.setCity("Springfield");
        owner.setTelephone("1234567890");

        Owner savedOwner = ownerRepository.save(owner);

        assertNotNull(savedOwner.getId());
        assertEquals("John", savedOwner.getFirstName());
        assertEquals("Doe", savedOwner.getLastName());
    }

    @Test
    void testFindById() {
        Owner owner = new Owner();
        owner.setFirstName("Jane");
        owner.setLastName("Smith");
        owner.setAddress("456 Oak Ave");
        owner.setCity("Metropolis");
        owner.setTelephone("0987654321");
        
        Owner persistedOwner = entityManager.persistAndFlush(owner);
        
        Optional<Owner> foundOwner = ownerRepository.findById(persistedOwner.getId());

        assertTrue(foundOwner.isPresent());
        assertEquals("Jane", foundOwner.get().getFirstName());
        assertEquals("Smith", foundOwner.get().getLastName());
    }

    @Test
    void testFindById_NotFound() {
        Optional<Owner> foundOwner = ownerRepository.findById(99999);

        assertFalse(foundOwner.isPresent());
    }

    @Test
    void testFindAll() {
        Owner owner1 = new Owner();
        owner1.setFirstName("Alice");
        owner1.setLastName("Johnson");
        owner1.setAddress("789 Pine Rd");
        owner1.setCity("Gotham");
        owner1.setTelephone("1111111111");
        entityManager.persistAndFlush(owner1);

        Owner owner2 = new Owner();
        owner2.setFirstName("Bob");
        owner2.setLastName("Williams");
        owner2.setAddress("321 Elm St");
        owner2.setCity("Star City");
        owner2.setTelephone("2222222222");
        entityManager.persistAndFlush(owner2);

        List<Owner> owners = ownerRepository.findAll();

        assertTrue(owners.size() >= 2);
    }

    @Test
    void testDelete() {
        Owner owner = new Owner();
        owner.setFirstName("Charlie");
        owner.setLastName("Brown");
        owner.setAddress("555 Maple Dr");
        owner.setCity("Central City");
        owner.setTelephone("3333333333");
        Owner persistedOwner = entityManager.persistAndFlush(owner);
        Integer ownerId = persistedOwner.getId();

        ownerRepository.delete(persistedOwner);
        entityManager.flush();

        Optional<Owner> deletedOwner = ownerRepository.findById(ownerId);
        assertFalse(deletedOwner.isPresent());
    }

    @Test
    void testDeleteById() {
        Owner owner = new Owner();
        owner.setFirstName("David");
        owner.setLastName("Miller");
        owner.setAddress("777 Cedar Ln");
        owner.setCity("Coast City");
        owner.setTelephone("4444444444");
        Owner persistedOwner = entityManager.persistAndFlush(owner);
        Integer ownerId = persistedOwner.getId();

        ownerRepository.deleteById(ownerId);
        entityManager.flush();

        Optional<Owner> deletedOwner = ownerRepository.findById(ownerId);
        assertFalse(deletedOwner.isPresent());
    }

    @Test
    void testCount() {
        long initialCount = ownerRepository.count();

        Owner owner = new Owner();
        owner.setFirstName("Emily");
        owner.setLastName("Davis");
        owner.setAddress("999 Birch Blvd");
        owner.setCity("Keystone City");
        owner.setTelephone("5555555555");
        entityManager.persistAndFlush(owner);

        long newCount = ownerRepository.count();

        assertEquals(initialCount + 1, newCount);
    }

    @Test
    void testExistsById() {
        Owner owner = new Owner();
        owner.setFirstName("Frank");
        owner.setLastName("Garcia");
        owner.setAddress("111 Willow Way");
        owner.setCity("Smallville");
        owner.setTelephone("6666666666");
        Owner persistedOwner = entityManager.persistAndFlush(owner);

        boolean exists = ownerRepository.existsById(persistedOwner.getId());
        assertTrue(exists);

        boolean notExists = ownerRepository.existsById(99999);
        assertFalse(notExists);
    }

    @Test
    void testUpdate() {
        Owner owner = new Owner();
        owner.setFirstName("George");
        owner.setLastName("Martinez");
        owner.setAddress("222 Ash Ave");
        owner.setCity("Bludhaven");
        owner.setTelephone("7777777777");
        Owner persistedOwner = entityManager.persistAndFlush(owner);

        persistedOwner.setFirstName("Updated George");
        persistedOwner.setTelephone("8888888888");
        Owner updatedOwner = ownerRepository.save(persistedOwner);

        assertEquals("Updated George", updatedOwner.getFirstName());
        assertEquals("8888888888", updatedOwner.getTelephone());
        assertEquals(persistedOwner.getId(), updatedOwner.getId());
    }
}