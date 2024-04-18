package project.common.security.oauth;

import lombok.Getter;
import lombok.Builder;

import java.util.*;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import project.domain.user.domain.User;

@Getter
public class UserInfo implements UserDetails {
    
	private Long userId;
    private String username;
    private String password;
    private String authority;
    
    @Builder
    public UserInfo(Long userId, String username, String authority, String password){
		this.userId = userId;
        this.username = username;
        this.authority = authority;
		this.password = password;
    }
    
    public static UserInfo from(User user){
        return UserInfo.builder()
    					.userId(user.getId())
						.password(user.getPassword())
                        .username(user.getEmail())
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
}