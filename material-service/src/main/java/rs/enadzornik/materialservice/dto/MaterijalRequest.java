// rs.enadzornik.materialservice.dto.MaterijalRequest
package rs.enadzornik.materialservice.dto;

import jakarta.validation.constraints.*;
import rs.enadzornik.materialservice.entity.TipMaterijala;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MaterijalRequest {

    @NotBlank
    private String naslov;

    @NotNull
    private TipMaterijala tipMaterijala;

    @NotBlank
    private String format; // "pdf" ili "mp4"

    @NotBlank
    private String putanjaFajla; // dobija se od file-service

    @Positive
    private Double velicinaMb;

    @NotBlank
    private String predmet;

    @NotBlank
    private String razred;

    private String nastavnaJedinica;

    private LocalDate datumCasa;        // za pripreme i video
    private LocalDate mesecPlaniranja;  // za mesečne planove

    // getters & setters
    // (ili koristi Lombok @Data)
}