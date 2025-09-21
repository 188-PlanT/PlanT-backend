package project.domain.workspaceUser.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;
import project.domain.user.dao.UserRepository;
import project.domain.workspace.dao.WorkspaceRepository;
import project.domain.workspace.domain.Workspace;
import project.domain.workspaceUser.dao.WorkspaceUserRepository;
import project.domain.workspaceUser.domain.WorkspaceUser;
import project.domain.workspaceUser.dto.request.WorkspaceUserCreateRequest;
import project.domain.workspaceUser.dto.request.WorkspaceUserUpdateRequest;
import project.domain.workspaceUser.dto.response.WorkspaceUsersResponse;

// TODO: 권한 검증 로직 추가
@Service
@RequiredArgsConstructor
public class WorkspaceUserService {

    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final WorkspaceUserRepository workspaceUserRepository;

    @Transactional(readOnly = true)
    public WorkspaceUsersResponse findAllWorkspaceUsers(Long workspaceId) {
        Workspace workspace = workspaceRepository
                .findById(workspaceId)
                .orElseThrow(() -> new PlantException(ErrorCode.WORKSPACE_NOT_FOUND));
        List<WorkspaceUser> workspaceUsers = workspaceUserRepository.findAllByWorkspace(workspace);
        return WorkspaceUsersResponse.of(workspaceUsers);
    }

    @Transactional
    public Long addUserToWorkspace(WorkspaceUserCreateRequest request) {
        var workspace = workspaceRepository
                .findById(request.workspaceId())
                .orElseThrow(() -> new PlantException(ErrorCode.WORKSPACE_NOT_FOUND));
        var user = userRepository
                .findById(request.userId())
                .orElseThrow(() -> new PlantException(ErrorCode.USER_NOT_FOUND));

        var workspaceUser = WorkspaceUser.create(workspace, user);
        workspaceUserRepository.save(workspaceUser);
        return workspaceUser.getId();
    }

    @Transactional
    public void changeWorkspaceUserRole(Long workspaceUserId, WorkspaceUserUpdateRequest request) {
        WorkspaceUser workspaceUser = workspaceUserRepository
                .findById(workspaceUserId)
                .orElseThrow(() -> new PlantException(ErrorCode.WORKSPACE_USER_NOT_FOUND));
        workspaceUser.updateRole(request.role());
        workspaceUserRepository.save(workspaceUser);
    }

    @Transactional
    public void removeWorkspaceUser(Long workspaceUserId) {
        WorkspaceUser workspaceUser = workspaceUserRepository
                .findById(workspaceUserId)
                .orElseThrow(() -> new PlantException(ErrorCode.WORKSPACE_USER_NOT_FOUND));

        workspaceUserRepository.delete(workspaceUser);
    }
}
