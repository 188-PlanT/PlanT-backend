package project.common.auth.oauth;

import project.domain.*;
    
import lombok.Getter;
import lombok.Builder;
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
    
    private String username;
    private String password;
    private Map<String, Object> attributes;
    // private Collection<? extends GrantedAuthority> authorities;
    private String authority;
    
    @Builder
    public UserInfo(String username, String password, String authority, Map<String, Object> attributes){
        this.username = username;
        this.password = password;
        this.authority = authority;
        this.attributes = attributes;
    }
    
    
    public static UserInfo ofOAuth2(User user, Map<String, Object> attributes){
        return UserInfo.builder()
                        .username(user.getEmail()) 
                        .password(user.getPassword()) 
                        .authority(user.getRoleKey())
                        .attributes(attributes)                                 
                        .build();
    }
    
    public static UserInfo from(User user){
        return UserInfo.builder()
                        .username(user.getEmail()) 
                        .password(user.getPassword()) 
                        .authority(user.getRoleKey())
                        .build();
    }
    
    //<== UserDetails ==>//
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return Collections.singleton(new SimpleGrantedAuthority(this.authority));
    }
    
    @Override
    public String getPassword(){
        return this.password;
    }
    
    @Override
    public String getUsername(){
        return this.username;
    }
    
    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }
    @Override
    public boolean isEnabled(){
        return true;
    }
    
    //<== Oauth2User ==>
    @Override
    public String getName(){
        return this.username;
    }
    @Override
    public Map<String, Object> getAttributes() {
        if (this.attributes == null){
            throw new IllegalStateException("OAuth2User입니다");
        }
        return this.attributes;
    }
}