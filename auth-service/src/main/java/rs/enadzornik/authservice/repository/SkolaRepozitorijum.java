package rs.enadzornik.authservice.repository;

import rs.enadzornik.authservice.entity.Skola;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkolaRepozitorijum extends JpaRepository<Skola, Integer> {
}
