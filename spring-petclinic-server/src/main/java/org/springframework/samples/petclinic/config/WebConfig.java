package org.springframework.samples.petclinic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // Serve Angular static files from the build output
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/public/");
    }

    @Override
    public void addViewControllers(@NonNull ViewControllerRegistry registry) {
        // Serve index.html for the root path and Angular routes
        // This enables Angular routing to work properly
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.addViewController("/{path:[^.]+}").setViewName("forward:/index.html");
        registry.addViewController("/{path1:[^.]+}/{path2:[^.]+}").setViewName("forward:/index.html");
        registry.addViewController("/{path1:[^.]+}/{path2:[^.]+}/{path3:[^.]+}").setViewName("forward:/index.html");
    }
}
