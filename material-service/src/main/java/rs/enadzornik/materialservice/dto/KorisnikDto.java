// rs.enadzornik.authservice.dto.KorisnikDto
package rs.enadzornik.materialservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import rs.enadzornik.materialservice.entity.UlogaKorisnika;

@Data
public class KorisnikDto {
    private Integer korisnikId;
    private UlogaKorisnika uloga;
    @JsonProperty("imeKorisnika")
    private String imeKorisnika;
    @JsonProperty("prezimeKorisnika")
    private String prezimeKorisnika;
    private String email;
}