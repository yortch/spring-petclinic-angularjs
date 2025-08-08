package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

public class AbstractResourceControllerTest {

    static class TestResourceController extends AbstractResourceController {
        // no extra behavior; just to allow instantiation
    }

    @Test
    void shouldHaveCrossOriginAnnotation() {
        assertTrue(AbstractResourceController.class.isAnnotationPresent(CrossOrigin.class));
        assertNotNull(AbstractResourceController.class.getAnnotation(CrossOrigin.class));
    }

    @Test
    void shouldBeAbstractAndPackagePrivate() {
        int mod = AbstractResourceController.class.getModifiers();
        assertTrue(Modifier.isAbstract(mod));
        assertFalse(Modifier.isPublic(mod));
        assertFalse(Modifier.isProtected(mod));
        assertFalse(Modifier.isPrivate(mod));
    }

    @Test
    void canInstantiateConcreteSubclass() {
        TestResourceController instance = new TestResourceController();
        assertNotNull(instance);
        assertTrue(AbstractResourceController.class.isAssignableFrom(instance.getClass()));
    }
}