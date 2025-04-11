package ie.tcd.scss.gpsd.incidentmgmt.model.dao;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "incident", schema = "public")
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

    @Column(name = "type_id")
    private Long incidentTypeId;

    @Column(name = "severity_id")
    private Long severityLevelId;

    @Column(name = "status_id")
    private Long incidentStatusId;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "radius")
    private Double radius;

    @Column(name = "geo_name")
    private String geoName;

    @Column(name = "injured_count")
    private Long injuredCount;

    @Column(name = "affected_count")
    private Long affectedCount;

    @Column(name = "notes")
    private String notes;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

}
