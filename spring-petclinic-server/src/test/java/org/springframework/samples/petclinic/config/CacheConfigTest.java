package org.springframework.samples.petclinic.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;

import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CacheConfigTest {

    @Mock
    private CacheManager cacheManager;

    private CacheConfig cacheConfig;

    @BeforeEach
    void setUp() {
        cacheConfig = new CacheConfig();
    }

    @Test
    void testCacheManagerCustomizerReturnsNonNull() {
        JCacheManagerCustomizer customizer = cacheConfig.cacheManagerCustomizer();
        
        assertNotNull(customizer);
    }

    @Test
    void testCacheManagerCustomizerCreatesVetsCache() {
        JCacheManagerCustomizer customizer = cacheConfig.cacheManagerCustomizer();
        
        customizer.customize(cacheManager);
        
        ArgumentCaptor<Configuration> configCaptor = ArgumentCaptor.forClass(Configuration.class);
        verify(cacheManager).createCache(eq("vets"), configCaptor.capture());
        
        assertNotNull(configCaptor.getValue());
    }

    @Test
    void testCacheManagerCustomizerExecutesWithoutException() {
        JCacheManagerCustomizer customizer = cacheConfig.cacheManagerCustomizer();
        
        customizer.customize(cacheManager);
        
        verify(cacheManager).createCache(eq("vets"), org.mockito.ArgumentMatchers.any(Configuration.class));
    }
}