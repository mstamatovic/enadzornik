package rs.enadzornik.materialservice.repository;

import rs.enadzornik.materialservice.entity.Materijal;
import rs.enadzornik.materialservice.entity.Status;
import rs.enadzornik.materialservice.entity.TipMaterijala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterijalRepozitorijum extends JpaRepository<Materijal, Integer> {
    List<Materijal> findByNastavnikId(Integer nastavnikId);

    List<Materijal> findByNastavnikIdAndStatus(Long nastavnikId, Status status);

    List<Materijal> findByStatus(Status status);

    List<Materijal> findByTipMaterijala(TipMaterijala tip);
}
