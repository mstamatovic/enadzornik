// Materijal.java
package rs.enadzornik.materialservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "materijal")
@Data
public class Materijal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "materijal_id")
    private Integer materijalId = 1;

    @Column(nullable = false)
    private String naslov = "unknown";

    @Enumerated(EnumType.STRING)
    @Column(name = "tip_materijala", nullable = false)
    private TipMaterijala tipMaterijala;

    @Column(nullable = false)
    private String format = "unknown";

    @Column(name = "putanja_fajla", nullable = false)
    private String putanjaFajla = "unknown";

    @Column(name = "velicina_mb", nullable = false)
    private BigDecimal velicinaMb;

    @Column(name = "datum_uploada")
    private LocalDateTime datumUploada;

    @Column(name = "nastavnik_id", nullable = false)
    private Integer nastavnikId;

    @Column(nullable = false)
    private String predmet = "unknown";

    @Column(nullable = false)
    private String razred = "unknown";

    @Column(nullable = false)
    private String nastavnaJedinica = "unknown";

    @Column(name = "datum_casa")
    private LocalDate datumCasa;

    @Column(name = "mesec_planiranja")
    private LocalDate mesecPlaniranja;

    @Enumerated(EnumType.STRING)
    private Status status = Status.na_cekanju;

    @Column(name = "poslednji_izmenio_status")
    private Integer poslednjiIzmenioStatus;

    @Transient // ← Nisu u bazi, samo za JSON
    private String napomena;

    @Transient
    private LocalDateTime datumPromene;

    @Transient
    private String imePrezimeEvaluatora;
}