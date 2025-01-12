package project.common.util;

import java.util.Map;
import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;

@Getter
public class Oauth2UrlUtil {
    /**
     * Oauth2 Provider 마다 제공하는 Url을 파싱해주는 유틸 클래스입니다.
     * Map을 이용해 provider 정보를 application.yml에서 읽어옵니다.
     */
    private Map<String, Provider> provider = new HashMap<>();
    
    public String getTokenUrl(String code, String clientName){
        
        Provider client = provider.get(clientName);
        
        if (client == null){
            throw new PlantException(ErrorCode.OAUTH_PROVIDER_NOT_FOUND);
        }
        
        String tokenUrl = makeTokenUrl(code, client);
        
        return tokenUrl;
    }
    
    public String getUserInfoUrl(String clientName){
        
        Provider client = provider.get(clientName);
        
        if (client == null){
            throw new PlantException(ErrorCode.OAUTH_PROVIDER_NOT_FOUND, "client가 올바르지 않습니다");
        }
        
        return client.getUserInfoUri();
    }
    
    private String makeTokenUrl(String code, Provider provider){
        return provider.getTokenUri()
                + "?grant_type=" + "authorization_code"
                + "&code=" + code
                + "&client_id=" + provider.getClientId()
                + "&client_secret=" + provider.getClientSecret()
                + "&redirect_uri=" + provider.getRedirectUri();
        
    }
    
    @Getter @Setter
    static class Provider{
        private String clientId;
        private String clientSecret;
        private String redirectUri;
        private String tokenUri;
        private String userInfoUri;
    }
}