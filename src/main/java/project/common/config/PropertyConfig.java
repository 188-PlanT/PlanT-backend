package project.common.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import project.common.property.*;

@EnableConfigurationProperties({
    JwtProperty.class,
    Oauth2Property.class,
    RedisProperty.class,
    EmailVerificationProperty.class,
    BasicAuthProperty.class
})
@Configuration
public class PropertyConfig {}
