// package project.common.auth.oauth;

// import lombok.Getter;
// import lombok.Builder;
// import project.domain.User;
// import project.domain.UserRole;
// import java.util.Map;
// import java.util.HashMap;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.security.oauth2.core.user.OAuth2User;

// @Getter
// @Slf4j
// public class OAuthAttributes{
    
//     public static Map<String, Object> of(String registrationId, OAuth2User oauth2User){
        
//         switch(registrationId){
//             case "google":
//                 return ofGoogle(oauth2User);
                
//             case "naver":
//                 return ofNaver(oauth2User);
            
//             default:
//                 throw new IllegalStateException("unvalid oauth2 login");
//         }
//     }
    
//     private static Map<String, Object> ofGoogle(OAuth2User oauth2User){
//         return oauth2User.getAttributes();
//     }

//     private static Map<String, Object> ofNaver(OAuth2User oauth2User){
        
//         return (Map<String, Object>) oauth2User.getAttributes().get("response");
//     }
// }