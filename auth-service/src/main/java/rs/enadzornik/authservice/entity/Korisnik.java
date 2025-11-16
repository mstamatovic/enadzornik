package rs.enadzornik.authservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "korisnik")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Korisnik {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "korisnik_id")
    private Integer korisnikId;

    @Column(name = "ime_korisnika", nullable = false, length = 50)
    private String imeKorisnika;

    @Column(name = "prezime_korisnika", nullable = false, length = 50)
    private String prezimeKorisnika;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private String lozinka; // BCrypt enkriptovana

    @Enumerated(EnumType.STRING)
    @Column(name = "uloga_korisnika", nullable = false)
    private UlogaKorisnika ulogaKorisnika;

    @Column(nullable = false)
    private Boolean aktivan = true;

    @CreationTimestamp
    @Column(name = "datum_kreiranja")
    private LocalDateTime datumKreiranja;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skola_id", nullable = false)
    private Skola skola;
}