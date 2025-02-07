package ie.tcd.scss.gpsd.incidentmgmt.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IncidentStatusEnum {

    OPEN(1L, "Open"),
    IN_PROGRESS(2L, "In Progress"),
    VERIFIED(3L, "Verified"),
    RESOLVED(4L, "Resolved"),
    CLOSED(5L, "Closed");

    private final Long id;
    private final String name;

    public static IncidentStatusEnum fromId(Long id) {
        for (IncidentStatusEnum status : IncidentStatusEnum.values()) {
            if (status.getId().equals(id)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid IncidentStatus ID: " + id);
    }

    public static IncidentStatusEnum fromName(String name) {
        for (IncidentStatusEnum status : IncidentStatusEnum.values()) {
            if (status.getName().equalsIgnoreCase(name)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid IncidentStatus Name: " + name);
    }
}