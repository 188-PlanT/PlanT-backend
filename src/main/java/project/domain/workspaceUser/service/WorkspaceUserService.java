package project.domain.workspaceUser.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;
import project.common.util.UserUtil;
import project.common.util.WorkspaceUserUtil;
import project.domain.user.dao.UserRepository;
import project.domain.workspace.dao.WorkspaceRepository;
import project.domain.workspace.domain.Workspace;
import project.domain.workspaceUser.dao.WorkspaceUserRepository;
import project.domain.workspaceUser.domain.WorkspaceUser;
import project.domain.workspaceUser.domain.WorkspaceUserDomainService;
import project.domain.workspaceUser.domain.WorkspaceUserRole;
import project.domain.workspaceUser.dto.request.WorkspaceUserCreateRequest;
import project.domain.workspaceUser.dto.request.WorkspaceUserUpdateRequest;
import project.domain.workspaceUser.dto.response.WorkspaceUsersResponse;

@Service
@RequiredArgsConstructor
public class WorkspaceUserService {

    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final WorkspaceUserRepository workspaceUserRepository;
    private final UserUtil userUtil;
    private final WorkspaceUserUtil workspaceUserUtil;
    private final WorkspaceUserDomainService workspaceUserDomainService;


    @Transactional(readOnly = true)
    public WorkspaceUsersResponse findWorkspaceUsersByWorkspace(Long workspaceId) {
        validateLoginUserInWorkspace();
        Workspace workspace = workspaceRepository
                .findById(workspaceId)
                .orElseThrow(() -> new PlantException(ErrorCode.WORKSPACE_NOT_FOUND));
        List<WorkspaceUser> workspaceUsers = workspaceUserRepository.findAllByWorkspace(workspace);
        return WorkspaceUsersResponse.of(workspaceUsers);
    }

    @Transactional
    public Long addUserToWorkspace(WorkspaceUserCreateRequest request) {
        validateLoginUserInWorkspace();
        var workspace = workspaceRepository
                .findById(request.workspaceId())
                .orElseThrow(() -> new PlantException(ErrorCode.WORKSPACE_NOT_FOUND));
        var user = userRepository
                .findById(request.userId())
                .orElseThrow(() -> new PlantException(ErrorCode.USER_NOT_FOUND));

        if (workspaceUserRepository.existsByWorkspaceIdAndUserId(workspace.getId(), user.getId())) {
            throw new PlantException(ErrorCode.WORKSPACE_USER_ALREADY_EXIST);
        }

        var workspaceUser = WorkspaceUser.create(workspace, user);
        workspaceUserRepository.save(workspaceUser);
        return workspaceUser.getId();
    }

    @Transactional
    public void changeWorkspaceUser(Long workspaceUserId, WorkspaceUserUpdateRequest request) {
        validateLoginUserIsAdmin();
        WorkspaceUser workspaceUser = workspaceUserRepository
                .findById(workspaceUserId)
                .orElseThrow(() -> new PlantException(ErrorCode.WORKSPACE_USER_NOT_FOUND));
        List<WorkspaceUser> workspaceUsers = workspaceUserRepository
                .findAllByWorkspaceId(workspaceUser.getWorkspace().getId());

        workspaceUserDomainService.validateWhenChangeRole(workspaceUser, request.role(), workspaceUsers);
        workspaceUser.updateRole(request.role());
        workspaceUserRepository.save(workspaceUser);
    }

    @Transactional
    public void removeUserFromWorkspace(Long workspaceUserId) {
        validateLoginUserIsAdmin();
        WorkspaceUser workspaceUser = workspaceUserRepository
                .findById(workspaceUserId)
                .orElseThrow(() -> new PlantException(ErrorCode.WORKSPACE_USER_NOT_FOUND));
        List<WorkspaceUser> workspaceUsers = workspaceUserRepository
                .findAllByWorkspaceId(workspaceUser.getWorkspace().getId());

        workspaceUserDomainService.validateWhenRemoveUser(workspaceUser, workspaceUsers);
        workspaceUserRepository.delete(workspaceUser);
    }

    private void validateLoginUserIsAdmin() {
        Long loginUserId = userUtil.getLoginUserId();
        boolean isAdmin = workspaceUserUtil.isAdminUser(1L, loginUserId);

        if (!isAdmin) {
            throw new PlantException(ErrorCode.WORKSPACE_USER_AUTHORITY_INVALID);
        }
    }

    private void validateLoginUserInWorkspace() {
        Long loginUserId = userUtil.getLoginUserId();
        boolean exists = workspaceUserUtil.existsUser(1L, loginUserId);

        if (!exists) {
            throw new PlantException(ErrorCode.WORKSPACE_USER_AUTHORITY_INVALID);
        }
    }
}
