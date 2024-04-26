package project.domain.user.api;

import project.common.security.oauth.UserInfo;
import project.common.util.DateFormatUtil;
import project.common.util.UserUtil;
import project.domain.schedule.domain.UserSchedule;
import project.domain.user.domain.User;
import project.domain.user.dto.user.*;
import project.domain.workspace.domain.UserWorkspace;
import project.common.exception.PlantException;
import project.domain.user.service.UserService;
import project.common.service.EmailService;
import project.common.security.jwt.JwtProvider;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController{
    
    private final UserService userService;
    private final EmailService emailService;
	private final JwtProvider jwtProvider;
    private final UserUtil userUtil;
    

    // <==유저 이메일 검증==>
    @PostMapping("/v1/users/email")
    public ResponseEntity<EmailCheckResponse> checkEmailAvailable(@Valid @RequestBody EmailCheckRequest request){
        
        try{
            userService.validateUserEmail(request.getEmail());
        } 
        catch (PlantException e){
            return ResponseEntity.ok(new EmailCheckResponse(false));
        }

        return ResponseEntity.ok(new EmailCheckResponse(true));
    }

    // <==유저 닉네임 검증==>
    @PostMapping("/v1/users/nickname") //응답 결과 양식은 이메일과 같으므로 공유함
    public ResponseEntity<EmailCheckResponse> checkNickNameAvailable(@Valid @RequestBody NickNameCheckRequest request){
        
        try{
            userService.validateUserNickName(request.getNickName());
            return ResponseEntity.ok(new EmailCheckResponse(true));
        } 
        catch (PlantException e){
            return ResponseEntity.ok(new EmailCheckResponse(false));
        }
    }
    
    // <== 유저 닉네임 추가 ==>
    @PutMapping("/v1/users/nickname")
    public ResponseEntity<FinishUserRegisterResponse> setNickNameUser(@Valid @RequestBody FinishUserRegisterRequest request){
        
        User user = userService.finishRegister(request.getNickName());
		
		String accessToken = jwtProvider.createAccessToken(user);

        return ResponseEntity.ok(FinishUserRegisterResponse.from(user, accessToken));
    }
    
    
    // <== 유저 정보 확인 ==>
    @GetMapping("/v1/users")
    public ResponseEntity<UserDto> findUserDetails(){
        
        User loginUser = userUtil.getLoginUser();
        
        return ResponseEntity.ok(UserDto.from(loginUser));
    }
    
    
    // <== 유저 정보 수정 ==>
    @PutMapping("/v1/users")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UpdateUserRequest request){
        
        User updateUser = userService.updateUser(request);
        
        return ResponseEntity.ok(UserDto.from(updateUser));
    }
    
    // <== 유저 워크스페이스 리스트 조회 ==>
    @GetMapping("/v1/users/workspaces")
    public ResponseEntity<UserWorkspacesResponse> readUserWorkspaces(){
		
//        List<UserWorkspace> userWorkspaces = userService.findWorkspaces();

        UserWorkspacesResponse response = userService.findWorkspaces();
        
        return ResponseEntity.ok(response);
    }
    
    // <== 유저 스케줄 리스트 조회 ==>
    @GetMapping("/v1/users/schedules")
    public ResponseEntity<UserSchedulesResponse> readUserSchedules(@RequestParam String date){
		
        LocalDateTime dateTime = DateFormatUtil.parseStartOfMonth(date);
        
        UserSchedulesResponse response  = userService.findSchedules(dateTime);
        
        return ResponseEntity.ok(response);
    }
    
    // <== 유저 검색 ==>
    @GetMapping("/v1/users/search")
    public ResponseEntity<SearchUserResponse> searchUser(@RequestParam String keyword){

        List<User> users = userService.searchUser(keyword);
        
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