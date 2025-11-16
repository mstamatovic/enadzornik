package rs.enadzornik.authservice.repository;

import rs.enadzornik.authservice.entity.Korisnik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KorisnikRepozitorijum extends JpaRepository<Korisnik, Integer> {
    Optional<Korisnik> findByEmail(String email);

    boolean existsByEmail(String email);
}