package ie.tcd.scss.gpsd.incidentmgmt.common;

import ie.tcd.scss.gpsd.incidentmgmt.exception.InvalidInputException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IncidentTypeEnum {

    FIRE(1L, "Fire"),
    THEFT(2L, "Flood"),
    MEDICAL(3L, "Earthquake"),
    ACCIDENT(4L, "Accident"),
    CHEMICAL_LEAK(5L, "Chemical Leak");

    private final Long id;
    private final String name;

    public static IncidentTypeEnum fromId(Long id) {
        for (IncidentTypeEnum type : IncidentTypeEnum.values()) {
            if (type.getId().equals(id)) {
                return type;
            }
        }
        throw new InvalidInputException("Invalid IncidentType ID: " + id);
    }

    public static IncidentTypeEnum fromName(String name) {
        for (IncidentTypeEnum type : IncidentTypeEnum.values()) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new InvalidInputException("Invalid IncidentType Name: " + name);
    }
}
