package project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication
@OpenAPIDefinition(servers = {@Server(url = "https://blazingdevs-calendar-ubvam.run.goorm.site", description = "Default url")})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}