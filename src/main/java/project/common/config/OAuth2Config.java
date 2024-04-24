package project.common.config;

import project.common.security.oauth.OAuthAuthenticationProvider;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OAuth2Config {
    @Bean
    @ConfigurationProperties(prefix = "oauth2")
    OAuthAuthenticationProvider oAuthAuthenticationProvider(){
        return new OAuthAuthenticationProvider();
    }

}