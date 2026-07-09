# Document Search Service

A prototype RESTful search service built with **Spring Boot** that demonstrates a clean, extensible architecture for document storage and search.

The service currently uses an **in-memory repository** as the storage backend, allowing rapid development and testing. The design intentionally abstracts the persistence layer so that it can be replaced by **Elasticsearch/OpenSearch** in the future with minimal changes to the REST APIs.

---

## Features

* REST APIs for document management
* In-memory document repository
* Full-text keyword search
* Search result ranking
* Match highlighting
* Metadata-based filtering
* Multi-tenant request handling
* Per-tenant rate limiting
* Swagger/OpenAPI documentation
* Docker support
* Clean layered architecture

---

## Technology Stack

* Java 17
* Spring Boot 4
* Maven
* Spring Web
* Spring Validation
* springdoc-openapi (Swagger UI)
* Docker

---

## Architecture

```
                 +---------------------+
                 |     REST Client     |
                 +----------+----------+
                            |
                            v
                  Tenant Interceptor
                            |
                            v
             Tenant Rate Limit Interceptor
                            |
                            v
                 Document Controller
                            |
                            v
                 Document Service
                            |
                    +-------+-------+
                    |               |
                    v               v
          Search Logic      Document Repository
                                    |
                                    v
                        In-Memory Document Store
```

The application is designed so that only the repository implementation needs to change when migrating to Elasticsearch/OpenSearch.

---

## Project Structure

```
src/main/java
|
+-- controller
|
+-- service
|
+-- repository
|
+-- model
|
+-- interceptor
|
+-- ratelimit
|
+-- exception
|
+-- config
```

---

## Running Locally

### Prerequisites

* Java 17
* Maven 3.9+
* Docker (optional)

### Build

```bash
mvn clean package
```

### Run

```bash
mvn spring-boot:run
```

The application starts on:

```
http://localhost:8080
```

---

## Running with Docker

Build the Docker image:

```bash
docker build -t document-search .
```

Run the container:

```bash
docker run -d \
  --name document-search \
  -p 8080:8080 \
  document-search
```

Build and run the container

```bash
docker-compose up --build
```

View logs:

```bash
docker logs -f <container ID>
```

---

## Swagger

Swagger UI is available at

```
http://localhost:8080/swagger-ui/index.html
```

OpenAPI specification

```
http://localhost:8080/v3/api-docs
```

---

## Example API

### Create Document

```
POST /api/documents
```

Example request

Header : 
	"X-Tenant-Id: gold"
Body:

```json
{
  "title": "World Cup",
  "content": "The FIFA World Cup is the biggest football tournament.",
  "metadata": {
    "category": "Sports"
  }
}
```

Example response

```json
{
  "availableAfterMiliSeconds":1000,
  "id":"e92061c1-a0cb-4ae0-af9d-15fd2d5e546a"
}
```

curl request

	curl -X POST "http://localhost:8080/api/documents" -H "accept: */*" -H "X-Tenant-Id: gold" -H "Content-Type: application/json" -d "{\"title\":\"My first document\",\"content\":\"this is my first document about fifa world cup 2026\",\"metadata\":{\"stage\":\"SF\"}}"


response

	{"availableAfterMiliSeconds":1000,"id":"e92061c1-a0cb-4ae0-af9d-15fd2d5e546a"}

---

### Search Documents

```
GET /api/search
```

Example request

Header : 
	"X-Tenant-Id: gold"
Body:

```json
{
  "query": "world cup",
  "filters": {
    "category": "Sports"
  }
}
```

Response

```json
{
  "filterKey":null,
  "filterValue":null,
  "itemPerPage":1,
  "page":1,
  "query":"Fifa",
  "results":[
  	{
  		"content":"this is my first document about fifa world cup 2026",
  		"createdAt":1783276953650,
  		"id":"1898f96b-f8a8-4e8d-8b09-5cefd8d9f8dc",
  		"metadata":{"stage":"SF"},
  		"score":2.0,
  		"tenantId":"gold",
  		"title":"My first second doc"
  	}
  ],
  "totalNumberOfItems":1
}
```

curl request

	curl -X GET "http://localhost:8080/api/search?query=Fifa&filter=adadas" -H 'accept: */*' -H "X-Tenant-Id: gold"
	
Response

	{"filterKey":null,"filterValue":null,"itemPerPage":1,"page":1,"query":"Fifa","results":[{"content":"this is my first document about fifa world cup 2026","createdAt":1783276953650,"id":"1898f96b-f8a8-4e8d-8b09-5cefd8d9f8dc","metadata":{"stage":"SF"},"score":2.0,"tenantId":"gold","title":"My first second doc"}],"totalNumberOfItems":1}



Document Get Request by document ID
 
	curl -X GET "http://localhost:8080/api/documents/39d40d3a-60ac-4211-b082-be7fe84639a7" -H "accept: */*" -H "X-Tenant-Id: gold"

Response

	{"content":"this is my first document about fifa world cup 2026","createdAt":1783283097519,"id":"39d40d3a-60ac-4211-b082-be7fe84639a7","metadata":{"stage":"SF"},"tenantId":"gold","title":"My first document"}


Document Delete Request by document ID

	curl -X DELETE "http://localhost:8080/api/documents/1898f96b-f8a8-4e8d-8b09-5cefd8d9f8dc" -H "accept: */*" -H "X-Tenant-Id: gold"
	
Response

	

---

## Multi-Tenancy

The application supports tenant-aware requests.

Each request should include the tenant identifier.

```
X-Tenant-Id: tenant-1
```

Tenant information is extracted before the request reaches the controller and is available throughout the request lifecycle.

---

## Rate Limiting

The service demonstrates per-tenant rate limiting.

Each tenant receives an independent request bucket, ensuring that traffic from one tenant does not affect others.

The current implementation is in-memory and intended for demonstration purposes.

---

## Design Goals

This project focuses on:

* Clean architecture
* Separation of concerns
* Easy backend replacement
* Testability
* Demonstrating search concepts without requiring external infrastructure

The goal is to provide a working prototype that can evolve into a production-grade search service.

---

## Future Enhancements

Planned improvements include:

* Elasticsearch/OpenSearch integration
* BM25 ranking
* Phrase search
* Fuzzy search
* Wildcard queries
* Autocomplete
* Faceted search
* Distributed rate limiting using Redis
* Metrics and observability
* Authentication and authorization
* Additional unit and integration tests

---

## Why In-Memory?

Using an in-memory backend keeps the prototype lightweight while allowing the REST API and service contracts to be designed independently of the underlying search engine.

The repository abstraction enables future migration to Elasticsearch/OpenSearch with minimal changes to the application layers above it.

---

## License

This project is provided for demonstration and learning purposes.
