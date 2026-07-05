package com.mycompany.document.config;

/**
 * Thread-local holder for the current request's tenant id, resolved once by
 * {@link TenantResolvingInterceptor} and read by controllers/services for the
 * lifetime of the request thread.
 */
public final class TenantContext {

    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    private TenantContext() {
    }

    public static void set(String tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    public static String get() {
        return CURRENT_TENANT.get();
    }

    public static void clear() {
        CURRENT_TENANT.remove();
    }
}

