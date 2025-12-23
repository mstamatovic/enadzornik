package rs.enadzornik.materialservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.enadzornik.materialservice.entity.IstorijaPromena;

public interface IstorijaPromenaRepozitorijum extends JpaRepository<IstorijaPromena, Long> {
}
