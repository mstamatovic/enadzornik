package rs.enadzornik.evaluationservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import rs.enadzornik.evaluationservice.dto.*;

@RestController
@RequestMapping("/api/v1/evaluation")
public class EvaluationController {

    private final RestTemplate restTemplate;
    private final String materialServiceUrl;
    private final String auditServiceUrl;

    public EvaluationController(RestTemplate restTemplate,
                                @Value("${material-service.url}") String materialServiceUrl) {
        this.restTemplate = restTemplate;
        this.materialServiceUrl = materialServiceUrl;
        this.auditServiceUrl = "http://audit-service:8086"; // fiksno
    }

    @PostMapping
    public ResponseEntity<Void> evaluate(@RequestBody EvaluationRequest request) {
        try {
            StatusUpdateRequest statusRequest = new StatusUpdateRequest();
            statusRequest.setStatus(request.getStatus());
            statusRequest.setNapomena(request.getNapomena());
            statusRequest.setEvaluatorId(request.getEvaluatorId());

            ResponseEntity<Void> response = restTemplate.postForEntity(
                    materialServiceUrl + "/api/v1/material/" + request.getMaterijalId() + "/status",
                    statusRequest,
                    Void.class
            );

            // KLJUČNA PROVERA:
            if (response.getStatusCode().is4xxClientError()) {
                System.err.println("Client error from material-service: " + response.getStatusCode());
                return ResponseEntity.badRequest().build(); // 400
            }
            if (response.getStatusCode().is5xxServerError()) {
                System.err.println("Server error from material-service: " + response.getStatusCode());
                return ResponseEntity.status(502).build(); // Bad Gateway
            }

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
