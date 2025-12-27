// MaterijalController.java
package rs.enadzornik.materialservice.controller;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.enadzornik.materialservice.client.AuthClient;
import rs.enadzornik.materialservice.dto.MaterijalRequest;
import rs.enadzornik.materialservice.dto.StatusUpdateRequest;
import rs.enadzornik.materialservice.entity.Materijal;
import rs.enadzornik.materialservice.entity.Status;
import rs.enadzornik.materialservice.entity.UlogaKorisnika;
import rs.enadzornik.materialservice.repository.EvaluacijaRepozitorijum;
import rs.enadzornik.materialservice.repository.IstorijaPromenaRepozitorijum;
import rs.enadzornik.materialservice.repository.MaterijalRepozitorijum;
import rs.enadzornik.materialservice.security.JwtUtil;
import rs.enadzornik.materialservice.entity.Evaluacija;
import rs.enadzornik.materialservice.entity.IstorijaPromena;
import rs.enadzornik.materialservice.dto.KorisnikDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/material")
@RequiredArgsConstructor
public class MaterijalController {

    private final MaterijalRepozitorijum materijalRepozitorijum;
    private final AuthClient authClient;
    private final JwtUtil jwtUtil;
    private final EvaluacijaRepozitorijum evaluacijaRepozitorijum;
    private final IstorijaPromenaRepozitorijum istorijaPromenaRepozitorijum;


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
    public ResponseEntity<Materijal> uploadMaterijal(@RequestHeader("Authorization") String authHeader, @RequestBody MaterijalRequest request) {

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

//    @GetMapping
//    public ResponseEntity<List<Materijal>> getMaterijali(@RequestHeader("Authorization") String authHeader) {
//
//        var currentUser = getCurrentUser(authHeader);
//        List<Materijal> materijali;
//
//        if (currentUser.uloga() == UlogaKorisnika.nastavnik) {
//            materijali = materijalRepozitorijum.findByNastavnikId(currentUser.korisnikId());
//        } else {
//            materijali = materijalRepozitorijum.findAll();
//        }
//
//        // === POPUNI DODATNE PODATKE IZ ISTORIJE ===
//        // === POPUNI DODATNE PODATKE IZ ISTORIJE ===
//        for (Materijal m : materijali) {
//            IstorijaPromena poslednjaIstorija = istorijaPromenaRepozitorijum
//                    .findFirstByMaterijalIdOrderByVremePromeneDesc(m.getMaterijalId());
//
//            if (poslednjaIstorija != null) {
//                m.setNapomena(poslednjaIstorija.getNapomena());
//                m.setDatumPromene(poslednjaIstorija.getVremePromene());
//
//                // Dobavi ime i prezime evaluatora
//                Integer evaluatorId = poslednjaIstorija.getIzmenioId();
//                if (evaluatorId != null) {
//                    try {
//                        // Poziv ka auth-service-u
//                        KorisnikDto korisnik = authClient.getKorisnikById(evaluatorId);
//                        if (korisnik != null) {
//                            m.setImePrezimeEvaluatora(korisnik.getImeKorisnika() + " " + korisnik.getPrezimeKorisnika());
//                        } else {
//                            m.setImePrezimeEvaluatora("Korisnik #" + evaluatorId);
//                        }
//                    } catch (Exception e) {
//                        m.setImePrezimeEvaluatora("Korisnik #" + evaluatorId);
//                    }
//                }
//            }
//        }
//

//    @GetMapping
//    public ResponseEntity<List<Materijal>> getMaterijali(@RequestHeader("Authorization") String authHeader) {
//
//        var currentUser = getCurrentUser(authHeader);
//        List<Materijal> materijali;
//
//        if (currentUser.uloga() == UlogaKorisnika.nastavnik) {
//            materijali = materijalRepozitorijum.findByNastavnikId(currentUser.korisnikId());
//        } else {
//            materijali = materijalRepozitorijum.findAll();
//        }
//
//        // === POPUNI DODATNE PODATKE IZ ISTORIJE (evaluator) ===
//        for (Materijal m : materijali) {
//            IstorijaPromena poslednjaIstorija = istorijaPromenaRepozitorijum
//                    .findFirstByMaterijalIdOrderByVremePromeneDesc(m.getMaterijalId());
//
//            if (poslednjaIstorija != null) {
//                m.setNapomena(poslednjaIstorija.getNapomena());
//                m.setDatumPromene(poslednjaIstorija.getVremePromene());
//
//                // Dobavi ime i prezime evaluatora
//                Integer evaluatorId = poslednjaIstorija.getIzmenioId();
//                if (evaluatorId != null) {
//                    try {
//                        KorisnikDto korisnik = authClient.getKorisnikById(evaluatorId);
//                        if (korisnik != null) {
//                            m.setImePrezimeEvaluatora(korisnik.getImeKorisnika() + " " + korisnik.getPrezimeKorisnika());
//                        } else {
//                            m.setImePrezimeEvaluatora("Korisnik #" + evaluatorId);
//                        }
//                    } catch (Exception e) {
//                        m.setImePrezimeEvaluatora("Korisnik #" + evaluatorId);
//                    }
//                }
//            }
//        }
//
//        // === NOVO: POPUNI PODATKE O NASTAVNICIMA I ŠKOLAMA ===
//        // Učitaj sve korisnike jednom
//        List<KorisnikDto> sviKorisnici = authClient.getSviKorisnici();
//        Map<Integer, KorisnikDto> korisniciMap = sviKorisnici.stream()
//                .collect(Collectors.toMap(KorisnikDto::getKorisnikId, k -> k));
//
//        // Mapiraj podatke o nastavnicima
//        for (Materijal m : materijali) {
//            KorisnikDto nastavnik = korisniciMap.get(m.getNastavnikId());
//            if (nastavnik != null) {
//                m.setImeNastavnika(nastavnik.getImeKorisnika());
//                m.setPrezimeNastavnika(nastavnik.getPrezimeKorisnika());
//                m.setSkolaId(nastavnik.getSkolaId());
//            }
//        }
//        // === KRAJ NOVOG KODA ===
//
//        return ResponseEntity.ok(materijali);
//    }

    @GetMapping
    public ResponseEntity<List<Materijal>> getMaterijali(@RequestHeader("Authorization") String authHeader) {
        var currentUser = getCurrentUser(authHeader);
        List<Materijal> materijali;

        if (currentUser.uloga() == UlogaKorisnika.nastavnik) {
            materijali = materijalRepozitorijum.findByNastavnikId(currentUser.korisnikId());
        } else {
            materijali = materijalRepozitorijum.findAll();
        }

        // === UČITAJ SVE KORISNIKE JEDNOM ===
        List<KorisnikDto> sviKorisnici;
        try {
            sviKorisnici = authClient.getSviKorisnici();
        } catch (Exception e) {
            System.err.println(">>> Greška pri učitavanju korisnika: " + e.getMessage());
            sviKorisnici = new ArrayList<>();
        }

        Map<Integer, KorisnikDto> korisniciMap = sviKorisnici.stream()
                .collect(Collectors.toMap(KorisnikDto::getKorisnikId, k -> k, (existing, replacement) -> existing));

        // === POPUNI PODATKE ZA SVAKI MATERIJAL ===
        for (Materijal m : materijali) {
            // Postojeća logika za evaluatora
            IstorijaPromena poslednjaIstorija = istorijaPromenaRepozitorijum
                    .findFirstByMaterijalIdOrderByVremePromeneDesc(m.getMaterijalId());
            if (poslednjaIstorija != null) {
                m.setNapomena(poslednjaIstorija.getNapomena());
                m.setDatumPromene(poslednjaIstorija.getVremePromene());
                Integer evaluatorId = poslednjaIstorija.getIzmenioId();
                if (evaluatorId != null) {
                    try {
                        KorisnikDto korisnik = authClient.getKorisnikById(evaluatorId);
                        if (korisnik != null) {
                            m.setImePrezimeEvaluatora(korisnik.getImeKorisnika() + " " + korisnik.getPrezimeKorisnika());
                        } else {
                            m.setImePrezimeEvaluatora("Korisnik #" + evaluatorId);
                        }
                    } catch (Exception e) {
                        m.setImePrezimeEvaluatora("Korisnik #" + evaluatorId);
                    }
                }
            }

            // === NOVO: POPUNI PODATKE O NASTAVNIKU ===
            KorisnikDto nastavnik = korisniciMap.get(m.getNastavnikId());
            if (nastavnik != null) {
                m.setImeNastavnika(nastavnik.getImeKorisnika());
                m.setPrezimeNastavnika(nastavnik.getPrezimeKorisnika());
                m.setSkolaId(nastavnik.getSkolaId());
            }
        }

        return ResponseEntity.ok(materijali);
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Integer id,
            @RequestBody StatusUpdateRequest request
    ) {
        System.out.println(">>> Primljen zahtev: id=" + id + ", status=" + request.getStatus() + ", evaluatorId=" + request.getEvaluatorId());

        // 1. Konverzija statusa
        Status noviStatus;
        try {
            noviStatus = Status.valueOf(request.getStatus());
            System.out.println(">>> Status konvertovan: " + noviStatus);
        } catch (Exception e) {
            System.err.println(">>> Greška u statusu: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        // 2. Provera materijala
        Optional<Materijal> materijalOpt = materijalRepozitorijum.findById(id);
        if (materijalOpt.isEmpty()) {
            System.out.println(">>> Materijal nije pronađen");
            return ResponseEntity.notFound().build();
        }

        // 3. Ažuriranje materijala
        try {
            Materijal materijal = materijalOpt.get();
            materijal.setStatus(noviStatus);
            materijal.setPoslednjiIzmenioStatus(request.getEvaluatorId());
            materijalRepozitorijum.save(materijal);
            System.out.println(">>> Materijal ažuriran");
        } catch (Exception e) {
            System.err.println(">>> Greška pri ažuriranju materijala: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }

        // 4. Proveri da li već postoji evaluacija
        Optional<Evaluacija> postojecaEvaluacija = evaluacijaRepozitorijum
                .findByMaterijalIdAndEvaluatorId(id, request.getEvaluatorId());

        Evaluacija evaluacija;
        if (postojecaEvaluacija.isPresent()) {
            // Ažuriraj postojeću
            evaluacija = postojecaEvaluacija.get();
            evaluacija.setStatus(request.getStatus());
            evaluacija.setNapomena(request.getNapomena());
            System.out.println(">>> Evaluacija ažurirana");
        } else {
            // Kreiraj novu
            evaluacija = new Evaluacija();
            evaluacija.setMaterijalId(id);
            evaluacija.setEvaluatorId(request.getEvaluatorId());
            evaluacija.setStatus(request.getStatus());
            evaluacija.setNapomena(request.getNapomena());
            evaluacija.setDatumEvaluacije(LocalDateTime.now());
            System.out.println(">>> Evaluacija kreirana");
        }
        evaluacijaRepozitorijum.save(evaluacija);

        // 5. Unos u istoriju (uvek novi red)
        try {
            IstorijaPromena istorija = new IstorijaPromena();
            istorija.setMaterijalId(id);
            istorija.setStariStatus(String.valueOf(materijalOpt.get().getStatus()));
            istorija.setNoviStatus(request.getStatus());
            istorija.setIzmenioId(request.getEvaluatorId());
            istorija.setNapomena(request.getNapomena());
            istorijaPromenaRepozitorijum.save(istorija);
            System.out.println(">>> Istorija sačuvana");
        } catch (Exception e) {
            System.err.println(">>> Greška u istoriji: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }

        return ResponseEntity.ok().build();
    }
}