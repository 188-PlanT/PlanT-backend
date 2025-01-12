package project.domain.auth.service;

import project.common.util.OAuth2AttributeUtil;
import project.common.util.Oauth2UrlUtil;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService{
    private final Oauth2UrlUtil oauth2UrlUtil;
    private final RestTemplate restTemplate;

    public String getOauth2UserEmailByAuthCode(String authCode, String provider){
        authCode = URLDecoder.decode(authCode, StandardCharsets.UTF_8);

        String providerAccessToken = getProviderAccessTokenByCode(authCode, provider);

        return getUserEmail(providerAccessToken, provider);
    }
    
    private String getProviderAccessTokenByCode(String code, String provider){
        
        String tokenUrl = oauth2UrlUtil.getTokenUrl(code, provider);
        
        URI uri = UriComponentsBuilder
                    .fromUriString(tokenUrl)
                    .path("")
                    .encode()
                    .build()
                    .toUri();

        TokenResponse response = restTemplate.postForObject(uri, null, TokenResponse.class);
        
        return response.getAccessToken();
    }
    
    private String getUserEmail(String accessToken, String provider){
        
        String userInfoUrl = oauth2UrlUtil.getUserInfoUrl(provider);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity request = new HttpEntity(headers);

        Map<String, Object> attributes = (Map<String, Object>) restTemplate.exchange(
              userInfoUrl,
              HttpMethod.GET,
              request,
              Object.class
            ).getBody();
        
        return OAuth2AttributeUtil.getEmail(provider, attributes);
    }
    
    @Setter
    static class TokenResponse{
        String access_token;
        
        public String getAccessToken(){
            return access_token;
        }
    }
}