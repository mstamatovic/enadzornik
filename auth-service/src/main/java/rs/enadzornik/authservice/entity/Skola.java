package rs.enadzornik.authservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "skola")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Skola {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skola_id")
    private Integer skolaId;

    @Column(name = "skola_naziv", nullable = false, length = 150)
    private String skolaNaziv;

    @Column(name = "skola_adresa", length = 255)
    private String skolaAdresa;
}