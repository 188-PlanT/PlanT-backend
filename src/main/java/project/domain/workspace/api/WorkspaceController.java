package project.domain.workspace.api;

import project.domain.user.domain.UserRole;
import project.domain.workspace.domain.Workspace;
import project.domain.workspace.dto.*;
import project.common.security.oauth.UserInfo;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;
import project.domain.workspace.service.WorkspaceService;
import project.common.interceptor.auth.PermitUserRole;

import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RequiredArgsConstructor
@RestController
public class WorkspaceController{
    
    private final WorkspaceService workspaceService;
    
    // <== 워크스페이스 생성 ==>
    @PostMapping("/v1/workspaces")
    public ResponseEntity<WorkspaceDto> createWorkspace(@AuthenticationPrincipal UserInfo userInfo,
                                                        @RequestBody CreateWorkspaceRequest request){
        
        Workspace workspace = workspaceService.makeWorkspace(request, userInfo.getUserId());
        
        WorkspaceDto response = WorkspaceDto.from(workspace);
        
        return ResponseEntity.ok(response);
    }
    
    // <== 워크스페이스 수정 ==>
    @PutMapping("/v1/workspaces/{workspaceId}")
    @PermitUserRole(value = {UserRole.ADMIN})
    public ResponseEntity<UpdateWorkspaceResponse> findAllWorkspaces(@PathVariable Long workspaceId,
                                                        @AuthenticationPrincipal UserInfo userInfo,
                                                        @RequestBody UpdateWorkspaceRequest request){
        
        Workspace workspace = workspaceService.updateWorkspace(workspaceId, userInfo.getUserId(), request);
        
        UpdateWorkspaceResponse response = UpdateWorkspaceResponse.from(workspace);
        
        return ResponseEntity.ok(response);
    }
    
    // <== 워크스페이스 삭제 ==>
    @DeleteMapping("/v1/workspaces/{workspaceId}")
    @PermitUserRole(value = {UserRole.ADMIN})
    public ResponseEntity<DeleteWorkspaceResponse> deleteWorkspaces(@PathVariable Long workspaceId,
                                                                    @AuthenticationPrincipal UserInfo userInfo){
        
        workspaceService.removeWorkspace(workspaceId, userInfo.getUserId());
        
        return ResponseEntity.ok(new DeleteWorkspaceResponse());
    }
    
    // <== 워크스페이스 유저 조회 ==>
	@PermitUserRole(value = {UserRole.ADMIN, UserRole.USER})
    @GetMapping("/v1/workspaces/{workspaceId}/users")
    public ResponseEntity<FindWorkspaceUsersResponse> findUsers(@PathVariable Long workspaceId,
                                                                @AuthenticationPrincipal UserInfo userInfo){
        
        Workspace workspace = workspaceService.findOne(workspaceId, userInfo.getUserId());
        
        FindWorkspaceUsersResponse response = FindWorkspaceUsersResponse.from(workspace);
        
        return ResponseEntity.ok(response);
    }
    
    // <== 유저 추가 ==>
    @PostMapping("/v1/workspaces/{workspaceId}/users")
    @PermitUserRole(value = {UserRole.ADMIN})
    public ResponseEntity<FindWorkspaceUsersResponse> addUser(@PathVariable Long workspaceId,
                                        @AuthenticationPrincipal UserInfo userInfo,
                                        @RequestBody AddUserRequest request){

        Workspace workspace = workspaceService.addUser(workspaceId, userInfo.getUserId(), request.getUserId());
		
        FindWorkspaceUsersResponse response = FindWorkspaceUsersResponse.from(workspace);
        
        return ResponseEntity.ok(response);
    }
    
    // <== 유저 권한 수정 ==>
    @PutMapping("/v1/workspaces/{workspaceId}/users/{userId}")
    @PermitUserRole(value = {UserRole.ADMIN, UserRole.PENDING}) // 유저 워크스페이스 수락도 여기서 진행되므로 PENDING도 허가
    public ResponseEntity<FindWorkspaceUsersResponse> changeUserAuthority(@PathVariable Long workspaceId,
                                        @AuthenticationPrincipal UserInfo userInfo,
										@PathVariable Long userId,
                                        @RequestBody UpdateUserRequest request){
        
        Workspace workspace = workspaceService.changeUserAuthority(workspaceId, 
                                                                   userInfo.getUserId(), 
                                                                   userId, 
                                                                   request.getAuthority());
        
        FindWorkspaceUsersResponse response = FindWorkspaceUsersResponse.from(workspace);
        
        return ResponseEntity.ok(response);
    }
    
    // <== 유저 추방 ==>
    @DeleteMapping("/v1/workspaces/{workspaceId}/users/{userId}")
    @PermitUserRole(value={UserRole.ADMIN})
    public ResponseEntity<RemoveUserResponse> removeUser(@PathVariable Long workspaceId,
                                                         @AuthenticationPrincipal UserInfo userInfo,
                                                         @PathVariable Long userId){
        
        workspaceService.removeUser(workspaceId, userInfo.getUserId(), userId);
        
        RemoveUserResponse response = new RemoveUserResponse();
        
        return ResponseEntity.ok(response);
    }
    
    // <== 캘린더 조회 ==>
    @GetMapping("/v1/workspaces/{workspaceId}/calendar")
    @PermitUserRole(value = {UserRole.ADMIN, UserRole.USER})
    public ResponseEntity<CalendarResponse> readCalendar(@PathVariable Long workspaceId,
                                                         @AuthenticationPrincipal UserInfo userInfo,
                                                         @RequestParam String date){
        
        LocalDateTime dateTime = parseDateMonthString(date);
        
        CalendarResponse response = workspaceService.getCalendar(workspaceId, userInfo.getUserId(), dateTime);
        
        return ResponseEntity.ok(response);
    }
    
    // <== 날짜별 조회 ==>
    @GetMapping("/v1/workspaces/{workspaceId}/schedules")
    @PermitUserRole(value = {UserRole.ADMIN, UserRole.USER})
    public ResponseEntity<CalendarResponse> readDailySchedule(@PathVariable Long workspaceId,
                                                                    @AuthenticationPrincipal UserInfo userInfo,
                                                                    @RequestParam String date){
        
        LocalDateTime dateTime = parseDateString(date);
        
        CalendarResponse response = workspaceService.getDailySchedules(workspaceId, userInfo.getUserId(), dateTime);
        
        return ResponseEntity.ok(response);
    }
    
    //이거 Util 객체 하나 만드는게 좋지 않을까
    private LocalDateTime parseDateString(String dateStr){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            return LocalDate.parse(dateStr, formatter).atStartOfDay();
        }
        catch (DateTimeParseException e){
            throw new PlantException(ErrorCode.DATE_INVALID);
        }
    }
    
    private LocalDateTime parseDateMonthString(String dateStr){
        try {
            // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM01");
            dateStr = dateStr + "01";
            return LocalDate.parse(dateStr, DateTimeFormatter.BASIC_ISO_DATE).atStartOfDay();
        }
        catch (DateTimeParseException e){
            throw new PlantException(ErrorCode.DATE_INVALID);
        }
    }
    
    @Getter @Setter
    static class AddUserRequest{
        private Long userId;
    }
    
    @Getter @Setter
    static class UpdateUserRequest{
        private UserRole authority;
    }
}