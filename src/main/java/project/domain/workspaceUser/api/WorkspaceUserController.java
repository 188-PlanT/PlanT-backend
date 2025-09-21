package project.domain.workspaceUser.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.domain.workspaceUser.dto.request.WorkspaceUserCreateRequest;
import project.domain.workspaceUser.dto.request.WorkspaceUserUpdateRequest;
import project.domain.workspaceUser.dto.response.WorkspaceUsersResponse;
import project.domain.workspaceUser.service.WorkspaceUserService;

@Tag(name = "[WorkspaceUser]", description = "워크스페이스 유저 관리 API")
@RestController
@RequiredArgsConstructor
public class WorkspaceUserController {

    private final WorkspaceUserService workspaceUserService;

    @Operation(summary = "워크스페이스 유저 조회", description = "워크스페이스 유저 목록을 조회합니다.")
    @GetMapping("/v1/workspace-users")
    public ResponseEntity<WorkspaceUsersResponse> getWorkspaceUsers(@RequestParam Long workspaceId) {
        var response = workspaceUserService.findWorkspaceUsersByWorkspace(workspaceId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "워크스페이스 유저 추가", description = "워크스페이스에 유저를 추가합니다. 워크스페이스 관리자 권한이 필요합니다.")
    @PostMapping("/v1/workspace-users")
    public ResponseEntity<Long> createWorkspaceUser(@Valid @RequestBody WorkspaceUserCreateRequest request) {
        var response = workspaceUserService.addUserToWorkspace(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "워크스페이스 유저 권한 변경", description = "워크스페이스 유저의 권한을 변경합니다. 워크스페이스 관리자 권한이 필요합니다.")
    @PutMapping("/v1/workspace-users/{workspaceUserId}")
    public ResponseEntity<Void> updateWorkspaceUser(
            @PathVariable Long workspaceUserId, @Valid @RequestBody WorkspaceUserUpdateRequest request) {
        workspaceUserService.changeWorkspaceUser(workspaceUserId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "워크스페이스 유저 삭제", description = "워크스페이스 유저를 삭제합니다. 워크스페이스 관리자 권한이 필요합니다.")
    @DeleteMapping("/v1/workspace-users/{workspaceUserId}")
    public ResponseEntity<Void> deleteWorkspaceUser(@PathVariable Long workspaceUserId) {
        workspaceUserService.removeUserFromWorkspace(workspaceUserId);
        return ResponseEntity.ok().build();
    }
}
