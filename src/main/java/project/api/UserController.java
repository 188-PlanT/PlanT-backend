package project.api;

import project.domain.*;
import project.common.auth.oauth.UserInfo;
import project.dto.user.*;
import project.service.UserService;
import project.exception.user.*;
import project.exception.schedule.DateFormatException;

import java.util.List;
import javax.validation.Valid;
import static java.util.stream.Collectors.toList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController{
    
    private final UserService userService;
    

    // <==유저 이메일 인증==>
    @PostMapping("/v1/users/email")
    public ResponseEntity<EmailCheckResponse> checkEmailAvailable(@RequestBody EmailCheckRequest request){
        
        try{
            userService.validateUserEmail(request.getEmail());
        } 
        catch (UserAlreadyExistException e){
            return ResponseEntity.ok(new EmailCheckResponse(false));
        }

        return ResponseEntity.ok(new EmailCheckResponse(true));
    }

    // <==유저 닉네임 인증==>
    @PostMapping("/v1/users/nickname") //응답 결과 양식은 이메일과 같으므로 공유함
    public ResponseEntity<EmailCheckResponse> checkNickNameAvailable(@RequestBody NickNameCheckRequest request){
        
        try{
            userService.validateUserNickName(request.getNickName());
            return ResponseEntity.ok(new EmailCheckResponse(true));
        } 
        catch (UserAlreadyExistException e){
            return ResponseEntity.ok(new EmailCheckResponse(false));
        }
    }
    
    // <== 유저 닉네임 추가 ==>
    @PutMapping("/v1/users/nickname")
    public ResponseEntity<UserDto> setNickNameUser(@AuthenticationPrincipal UserInfo userInfo, 
                                                 @RequestBody FinishUserRegisterRequest request){
        
        User loginUser = userInfo.getUser();
        
        User user = userService.finishRegister(loginUser.getId(), request.getNickName());
        
        //여기서 부터 개발 시작
        return ResponseEntity.ok(UserDto.from(user));
    }
    
    
    // <== 유저 정보 확인 ==>
    @GetMapping("/v1/users")
    public ResponseEntity<UserDto> findUserDetails(@AuthenticationPrincipal UserInfo userInfo){
        
        User loginUser = userInfo.getUser();
        
        return ResponseEntity.ok(UserDto.from(loginUser));
    }
    
    
    // <== 유저 정보 수정 ==>
    @PutMapping("/v1/users")
    public ResponseEntity<UserDto> updateUser(@AuthenticationPrincipal UserInfo userInfo,
                                                            @RequestBody UpdateUserRequest request){
        
        User loginUser = userInfo.getUser();
        
        User updateUser = userService.updateUser(loginUser.getId(), request.getNickName(), request.getPassword(), request.getProfile());
        
        return ResponseEntity.ok(UserDto.from(updateUser));
    }
    
    // <== 유저 워크스페이스 리스트 조회 ==>
    @GetMapping("/v1/users/workspaces")
    public ResponseEntity<UserWorkspacesResponse> readUserWorkspaces(@AuthenticationPrincipal UserInfo userInfo, Pageable pageable){
        
        User loginUser = userInfo.getUser();
        
        
        
        Page<UserWorkspace> page = userService.findWorkspaces(loginUser, pageable);
        
        List<Workspace> workspaces = page.getContent()
                                        .stream()
                                        .map(uw -> uw.getWorkspace())
                                        .collect(toList());
        
        UserWorkspacesResponse response = UserWorkspacesResponse.of(loginUser, page.getTotalPages(), page.getNumber(), workspaces);
        
        return ResponseEntity.ok(response);
    }
    
    // <== 유저 스케줄 리스트 조회 ==>
    @GetMapping("/v1/users/schedules")
    public ResponseEntity<UserSchedulesResponse> readUserSchedules(@AuthenticationPrincipal UserInfo userInfo, 
                                                                    @RequestParam String date,
                                                                    Pageable pageable){
        
        User loginUser = userInfo.getUser();
        
        LocalDateTime dateTime = parseDateString(date);
        
        Page<UserSchedule> page = userService.findSchedules(loginUser, dateTime, pageable);
        
        List<Schedule> schedules = page.getContent()
                                        .stream()
                                        .map(us -> us.getSchedule())
                                        .collect(toList());
        
        UserSchedulesResponse response = UserSchedulesResponse.of(loginUser, page.getTotalPages(), page.getNumber(), schedules);
        
        return ResponseEntity.ok(response);
    }
    
    private LocalDateTime parseDateString(String dateStr){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            return LocalDate.parse(dateStr, formatter).atStartOfDay();
        }
        catch (DateTimeParseException e){
            throw new DateFormatException();
        }
    }
    
    // @PutMapping("/users/{userId}")
    // public ResponseEntity<FindSingleUserResponse> updateUser(@PathVariable("userId") Long userId,
    //                                                         @RequestBody UpdateUserRequest request){
        
    //     User updateUser = userService.updateUser(userId, request.getPassword(), request.getName());
        
    //     FindSingleUserResponse response = new FindSingleUserResponse(updateUser);
        
    //     return ResponseEntity.ok(response);
    // }
    
    // @DeleteMapping("/users/{userId}")
    // public ResponseEntity<DeleteUserResponse> deleteUser(@PathVariable("userId") Long userId){
    //     userService.deleteUser(userId);
        
    //     return ResponseEntity.ok(new DeleteUserResponse());
    // }
}