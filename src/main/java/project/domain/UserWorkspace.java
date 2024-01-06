package project.domain;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_workspace")
@Getter @Setter
@NoArgsConstructor
public class UserWorkspace extends BaseEntity{
    
    @Id @GeneratedValue
    @Column(name = "user_workspace_id")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    public UserWorkspace(User user, Workspace workspace){
        this.user = user;
        this.workspace = workspace;
    }
    
    //AccounrId Field 추가하기
}