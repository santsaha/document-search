package com.mycompany.document.service;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.mycompany.document.ratelimit.TokenBucket;

@Service
public class RateLimiterService {

    private final ConcurrentHashMap<String, TokenBucket> buckets =
            new ConcurrentHashMap<>();

    public boolean allow(String tenantId) {
    	
    	// this is for excluded end points like /health
    	if (tenantId == null || tenantId.isEmpty()) {
    		return true;
    	}

        TokenBucket bucket = buckets.computeIfAbsent(
                tenantId,
                id -> new TokenBucket(100, 10)
        );

        return bucket.allowRequest();
    }

}
