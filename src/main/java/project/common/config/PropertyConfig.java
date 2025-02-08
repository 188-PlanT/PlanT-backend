package project.common.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import project.common.property.JwtProperty;
import project.common.property.Oauth2Property;
import project.common.property.RedisProperty;

@EnableConfigurationProperties({
        JwtProperty.class,
        Oauth2Property.class,
        RedisProperty.class
})
@Configuration
public class PropertyConfig {}