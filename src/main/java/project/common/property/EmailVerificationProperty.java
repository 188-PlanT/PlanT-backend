package project.common.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "email-verification")
public class EmailVerificationProperty {

    private final long ExpirationSeconds;
}
