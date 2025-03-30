package project.common.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "basic-auth")
public class BasicAuthProperty {

    private final String username;
    private final String password;
}
