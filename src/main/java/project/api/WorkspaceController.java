package project.api;

import project.domain.*;
import project.dto.workspace.*;
import project.common.auth.oauth.UserInfo;
import project.service.WorkspaceService;
import project.exception.schedule.DateFormatException;

import java.util.List;
import java.util.ArrayList;
import static java.util.stream.Collectors.toList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RequiredArgsConstructor
@RestController
public class WorkspaceController{
    
    private final WorkspaceService workspaceService;
    
    // <== 워크스페이스 생성 ==>
    @PostMapping("/v1/workspaces")
    public ResponseEntity<WorkspaceDto> createWorkspace(@AuthenticationPrincipal UserInfo userInfo,
                                                        @RequestBody CreateWorkspaceRequest request){
        
        User loginUser = userInfo.getUser();
        
        Workspace workspace = workspaceService.makeWorkspace(request, loginUser.getId());
        
        WorkspaceDto response = WorkspaceDto.from(workspace);
        
        return ResponseEntity.ok(response);
    }
    
    // <== 워크스페이스 수정 ==>
    @PutMapping("/v1/workspaces/{workspaceId}")
    public ResponseEntity<WorkspaceDto> findAllWorkspaces(@PathVariable Long workspaceId,
                                                        @AuthenticationPrincipal UserInfo userInfo,
                                                        @RequestBody UpdateWorkspaceRequest request){
        
        User loginUser = userInfo.getUser();
        
        Workspace workspace = workspaceService.updateWorkspace(workspaceId, loginUser.getId(), request);
        
        WorkspaceDto response = WorkspaceDto.from(workspace);
        
        return ResponseEntity.ok(response);
    }
    
    // <== 워크스페이스 삭제 ==>
    @DeleteMapping("/v1/workspaces/{workspaceId}")
    public ResponseEntity<DeleteWorkspaceResponse> deleteWorkspaces(@PathVariable Long workspaceId,
                                                                    @AuthenticationPrincipal UserInfo userInfo){
        
        User loginUser = userInfo.getUser();
        
        workspaceService.removeWorkspace(workspaceId, loginUser.getId());
        
        return ResponseEntity.ok(new DeleteWorkspaceResponse());
    }
    
    // <== 워크스페이스 유저 조회 ==>
    @GetMapping("/v1/workspaces/{workspaceId}/users")
    public ResponseEntity<FindWorkspaceUsersResponse> findUsers(@PathVariable Long workspaceId,
                                                               @AuthenticationPrincipal UserInfo userInfo){
        
        User loginUser = userInfo.getUser();                                                        
        
        Workspace workspace = workspaceService.findOne(workspaceId, loginUser.getId());
        
        FindWorkspaceUsersResponse response = FindWorkspaceUsersResponse.from(workspace);
        
        return ResponseEntity.ok(response);
    }
    
    // <== 유저 추가 ==>
    @PostMapping("/v1/workspaces/{workspaceId}/users")
    public ResponseEntity<FindWorkspaceUsersResponse> addUser(@PathVariable Long workspaceId,
                                        @AuthenticationPrincipal UserInfo userInfo,
                                        @RequestBody AddUserRequest request){
        
        User loginUser = userInfo.getUser(); 
        
        Workspace workspace = workspaceService.addUser(workspaceId, loginUser.getId(), request.getUserId());
        
        FindWorkspaceUsersResponse response = FindWorkspaceUsersResponse.from(workspace);
        
        return ResponseEntity.ok(response);
    }
    
    // <== 유저 권한 수정 ==>
    @PutMapping("/v1/workspaces/{workspaceId}/users")
    public ResponseEntity<FindWorkspaceUsersResponse> changeUserAuthority(@PathVariable Long workspaceId,
                                        @AuthenticationPrincipal UserInfo userInfo,
                                        @RequestBody UpdateUserRequest request){
        
        User loginUser = userInfo.getUser(); 
        
        Workspace workspace = workspaceService.changeUserAuthority(workspaceId, 
                                                                   loginUser.getId(), 
                                                                   request.getUserId(), 
                                                                   request.getAuthority());
        
        FindWorkspaceUsersResponse response = FindWorkspaceUsersResponse.from(workspace);
        
        return ResponseEntity.ok(response);
    }
    
    // <== 유저 추방 ==>
    @DeleteMapping("/v1/workspaces/{workspaceId}/users")
    public ResponseEntity<RemoveUserResponse> removeUser(@PathVariable Long workspaceId,
                                                        @AuthenticationPrincipal UserInfo userInfo,
                                                         @RequestBody AddUserRequest request){
        
        User loginUser = userInfo.getUser(); 
        
        workspaceService.removeUser(workspaceId, loginUser.getId(), request.getUserId());
        
        RemoveUserResponse response = new RemoveUserResponse();
        
        return ResponseEntity.ok(response);
    }
    
    // <== 캘린더 조회 ==>
    @GetMapping("/v1/workspaces/{workspaceId}/calendar")
    public ResponseEntity<CalendarResponse> readCalendar(@PathVariable Long workspaceId,
                                                                    @AuthenticationPrincipal UserInfo userInfo,
                                                                    @RequestParam String date){
        
        User loginUser = userInfo.getUser();
        
        LocalDateTime dateTime = parseDateString(date);
        
        CalendarResponse response = workspaceService.getCalendar(workspaceId, loginUser.getId(), dateTime);
        
        return ResponseEntity.ok(response);
    }
    
    // <== 날짜별 조회 ==>
    @GetMapping("/v1/workspaces/{workspaceId}/schedules")
    public ResponseEntity<CalendarResponse> readDailySchedule(@PathVariable Long workspaceId,
                                                                    @AuthenticationPrincipal UserInfo userInfo,
                                                                    @RequestParam String date){
        
        User loginUser = userInfo.getUser();
        
        LocalDateTime dateTime = parseDateString(date);
        
        CalendarResponse response = workspaceService.getDailySchedules(workspaceId, loginUser.getId(), dateTime);
        
        return ResponseEntity.ok(response);
    }
    
    // @GetMapping("/workspaces/{workspaceId}")
    // public ResponseEntity<FindSingleWorkspaceResponse> findOneWorkspace(@PathVariable Long workspaceId){
        
    //     Workspace findWorkspace = workspaceService.findOne(workspaceId);
        
    //     FindSingleWorkspaceResponse response = new FindSingleWorkspaceResponse(findWorkspace);
        
    //     return ResponseEntity.ok(response);
    // }
    
    // @DeleteMapping("/workspaces/{workspaceId}/users/{userId}")
    // public ResponseEntity<RemoveUserResponse> removeUser(@PathVariable Long workspaceId,
    //                                                     @PathVariable Long userId){
        
    //     workspaceService.removeUser(workspaceId, userId);
        
    //     RemoveUserResponse response = new RemoveUserResponse();
        
    //     return ResponseEntity.ok(response);
    // }
    
    
    //이거 Util 객체 하나 만드는게 좋지 않을까
    private LocalDateTime parseDateString(String dateStr){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            return LocalDate.parse(dateStr, formatter).atStartOfDay();
        }
        catch (DateTimeParseException e){
            throw new DateFormatException();
        }
    }
    
    @Getter
    static class AddUserRequest{
        private Long userId;
    }
    
    @Getter
    static class UpdateUserRequest{
        private Long userId;
        private UserRole authority;
    }
}