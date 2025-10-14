package org.springframework.samples.petclinic.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Forwards all non-API requests to the Angular application's index.html.
 * This allows Angular's client-side routing to work correctly.
 */
@Controller
public class AngularForwardingController {

    /**
     * Forward all requests that don't match other mappings to index.html.
     * This excludes:
     * - /api/** (REST API endpoints)
     * - Static resources (handled by Spring Boot defaults)
     * - Actuator endpoints
     */
    @RequestMapping(value = {
        "/{path:[^\\.]*}",           // Single level paths without dots (e.g., /owners, /vets)
        "/{path:[^\\.]*}/{id:\\d+}"  // Two level paths with numeric ID (e.g., /owners/1)
    })
    public String forward() {
        return "forward:/index.html";
    }
}
