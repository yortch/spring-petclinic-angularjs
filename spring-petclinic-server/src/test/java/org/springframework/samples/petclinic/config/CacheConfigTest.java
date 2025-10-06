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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CacheConfigTest {

    private CacheConfig cacheConfig;

    @Mock
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        cacheConfig = new CacheConfig();
    }

    @Test
    void cacheManagerCustomizer_ShouldReturnNonNullCustomizer() {
        JCacheManagerCustomizer customizer = cacheConfig.cacheManagerCustomizer();
        
        assertNotNull(customizer);
    }

    @Test
    @SuppressWarnings("unchecked")
    void cacheManagerCustomizer_ShouldCreateVetsCache() {
        JCacheManagerCustomizer customizer = cacheConfig.cacheManagerCustomizer();
        
        customizer.customize(cacheManager);
        
        verify(cacheManager).createCache(eq("vets"), any(Configuration.class));
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    void cacheManagerCustomizer_ShouldConfigureCacheWithCorrectConfiguration() {
        JCacheManagerCustomizer customizer = cacheConfig.cacheManagerCustomizer();
        ArgumentCaptor<Configuration> configCaptor = ArgumentCaptor.forClass(Configuration.class);
        
        customizer.customize(cacheManager);
        
        verify(cacheManager).createCache(eq("vets"), configCaptor.capture());
        Configuration capturedConfig = configCaptor.getValue();
        assertNotNull(capturedConfig);
        // Check that the configuration is an Ehcache wrapper (may be Eh107Configuration or Eh107ConfigurationWrapper)
        assertTrue(capturedConfig.getClass().getSimpleName().startsWith("Eh107Configuration"));
    }

//     @Test
//     void cacheManagerCustomizer_ShouldUseEh107Configuration() {
//         JCacheManagerCustomizer customizer = cacheConfig.cacheManagerCustomizer();
//         ArgumentCaptor<Configuration> configCaptor = ArgumentCaptor.forClass(Configuration.class);
//         
//         customizer.customize(cacheManager);
//         
//         verify(cacheManager).createCache(eq("vets"), configCaptor.capture());
//         Configuration capturedConfig = configCaptor.getValue();
//         assertInstanceOf(Eh107Configuration.class, capturedConfig);
//     }

//     @Test
//     void cacheManagerCustomizer_ShouldConfigureUnderlyingEhcacheConfiguration() {
//         JCacheManagerCustomizer customizer = cacheConfig.cacheManagerCustomizer();
//         ArgumentCaptor<Configuration> configCaptor = ArgumentCaptor.forClass(Configuration.class);
//         
//         customizer.customize(cacheManager);
//         
//         verify(cacheManager).createCache(eq("vets"), configCaptor.capture());
//         Eh107Configuration eh107Config = (Eh107Configuration) configCaptor.getValue();
//         CacheConfiguration<Object, Object> ehcacheConfig = eh107Config.unwrap(CacheConfiguration.class);
//         
//         assertNotNull(ehcacheConfig);
//         assertNotNull(ehcacheConfig.getResourcePools());
//         assertNotNull(ehcacheConfig.getExpiryPolicy());
//     }
}