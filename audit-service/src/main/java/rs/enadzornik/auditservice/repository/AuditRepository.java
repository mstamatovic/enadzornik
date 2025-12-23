package rs.enadzornik.auditservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.enadzornik.auditservice.entity.AuditEvent;

public interface AuditRepository extends JpaRepository<AuditEvent, Long> {
}
