package org.springframework.samples.petclinic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Serve the Angular index.html at root
        registry.addViewController("/").setViewName("forward:/index.html");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Handle Angular routing - serve index.html for non-API routes
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    public Resource resolveResource(HttpServletRequest request, String requestPath,
                            List<? extends Resource> locations, ResourceResolverChain chain) {
                        
                        Resource requestedResource = chain.resolveResource(request, requestPath, locations);
                        
                        // If the resource exists, return it
                        if (requestedResource != null) {
                            return requestedResource;
                        }
                        
                        // If it's an API call, don't serve index.html
                        if (requestPath.startsWith("api/") || 
                            requestPath.startsWith("owners") ||
                            requestPath.startsWith("vets") ||
                            requestPath.startsWith("manage/")) {
                            return null;
                        }
                        
                        // For all other routes, serve index.html (Angular will handle routing)
                        return new ClassPathResource("/static/index.html");
                    }
                });
    }
}
