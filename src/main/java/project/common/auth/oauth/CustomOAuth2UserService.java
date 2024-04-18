package project.common.auth.oauth;

import project.domain.User;
import project.domain.Image;
import project.exception.ErrorCode;
import project.exception.PlantException;
import project.repository.UserRepository;
import project.repository.ImageRepository;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.util.Collections;
import java.util.Optional;
import java.util.Map;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Value;


 
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService{
    
    private final UserRepository userRepository;
    private final OAuthAuthenticationProvider oAuthAuthenticationProvider;
    private final RestTemplate restTemplate;
    private final ImageRepository imageRepository;
    
    @Value("${s3.default-image-url.user}")
    private String DEFAULT_USER_IMAGE_URL;
    
    // OAuth2 유저 생성, 조회
    public User getOAuth2User(String code, String provider){
        
        String decode = URLDecoder.decode(code, StandardCharsets.UTF_8);
        
        String accessToken = getAccessToken(decode, provider);
        
        String userEmail = getUserEmail(accessToken, provider);
        
        User user = saveOrUpdate(userEmail);
        
        return user;
    }
    
    private String getAccessToken(String code, String provider){
        
        String tokenUrl = oAuthAuthenticationProvider.getTokenUrl(code, provider);
        
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
        
        String userInfoUrl = oAuthAuthenticationProvider.getUserInfoUrl(provider);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity request = new HttpEntity(headers);

        Map<String, Object> attributes = (Map<String, Object>) restTemplate.exchange(
              userInfoUrl,
              HttpMethod.GET,
              request,
              Object.class
            ).getBody();
        
        return OAuth2AttributeHelper.getEmail(provider, attributes);
    }
    
    //유저 생성 로직
    private User saveOrUpdate(String email){
        
        Optional<User> findUser = userRepository.findByEmail(email);
        
        if(findUser.isPresent()){
            return findUser.get();
        }
        else{
            
            Image defaultUserProfile = imageRepository.findByUrl(DEFAULT_USER_IMAGE_URL)
                    .orElseThrow(() -> new PlantException(ErrorCode.IMAGE_NOT_FOUND));
            
            User user = User.fromOAuth2Attributes(email, defaultUserProfile);
            userRepository.save(user);
            
            return user;
        }
    }
    
    @Setter
    static class TokenResponse{
        String access_token;
        
        public String getAccessToken(){
            return access_token;
        }
    }
}