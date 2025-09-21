package project.domain.workspaceUser.domain;

import jakarta.persistence.*;
import lombok.Getter;
import project.domain.common.BaseEntity;
import project.domain.user.domain.User;
import project.domain.workspace.domain.Workspace;

@Getter
@Entity
@Table(
        name = "workspace_user",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "workspace_user_unique",
                    columnNames = {"workspace_id", "user_id"})
        })
public class WorkspaceUser extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "workspace_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private WorkspaceUserRole role;

    protected WorkspaceUser() {} // JPA용 생성자

    private WorkspaceUser(Workspace workspace, User user, WorkspaceUserRole role) { // 기본 생성자
        this.workspace = workspace;
        this.user = user;
        this.role = role;
    }

    public static WorkspaceUser createUser(Workspace workspace, User user) {
        return new WorkspaceUser(workspace, user, WorkspaceUserRole.USER);
    }

    public static WorkspaceUser createAdmin(Workspace workspace, User user) {
        return new WorkspaceUser(workspace, user, WorkspaceUserRole.ADMIN);
    }

    public void updateRole(WorkspaceUserRole role) {
        this.role = role;
    }
}
