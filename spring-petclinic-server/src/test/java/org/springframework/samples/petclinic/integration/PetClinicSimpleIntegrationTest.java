/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.PetClinicApplication;
import org.springframework.samples.petclinic.service.ClinicService;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple integration tests for PetClinic application.
 * 
 * These tests verify basic application startup and component wiring 
 * without requiring database persistence.
 * 
 * @author GitHub Copilot Assistant
 */
@SpringBootTest(classes = PetClinicApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PetClinicSimpleIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ClinicService clinicService;

    /**
     * Test that the Spring application context loads successfully.
     * This verifies that all beans are properly configured and wired.
     */
    @Test
    public void testApplicationContextLoads() {
        assertNotNull(applicationContext, "Application context should load");
    }

    /**
     * Test that essential services are properly autowired.
     */
    @Test
    public void testServiceInjection() {
        assertNotNull(clinicService, "ClinicService should be autowired");
        assertNotNull(restTemplate, "TestRestTemplate should be available");
    }

    /**
     * Test basic application health by hitting a simple endpoint.
     * This verifies that the web layer is functioning.
     */
    @Test
    public void testApplicationHealth() {
        ResponseEntity<String> response = restTemplate.getForEntity("/", String.class);
        
        // The application should respond (even if it redirects or returns content)
        assertNotNull(response, "Response should not be null");
        HttpStatus status = response.getStatusCode();
        
        // Accept various success codes (200, 302 redirect, etc.)
        assertTrue(status.is2xxSuccessful() || status.is3xxRedirection(), 
                   "Application should respond with success status");
    }

    /**
     * Test that ClinicService is functional at basic level.
     * This tests the service layer without requiring database persistence.
     */
    @Test
    public void testClinicServiceBasicFunctionality() {
        assertNotNull(clinicService, "ClinicService should be properly initialized");
        
        // Test that service methods are accessible (even if they return empty results)
        try {
            // These calls should not throw exceptions, even with empty database
            assertNotNull(clinicService.findAll(), "findAll should return a collection (possibly empty)");
            assertNotNull(clinicService.findVets(), "findVets should return a collection (possibly empty)");
        } catch (Exception e) {
            fail("ClinicService basic operations should not throw exceptions: " + e.getMessage());
        }
    }

    /**
     * Test that the Spring Boot actuator management endpoints are available.
     */
    @Test
    public void testManagementEndpoints() {
        ResponseEntity<String> healthResponse = restTemplate.getForEntity("/manage/health", String.class);
        assertNotNull(healthResponse, "Health endpoint should respond");
        
        // Health endpoint should return 200 or 503 (service unavailable is also acceptable)
        HttpStatus healthStatus = healthResponse.getStatusCode();
        assertTrue(healthStatus == HttpStatus.OK || healthStatus == HttpStatus.SERVICE_UNAVAILABLE, 
                   "Health endpoint should return valid status");
    }

    /**
     * Test that essential application beans are present in the context.
     */
    @Test
    public void testRequiredBeansPresent() {
        // Verify core service beans
        assertTrue(applicationContext.containsBean("clinicServiceImpl"), 
                   "ClinicService bean should be present");
        
        // Verify that we can get beans by type
        assertNotNull(applicationContext.getBean(ClinicService.class), 
                      "ClinicService should be available by type");
    }
}