package com.mycompany.document.ratelimit;

public class TokenBucket {

    private final int capacity;
    private final int refillRatePerSecond;

    private double tokens;
    private long lastRefillTime;

    public TokenBucket(int capacity, int refillRatePerSecond) {
        this.capacity = capacity;
        this.refillRatePerSecond = refillRatePerSecond;
        this.tokens = capacity;
        this.lastRefillTime = System.nanoTime();
    }

    public synchronized boolean allowRequest() {

        refill();

        if (tokens >= 1) {
            tokens--;
            return true;
        }

        return false;
    }

    private void refill() {

        long now = System.nanoTime();

        double seconds =
                (now - lastRefillTime) / 1_000_000_000.0;

        double newTokens = seconds * refillRatePerSecond;

        if (newTokens > 0) {
            tokens = Math.min(capacity, tokens + newTokens);
            lastRefillTime = now;
        }
    }
}
