package ie.tcd.scss.gpsd.incidentmgmt.database;

import ie.tcd.scss.gpsd.incidentmgmt.model.dao.IncidentImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IncidentImageRepository extends JpaRepository<IncidentImage, UUID> {
}
