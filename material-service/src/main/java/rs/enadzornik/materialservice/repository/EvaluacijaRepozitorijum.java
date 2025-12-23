package rs.enadzornik.materialservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.enadzornik.materialservice.entity.Evaluacija;

import java.util.Optional;

public interface EvaluacijaRepozitorijum extends JpaRepository<Evaluacija, Integer> {
    Optional<Evaluacija> findByMaterijalIdAndEvaluatorId(Integer materijalId, Integer evaluatorId);

}
