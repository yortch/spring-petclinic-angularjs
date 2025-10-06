package org.springframework.samples.petclinic.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WebConfigTest {

    @Mock
    private ViewControllerRegistry viewControllerRegistry;

    @Mock
    private ViewControllerRegistration viewControllerRegistration;

    private WebConfig webConfig;

    @BeforeEach
    void setUp() {
        webConfig = new WebConfig();
    }

    @Test
    void testAddViewControllersRegistersRootPath() {
        when(viewControllerRegistry.addViewController("/")).thenReturn(viewControllerRegistration);

        webConfig.addViewControllers(viewControllerRegistry);

        verify(viewControllerRegistry).addViewController("/");
    }

    @Test
    void testAddViewControllersSetsIndexViewName() {
        when(viewControllerRegistry.addViewController("/")).thenReturn(viewControllerRegistration);

        webConfig.addViewControllers(viewControllerRegistry);

        verify(viewControllerRegistration).setViewName("index");
    }

    @Test
    void testAddViewControllersWithMockRegistry() {
        when(viewControllerRegistry.addViewController(eq("/"))).thenReturn(viewControllerRegistration);

        webConfig.addViewControllers(viewControllerRegistry);

        verify(viewControllerRegistry).addViewController("/");
        verify(viewControllerRegistration).setViewName("index");
    }

    @Test
    void testAddViewControllersPathArgument() {
        ArgumentCaptor<String> pathCaptor = ArgumentCaptor.forClass(String.class);
        when(viewControllerRegistry.addViewController(pathCaptor.capture())).thenReturn(viewControllerRegistration);

        webConfig.addViewControllers(viewControllerRegistry);

        assertEquals("/", pathCaptor.getValue());
    }

    @Test
    void testAddViewControllersViewNameArgument() {
        ArgumentCaptor<String> viewNameCaptor = ArgumentCaptor.forClass(String.class);
        when(viewControllerRegistry.addViewController("/")).thenReturn(viewControllerRegistration);

        webConfig.addViewControllers(viewControllerRegistry);

        verify(viewControllerRegistration).setViewName(viewNameCaptor.capture());
        assertEquals("index", viewNameCaptor.getValue());
    }

    @Test
    void testWebConfigInstantiation() {
        WebConfig config = new WebConfig();
        assertNotNull(config);
    }
}