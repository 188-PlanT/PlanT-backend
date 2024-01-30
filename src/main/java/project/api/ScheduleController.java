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
    
    @PostMapping("/v1/schedules")
    public ResponseEntity<ScheduleDto> createSchedule(@RequestBody CreateScheduleRequest request){ // 파라미터가 많아 DTO로 직접 전달

         ScheduleDto response = scheduleService.createSchedule(request);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/v1/schedules/{scheduleId}") 
    public ResponseEntity<ScheduleDto> findSingleSchedule(@PathVariable Long scheduleId){

        ScheduleDto response = scheduleService.findOne(scheduleId);
        
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/v1/schedules/{scheduleId}") 
    public ResponseEntity<ScheduleDto> updateSchedule(@PathVariable Long scheduleId,
                                                    @RequestBody UpdateScheduleRequest request){

        ScheduleDto response = scheduleService.updateSchedule(scheduleId, request);
        
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
        
        ScheduleDto response = scheduleService.moveScheduleState(scheduleId, request.getState());
        
        return ResponseEntity.ok(response);
    }
    
    
    @PostMapping("/v1/schedules/{scheduleId}/chat")
    public ResponseEntity<ScheduleDto> addScheduleChat( @AuthenticationPrincipal UserInfo userInfo,
                                                        @PathVariable Long scheduleId,
                                                        @RequestBody AddChatRequest request){

        ScheduleDto response = scheduleService.addChat(userInfo.getUsername(), scheduleId ,request.getContent());
        
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/v1/schedules/{scheduleId}/chat/{chatId}")
    public ResponseEntity<ScheduleDto> updateScheduleChat( @AuthenticationPrincipal UserInfo userInfo,
                                                        @PathVariable Long scheduleId,
                                                        @PathVariable Long chatId,
                                                        @RequestBody AddChatRequest request){

        ScheduleDto response = scheduleService.updateChat(userInfo.getUsername(), scheduleId , chatId, request.getContent());
        
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/v1/schedules/{scheduleId}/chat/{chatId}")
    public ResponseEntity<ScheduleDto> deleteScheduleChat( @AuthenticationPrincipal UserInfo userInfo,
                                                        @PathVariable Long scheduleId,
                                                        @PathVariable Long chatId){

        ScheduleDto response = scheduleService.removeChat(userInfo.getUsername(), scheduleId , chatId);
        
        return ResponseEntity.ok(response);
    }
    
    @Getter
    static class UpdateScheduleStateRequest{
        private Progress state;
    }
    
    @Getter
    static class AddChatRequest{
        private String content;
    }
}