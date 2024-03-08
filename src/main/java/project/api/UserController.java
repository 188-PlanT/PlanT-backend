package project.api;

import project.domain.*;
import project.common.auth.oauth.UserInfo;
import project.dto.user.*;
import project.service.UserService;
import project.service.EmailService;
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
    private final EmailService emailService;
    

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
        
        User user = userService.finishRegister(userInfo.getUsername(), request.getNickName());
        
        //여기서 부터 개발 시작
        return ResponseEntity.ok(UserDto.from(user));
    }
    
    
    // <== 유저 정보 확인 ==>
    @GetMapping("/v1/users")
    public ResponseEntity<UserDto> findUserDetails(@AuthenticationPrincipal UserInfo userInfo){
        
        User loginUser = userService.findByEmail(userInfo.getUsername());
        
        return ResponseEntity.ok(UserDto.from(loginUser));
    }
    
    
    // <== 유저 정보 수정 ==>
    @PutMapping("/v1/users")
    public ResponseEntity<UserDto> updateUser(@AuthenticationPrincipal UserInfo userInfo,
                                                            @RequestBody UpdateUserRequest request){
        
        User updateUser = userService.updateUser(userInfo.getUsername(), request.getNickName(), request.getPassword(), request.getProfile());
        
        return ResponseEntity.ok(UserDto.from(updateUser));
    }
    
    // <== 유저 워크스페이스 리스트 조회 ==>
    @GetMapping("/v1/users/workspaces")
    public ResponseEntity<UserWorkspacesResponse> readUserWorkspaces(@AuthenticationPrincipal UserInfo userInfo){
        
		User loginUser = userService.findByEmail(userInfo.getUsername());
		
        List<UserWorkspace> userWorkspaces = userService.findWorkspaces(userInfo.getUsername());
        
        UserWorkspacesResponse response = UserWorkspacesResponse.of(loginUser, userWorkspaces);
        
        return ResponseEntity.ok(response);
    }
    
    // <== 유저 스케줄 리스트 조회 ==>
    @GetMapping("/v1/users/schedules")
    public ResponseEntity<UserSchedulesResponse> readUserSchedules(@AuthenticationPrincipal UserInfo userInfo, 
                                                                    @RequestParam String date){
		
		User loginUser = userService.findByEmail(userInfo.getUsername());
		
        LocalDateTime dateTime = parseDateMonthString(date);
        
        List<UserSchedule> userSchedules = userService.findSchedules(userInfo.getUsername(), dateTime);
        
        UserSchedulesResponse response = UserSchedulesResponse.of(loginUser, userSchedules);
        
        return ResponseEntity.ok(response);
    }
    
    private LocalDateTime parseDateMonthString(String dateStr){
        try {
            dateStr = dateStr + "01";
            return LocalDate.parse(dateStr, DateTimeFormatter.BASIC_ISO_DATE).atStartOfDay();
        }
        catch (DateTimeParseException e){
            throw new DateFormatException();
        }
    }
    
    // <== 유저 검색 ==>
    @GetMapping("/v1/users/search")
    public ResponseEntity<SearchUserResponse> searchUser(@AuthenticationPrincipal UserInfo userInfo,
														 @RequestParam String keyword){
		Long loginUserId = userInfo.getUserId();
		
        List<User> users = userService.searchUser(loginUserId, keyword);
        
        return ResponseEntity.ok(SearchUserResponse.from(users));
    }
    
    
    // <== 이메일 인증 메일 보내기 ==>
    @GetMapping("/v1/users/email/code")
    public ResponseEntity<String> getEmailValidateCode(@RequestParam String email){
        
        int code = userService.getEmailValidateCode(email);
        
        emailService.sendValidateMail(email, code);
        
        return ResponseEntity.ok("successfully send email");
    }
    
    @PostMapping("/v1/users/email/code")
    public ResponseEntity<String> validateCode(@RequestParam String email,
                                               @RequestBody CodeRequest request){
        
        userService.validateEmailCode(email, request.getCode());

        return ResponseEntity.ok("code success");
    }
    
    @Getter
    static class CodeRequest{
        int code;
    } 
}