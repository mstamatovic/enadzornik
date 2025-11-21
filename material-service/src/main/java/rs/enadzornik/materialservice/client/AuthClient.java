// AuthClient.java
package rs.enadzornik.materialservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import rs.enadzornik.materialservice.dto.KorisnikDto;

@FeignClient(name = "auth-service", url = "http://localhost:8083")
public interface AuthClient {
    @GetMapping("/api/v1/auth/internal/korisnik/{id}")
    KorisnikDto getKorisnikById(@PathVariable("id") Integer id);
}
