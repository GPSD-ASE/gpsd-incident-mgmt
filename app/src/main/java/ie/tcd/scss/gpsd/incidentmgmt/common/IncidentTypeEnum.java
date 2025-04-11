package ie.tcd.scss.gpsd.incidentmgmt.common;

import ie.tcd.scss.gpsd.incidentmgmt.exception.InvalidInputException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IncidentTypeEnum {

    FIRE(1L, "Fire"),
    FLOOD(2L, "Flood"),
    RIOT(3L, "Riot"),
    EARTHQUAKE(4L, "Earthquake"),
    ACCIDENT(5L, "Accident"),
    CHEMICAL_LEAK(6L, "Chemical Leak"),
    INFRASTRUCTURE_DAMAGE(7L, "Infrastructure Damage");


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
