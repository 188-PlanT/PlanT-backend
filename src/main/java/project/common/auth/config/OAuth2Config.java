package project.common.auth.config;

import project.common.auth.oauth.OAuthAuthenticationProvider;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// @EnableConfigurationProperties(OAuthProvider.class)
public class OAuth2Config {
    @Bean
    @ConfigurationProperties(prefix = "oauth2")
    OAuthAuthenticationProvider oAuthAuthenticationProvider(){
        return new OAuthAuthenticationProvider();
    }

}