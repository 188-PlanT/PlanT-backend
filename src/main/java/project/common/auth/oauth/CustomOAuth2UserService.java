package project.common.auth.oauth;

import org.springframework.security.oauth2.client.userinfo.*;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import project.domain.User;
import project.exception.user.*;
import project.repository.UserRepository;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Optional;
import java.util.Map;

 
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    
    private final UserRepository userRepository;
    private final HttpSession httpSession;
    
    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        
        //attributes 얻기 위한 코드
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        
        //OAuth2.0 서비스 id
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        
        Map<String, Object> attributes = OAuthAttributes.of(registrationId, oAuth2User);

        User user = saveOrUpdate(attributes);

        return UserInfo.ofOAuth2(user, attributes);
    }
    
    //유저 생성 로직
    private User saveOrUpdate(Map<String, Object> attributes){
        
        Optional<User> findUser = userRepository.findByEmail((String) attributes.get("email"));
        
        if(findUser.isPresent()){
            return findUser.get();
        }
        else{
            User user = User.fromOAuth2Attributes(attributes);
            userRepository.save(user);
            
            return user;
        }
    }
}