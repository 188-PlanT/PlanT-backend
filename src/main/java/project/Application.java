package project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;

@EntityScan(
    basePackageClasses = {Jsr310JpaConverters.class},
	basePackages = {"project"})
@SpringBootApplication
// @OpenAPIDefinition(servers = {@Server(url = "https://blazingdevs-calendar-ubvam.run.goorm.site", description = "Default url")})
// @OpenAPIDefinition(servers = {@Server(url = "https://blazingdevs-calendar-ubvam.run.goorm.site", description = "Default url")})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}