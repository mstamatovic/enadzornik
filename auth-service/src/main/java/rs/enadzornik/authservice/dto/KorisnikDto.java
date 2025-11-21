// rs.enadzornik.authservice.dto.KorisnikDto
package rs.enadzornik.authservice.dto;

import rs.enadzornik.authservice.entity.UlogaKorisnika;

public record KorisnikDto(Integer korisnikId, UlogaKorisnika uloga) {
}