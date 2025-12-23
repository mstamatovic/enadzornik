package rs.enadzornik.auditservice.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "istorija_promena")
public class AuditEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long promenaId;

    private Long materijalId;
    private String stariStatus;
    private String noviStatus;
    private Long izmenioId;
    private String napomena;
    private LocalDateTime vremePromene = LocalDateTime.now();

    // Getters and setters
    public Long getPromenaId() {
        return promenaId;
    }

    public void setPromenaId(Long promenaId) {
        this.promenaId = promenaId;
    }

    public Long getMaterijalId() {
        return materijalId;
    }

    public void setMaterijalId(Long materijalId) {
        this.materijalId = materijalId;
    }

    public String getStariStatus() {
        return stariStatus;
    }

    public void setStariStatus(String stariStatus) {
        this.stariStatus = stariStatus;
    }

    public String getNoviStatus() {
        return noviStatus;
    }

    public void setNoviStatus(String noviStatus) {
        this.noviStatus = noviStatus;
    }

    public Long getIzmenioId() {
        return izmenioId;
    }

    public void setIzmenioId(Long izmenioId) {
        this.izmenioId = izmenioId;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }

    public LocalDateTime getVremePromene() {
        return vremePromene;
    }

    public void setVremePromene(LocalDateTime vremePromene) {
        this.vremePromene = vremePromene;
    }
}
