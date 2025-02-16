package project.common.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.Map;

@AllArgsConstructor
@ConfigurationProperties(prefix = "oauth2")
public class Oauth2Property {

    /**
     * Oauth2 Provider 마다 제공하는 Url을 파싱해주는 유틸 클래스입니다.
     * Map을 이용해 provider 정보를 application.yml에서 읽어옵니다.
     */
    private final Map<String, Provider> provider;

    public Provider getProvider(String name){
        return provider.get(name);
    }

    @Getter
    @AllArgsConstructor
    public static class Provider{
        private final String clientId;
        private final String clientSecret;
        private final String redirectUri;
        private final String tokenUri;
        private final String userInfoUri;
    }
}
