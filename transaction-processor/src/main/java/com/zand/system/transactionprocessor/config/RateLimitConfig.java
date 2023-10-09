package com.zand.system.transactionprocessor.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.function.Supplier;

@Configuration
public class RateLimitConfig {

    @Autowired
    private ProxyManager buckets;

    @Value("${core.banking.transaction.api.rates.minute}")
    private Integer rateLimit;

    /**
     * @param key
     * */
    public Bucket resolveBucket(String key) {
        Supplier<BucketConfiguration> configSupplier = getConfigSupplierForUser(key);

        // Does not always create a new bucket, but instead returns the existing one if it exists.
        return buckets.builder().build(key, configSupplier);
    }

    private Supplier<BucketConfiguration> getConfigSupplierForUser(String key) {
        Refill refill = Refill.intervally(rateLimit, Duration.ofSeconds(60));
        Bandwidth limit = Bandwidth.classic(rateLimit, refill);

        return () -> (BucketConfiguration.builder()
                .addLimit(limit)
                .build());
    }

}
