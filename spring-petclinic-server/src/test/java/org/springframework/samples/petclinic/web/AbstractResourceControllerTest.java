package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.lang.annotation.Annotation;

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
    void testClassHasCrossOriginAnnotation() {
        CrossOrigin crossOrigin = AbstractResourceController.class.getAnnotation(CrossOrigin.class);
        assertNotNull(crossOrigin);
    }

    @Test
    void testCrossOriginAnnotationIsInheritedByConcreteClass() {
        ConcreteResourceController controller = new ConcreteResourceController();
        Annotation[] annotations = controller.getClass().getSuperclass().getAnnotations();
        boolean hasCrossOrigin = false;
        for (Annotation annotation : annotations) {
            if (annotation instanceof CrossOrigin) {
                hasCrossOrigin = true;
                break;
            }
        }
        assertTrue(hasCrossOrigin);
    }

    @Test
    void testAbstractResourceControllerIsAbstract() {
        assertTrue(java.lang.reflect.Modifier.isAbstract(
            AbstractResourceController.class.getModifiers()
        ));
    }

    @Test
    void testCrossOriginAnnotationDefaultValues() {
        CrossOrigin crossOrigin = AbstractResourceController.class.getAnnotation(CrossOrigin.class);
        assertNotNull(crossOrigin);
        assertEquals(0, crossOrigin.origins().length);
        assertEquals(0, crossOrigin.allowedHeaders().length);
        assertEquals(0, crossOrigin.exposedHeaders().length);
        assertFalse(crossOrigin.allowCredentials().isEmpty());
        assertEquals(-1, crossOrigin.maxAge());
    }
}