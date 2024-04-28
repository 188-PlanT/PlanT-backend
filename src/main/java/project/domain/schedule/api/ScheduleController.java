package project.domain.schedule.api;

import project.domain.schedule.domain.Progress;
import project.domain.schedule.dto.*;
import project.domain.user.domain.UserRole;
import project.domain.schedule.service.ScheduleService;
import project.common.interceptor.auth.PermitUserRole;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
public class ScheduleController {
    
    private final ScheduleService scheduleService;
    
    @PostMapping("/v1/schedules")
    public ResponseEntity<ScheduleDto> createSchedule(@Valid @RequestBody CreateScheduleRequest request){ // 파라미터가 많아 DTO로 직접 전달

         ScheduleDto response = scheduleService.createSchedule(request);
        
        return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response); 
    }
    
	@PermitUserRole(value = {UserRole.ADMIN, UserRole.USER})
    @GetMapping("/v1/schedules/{scheduleId}") 
    public ResponseEntity<ScheduleDto> findSingleSchedule(@PathVariable Long scheduleId){

        ScheduleDto response = scheduleService.findOne(scheduleId);
        
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/v1/schedules/{scheduleId}")
    @PermitUserRole(value = {UserRole.ADMIN, UserRole.USER})
    public ResponseEntity<ScheduleDto> updateSchedule(@PathVariable Long scheduleId,
                                                    @Valid @RequestBody UpdateScheduleRequest request){

        ScheduleDto response = scheduleService.updateSchedule(scheduleId, request);
        
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/v1/schedules/{scheduleId}")
    @PermitUserRole(value = {UserRole.ADMIN, UserRole.USER})
    public ResponseEntity<DeleteScheduleResponse> deleteSchedule(@PathVariable Long scheduleId){
            
        scheduleService.removeSchedule(scheduleId);
        
        return ResponseEntity.ok(new DeleteScheduleResponse());
    }
    
    @PutMapping("/v1/schedules/{scheduleId}/state")
    @PermitUserRole(value = {UserRole.ADMIN, UserRole.USER})
    public ResponseEntity<ScheduleDto> updateSchedule(@PathVariable Long scheduleId,
                                                    @Valid @RequestBody UpdateScheduleStateRequest request){
        
        ScheduleDto response = scheduleService.moveScheduleState(scheduleId, request.getState());
        
        return ResponseEntity.ok(response);
    }
    
    
    @PostMapping("/v1/schedules/{scheduleId}/chat")
    @PermitUserRole(value = {UserRole.ADMIN, UserRole.USER})
    public ResponseEntity<AddChatResponse> addScheduleChat(@PathVariable Long scheduleId,
                                                           @RequestBody AddChatRequest request){

        AddChatResponse response = scheduleService.addChat(scheduleId ,request.getContent());
        
        return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response); 
    }
    
    @PutMapping("/v1/schedules/{scheduleId}/chat/{chatId}")
    @PermitUserRole(value = {UserRole.ADMIN, UserRole.USER})
    public ResponseEntity<AddChatResponse> updateScheduleChat(@PathVariable Long scheduleId,
                                                              @PathVariable Long chatId,
                                                              @RequestBody AddChatRequest request){

        AddChatResponse response = scheduleService.updateChat(scheduleId , chatId, request.getContent());
        
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/v1/schedules/{scheduleId}/chat/{chatId}")
    @PermitUserRole(value = {UserRole.ADMIN, UserRole.USER})
    public ResponseEntity<RemoveChatResponse> deleteScheduleChat(@PathVariable Long scheduleId,
                                                                 @PathVariable Long chatId){
        
        scheduleService.removeChat(scheduleId , chatId);
        
        RemoveChatResponse response = new RemoveChatResponse();
        
        return ResponseEntity.ok(response);
    }
    
    @Getter
    static class UpdateScheduleStateRequest{
        @NotNull
        private Progress state;
    }
    
    @Getter
    static class AddChatRequest{
        private String content;
    }
}