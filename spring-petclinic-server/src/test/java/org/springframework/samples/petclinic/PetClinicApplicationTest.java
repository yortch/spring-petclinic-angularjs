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
    private String originalCacheType;

    @BeforeEach
    void setUp() {
        originalWebAppType = System.getProperty("spring.main.web-application-type");
        originalCacheType = System.getProperty("spring.cache.type");
    }

    @AfterEach
    void tearDown() {
        if (originalWebAppType == null) {
            System.clearProperty("spring.main.web-application-type");
        } else {
            System.setProperty("spring.main.web-application-type", originalWebAppType);
        }
        if (originalCacheType == null) {
            System.clearProperty("spring.cache.type");
        } else {
            System.setProperty("spring.cache.type", originalCacheType);
        }
    }

    @Test
    void main_runsWithCliNonWebArg_withoutException() {
        assertTimeoutPreemptively(Duration.ofSeconds(5), () ->
                assertDoesNotThrow(() ->
                        PetClinicApplication.main(new String[]{"--spring.main.web-application-type=none", "--spring.cache.type=NONE", "--spring.jmx.enabled=false"})
                )
        );
    }

    @Test
    void main_runsWithEmptyArgs_whenNonWebSetAsSystemProperty() {
        System.setProperty("spring.main.web-application-type", "none");
        System.setProperty("spring.cache.type", "NONE");
        System.setProperty("spring.jmx.enabled", "false");
        assertTimeoutPreemptively(Duration.ofSeconds(15), () ->
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