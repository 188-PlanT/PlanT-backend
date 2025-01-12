package project.common.util;

import lombok.Getter;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class OAuth2AttributeUtil {

    /**
     * Oauth2 Provider 서버에서 응답해준 Attribute(유저 정보)에서 이메일을 파싱해주는 유틸 클래스입니다.
     *
     * @param registrationId : Provider 서버 이름
     * @param attributes : 응답 결과 Json을 Map 형태로 역직렬화한 내용
     */
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

    /**
     * 각 Oauth2 서버마다 다른 응답 양식에 맞춰 email을 꺼내옵니다
     */
    private static String ofGoogle(Map<String, Object> attributes){
        return (String) attributes.get("email");
    }

    private static String ofNaver(Map<String, Object> attributes){
        
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        
        return (String) response.get("email");
    }
}