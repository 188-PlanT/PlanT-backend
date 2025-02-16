package project.common.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisProperty {

    private final String host;
    private final int port;
}
