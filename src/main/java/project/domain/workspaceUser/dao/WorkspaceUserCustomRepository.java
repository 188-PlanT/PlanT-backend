package project.domain.workspaceUser.dao;

import java.util.List;
import project.domain.workspaceUser.domain.WorkspaceUser;

public interface WorkspaceUserCustomRepository {

    List<WorkspaceUser> searchByUserId(Long userId);
}
