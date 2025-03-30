package project.domain.schedule.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.common.interceptor.auth.PermitUserRole;
import project.domain.schedule.dto.*;
import project.domain.schedule.service.ScheduleService;
import project.domain.user.domain.UserRole;

@Tag(name = "4. [Schedule]", description = "스케줄 관리 API")
@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Operation(summary = "스케줄 생성", description = "스케줄을 생성합니다.")
    @PostMapping("/v1/schedules")
    public ResponseEntity<ScheduleDto> createSchedule(
            @Valid @RequestBody CreateScheduleRequest request) { // 파라미터가 많아 DTO로 직접 전달

        ScheduleDto response = scheduleService.createSchedule(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "스케줄 상세 조회", description = "스케줄 상세 정보를 조회합니다.")
    @PermitUserRole(value = {UserRole.ADMIN, UserRole.USER})
    @GetMapping("/v1/schedules/{scheduleId}")
    public ResponseEntity<ScheduleDto> findSingleSchedule(@PathVariable Long scheduleId) {

        ScheduleDto response = scheduleService.findOne(scheduleId);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "스케줄 정보 수정", description = "스케줄 정보를 수정합니다.")
    @PermitUserRole(value = {UserRole.ADMIN, UserRole.USER})
    @PutMapping("/v1/schedules/{scheduleId}")
    public ResponseEntity<ScheduleDto> updateSchedule(
            @PathVariable Long scheduleId, @Valid @RequestBody UpdateScheduleRequest request) {

        ScheduleDto response = scheduleService.updateSchedule(scheduleId, request);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "스케줄 삭제", description = "스케줄을 삭제합니다.")
    @PermitUserRole(value = {UserRole.ADMIN, UserRole.USER})
    @DeleteMapping("/v1/schedules/{scheduleId}")
    public ResponseEntity<DeleteScheduleResponse> deleteSchedule(@PathVariable Long scheduleId) {

        scheduleService.removeSchedule(scheduleId);

        return ResponseEntity.ok(new DeleteScheduleResponse());
    }

    @Operation(summary = "스케줄 상태 변경", description = "스케줄 진행 상태를 변경합니다.")
    @PermitUserRole(value = {UserRole.ADMIN, UserRole.USER})
    @PutMapping("/v1/schedules/{scheduleId}/state")
    public ResponseEntity<ScheduleDto> updateSchedule(
            @PathVariable Long scheduleId, @Valid @RequestBody UpdateScheduleStateRequest request) {

        ScheduleDto response = scheduleService.moveScheduleState(scheduleId, request.getState());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "스케줄 댓글 추가", description = "스케줄에 댓글을 추가합니다.")
    @PermitUserRole(value = {UserRole.ADMIN, UserRole.USER})
    @PostMapping("/v1/schedules/{scheduleId}/chat")
    public ResponseEntity<AddChatResponse> addScheduleChat(
            @PathVariable Long scheduleId, @RequestBody AddChatRequest request) {

        AddChatResponse response = scheduleService.addChat(scheduleId, request.getContent());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "스케줄 댓글 수정", description = "스케줄에 댓글을 수정합니다.")
    @PermitUserRole(value = {UserRole.ADMIN, UserRole.USER})
    @PutMapping("/v1/schedules/{scheduleId}/chat/{chatId}")
    public ResponseEntity<AddChatResponse> updateScheduleChat(
            @PathVariable Long scheduleId, @PathVariable Long chatId, @RequestBody AddChatRequest request) {

        AddChatResponse response = scheduleService.updateChat(scheduleId, chatId, request.getContent());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "스케줄 댓글 삭제", description = "스케줄에 댓글을 삭제합니다.")
    @PermitUserRole(value = {UserRole.ADMIN, UserRole.USER})
    @DeleteMapping("/v1/schedules/{scheduleId}/chat/{chatId}")
    public ResponseEntity<RemoveChatResponse> deleteScheduleChat(
            @PathVariable Long scheduleId, @PathVariable Long chatId) {

        scheduleService.removeChat(scheduleId, chatId);

        RemoveChatResponse response = new RemoveChatResponse();

        return ResponseEntity.ok(response);
    }

    @Getter
    static class UpdateScheduleStateRequest {
        @NotNull private Progress state;
    }

    @Getter
    static class AddChatRequest {
        private String content;
    }
}
