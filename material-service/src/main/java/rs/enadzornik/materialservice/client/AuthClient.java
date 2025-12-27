// AuthClient.java
package rs.enadzornik.materialservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import rs.enadzornik.materialservice.dto.KorisnikDto;

import java.util.List;

//@FeignClient(name = "auth-service", url = "http://localhost:8083")
@FeignClient(name = "auth-service", url = "${auth-service.url:http://auth-service:8083}")
public interface AuthClient {
    @GetMapping("/api/v1/auth/internal/korisnik/{id}")
    KorisnikDto getKorisnikById(@PathVariable("id") Integer id);

    @GetMapping("/api/v1/auth/korisnici")
    List<KorisnikDto> getSviKorisnici();
}
