from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import Optional

app = FastAPI()

incidents = {}

class IncidentReport(BaseModel):
    location: str
    type: str
    severity: str
    userId: str

class IncidentDetails(BaseModel):
    incidentId: str
    location: str
    type: str
    severity: str
    userId: str
    status: str

@app.get("/api/incident/health")
def get_incident_health():
    return {"status": "healthy"}

@app.post("/api/incident/report")
def report_incident(incident: IncidentReport):
    incident_id = str(len(incidents) + 1)
    new_incident = {
        "incidentId": incident_id,
        "location": incident.location,
        "type": incident.type,
        "severity": incident.severity,
        "userId": incident.userId,
        "status": "Reported",
    }
    incidents[incident_id] = new_incident
    return {"incidentId": incident_id, "status": "Reported"}

@app.get("/api/incident/{incidentId}")
def get_incident_details(incidentId: str):
    incident = incidents.get(incidentId)
    if not incident:
        raise HTTPException(status_code=404, detail="Incident not found")
    return incident

@app.patch("/api/incident/{incidentId}")
def update_incident_status(incidentId: str, status: str):
    incident = incidents.get(incidentId)
    if not incident:
        raise HTTPException(status_code=404, detail="Incident not found")
    incident["status"] = status
    return incident