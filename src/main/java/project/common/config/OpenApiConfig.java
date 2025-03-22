package project.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.common.util.UrlUtil;

/**
 * Swagger springdoc-ui 구성 파일
 */
@Configuration
@RequiredArgsConstructor
public class OpenApiConfig {

    private final UrlUtil urlUtil;

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info().title("도마잎 API Document").version("v0.0.1").description("API 명세서입니다.");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
                .servers(getSwaggerServers())
                .components(getSwaggerSecurityComponents())
                .addSecurityItem(securityRequirement)
                .info(info);
    }

    private List<Server> getSwaggerServers() {
        Server server = new Server().url(urlUtil.getApiUrl());
        return List.of(server);
    }

    private Components getSwaggerSecurityComponents() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        return new Components().addSecuritySchemes("bearerAuth", securityScheme);
    }
}
