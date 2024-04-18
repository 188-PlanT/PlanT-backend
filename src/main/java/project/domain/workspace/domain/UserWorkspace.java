package project.domain.workspace.domain;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import project.domain.BaseEntity;
import project.domain.user.domain.User;
import project.domain.user.domain.UserRole;
import project.domain.workspace.domain.Workspace;

@Entity
@Table(name = "user_workspace")
@Getter
@Setter
public class UserWorkspace extends BaseEntity {
    
    @Id @GeneratedValue
    @Column(name = "user_workspace_id")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;
    
    // <== 생성자 ==>
    
    //JPA용 생성자
    protected UserWorkspace () {} 
    
    @Builder //빌더 패턴 사용
    public UserWorkspace(User user, Workspace workspace, UserRole userRole){
        this.user = user;
        this.workspace = workspace;
        this.userRole = userRole;
    }
}