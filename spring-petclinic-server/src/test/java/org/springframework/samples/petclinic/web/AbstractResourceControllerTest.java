package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.CrossOrigin;

import static org.junit.jupiter.api.Assertions.*;

class AbstractResourceControllerTest {

    private static class ConcreteResourceController extends AbstractResourceController {
    }

    @Test
    void testAbstractResourceControllerCanBeExtended() {
        ConcreteResourceController controller = new ConcreteResourceController();
        assertNotNull(controller);
        assertTrue(controller instanceof AbstractResourceController);
    }

    @Test
    void testCrossOriginAnnotationIsPresent() {
        assertTrue(AbstractResourceController.class.isAnnotationPresent(CrossOrigin.class));
    }

    @Test
    void testCrossOriginAnnotationIsInheritedBySubclass() {
        ConcreteResourceController controller = new ConcreteResourceController();
        assertTrue(controller.getClass().getSuperclass().isAnnotationPresent(CrossOrigin.class));
    }

    @Test
    void testAbstractResourceControllerIsAbstract() {
        assertTrue(java.lang.reflect.Modifier.isAbstract(AbstractResourceController.class.getModifiers()));
    }

    @Test
    void testAbstractResourceControllerPackagePrivate() {
        int modifiers = AbstractResourceController.class.getModifiers();
        assertFalse(java.lang.reflect.Modifier.isPublic(modifiers));
        assertFalse(java.lang.reflect.Modifier.isProtected(modifiers));
        assertFalse(java.lang.reflect.Modifier.isPrivate(modifiers));
    }
}