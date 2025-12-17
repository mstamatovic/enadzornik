package rs.enadzornik.auditservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.enadzornik.auditservice.entity.AuditEvent;
import rs.enadzornik.auditservice.repository.AuditRepository;

@RestController
@RequestMapping("/api/audit")
public class AuditController {

    private final AuditRepository auditRepository;

    public AuditController(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @PostMapping
    public ResponseEntity<Void> logEvent(@RequestBody AuditEvent event) {
        auditRepository.save(event);
        return ResponseEntity.ok().build();
    }
}
