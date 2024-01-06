package project.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Swagger springdoc-ui 구성 파일
 */
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("도마잎 API Document")
                .version("v0.0.1")
                .description("API 명세서입니다.");
        
        //-------------------- 인가 방식 지정 ---------------------
        // SecurityScheme auth = new SecurityScheme()
        //   .type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.COOKIE).name("JSESSIONID");
        // SecurityRequirement securityRequirement = new SecurityRequirement().addList("basicAuth");

        // return new OpenAPI()
        //   .components(new Components().addSecuritySchemes("basicAuth", auth))
        //   .addSecurityItem(securityRequirement)
        //   .info(info);
        
        SecurityScheme securityScheme = new SecurityScheme()
            .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
            .in(SecurityScheme.In.HEADER).name("Authorization");
        
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");
        
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .addSecurityItem(securityRequirement)
                .info(info);
    }
}