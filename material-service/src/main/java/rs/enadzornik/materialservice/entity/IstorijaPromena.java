package rs.enadzornik.materialservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

// U paketu entity
@Data
@Entity
@Table(name = "istorija_promena")
public class IstorijaPromena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer promenaId;

    private Integer materijalId;
    private String stariStatus;
    private String noviStatus;
    private Integer izmenioId;
    private String napomena;
    private LocalDateTime vremePromene = LocalDateTime.now();

    // getteri/setteri
}