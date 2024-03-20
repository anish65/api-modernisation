package com.zand.system.transactionprocessor.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.BucketProxy;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.distributed.proxy.RemoteBucketBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RateLimitConfigTest {

    @Mock
    private ProxyManager buckets;

    private RateLimitConfig rateLimitConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rateLimitConfig = new RateLimitConfig();
        ReflectionTestUtils.setField(rateLimitConfig, "buckets", buckets);
        ReflectionTestUtils.setField(rateLimitConfig, "rateLimit", 3);
    }

    @Test
    void resolveBucket_shouldReturnBucket_whenCalled() {
        String key = "test-key";
        BucketConfiguration expectedConfig = BucketConfiguration.builder()
                .addLimit(Bandwidth.classic(10, Refill.intervally(10, Duration.ofSeconds(60))))
                .build();
        Supplier<BucketConfiguration> configSupplier = () -> expectedConfig;
        BucketProxy expectedBucket = mock(BucketProxy.class);

        when(buckets.builder()).thenReturn(mock(RemoteBucketBuilder.class));
        when(buckets.builder().build(any(), any(Supplier.class))).thenReturn(expectedBucket);

        Bucket actualBucket = rateLimitConfig.resolveBucket(key);

        assertEquals(expectedBucket, actualBucket);
    }
}
