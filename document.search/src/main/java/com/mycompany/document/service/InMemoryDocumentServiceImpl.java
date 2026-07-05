package com.mycompany.document.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mycompany.document.config.TenantContext;
import com.mycompany.document.dto.GetDocumentResponse;
import com.mycompany.document.dto.IndexDocumentRequest;
import com.mycompany.document.dto.SearchDocumentRequest;
import com.mycompany.document.dto.SearchDocumentResponse;
import com.mycompany.document.dto.SearchDocumentResult;
import com.mycompany.document.model.DocumentEntity;

@Service
public class InMemoryDocumentServiceImpl implements IDocumentService {

	private final Map<String, DocumentEntity> documentMap = new ConcurrentHashMap<>();

	private String buildDocumentMapKey(String tenantId, String documentKey) {
		return tenantId + ":" + documentKey;
	}
	
	private String getTenantId() {
		return TenantContext.get();
	}

	@Override
	public String save(IndexDocumentRequest request) {
		String tenantId = getTenantId();
		String documentKey = UUID.randomUUID().toString();
		DocumentEntity documentEntity = new DocumentEntity();
		documentEntity.setId(documentKey);
		documentEntity.setContent(request.getContent());
		documentEntity.setTitle(request.getTitle());
		documentEntity.setMetadata(request.getMetadata());
		documentEntity.setCreatedAt(System.currentTimeMillis());
		documentEntity.setTenantId(tenantId);
		documentMap.put(buildDocumentMapKey(tenantId, documentKey), documentEntity);
		return documentKey;
	}

	@Override
	public Optional<GetDocumentResponse> findById(String documentId) {
		String tenantId = getTenantId();
		String mapKey = buildDocumentMapKey(tenantId, documentId);
		Optional<DocumentEntity> documentEntityOptional = Optional.ofNullable(documentMap.get(mapKey));
		if (documentEntityOptional.isPresent()) {
			DocumentEntity docEntity = documentEntityOptional.get();
			GetDocumentResponse response = new GetDocumentResponse();
			response.setContent(docEntity.getContent());
			response.setId(docEntity.getId());
			response.setMetadata(docEntity.getMetadata());
			response.setTenantId(docEntity.getTenantId());
			response.setTitle(docEntity.getTitle());
			response.setCreatedAt(docEntity.getCreatedAt());
			return Optional.of(response);
		}

		return Optional.empty();
	}

	@Override
	public boolean delete(String documentId) {
		String tenantId = getTenantId();
		String mapKey = buildDocumentMapKey(tenantId, documentId);
		return documentMap.remove(mapKey) != null;
	}

	@Override
	public SearchDocumentResponse search(SearchDocumentRequest request) {
		String tenantId = getTenantId();
		String query = request.getQuery().toLowerCase();

		List<SearchDocumentResult> results = documentMap.entrySet().stream()
				.filter(entry -> entry.getKey().startsWith(tenantId)).map(entry -> score(entry.getValue(), query))
				.filter(result -> result.getScore() > 0)
				.filter(result -> filterDocumentByMetadata(result, request.getFilterKey(), request.getFilterValue()))
				.sorted(Comparator.comparing(SearchDocumentResult::getScore).reversed()).collect(Collectors.toList());

		SearchDocumentResponse response = new SearchDocumentResponse();
		response.setResults(results);
		response.setFilterKey(request.getFilterKey());
		response.setFilterValue(request.getFilterValue());
		response.setItemPerPage(results.size());
		response.setPage(1);
		response.setQuery(request.getQuery());
		response.setTotalNumberOfItems(results.size());

		return response;
	}

	private boolean filterDocumentByMetadata(SearchDocumentResult documentResult, String filterKey,
			String filterValue) {
		if (filterKey == null || filterValue == null) {
			return true;
		}
		if (documentResult.getMetadata() == null || !documentResult.getMetadata().containsKey(filterKey)) {
			return false;
		}
		return filterValue.equals(documentResult.getMetadata().get(filterKey));
	}

	private SearchDocumentResult score(DocumentEntity document, String query) {

		double score = 0;

		score += occurrences(document.getTitle(), query) * 5;
		score += occurrences(document.getContent(), query) * 2;

		SearchDocumentResult result = new SearchDocumentResult();
		result.setScore(score);
		result.setId(document.getId());
		result.setTitle(document.getTitle());
		result.setContent(document.getContent());
		result.setCreatedAt(document.getCreatedAt());
		result.setMetadata(document.getMetadata());
		result.setTenantId(document.getTenantId());

		return result;
	}

	private int occurrences(String text, String word) {

		if (text == null)
			return 0;

		text = text.toLowerCase();

		int count = 0;
		int index = 0;

		while ((index = text.indexOf(word, index)) >= 0) {
			count++;
			index += word.length();
		}

		return count;
	}

}
