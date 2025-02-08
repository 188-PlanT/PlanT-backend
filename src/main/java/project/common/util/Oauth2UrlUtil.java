package project.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;
import project.common.property.Oauth2Property;

@Component
@RequiredArgsConstructor
public class Oauth2UrlUtil {

    private final Oauth2Property oauth2Property;
    
    public String getTokenUrl(String code, String clientName){
        
        Oauth2Property.Provider client = oauth2Property.getProvider(clientName);
        
        if (client == null){
            throw new PlantException(ErrorCode.OAUTH_PROVIDER_NOT_FOUND);
        }
        
        String tokenUrl = makeTokenUrl(code, client);
        
        return tokenUrl;
    }
    
    public String getUserInfoUrl(String clientName){
        
        Oauth2Property.Provider client = oauth2Property.getProvider(clientName);
        
        if (client == null){
            throw new PlantException(ErrorCode.OAUTH_PROVIDER_NOT_FOUND, "client가 올바르지 않습니다");
        }
        
        return client.getUserInfoUri();
    }
    
    private String makeTokenUrl(String code, Oauth2Property.Provider provider){
        return provider.getTokenUri()
                + "?grant_type=" + "authorization_code"
                + "&code=" + code
                + "&client_id=" + provider.getClientId()
                + "&client_secret=" + provider.getClientSecret()
                + "&redirect_uri=" + provider.getRedirectUri();
        
    }
}