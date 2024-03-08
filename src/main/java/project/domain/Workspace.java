package project.domain;

import project.exception.user.*;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import javax.persistence.*;
import static java.util.stream.Collectors.toList;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
// import lombok.Builder;

@Slf4j
@Entity
@Table(name = "workspaces")
@Getter 
public class Workspace extends BaseEntity{
    
    @Id @GeneratedValue
    @Column(name = "workspace_id")
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image profile;
    
    
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
    //유저 추가
    public void addUser(User user){
        
        if (this.hasUser(user)){
            throw new UserAlreadyExistException();
        }
        
        UserWorkspace userWorkspace = UserWorkspace.builder()
                                                    .user(user)
                                                    .workspace(this)
                                                    .userRole(UserRole.PENDING)
                                                    .build();
        
        this.userWorkspaces.add(userWorkspace);
    }
    
    //유저 목록 추가
    public void addUserByList(List<User> userList){
        for (User user : userList){
            this.addUser(user);
        }
    }
    
    // 유저 삭제
    public void removeUser(User user){
        if (!this.hasUser(user)){
            throw new NoSuchUserException();
        }
        
        this.userWorkspaces.removeIf(uw -> uw.getUser().equals(user));
    }
    
    public void checkAdmin(Long userId){
        for (UserWorkspace uw : this.userWorkspaces){
            if (uw.getUser().getId() == userId && uw.getUserRole().equals(UserRole.ADMIN)){
                return;
            }
        }
        
        throw new InvalidAuthorityException();
    }
	
	public void checkAdmin(User user){
        return;
    }
    
    public void checkUser(User user){
        if (!this.hasUser(user)){
            throw new NoSuchUserException();
        }
        
        //여기 로직 수정 필요함
        for (UserWorkspace uw : this.userWorkspaces){
            if (uw.getUser().equals(user)){
                if (uw.getUserRole().equals(UserRole.ADMIN) || uw.getUserRole().equals(UserRole.USER)){
                    return;
                }
            }
        }
        throw new InvalidAuthorityException();
    }
    
    // 유저 권한 변경
    public void giveAuthority(User user, UserRole userRole){
        if (!this.hasUser(user)){
            throw new NoSuchUserException();
        }
        
        this.userWorkspaces.stream()
                            .filter(uw -> uw.getUser().equals(user))
                            .forEach(uw -> uw.setUserRole(userRole));
    }
    
    // 수정 로직
    public void updateWorkspace(String name, Image profile){
        this.name = name;
        this.profile = profile;
    }
    
    
    // 유저 검증
    public boolean hasUser(User user){
        return (this.userWorkspaces.stream()
                                    .filter(uw -> uw.getUser().equals(user))
                                    .count() == 1);
    }
    
    // <=== Builder 구현 ===>
    public static Builder builder(){
        return new Builder();
    }
    
    public static class Builder{
        private String name;
        private Image profile;
        private User user;

        
        public Builder name(String name){
            this.name = name;
            return this;
        }
        
        public Builder profile(Image profile){
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