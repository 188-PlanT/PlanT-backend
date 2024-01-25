package project.api;

import project.domain.*;
import project.dto.schedule.*;
import project.service.ScheduleService;
import project.common.auth.oauth.UserInfo;

import java.util.List;
import java.util.ArrayList;
import static java.util.stream.Collectors.toList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequiredArgsConstructor
public class ScheduleController {
    
    private final ScheduleService scheduleService;
    
    // @GetMapping("/schedules")
    // public ResponseEntity<FindAllScheduleResponse> findAllSchedule(Pageable pageable){
    //     // ,@RequestParam(value = "accountId", required = false) String accountId){
            
    //     Page<Schedule> page = scheduleService.findSchedules(pageable);
        
    //     List<ScheduleDto> responseData = page.getContent()
    //         .stream()
    //         .map(ScheduleDto::new) 
    //         .collect(toList());
        
    //     FindAllScheduleResponse response = new FindAllScheduleResponse(page.getTotalPages(), page.getNumber(), responseData);
        
    //     return ResponseEntity.ok(response);
    // }
    
    @PostMapping("/v1/schedules")
    public ResponseEntity<ScheduleDto> createSchedule(@RequestBody CreateScheduleRequest request){
        
        Schedule schedule = scheduleService.createSchedule(request);
        
        ScheduleDto response = ScheduleDto.from(schedule);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/v1/schedules/{scheduleId}") 
    public ResponseEntity<ScheduleDto> findSingleSchedule(@PathVariable Long scheduleId){
            
        Schedule schedule = scheduleService.findOne(scheduleId);
        
        ScheduleDto response = ScheduleDto.from(schedule);
        
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/v1/schedules/{scheduleId}") 
    public ResponseEntity<ScheduleDto> updateSchedule(@PathVariable Long scheduleId,
                                                    @RequestBody UpdateScheduleRequest request){
            
        Schedule schedule = scheduleService.updateSchedule(scheduleId, request);
        
        ScheduleDto response = ScheduleDto.from(schedule);
        
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/v1/schedules/{scheduleId}")
    public ResponseEntity<DeleteScheduleResponse> deleteSchedule(@PathVariable Long scheduleId){
            
        scheduleService.removeSchedule(scheduleId);
        
        return ResponseEntity.ok(new DeleteScheduleResponse());
    }
    
    @PutMapping("/v1/schedules/{scheduleId}/state") 
    public ResponseEntity<ScheduleDto> updateSchedule(@PathVariable Long scheduleId,
                                                    @RequestBody UpdateScheduleStateRequest request){
            
        Schedule schedule = scheduleService.moveScheduleState(scheduleId, request.getState());
        
        ScheduleDto response = ScheduleDto.from(schedule);
        
        return ResponseEntity.ok(response);
    }
    
    
    @PostMapping("/v1/schedules/{scheduleId}/chat")
    public ResponseEntity<ScheduleDto> addScheduleChat( @AuthenticationPrincipal UserInfo userInfo,
                                                        @PathVariable Long scheduleId,
                                                        @RequestBody AddChatRequest request){
        
        User loginUser = userInfo.getUser();
            
        Schedule schedule = scheduleService.addChat(loginUser.getId(), scheduleId ,request.getContent());
        
        ScheduleDto response = ScheduleDto.from(schedule);
        
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/v1/schedules/{scheduleId}/chat/{chatId}")
    public ResponseEntity<ScheduleDto> updateScheduleChat( @AuthenticationPrincipal UserInfo userInfo,
                                                        @PathVariable Long scheduleId,
                                                        @PathVariable Long chatId,
                                                        @RequestBody AddChatRequest request){
        
        User loginUser = userInfo.getUser();
            
        Schedule schedule = scheduleService.updateChat(loginUser.getId(), scheduleId , chatId, request.getContent());
        
        ScheduleDto response = ScheduleDto.from(schedule);
        
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/v1/schedules/{scheduleId}/chat/{chatId}")
    public ResponseEntity<ScheduleDto> deleteScheduleChat( @AuthenticationPrincipal UserInfo userInfo,
                                                        @PathVariable Long scheduleId,
                                                        @PathVariable Long chatId){
        
        User loginUser = userInfo.getUser();
            
        Schedule schedule = scheduleService.removeChat(loginUser.getId(), scheduleId , chatId);
        
        ScheduleDto response = ScheduleDto.from(schedule);
        
        return ResponseEntity.ok(response);
    }
    
    
    // @GetMapping("/schedules/{scheduleId}/users")
    // public ResponseEntity<FindScheduleUsersResponse> findScheduleUsers(@PathVariable Long scheduleId){
            
    //     Schedule schedule = scheduleService.findOne(scheduleId);
        
    //     FindScheduleUsersResponse response = new FindScheduleUsersResponse(schedule);
        
    //     return ResponseEntity.ok(response);
    // }
    
    // @PostMapping("/schedules/{scheduleId}/users")
    // public ResponseEntity<Long> addScheduleUsers(
    //                                                     @PathVariable Long scheduleId,
    //                                                     @RequestBody AddUserReqeust request){
            
    //     Schedule schedule = scheduleService.addUser(scheduleId,request.getEmail());
        
    //     Long responseId = schedule.getId();
        
    //     return ResponseEntity.ok(responseId);
    // }
    
    // @DeleteMapping("/schedules/{scheduleId}/users/{userId}")
    // public ResponseEntity<DeleteScheduleUserResponse> removeScheduleUsers(
    //                                                     @PathVariable Long scheduleId,
    //                                                     @PathVariable Long userId){
            
    //     scheduleService.removeUser(scheduleId, userId);
        
    //     DeleteScheduleUserResponse response = new DeleteScheduleUserResponse();
        
    //     return ResponseEntity.ok(response);
    // }
    
    @Getter
    static class UpdateScheduleStateRequest{
        private Progress state;
    }
    
    @Getter
    static class AddChatRequest{
        private String content;
    }
}