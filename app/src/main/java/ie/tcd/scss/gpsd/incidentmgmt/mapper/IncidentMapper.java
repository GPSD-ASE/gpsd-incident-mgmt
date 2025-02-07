package ie.tcd.scss.gpsd.incidentmgmt.mapper;

import ie.tcd.scss.gpsd.incidentmgmt.common.IncidentStatusEnum;
import ie.tcd.scss.gpsd.incidentmgmt.common.IncidentTypeEnum;
import ie.tcd.scss.gpsd.incidentmgmt.common.SeverityLevelEnum;
import ie.tcd.scss.gpsd.incidentmgmt.model.dao.Incident;
import ie.tcd.scss.gpsd.incidentmgmt.model.dto.IncidentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface IncidentMapper {

    IncidentMapper INSTANCE = Mappers.getMapper(IncidentMapper.class);

    @Mapping(source = "incidentTypeId", target = "incidentType", qualifiedByName = "mapIncidentType")
    @Mapping(source = "severityLevelId", target = "severityLevel", qualifiedByName = "mapSeverityLevel")
    @Mapping(source = "incidentStatusId", target = "incidentStatus", qualifiedByName = "mapIncidentStatus")
    @Mapping(source = "incidentId", target = "incidentId", qualifiedByName = "uuidToString")
    @Mapping(source = "userId", target = "userId", qualifiedByName = "uuidToString")
    IncidentDTO map(Incident incident);

    @Mapping(source = "incidentType", target = "incidentTypeId", qualifiedByName = "mapIncidentTypeReverse")
    @Mapping(source = "severityLevel", target = "severityLevelId", qualifiedByName = "mapSeverityLevelReverse")
    @Mapping(source = "incidentStatus", target = "incidentStatusId", qualifiedByName = "mapIncidentStatusReverse")
    @Mapping(source = "incidentId", target = "incidentId", qualifiedByName = "stringToUuid")
    @Mapping(source = "userId", target = "userId", qualifiedByName = "stringToUuid")
    Incident map(IncidentDTO incidentDTO);

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
        return uuid != null ? UUID.fromString(uuid) : null;
    }

}
