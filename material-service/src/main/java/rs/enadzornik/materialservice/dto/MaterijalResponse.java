// rs.enadzornik.materialservice.dto.MaterijalResponse
package rs.enadzornik.materialservice.dto;

import rs.enadzornik.materialservice.entity.TipMaterijala;
import rs.enadzornik.materialservice.entity.Status;

import java.time.LocalDateTime;
import java.time.LocalDate;

public class MaterijalResponse {

    private Integer materijalId;
    private String naslov;
    private TipMaterijala tipMaterijala;
    private String format;
    private Double velicinaMb;
    private LocalDateTime datumUploada;
    private Integer nastavnikId;
    private String predmet;
    private String razred;
    private String nastavnaJedinica;
    private LocalDate datumCasa;
    private LocalDate mesecPlaniranja;
    private Status status;

    // Možeš dodati i ime nastavnika (ali za to treba join sa auth-service)
    // private String nastavnikImePrezime;

    // getters & setters
}