// auth-service/src/main/java/rs/enadzornik/authservice/dto/KorisnikDto.java
package rs.enadzornik.authservice.dto;

import lombok.Data;
import rs.enadzornik.authservice.entity.UlogaKorisnika;

@Data
public class KorisnikDto {
    private Integer korisnikId;
    private UlogaKorisnika uloga;
    private String imeKorisnika;
    private String prezimeKorisnika;
    private String email; // opcionalno
}