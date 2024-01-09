package project.domain;

import project.exception.user.NoSuchUserException;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import javax.persistence.*;
import static java.util.stream.Collectors.toList;
import lombok.Getter;
import lombok.Builder;

@Entity
@Table(name = "workspaces")
@Getter 
public class Workspace extends BaseEntity{
    
    @Id @GeneratedValue
    @Column(name = "workspace_id")
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String profile;
    
    
    // 연관관계 삭제용
    @OneToMany(mappedBy="workspace", orphanRemoval = true)
    private List<UserWorkspace> userWorkspaces = new ArrayList<>();
    
    @OneToMany(mappedBy="workspace", orphanRemoval = true)
    private List<Schedule> schedules = new ArrayList<>();
    
    
    // <== 생성자 ==>
    
    protected Workspace(){} //JPA용 생성자
    
    @Builder //빌더 패턴용
    public Workspace(String name, String proflie){
        this.name = name;
        this.proflie = proflie;
    }
    
    // <== 정적 팩토리 메서드 ==> //
    public static Workspace ofNameAndUsers(String name, String profile, List<User> users){
        Workspace workspace = Workspace.builder()
                                        .name(name)
                                        .profile(profile);
        
        for(User user : users){
            workspace.addUser(user);
        }
        
        return workspace;
    }
    
    // <== 비즈니스 로직 == > //
    public void addUser(User user){
        
        if (this.hasUser(user)){
            throw new IllegalStateException("이미 존재하는 user 입니다");
        }
        
        UserWorkspace userWorkspace = UserWorkspace.builder()
                                                    .user(user)
                                                    .workspace(this)
                                                    .authority(UserRole.USER);
        
        this.userWorkspaces.add(userWorkspace);
    }
    
    public void removeUser(User user){
        if (!this.hasUser(user)){
            throw new IllegalStateException("존재하지 않는 user 입니다");
        }
        
        this.userWorkspaces.removeIf(userWorkspace -> userWorkspace.getUser().equals(user));
    }
    
    // 수정 로직
    public void updateWorkspace(String name, List<User> users){
        this.name = name;
        this.userWorkspaces.clear();
        
        for(User user : users){
            this.addUser(user);
        }
    }
    
    public boolean hasUser(User user){
        return (this.userWorkspaces.stream()
                                .filter(uw -> uw.getUser().equals(user))
                                .count() == 1);
    }
}