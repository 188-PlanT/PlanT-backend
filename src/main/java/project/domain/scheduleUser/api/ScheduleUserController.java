package project.domain.scheduleUser.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.domain.scheduleUser.dto.request.ScheduleUserCreateRequest;
import project.domain.scheduleUser.service.ScheduleUserService;

@Tag(name = "[ScheduleUser]", description = "스케줄 유저 관리 API")
@RestController
@RequiredArgsConstructor
public class ScheduleUserController {

    private final ScheduleUserService scheduleUserService;

    @Operation(summary = "스케줄 유저 추가", description = "스케줄에 유저를 추가합니다.")
    @PostMapping("/v1/schedules/users")
    public ResponseEntity<Long> addUserToSchedule(@RequestBody @Valid ScheduleUserCreateRequest request) {
        var response = scheduleUserService.addUserToSchedule(request.scheduleId(), request.userId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "스케줄 유저 제거", description = "스케줄에서 유저를 제거합니다.")
    @DeleteMapping("/v1/schedules/users/{scheduleUserId}")
    public ResponseEntity<Void> removeUserToSchedule(@PathVariable Long scheduleUserId) {
        scheduleUserService.removeUserToSchedule(scheduleUserId);
        return ResponseEntity.ok().build();
    }
}
