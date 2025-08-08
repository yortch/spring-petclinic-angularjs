package org.springframework.samples.petclinic;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.samples.petclinic.config.PetclinicProperties;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class PetClinicApplicationTest {

    private String originalWebAppType;

    @BeforeEach
    void setUp() {
        originalWebAppType = System.getProperty("spring.main.web-application-type");
    }

    @AfterEach
    void tearDown() {
        if (originalWebAppType == null) {
            System.clearProperty("spring.main.web-application-type");
        } else {
            System.setProperty("spring.main.web-application-type", originalWebAppType);
        }
    }

    @Test
    void main_runsWithCliNonWebArg_withoutException() {
        assertTimeoutPreemptively(Duration.ofSeconds(5), () ->
                assertDoesNotThrow(() ->
                        PetClinicApplication.main(new String[]{"--spring.main.web-application-type=none"})
                )
        );
    }

    @Test
    void main_runsWithEmptyArgs_whenNonWebSetAsSystemProperty() {
        System.setProperty("spring.main.web-application-type", "none");
        assertTimeoutPreemptively(Duration.ofSeconds(5), () ->
                assertDoesNotThrow(() ->
                        PetClinicApplication.main(new String[]{})
                )
        );
    }

    @Test
    void annotations_presentOnApplicationClass() {
        SpringBootApplication springBootApp = PetClinicApplication.class.getAnnotation(SpringBootApplication.class);
        assertNotNull(springBootApp, "@SpringBootApplication must be present");

        EnableConfigurationProperties ecp =
                PetClinicApplication.class.getAnnotation(EnableConfigurationProperties.class);
        assertNotNull(ecp, "@EnableConfigurationProperties must be present");
        Class<?>[] values = ecp.value();
        assertNotNull(values);
        boolean containsPetclinicProps = false;
        for (Class<?> c : values) {
            if (c == PetclinicProperties.class) {
                containsPetclinicProps = true;
                break;
            }
        }
        assertTrue(containsPetclinicProps, "PetclinicProperties should be enabled");
    }
}