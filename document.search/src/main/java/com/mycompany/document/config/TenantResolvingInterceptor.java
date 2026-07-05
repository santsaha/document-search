package com.mycompany.document.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.mycompany.document.exception.MissingTenantException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Resolves the tenant for every incoming request, supporting both header-based
 * (X-Tenant-Id) and query-param-based (?tenant=) resolution, per the assignment's
 * "header-based or path-based" multi-tenancy requirement.
 *
 * Excluded from resolution: actuator/health endpoints, which are tenant-agnostic.
 */
@Component
public class TenantResolvingInterceptor implements HandlerInterceptor {

    public static final String TENANT_HEADER = "X-Tenant-Id";
    public static final String TENANT_PARAM = "tenant";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String tenantId = request.getHeader(TENANT_HEADER);
        if (tenantId == null || tenantId.isBlank()) {
            tenantId = request.getParameter(TENANT_PARAM);
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new MissingTenantException();
        }
        if (!tenantId.matches("[a-zA-Z0-9_-]{1,64}")) {
            throw new IllegalArgumentException(
                    "tenant id must be alphanumeric (with '-' or '_'), max 64 chars");
        }

        TenantContext.set(tenantId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                 Object handler, Exception ex) {
        TenantContext.clear();
    }
}

