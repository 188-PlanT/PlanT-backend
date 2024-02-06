package project.common.auth.oauth;

import lombok.Getter;
import lombok.Builder;
import java.util.Map;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class OAuth2AttributeHelper{
    
    public static String getEmail(String registrationId, Map<String, Object> attributes){
        
        switch(registrationId){
            case "google":
                return ofGoogle(attributes);
                
            case "naver":
                return ofNaver(attributes);
            
            default:
                throw new IllegalStateException("unvalid oauth2 login");
        }
    }
    
    private static String ofGoogle(Map<String, Object> attributes){
        return (String) attributes.get("email");
    }

    private static String ofNaver(Map<String, Object> attributes){
        
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        
        return (String) response.get("email");
    }
}