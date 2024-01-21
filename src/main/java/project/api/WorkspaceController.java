package project.api;

import project.domain.*;
import project.dto.workspace.*;
import project.common.auth.oauth.UserInfo;
import project.service.WorkspaceService;

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
    
    // @GetMapping("/workspaces")
    // public ResponseEntity<FindAllWorkspacesResponse> findAllWorkspaces(Pageable pageable){
        
    //     Page<Workspace> page = workspaceService.findAll(pageable);
        
    //     List<WorkspaceDto> responseData = page.getContent()
    //         .stream()
    //         .map(WorkspaceDto::new)
    //         .collect(toList());
        
    //     FindAllWorkspacesResponse response = new FindAllWorkspacesResponse(
    //         page.getTotalPages(), 
    //         page.getNumber(), 
    //         responseData);
        
    //     return ResponseEntity.ok(response);
    // }
    
    // @GetMapping("/workspaces/{workspaceId}")
    // public ResponseEntity<FindSingleWorkspaceResponse> findOneWorkspace(@PathVariable Long workspaceId){
        
    //     Workspace findWorkspace = workspaceService.findOne(workspaceId);
        
    //     FindSingleWorkspaceResponse response = new FindSingleWorkspaceResponse(findWorkspace);
        
    //     return ResponseEntity.ok(response);
    // }
    
    // @DeleteMapping("/workspaces/{workspaceId}")
    // public ResponseEntity<DeleteWorkspaceResponse> deleteWorkspaces(@PathVariable Long workspaceId){
        
    //     workspaceService.removeWorkspace(workspaceId);
        
    //     return ResponseEntity.ok(new DeleteWorkspaceResponse());
    // }
    
    // @GetMapping("/workspaces/{workspaceId}/users")
    // public ResponseEntity<FindWorkspaceUsersResponse> findUsers(@PathVariable Long workspaceId){
        
    //     Workspace workspace = workspaceService.findOne(workspaceId);
        
    //     FindWorkspaceUsersResponse response = new FindWorkspaceUsersResponse(workspace);
        
    //     return ResponseEntity.ok(response);
    // }
    
    // @PostMapping("/workspaces/{workspaceId}/users")
    // public ResponseEntity<Long> addUser(@PathVariable Long workspaceId,
    //                                     @RequestBody AddUserRequest request){
        
    //     Workspace workspace = workspaceService.addUser(workspaceId, request.getEmail());
        
    //     Long responseId = workspace.getId();
        
    //     return ResponseEntity.ok(responseId);
    // }
    
    // @DeleteMapping("/workspaces/{workspaceId}/users/{userId}")
    // public ResponseEntity<RemoveUserResponse> removeUser(@PathVariable Long workspaceId,
    //                                                     @PathVariable Long userId){
        
    //     workspaceService.removeUser(workspaceId, userId);
        
    //     RemoveUserResponse response = new RemoveUserResponse();
        
    //     return ResponseEntity.ok(response);
    // }
    
    // @Getter
    // static class AddUserRequest{
    //     private Long userId;
    // }
}