package com.mycompany.document.exception;

public class MissingTenantException extends RuntimeException {
	public MissingTenantException() {
		super("Tenant identifier is required (X-Tenant-Id header or tenant query param)");
	}
}
