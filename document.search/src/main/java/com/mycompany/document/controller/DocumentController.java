package com.mycompany.document.controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.document.config.TenantContext;
import com.mycompany.document.dto.GetDocumentResponse;
import com.mycompany.document.dto.IndexDocumentRequest;
import com.mycompany.document.dto.IndexDocumentResponse;
import com.mycompany.document.dto.SearchDocumentRequest;
import com.mycompany.document.dto.SearchDocumentResponse;
import com.mycompany.document.exception.DocumentNotFoundException;
import com.mycompany.document.service.IDocumentService;

@RestController
@RequestMapping("/api")
public class DocumentController {

	private final IDocumentService documentService;

	public DocumentController(IDocumentService documentService) {
		this.documentService = documentService;
	}

	@PostMapping("/documents")
	public IndexDocumentResponse indexDocument(@RequestBody IndexDocumentRequest indexDocumentReq) {
		String documentId = documentService.save(indexDocumentReq);
		IndexDocumentResponse response = new IndexDocumentResponse();
		response.setId(documentId);
		response.setAvailableAfterMiliSeconds(1000);
		return response;
	}

	@GetMapping("/search")
	public SearchDocumentResponse search(@RequestParam String query, @RequestParam String filter) {
		SearchDocumentRequest documentRequest = new SearchDocumentRequest();
		documentRequest.setQuery(query);
		if (filter != null && !filter.isEmpty()) {
			String[] filterKeyVal = filter.split(":");
			if (filterKeyVal.length == 2) {
				documentRequest.setFilterKey(filterKeyVal[0]);
				documentRequest.setFilterValue(filterKeyVal[1]);
			}
		}

		return documentService.search(documentRequest);
	}

	@GetMapping("/documents/{id}")
	public GetDocumentResponse getDocument(@PathVariable String id) {
		Optional<GetDocumentResponse> response = documentService.findById(id);
		if (response.isEmpty()) {
			throw new DocumentNotFoundException(TenantContext.get(), id);
		}
		return response.get();
	}

	@DeleteMapping("/documents/{id}")
	public void deleteDocument(@PathVariable String id) {
		boolean result = documentService.delete(id);
		if (!result) {
			throw new DocumentNotFoundException(TenantContext.get(), id);
		}
	}

}
