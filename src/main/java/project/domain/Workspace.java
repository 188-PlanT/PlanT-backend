package project.domain;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import javax.persistence.*;
import static java.util.stream.Collectors.toList;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "workspaces")
@Getter @Setter
@NoArgsConstructor
public class Workspace extends BaseEntity{
    
    @Id @GeneratedValue
    @Column(name = "workspace_id")
    private Long id;
    
    @Column(unique = true)
    private String name;
    
    @OneToMany(mappedBy="workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserWorkspace> userWorkspaces = new ArrayList<>();
    
    @OneToMany(mappedBy="workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> schedules = new ArrayList<>();
    
    public Workspace(String name, List<User> users){
        this.name = name;
        
        for(User user : users){
            this.addUser(user);
        }
    }
    
    // <== 비즈니스 로직 == > //
    
    public void addUser(User user){
        if (this.hasUser(user)){
            throw new IllegalStateException("이미 존재하는 user 입니다");
        }
        
        UserWorkspace userWorkspace = new UserWorkspace(user,this);
        
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