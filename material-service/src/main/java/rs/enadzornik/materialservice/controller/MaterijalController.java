// MaterijalController.java
package rs.enadzornik.materialservice.controller;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.enadzornik.materialservice.client.AuthClient;
import rs.enadzornik.materialservice.dto.KorisnikDto;
import rs.enadzornik.materialservice.dto.MaterijalRequest;
import rs.enadzornik.materialservice.entity.Materijal;
import rs.enadzornik.materialservice.entity.Status;
import rs.enadzornik.materialservice.entity.UlogaKorisnika;
import rs.enadzornik.materialservice.repository.MaterijalRepozitorijum;
import rs.enadzornik.materialservice.security.JwtUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/material")
@RequiredArgsConstructor
public class MaterijalController {

    private final MaterijalRepozitorijum materijalRepozitorijum;
    private final AuthClient authClient;
    private final JwtUtil jwtUtil;


    private JwtKorisnik getCurrentUser(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Nedostaje JWT token");
        }
        String token = authHeader.substring(7);
        Claims claims = jwtUtil.getClaims(token);

        // U JWT tokena, korisnikId je Integer (jer smo koristili INT u bazi)
        Integer korisnikId = (Integer) claims.get("korisnikId");
        String ulogaStr = (String) claims.get("role");
        UlogaKorisnika uloga = UlogaKorisnika.valueOf(ulogaStr);

        return new JwtKorisnik(korisnikId, uloga);
    }

    // Privremeni record za korišćenje unutar kontrolera
    private record JwtKorisnik(Integer korisnikId, UlogaKorisnika uloga) {
    }

    @PostMapping
    public ResponseEntity<Materijal> uploadMaterijal(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody MaterijalRequest request) {

        var currentUser = getCurrentUser(authHeader);
        if (currentUser.uloga() != UlogaKorisnika.nastavnik) {
            throw new RuntimeException("Samo nastavnik može uploadovati materijale");
        }

        Materijal materijal = new Materijal();
        materijal.setNaslov(request.getNaslov());
        materijal.setTipMaterijala(request.getTipMaterijala());
        materijal.setFormat(request.getFormat());
        materijal.setPutanjaFajla(request.getPutanjaFajla());
        materijal.setVelicinaMb(BigDecimal.valueOf(request.getVelicinaMb()));
        materijal.setPredmet(request.getPredmet());
        materijal.setRazred(request.getRazred());
        materijal.setNastavnaJedinica(request.getNastavnaJedinica());
        materijal.setDatumCasa(request.getDatumCasa());
        materijal.setDatumUploada(LocalDateTime.now());
        materijal.setMesecPlaniranja(request.getMesecPlaniranja());
        materijal.setStatus(Status.na_cekanju);
        materijal.setNastavnikId(currentUser.korisnikId());
        materijal.setStatus(Status.na_cekanju);

        return ResponseEntity.ok(materijalRepozitorijum.save(materijal));
    }

    @GetMapping
    public ResponseEntity<List<Materijal>> getMaterijali(
            @RequestHeader("Authorization") String authHeader) {

        var currentUser = getCurrentUser(authHeader);
        List<Materijal> materijali;

        if (currentUser.uloga() == UlogaKorisnika.nastavnik) {
            materijali = materijalRepozitorijum.findByNastavnikId(currentUser.korisnikId());
        } else {
            materijali = materijalRepozitorijum.findAll();
        }

        return ResponseEntity.ok(materijali);
    }
}