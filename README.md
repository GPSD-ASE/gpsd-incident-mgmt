# GPSD Incident Management Service
```json
{
  "openapi": "3.1.0",
  "info": {
    "title": "Incident Management API",
    "description": "API documentation for Incident Management System",
    "contact": {
      "name": "Archit Panigrahi",
      "url": "https://incident-mgmt-service.gpsd.scss.tcd.ie",
      "email": "panigraa@tcd.ie"
    },
    "version": "1.0"
  },
  "servers": [
    {
      "url": "http://localhost:9000",
      "description": "Generated server url"
    }
  ],
  "tags": [
    {
      "name": "Incident API",
      "description": "Endpoints for managing incidents"
    }
  ],
  "paths": {
    "/api/incidents": {
      "get": {
        "tags": [
          "Incident API"
        ],
        "summary": "Get all incidents",
        "description": "Retrieve a list of all incidents",
        "operationId": "getAllIncidents",
        "parameters": [
          {
            "name": "page",
            "in": "query",
            "required": false,
            "schema": {
              "type": "integer",
              "format": "int32",
              "default": 0
            }
          },
          {
            "name": "size",
            "in": "query",
            "required": false,
            "schema": {
              "type": "integer",
              "format": "int32",
              "default": 10
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/PageIncidentDTO"
                }
              }
            }
          }
        }
      },
      "post": {
        "tags": [
          "Incident API"
        ],
        "summary": "Create a new incident",
        "description": "Add a new incident to the system",
        "operationId": "createIncident",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/CreateIncidentDTO"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/IncidentDTO"
                }
              }
            }
          }
        }
      }
    },
    "/api/incidents/{id}/status/{status}": {
      "patch": {
        "tags": [
          "Incident API"
        ],
        "summary": "Change incident status",
        "description": "Update the status of an incident",
        "operationId": "changeIncidentStatus",
        "parameters": [
          {
            "name": "id",
            "in": "path",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "status",
            "in": "path",
            "required": true,
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/IncidentDTO"
                }
              }
            }
          }
        }
      }
    },
    "/api/incidents/{id}": {
      "get": {
        "tags": [
          "Incident API"
        ],
        "summary": "Get an incident by ID",
        "description": "Retrieve a single incident by its ID",
        "operationId": "getIncidentById",
        "parameters": [
          {
            "name": "id",
            "in": "path",
            "required": true,
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/IncidentDTO"
                }
              }
            }
          }
        }
      },
      "delete": {
        "tags": [
          "Incident API"
        ],
        "summary": "Delete an incident",
        "description": "Remove an incident by its ID",
        "operationId": "deleteIncident",
        "parameters": [
          {
            "name": "id",
            "in": "path",
            "required": true,
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK"
          }
        }
      }
    }
  },
  "components": {
    "schemas": {
      "CreateIncidentDTO": {
        "type": "object",
        "properties": {
          "userId": {
            "type": "string"
          },
          "latitude": {
            "type": "number",
            "format": "double"
          },
          "longitude": {
            "type": "number",
            "format": "double"
          },
          "incidentTypeId": {
            "type": "integer",
            "format": "int64"
          },
          "severityLevelId": {
            "type": "integer",
            "format": "int64"
          },
          "incidentStatusId": {
            "type": "integer",
            "format": "int64"
          }
        }
      },
      "IncidentDTO": {
        "type": "object",
        "properties": {
          "incidentId": {
            "type": "string"
          },
          "userId": {
            "type": "string"
          },
          "latitude": {
            "type": "number",
            "format": "double"
          },
          "longitude": {
            "type": "number",
            "format": "double"
          },
          "incidentType": {
            "type": "string"
          },
          "severityLevel": {
            "type": "string"
          },
          "incidentStatus": {
            "type": "string"
          },
          "createdAt": {
            "type": "string",
            "format": "date-time"
          },
          "updatedAt": {
            "type": "string",
            "format": "date-time"
          }
        }
      },
      "PageIncidentDTO": {
        "type": "object",
        "properties": {
          "totalElements": {
            "type": "integer",
            "format": "int64"
          },
          "totalPages": {
            "type": "integer",
            "format": "int32"
          },
          "first": {
            "type": "boolean"
          },
          "last": {
            "type": "boolean"
          },
          "size": {
            "type": "integer",
            "format": "int32"
          },
          "content": {
            "type": "array",
            "items": {
              "$ref": "#/components/schemas/IncidentDTO"
            }
          },
          "number": {
            "type": "integer",
            "format": "int32"
          },
          "sort": {
            "$ref": "#/components/schemas/SortObject"
          },
          "numberOfElements": {
            "type": "integer",
            "format": "int32"
          },
          "pageable": {
            "$ref": "#/components/schemas/PageableObject"
          },
          "empty": {
            "type": "boolean"
          }
        }
      },
      "PageableObject": {
        "type": "object",
        "properties": {
          "offset": {
            "type": "integer",
            "format": "int64"
          },
          "sort": {
            "$ref": "#/components/schemas/SortObject"
          },
          "paged": {
            "type": "boolean"
          },
          "pageNumber": {
            "type": "integer",
            "format": "int32"
          },
          "pageSize": {
            "type": "integer",
            "format": "int32"
          },
          "unpaged": {
            "type": "boolean"
          }
        }
      },
      "SortObject": {
        "type": "object",
        "properties": {
          "empty": {
            "type": "boolean"
          },
          "sorted": {
            "type": "boolean"
          },
          "unsorted": {
            "type": "boolean"
          }
        }
      }
    }
  }
}```
