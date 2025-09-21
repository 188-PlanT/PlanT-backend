package project.domain.workspaceUser.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import project.domain.workspaceUser.domain.WorkspaceUser;

public interface WorkspaceUserRepository extends JpaRepository<WorkspaceUser, Long> {

    List<WorkspaceUser> findAllByWorkspaceId(Long workspaceId);
}
