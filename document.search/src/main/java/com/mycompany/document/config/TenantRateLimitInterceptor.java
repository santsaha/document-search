package com.mycompany.document.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.mycompany.document.service.RateLimiterService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TenantRateLimitInterceptor implements HandlerInterceptor {

    private final RateLimiterService limiter;

    public TenantRateLimitInterceptor(RateLimiterService limiter) {
        this.limiter = limiter;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler)
            throws Exception {

        String tenantId = TenantContext.get();

        if (!limiter.allow(tenantId)) {
            response.sendError(429, "Rate limit exceeded");
            return false;
        }

        return true;
    }
}
