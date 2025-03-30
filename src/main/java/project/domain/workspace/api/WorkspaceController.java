package project.domain.workspace.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.common.interceptor.auth.PermitUserRole;
import project.common.util.DateFormatUtil;
import project.domain.user.domain.UserRole;
import project.domain.workspace.domain.Workspace;
import project.domain.workspace.dto.*;
import project.domain.workspace.service.WorkspaceService;

@Tag(name = "3. [Workspace]", description = "워크스페이스 관리 API")
@RequiredArgsConstructor
@RestController
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @Operation(summary = "워크스페이스 생성", description = "새 워크스페이스를 생성합니다.")
    @PostMapping("/v1/workspaces")
    public ResponseEntity<WorkspaceDto> createWorkspace(@Valid @RequestBody CreateWorkspaceRequest request) {

        Workspace workspace = workspaceService.makeWorkspace(request);

        WorkspaceDto response = WorkspaceDto.from(workspace);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "워크스페이스 수정", description = "워크스페이스 정보를 수정합니다. 워크스페이스 관리자 권한이 필요합니다.")
    @PutMapping("/v1/workspaces/{workspaceId}")
    @PermitUserRole(value = {UserRole.ADMIN})
    public ResponseEntity<UpdateWorkspaceResponse> findAllWorkspaces(
            @PathVariable Long workspaceId, @Valid @RequestBody UpdateWorkspaceRequest request) {

        Workspace workspace = workspaceService.updateWorkspace(workspaceId, request);

        UpdateWorkspaceResponse response = UpdateWorkspaceResponse.from(workspace);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "워크스페이스 삭제", description = "워크스페이스를 삭제합니다. 워크스페이스 관리자 권한이 필요합니다.")
    @DeleteMapping("/v1/workspaces/{workspaceId}")
    @PermitUserRole(value = {UserRole.ADMIN})
    public ResponseEntity<DeleteWorkspaceResponse> deleteWorkspaces(@PathVariable Long workspaceId) {

        workspaceService.removeWorkspace(workspaceId);

        return ResponseEntity.ok(new DeleteWorkspaceResponse());
    }

    @Operation(summary = "워크스페이스 유저 조회", description = "워크스페이스 유저 목록을 조회합니다.")
    @PermitUserRole(value = {UserRole.ADMIN, UserRole.USER})
    @GetMapping("/v1/workspaces/{workspaceId}/users")
    public ResponseEntity<FindWorkspaceUsersResponse> findUsers(@PathVariable Long workspaceId) {

        Workspace workspace = workspaceService.findOne(workspaceId);

        FindWorkspaceUsersResponse response = FindWorkspaceUsersResponse.from(workspace);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "워크스페이스 유저 추가", description = "워크스페이스에 유저를 추가합니다. 워크스페이스 관리자 권한이 필요합니다.")
    @PostMapping("/v1/workspaces/{workspaceId}/users")
    @PermitUserRole(value = {UserRole.ADMIN})
    public ResponseEntity<FindWorkspaceUsersResponse> addUser(
            @PathVariable Long workspaceId, @Valid @RequestBody AddUserRequest request) {

        Workspace workspace = workspaceService.addUser(workspaceId, request.getUserId());

        FindWorkspaceUsersResponse response = FindWorkspaceUsersResponse.from(workspace);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "워크스페이스 유저 권한 변경", description = "워크스페이스 유저의 권한을 변경합니다. 워크스페이스 관리자 권한이 필요합니다.")
    @PutMapping("/v1/workspaces/{workspaceId}/users/{userId}")
    @PermitUserRole(value = {UserRole.ADMIN})
    public ResponseEntity<FindWorkspaceUsersResponse> changeUserAuthority(
            @PathVariable Long workspaceId, @PathVariable Long userId, @Valid @RequestBody UpdateUserRequest request) {

        Workspace workspace = workspaceService.changeUserAuthority(workspaceId, userId, request.getAuthority());

        FindWorkspaceUsersResponse response = FindWorkspaceUsersResponse.from(workspace);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "워크스페이스 유저 삭제", description = "워크스페이스 유저를 삭제합니다. 워크스페이스 관리자 권한이 필요합니다.")
    @DeleteMapping("/v1/workspaces/{workspaceId}/users/{userId}")
    @PermitUserRole(value = {UserRole.ADMIN})
    public ResponseEntity<RemoveUserResponse> removeUser(@PathVariable Long workspaceId, @PathVariable Long userId) {

        workspaceService.removeUser(workspaceId, userId);

        RemoveUserResponse response = new RemoveUserResponse();

        return ResponseEntity.ok(response);
    }

    // TODO: 패키지 이동 검토
    @Operation(summary = "워크스페이스 별 스케줄 달력 조회", description = "워크스페이스 별 입력한 달의 스케줄 달력을 조회합니다.")
    @GetMapping("/v1/workspaces/{workspaceId}/calendar")
    @PermitUserRole(value = {UserRole.ADMIN, UserRole.USER})
    public ResponseEntity<CalendarResponse> readCalendar(
            @PathVariable Long workspaceId, @Parameter(required = true, description = "yyyyMM") String date) {

        LocalDateTime dateTime = DateFormatUtil.parseStartOfMonth(date);

        CalendarResponse response = workspaceService.getCalendar(workspaceId, dateTime);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "워크스페이스 별 일일 스케줄 조회", description = "워크스페이스 별 입력한 날짜의 일일 스케줄을 조회합니다.")
    @GetMapping("/v1/workspaces/{workspaceId}/schedules")
    @PermitUserRole(value = {UserRole.ADMIN, UserRole.USER})
    public ResponseEntity<CalendarResponse> readDailySchedule(
            @PathVariable Long workspaceId, @Parameter(required = true, description = "yyyyMMdd") String date) {

        LocalDateTime dateTime = DateFormatUtil.parseStartOfDay(date);

        CalendarResponse response = workspaceService.getDailySchedules(workspaceId, dateTime);

        return ResponseEntity.ok(response);
    }
}
