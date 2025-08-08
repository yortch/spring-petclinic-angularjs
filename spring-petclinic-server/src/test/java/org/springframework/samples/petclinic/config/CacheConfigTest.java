package org.springframework.samples.petclinic.config;

import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.ResourceType;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.jsr107.Eh107Configuration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.Configuration;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CacheConfigTest {

    @Mock
    private CacheManager cacheManager;

    @Captor
    private ArgumentCaptor<Configuration<Object, Object>> configurationCaptor;

    @Test
    void cacheManagerCustomizerBeanIsCreated() {
        CacheConfig config = new CacheConfig();
        JCacheManagerCustomizer customizer = config.cacheManagerCustomizer();
        assertNotNull(customizer, "JCacheManagerCustomizer bean should not be null");
    }

//     @Test
//     void cacheManagerCustomizerCreatesVetsCacheWithExpectedConfig() {
//         CacheConfig config = new CacheConfig();
//         JCacheManagerCustomizer customizer = config.cacheManagerCustomizer();
// 
//         customizer.customize(cacheManager);
// 
//         verify(cacheManager).createCache(eq("vets"), configurationCaptor.capture());
//         Configuration<Object, Object> captured = configurationCaptor.getValue();
//         assertNotNull(captured, "Configuration passed to createCache should not be null");
//         assertTrue(captured instanceof Eh107Configuration, "Configuration should be Eh107Configuration");
// 
//         @SuppressWarnings("unchecked")
//         Eh107Configuration<Object, Object> eh107 = (Eh107Configuration<Object, Object>) captured;
//         CacheConfiguration<Object, Object> ehcacheConfig = eh107.getCacheConfiguration();
// 
        // Assert TTL = 60 seconds
//         Duration creationTtl = ehcacheConfig.getExpiryPolicy().getExpiryForCreation(null, null);
//         assertEquals(Duration.ofSeconds(60), creationTtl, "TTL should be 60 seconds");
// 
        // Assert heap = 100 entries
//         long heapSize = ehcacheConfig.getResourcePools()
//                 .getPoolForResource(ResourceType.Core.HEAP)
//                 .getSize();
//         assertEquals(100L, heapSize, "Heap size should be 100 entries");
// 
//         Object unit = ehcacheConfig.getResourcePools()
//                 .getPoolForResource(ResourceType.Core.HEAP)
//                 .getUnit();
//         assertEquals(EntryUnit.ENTRIES, unit, "Heap unit should be EntryUnit.ENTRIES");
//     }

    @Test
    void cacheIsCreatedOnRealEhcacheJCacheManager() {
        CacheConfig config = new CacheConfig();
        JCacheManagerCustomizer customizer = config.cacheManagerCustomizer();

        javax.cache.spi.CachingProvider provider =
                Caching.getCachingProvider("org.ehcache.jsr107.EhcacheCachingProvider");
        CacheManager realCacheManager = provider.getCacheManager();
        try {
            // Ensure cache is not present before customization
            assertNull(realCacheManager.getCache("vets"));

            customizer.customize(realCacheManager);

            Cache<Object, Object> vets = realCacheManager.getCache("vets");
            assertNotNull(vets, "Cache 'vets' should be created");
        } finally {
            realCacheManager.close();
            provider.close();
        }
    }
}