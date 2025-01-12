package project.domain.auth.domain;

import lombok.Getter;
import lombok.Builder;

import java.util.*;
import java.util.stream.Collectors;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import project.domain.user.domain.User;
import project.domain.user.domain.UserRole;

@Getter
public class UserInfo implements UserDetails {

    private final Long userId;
    private final String username;
    private final String password;
    private final String authority;
    private final List<Long> workspaceAdminIds;
    private final List<Long> workspaceUserIds;

    @Builder
    public UserInfo(Long userId, String username, String authority, String password, List<Long> workspaceAdminIds,List<Long> workspaceUserIds){
        this.userId = userId;
        this.username = username;
        this.authority = authority;
        this.password = password;
        this.workspaceAdminIds = workspaceAdminIds;
        this.workspaceUserIds = workspaceUserIds;
    }

    public static UserInfo from(User user){
        return UserInfo.builder()
                .userId(user.getId())
                .password(user.getPassword())
                .username(user.getEmail())
                .authority(user.getRoleKey())
                .workspaceAdminIds(getWorkspaceIds(user, UserRole.ADMIN))
                .workspaceUserIds(getWorkspaceIds(user, UserRole.USER))
                .build();
    }

    private static List<Long> getWorkspaceIds(User user, UserRole userRole){
        return user.getUserWorkspaces().stream()
                .filter(uw -> uw.getUserRole().equals(userRole))
                .map(uw -> uw.getWorkspace().getId())
                .collect(Collectors.toList());
    }

    public static UserInfo from(Claims claims){
        String userId =  (String)claims.get("userId");

        String email = (String)claims.get("email");

        String authority = (String)claims.get("authorities");

        List<Long> workspaceAdminIds = ((List<Integer>) claims.get("workspaceAdminIds"))
                .stream().map(Integer::longValue).collect(Collectors.toList());

        List<Long> workspaceUserIds = ((List<Integer>) claims.get("workspaceUserIds"))
                .stream().map(Integer::longValue).collect(Collectors.toList());

        return UserInfo.builder()
                .userId(Long.parseLong(userId))
                .username(email)
                .authority(authority)
                .workspaceAdminIds(workspaceAdminIds)
                .workspaceUserIds(workspaceUserIds)
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
