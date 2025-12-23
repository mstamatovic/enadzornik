package rs.enadzornik.materialservice.dto;

import lombok.Data;

@Data
public class StatusUpdateRequest {
    private String status;      // "potvrdjen" ili "odbijen"
    private String napomena;
    private Integer evaluatorId;
}
