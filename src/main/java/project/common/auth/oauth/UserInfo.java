package project.common.auth.oauth;

import project.domain.*;
    
import lombok.Getter;
import lombok.Builder;
import java.util.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

//OAuth2 유저 반환을 위한 프록시 객체
@Getter
public class UserInfo implements OAuth2User, UserDetails{

    private User user;
    private Map<String, Object> attributes;

    private UserInfo(User user, Map<String, Object> attributes){
        this.user = user;
        this.attributes = attributes;
    }
    
    public static UserInfo ofOAuth2(User user, Map<String, Object> attributes){
        return new UserInfo(user, attributes);
    }
    
    public static UserInfo from(User user){
        return new UserInfo(user, null);
    }
    
    //<== UserDetails ==>//
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey()));
    }

    public String getPassword(){
        return user.getPassword();
    }

    public String getUsername(){
        return user.getName();
    }

    public boolean isAccountNonExpired(){
        return true;
    }

    public boolean isAccountNonLocked(){
        return true;
    }

    public boolean isCredentialsNonExpired(){
        return true;
    }

    public boolean isEnabled(){
        return true;
    }
    
    //<== Oauth2User ==>
    public String getName(){
        return user.getName();
    }
    
    public Map<String, Object> getAttributes() {
        if (this.attributes == null){
            throw new IllegalStateException("OAuth2User입니다");
        }
        return this.attributes;
    }
}