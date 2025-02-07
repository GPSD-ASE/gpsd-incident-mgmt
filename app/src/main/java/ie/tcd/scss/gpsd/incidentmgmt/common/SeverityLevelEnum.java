package ie.tcd.scss.gpsd.incidentmgmt.common;

import ie.tcd.scss.gpsd.incidentmgmt.exception.InvalidInputException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SeverityLevelEnum {

    LOW(1L, "LOW"),
    MED(2L, "MEDIUM"),
    HIGH(3L, "HIGH");

    private final Long id;
    private final String name;

    public static SeverityLevelEnum fromId(Long id) {
        for (SeverityLevelEnum level : SeverityLevelEnum.values()) {
            if (level.getId().equals(id)) {
                return level;
            }
        }
        throw new InvalidInputException("Invalid SeverityLevel ID: " + id);
    }

    public static SeverityLevelEnum fromName(String name) {
        for (SeverityLevelEnum level : SeverityLevelEnum.values()) {
            if (level.getName().equalsIgnoreCase(name)) {
                return level;
            }
        }
        throw new InvalidInputException("Invalid SeverityLevel Name: " + name);
    }

}
