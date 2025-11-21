// rs.enadzornik.materialservice.dto.KorisnikDto
package rs.enadzornik.materialservice.dto;

import rs.enadzornik.materialservice.entity.UlogaKorisnika;

public record KorisnikDto(Integer korisnikId, UlogaKorisnika uloga) {
}