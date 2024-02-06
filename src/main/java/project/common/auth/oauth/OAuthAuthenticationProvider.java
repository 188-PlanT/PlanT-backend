package project.common.auth.oauth;

import project.exception.auth.*;

import java.util.Map;
import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;

@Getter
public class OAuthAuthenticationProvider {
    
    private Map<String, Provider> provider = new HashMap<>();
    
    public String getTokenUrl(String code, String clientName){
        
        Provider client = provider.get(clientName);
        
        if (client == null){
            throw new NoSuchProviderException("client가 올바르지 않습니다");
        }
        
        String tokenUrl = makeTokenUrl(code, client);
        
        return tokenUrl;
    }
    
    public String getUserInfoUrl(String clientName){
        
        Provider client = provider.get(clientName);
        
        if (client == null){
            throw new NoSuchProviderException("client가 올바르지 않습니다");
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