package project.common.config;

import project.common.util.Oauth2UrlUtil;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OAuth2Config {
    @Bean
    @ConfigurationProperties(prefix = "oauth2")
    Oauth2UrlUtil oAuthAuthenticationProvider(){
        return new Oauth2UrlUtil();
    }

}