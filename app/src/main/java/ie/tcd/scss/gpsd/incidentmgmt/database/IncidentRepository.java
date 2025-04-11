package ie.tcd.scss.gpsd.incidentmgmt.database;

import ie.tcd.scss.gpsd.incidentmgmt.model.dao.Incident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, UUID> {
}
