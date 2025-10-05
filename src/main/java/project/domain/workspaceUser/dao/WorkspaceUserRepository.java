package project.domain.workspaceUser.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import project.domain.user.domain.User;
import project.domain.workspace.domain.Workspace;
import project.domain.workspaceUser.domain.WorkspaceUser;

public interface WorkspaceUserRepository extends JpaRepository<WorkspaceUser, Long>, WorkspaceUserCustomRepository {

    List<WorkspaceUser> findAllByWorkspace(Workspace workspace);

    List<WorkspaceUser> findAllByWorkspaceId(Long workspaceId);

    Optional<WorkspaceUser> findByWorkspaceIdAndUserId(Long workspaceId, Long userId);

    boolean existsByWorkspaceIdAndUserId(Long workspaceId, Long userId);

    List<WorkspaceUser> findAllByUser(User user);
}
