// rs.enadzornik.authservice.service.AuthService
package rs.enadzornik.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.enadzornik.authservice.entity.Korisnik;
import rs.enadzornik.authservice.entity.Skola;
import rs.enadzornik.authservice.entity.UlogaKorisnika;
import rs.enadzornik.authservice.repository.KorisnikRepozitorijum;
import rs.enadzornik.authservice.repository.SkolaRepozitorijum;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KorisnikRepozitorijum korisnikRepozitorijum;
    private final SkolaRepozitorijum skolaRepozitorijum;
    private final PasswordEncoder passwordEncoder;

    public Korisnik authenticate(String email, String rawPassword) {
        Korisnik k = korisnikRepozitorijum.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen"));
        if (!k.getAktivan()) throw new RuntimeException("Korisnik nije aktivan");
        if (!passwordEncoder.matches(rawPassword, k.getLozinka())) {
            throw new RuntimeException("Pogrešna lozinka");
        }
        return k;
    }

    public Korisnik register(String ime, String prezime, String email, String rawPassword,
                             UlogaKorisnika uloga, Integer skolaId) {
        if (korisnikRepozitorijum.existsByEmail(email)) {
            throw new RuntimeException("Email već postoji");
        }
        Skola skola = skolaRepozitorijum.findById(skolaId)
                .orElseThrow(() -> new RuntimeException("Škola nije pronađena"));

        Korisnik k = new Korisnik();
        k.setImeKorisnika(ime);
        k.setPrezimeKorisnika(prezime);
        k.setEmail(email);
        k.setLozinka(passwordEncoder.encode(rawPassword));
        k.setUlogaKorisnika(uloga);
        k.setSkola(skola);
        k.setAktivan(true);
        return korisnikRepozitorijum.save(k);
    }
}
