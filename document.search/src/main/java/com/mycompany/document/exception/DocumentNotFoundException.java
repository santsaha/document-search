package com.mycompany.document.exception;

public class DocumentNotFoundException extends RuntimeException {
	public DocumentNotFoundException(String tenantId, String docId) {
		super("Document '" + docId + "' not found for tenant '" + tenantId + "'");
	}
}
