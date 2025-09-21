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
import project.domain.workspace.dto.request.WorkspaceCreateRequest;
import project.domain.workspace.dto.request.WorkspaceUpdateRequest;
import project.domain.workspace.dto.response.*;
import project.domain.workspace.service.WorkspaceService;

@Tag(name = "[Workspace]", description = "워크스페이스 관리 API")
@RequiredArgsConstructor
@RestController
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @Operation(summary = "워크스페이스 생성", description = "새 워크스페이스를 생성합니다.")
    @PostMapping("/v1/workspaces")
    public ResponseEntity<Long> createWorkspace(@Valid @RequestBody WorkspaceCreateRequest request) {
        var response = workspaceService.makeWorkspace(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "워크스페이스 수정", description = "워크스페이스 정보를 수정합니다. 워크스페이스 관리자 권한이 필요합니다.")
    @PutMapping("/v1/workspaces/{workspaceId}")
    public ResponseEntity<Void> updateWorkspace(
            @PathVariable Long workspaceId, @Valid @RequestBody WorkspaceUpdateRequest request) {
        workspaceService.updateWorkspace(workspaceId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "워크스페이스 삭제", description = "워크스페이스를 삭제합니다. 워크스페이스 관리자 권한이 필요합니다.")
    @DeleteMapping("/v1/workspaces/{workspaceId}")
    public ResponseEntity<Void> deleteWorkspace(@PathVariable Long workspaceId) {
        workspaceService.removeWorkspace(workspaceId);
        return ResponseEntity.ok().build();
    }

    // TODO: 패키지 이동 검토
    @Operation(summary = "워크스페이스 별 스케줄 달력 조회", description = "워크스페이스 별 입력한 달의 스케줄 달력을 조회합니다.")
    @PermitUserRole(value = {UserRole.ADMIN, UserRole.USER})
    @GetMapping("/v1/workspaces/{workspaceId}/calendar")
    public ResponseEntity<CalendarResponse> readCalendar(
            @PathVariable Long workspaceId, @Parameter(required = true, description = "yyyyMM") String date) {
        // TODO: ObjectMapper 사용해서 변환하는 방법 검토
        LocalDateTime dateTime = DateFormatUtil.parseStartOfMonth(date);
        CalendarResponse response = workspaceService.getCalendar(workspaceId, dateTime);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "워크스페이스 별 일일 스케줄 조회", description = "워크스페이스 별 입력한 날짜의 일일 스케줄을 조회합니다.")
    @PermitUserRole(value = {UserRole.ADMIN, UserRole.USER})
    @GetMapping("/v1/workspaces/{workspaceId}/schedules")
    public ResponseEntity<CalendarResponse> readDailySchedule(
            @PathVariable Long workspaceId, @Parameter(required = true, description = "yyyyMMdd") String date) {
        LocalDateTime dateTime = DateFormatUtil.parseStartOfDay(date);
        CalendarResponse response = workspaceService.getDailySchedules(workspaceId, dateTime);
        return ResponseEntity.ok(response);
    }
}
