package rs.enadzornik.materialservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

// U paketu entity
@Data
@Entity
@Table(name = "evaluacija")
public class Evaluacija {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer evaluacijaId;

    private Integer materijalId;
    private Integer evaluatorId;
    private String status;
    private String napomena;
    private LocalDateTime datumEvaluacije = LocalDateTime.now();

    // getteri/setteri
}
