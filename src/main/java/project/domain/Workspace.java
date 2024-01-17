package project.domain;

import project.exception.user.NoSuchUserException;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import javax.persistence.*;
import static java.util.stream.Collectors.toList;
import lombok.Getter;
// import lombok.Builder;

@Entity
@Table(name = "workspaces")
@Getter 
public class Workspace extends BaseEntity{
    
    private static final String DEFAULT_PROFILE_URL = "";
    
    @Id @GeneratedValue
    @Column(name = "workspace_id")
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String profile;
    
    
    // Workspace가 UserWorkspace 영속성 관리
    @OneToMany(mappedBy="workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserWorkspace> userWorkspaces = new ArrayList<>();
    
    // 연관관계 삭제용
    @OneToMany(mappedBy="workspace", orphanRemoval = true)
    private List<Schedule> schedules = new ArrayList<>();
    
    
    // <== 생성자 ==>
    protected Workspace(){} //JPA용 생성자
    
    //@Builder //빌더 패턴용
    private Workspace(Builder builder){
        this.name = builder.name;
        this.profile = builder.profile;
    }
    
    // <== 비즈니스 로직 == > //
    public void addUser(User user){
        
        if (this.hasUser(user)){
            throw new IllegalStateException("이미 존재하는 user 입니다");
        }
        
        UserWorkspace userWorkspace = UserWorkspace.builder()
                                                    .user(user)
                                                    .workspace(this)
                                                    .userRole(UserRole.PENDING)
                                                    .build();
        
        this.userWorkspaces.add(userWorkspace);
    }
    
    public void removeUser(User user){
        if (!this.hasUser(user)){
            throw new IllegalStateException("존재하지 않는 user 입니다");
        }
        
        this.userWorkspaces.removeIf(uw -> uw.getUser().equals(user));
    }
    
    public void giveAuthority(User user, UserRole userRole){
        if (!this.hasUser(user)){
            throw new IllegalStateException("존재하지 않는 user 입니다");
        }
        
        this.userWorkspaces.stream()
                            .filter(uw -> uw.getUser().equals(user))
                            .forEach(uw -> uw.setUserRole(userRole));
    }

    
    // 수정 로직
    public void updateWorkspace(String name, String profile){
        this.name = name;
        this.profile = profile;
    }
    
    public boolean hasUser(User user){
        return (this.userWorkspaces.stream()
                                    .filter(uw -> uw.getUser().equals(user))
                                    .count() == 1);
        // return this.userWorkspaces.contains(user);
    }
    
    
    public static Builder builder(){
        return new Builder();
    }
    
    // <=== Builder 구현 ===>
    public static class Builder{
        private String name;
        private String profile = Workspace.DEFAULT_PROFILE_URL;
        private User user;

        
        public Builder name(String name){
            this.name = name;
            return this;
        }
        
        public Builder profile(String profile){
            this.profile = profile;
            return this;
        }
        
        public Builder user(User user){
            this.user = user;
            return this;
        }
        
        public Workspace build(){
            Workspace workspace = new Workspace(this);
                    
            workspace.addUser(this.user);
            workspace.giveAuthority(this.user, UserRole.ADMIN);
            
            return workspace;
        }
    }
}