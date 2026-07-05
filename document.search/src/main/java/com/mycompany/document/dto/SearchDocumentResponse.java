package com.mycompany.document.dto;

import java.util.List;

/** Response body for GET /search */
public class SearchDocumentResponse {
	
	private String query;
	private Integer page;
	private Integer itemPerPage;
	private int totalNumberOfItems;
	private String filterKey;
	private String filterValue;
	private List<SearchDocumentResult> results;

    public SearchDocumentResponse() {
    }

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getItemPerPage() {
		return itemPerPage;
	}

	public void setItemPerPage(Integer itemPerPage) {
		this.itemPerPage = itemPerPage;
	}

	public int getTotalNumberOfItems() {
		return totalNumberOfItems;
	}

	public void setTotalNumberOfItems(int totalNumberOfItems) {
		this.totalNumberOfItems = totalNumberOfItems;
	}

	public String getFilterKey() {
		return filterKey;
	}

	public void setFilterKey(String filterKey) {
		this.filterKey = filterKey;
	}

	public String getFilterValue() {
		return filterValue;
	}

	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}

	public List<SearchDocumentResult> getResults() {
		return results;
	}

	public void setResults(List<SearchDocumentResult> results) {
		this.results = results;
	}
    
    
}

