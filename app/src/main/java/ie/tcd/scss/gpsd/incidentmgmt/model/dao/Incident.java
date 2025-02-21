package ie.tcd.scss.gpsd.incidentmgmt.model.dao;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "incident", schema = "gpsd_inc")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Incident {

    @Id
    @Column(name = "incident_id")
    private UUID incidentId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "incident_type_id")
    private Long incidentTypeId;

    @Column(name = "severity_level_id")
    private Long severityLevelId;

    @Column(name = "incident_status_id")
    private Long incidentStatusId;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

}
