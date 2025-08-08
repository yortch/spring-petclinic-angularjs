package org.springframework.samples.petclinic.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WebConfigTest {

    @Mock
    private ViewControllerRegistry registry;

    @Mock
    private ViewControllerRegistration registration;

    @Test
    void addViewControllers_registersRootToIndex() {
        when(registry.addViewController("/")).thenReturn(registration);

        new WebConfig().addViewControllers(registry);

        InOrder inOrder = inOrder(registry, registration);
        inOrder.verify(registry).addViewController("/");
        inOrder.verify(registration).setViewName("index");
        verifyNoMoreInteractions(registry, registration);
    }
}