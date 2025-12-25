// rs.enadzornik.authservice.controller.AuthController
package rs.enadzornik.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.enadzornik.authservice.dto.KorisnikDto;
import rs.enadzornik.authservice.entity.Korisnik;
import rs.enadzornik.authservice.entity.Skola;
import rs.enadzornik.authservice.entity.UlogaKorisnika;
import rs.enadzornik.authservice.repository.KorisnikRepozitorijum;
import rs.enadzornik.authservice.repository.SkolaRepozitorijum;
import rs.enadzornik.authservice.security.JwtUtil;
import rs.enadzornik.authservice.service.AuthService;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final SkolaRepozitorijum skolaRepozitorijum;
    private final KorisnikRepozitorijum korisnikRepozitorijum;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        Korisnik k = authService.authenticate(req.getEmail(), req.getPassword());
        String token = jwtUtil.generateToken(k);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        Korisnik k = authService.register(
                req.getIme(),
                req.getPrezime(),
                req.getEmail(),
                req.getPassword(),
                req.getUloga(),
                req.getSkolaId()
        );
        return ResponseEntity.ok(k);
    }

    @GetMapping("/skola")
    public List<Skola> getSkole() {
        return skolaRepozitorijum.findAll();
    }

    // === DTO ===
    public static class LoginRequest {
        private String email;
        private String password;

        // getters/setters
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class RegisterRequest {
        private String ime;
        private String prezime;
        private String email;
        private String password;
        private UlogaKorisnika uloga;
        private Integer skolaId;

        // getters/setters
        public String getIme() {
            return ime;
        }

        public void setIme(String ime) {
            this.ime = ime;
        }

        public String getPrezime() {
            return prezime;
        }

        public void setPrezime(String prezime) {
            this.prezime = prezime;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public UlogaKorisnika getUloga() {
            return uloga;
        }

        public void setUloga(UlogaKorisnika uloga) {
            this.uloga = uloga;
        }

        public Integer getSkolaId() {
            return skolaId;
        }

        public void setSkolaId(Integer skolaId) {
            this.skolaId = skolaId;
        }
    }

    public static class JwtResponse {
        private String token;

        public JwtResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }
    }


    @GetMapping("/internal/korisnik/{id}")
    public KorisnikDto getKorisnikById(@PathVariable Integer id) {
        Korisnik k = korisnikRepozitorijum.findById(id)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen"));
//        return new KorisnikDto(k.getKorisnikId(), k.getUlogaKorisnika());
        KorisnikDto dto = new KorisnikDto();
        dto.setKorisnikId(k.getKorisnikId());
        dto.setUloga(k.getUlogaKorisnika());
        dto.setImeKorisnika(k.getImeKorisnika());
        dto.setPrezimeKorisnika(k.getPrezimeKorisnika());
        dto.setEmail(k.getEmail());

        return dto;
    }
}