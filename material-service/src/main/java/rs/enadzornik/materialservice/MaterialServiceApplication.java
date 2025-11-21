// MaterialServiceApplication.java
package rs.enadzornik.materialservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MaterialServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MaterialServiceApplication.class, args);
    }
}