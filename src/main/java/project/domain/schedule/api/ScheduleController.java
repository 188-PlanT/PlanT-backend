package project.domain.schedule.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.domain.schedule.dto.*;
import project.domain.schedule.dto.request.ScheduleCreateRequest;
import project.domain.schedule.dto.request.ScheduleSearchByWorkspaceRequest;
import project.domain.schedule.dto.request.ScheduleUpdateRequest;
import project.domain.schedule.dto.request.ScheduleUpdateStateRequest;
import project.domain.schedule.dto.response.ScheduleSearchByWorkspaceResponse;
import project.domain.schedule.service.ScheduleService;

@Tag(name = "[Schedule]", description = "스케줄 관리 API")
@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Operation(summary = "스케줄 생성", description = "스케줄을 생성합니다.")
    @PostMapping("/v1/schedules")
    public ResponseEntity<Long> createSchedule(
            @Valid @RequestBody ScheduleCreateRequest request) { // 파라미터가 많아 DTO로 직접 전달
        var response = scheduleService.createSchedule(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "스케줄 상세 조회", description = "스케줄 상세 정보를 조회합니다.")
    @GetMapping("/v1/schedules/{scheduleId}")
    public ResponseEntity<ScheduleFullDto> findSingleSchedule(@PathVariable Long scheduleId) {
        var response = scheduleService.findOne(scheduleId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "스케줄 정보 수정", description = "스케줄 정보를 수정합니다.")
    @PutMapping("/v1/schedules/{scheduleId}")
    public ResponseEntity<Void> updateSchedule(
            @PathVariable Long scheduleId, @Valid @RequestBody ScheduleUpdateRequest request) {
        scheduleService.updateSchedule(scheduleId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "스케줄 삭제", description = "스케줄을 삭제합니다.")
    @DeleteMapping("/v1/schedules/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId) {
        scheduleService.removeSchedule(scheduleId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "스케줄 상태 변경", description = "스케줄 진행 상태를 변경합니다.")
    @PutMapping("/v1/schedules/{scheduleId}/state")
    public ResponseEntity<Void> updateSchedule(
            @PathVariable Long scheduleId, @Valid @RequestBody ScheduleUpdateStateRequest request) {
        scheduleService.moveScheduleState(scheduleId, request.state());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "워크스페이스 별 스케줄 달력 조회", description = "워크스페이스 별 스케줄 달력을 조회합니다.")
    @GetMapping("/v1/schedules")
    public ResponseEntity<ScheduleSearchByWorkspaceResponse> searchScheduleByWorkspace(
            @ParameterObject ScheduleSearchByWorkspaceRequest request) {
        var response = scheduleService.searchScheduleByWorkspace(request);
        return ResponseEntity.ok(response);
    }
}
