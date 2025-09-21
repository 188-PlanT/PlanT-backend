package project.domain.scheduleUser.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.domain.scheduleUser.dto.request.ScheduleUserCreateRequest;
import project.domain.scheduleUser.service.ScheduleUserService;

@RestController
@RequiredArgsConstructor
public class ScheduleUserController {

    private final ScheduleUserService scheduleUserService;

    @PostMapping("/v1/schedules/users")
    public ResponseEntity<Long> addUserToSchedule(@RequestBody @Valid ScheduleUserCreateRequest request) {
        var response = scheduleUserService.addUserToSchedule(request.scheduleId(), request.userId());
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/v1/schedules/users/{scheduleUserId}")
    public ResponseEntity<Void> removeUserToSchedule(@PathVariable Long scheduleUserId) {
        scheduleUserService.removeUserToSchedule(scheduleUserId);
        return ResponseEntity.ok().build();
    }
}
