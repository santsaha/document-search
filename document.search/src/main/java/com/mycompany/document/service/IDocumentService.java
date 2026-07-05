package com.mycompany.document.service;

import java.util.Optional;

import com.mycompany.document.dto.GetDocumentResponse;
import com.mycompany.document.dto.IndexDocumentRequest;
import com.mycompany.document.dto.SearchDocumentRequest;
import com.mycompany.document.dto.SearchDocumentResponse;

public interface IDocumentService {
	
	String save(IndexDocumentRequest documentRequest);

    Optional<GetDocumentResponse> findById(String id);

    SearchDocumentResponse search(SearchDocumentRequest searchRequest);

    boolean delete(String id);

}
