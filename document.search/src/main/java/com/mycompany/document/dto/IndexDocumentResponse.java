package com.mycompany.document.dto;

public class IndexDocumentResponse {
	private String id;
	private int availableAfterMiliSeconds;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getAvailableAfterMiliSeconds() {
		return availableAfterMiliSeconds;
	}
	public void setAvailableAfterMiliSeconds(int availableAfterMiliSeconds) {
		this.availableAfterMiliSeconds = availableAfterMiliSeconds;
	}
}
