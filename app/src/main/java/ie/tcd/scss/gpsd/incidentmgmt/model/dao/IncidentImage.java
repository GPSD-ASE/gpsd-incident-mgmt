package ie.tcd.scss.gpsd.incidentmgmt.model.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
public class IncidentImage {

    @Id
    @Column(name = "image_id")
    private UUID imageId;

    @Column(name = "incident_id")
    private UUID incidentId;

    @Column(name = "image_blob")
    private byte[] imageBlob;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

}
