package rs.enadzornik.evaluationservice.dto;

import lombok.Data;

@Data
public class EvaluationRequest {
    private Integer materijalId;
    private String status; // "potvrdjen" ili "odbijen"
    private String napomena;
    private Integer evaluatorId;
}
