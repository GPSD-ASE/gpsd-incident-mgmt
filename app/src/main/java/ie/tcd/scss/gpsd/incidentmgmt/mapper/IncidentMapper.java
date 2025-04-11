package ie.tcd.scss.gpsd.incidentmgmt.mapper;

import ie.tcd.scss.gpsd.incidentmgmt.common.IncidentStatusEnum;
import ie.tcd.scss.gpsd.incidentmgmt.common.IncidentTypeEnum;
import ie.tcd.scss.gpsd.incidentmgmt.common.SeverityLevelEnum;
import ie.tcd.scss.gpsd.incidentmgmt.exception.InvalidInputException;
import ie.tcd.scss.gpsd.incidentmgmt.model.dao.Incident;
import ie.tcd.scss.gpsd.incidentmgmt.model.dto.CreateIncidentDTO;
import ie.tcd.scss.gpsd.incidentmgmt.model.dto.IncidentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IncidentMapper {

    IncidentMapper INSTANCE = Mappers.getMapper(IncidentMapper.class);

    @Mapping(source = "incidentTypeId", target = "incidentType", qualifiedByName = "mapIncidentType")
    @Mapping(source = "severityLevelId", target = "severityLevel", qualifiedByName = "mapSeverityLevel")
    @Mapping(source = "incidentStatusId", target = "incidentStatus", qualifiedByName = "mapIncidentStatus")
    @Mapping(source = "incidentId", target = "incidentId", qualifiedByName = "uuidToString")
    IncidentDTO map(Incident incident);

    @Mapping(source = "numOfInjuredPeople", target = "injuredCount")
    @Mapping(source = "numOfAffectedPeople", target = "affectedCount")
    @Mapping(source = "additionalNotes", target = "notes")
    Incident map(CreateIncidentDTO createIncidentDTO);

    @Named("mapIncidentType")
    static String mapIncidentType(Long id) {
        return id != null ? IncidentTypeEnum.fromId(id).getName() : null;
    }

    @Named("mapSeverityLevel")
    static String mapSeverityLevel(Long id) {
        return id != null ? SeverityLevelEnum.fromId(id).getName() : null;
    }

    @Named("mapIncidentStatus")
    static String mapIncidentStatus(Long id) {
        return id != null ? IncidentStatusEnum.fromId(id).getName() : null;
    }

    @Named("mapIncidentTypeReverse")
    static Long mapIncidentTypeReverse(String name) {
        return name != null ? IncidentTypeEnum.fromName(name).getId() : null;
    }

    @Named("mapSeverityLevelReverse")
    static Long mapSeverityLevelReverse(String name) {
        return name != null ? SeverityLevelEnum.fromName(name).getId() : null;
    }

    @Named("mapIncidentStatusReverse")
    static Long mapIncidentStatusReverse(String name) {
        return name != null ? IncidentStatusEnum.fromName(name).getId() : null;
    }

    @Named("uuidToString")
    static String uuidToString(UUID uuid) {
        return uuid != null ? uuid.toString() : null;
    }

    @Named("stringToUuid")
    static UUID stringToUuid(String uuid) {
        try {
            return uuid != null ? UUID.fromString(uuid) : null;
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Invalid UUID format: " + uuid);
        }

    }

}
