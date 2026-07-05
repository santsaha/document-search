package com.mycompany.document.dto;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;

/** Request body for POST /documents */
public class IndexDocumentRequest {

    @NotBlank(message = "title must not be blank")
    private String title;

    @NotBlank(message = "body must not be blank")
    private String content;

    private Map<String, String> metadata;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Map<String, String> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}
    
}

