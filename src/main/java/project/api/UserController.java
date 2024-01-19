package project.api;

import project.domain.*;
import project.common.auth.oauth.UserInfo;
import project.dto.user.*;
import project.service.UserService;
import project.exception.user.*;

import java.util.List;
import javax.validation.Valid;
import static java.util.stream.Collectors.toList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
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
    
    // <==유저 추가==>
    @PostMapping("/v1/users")
    public ResponseEntity<CreateUserResponse> registerUser(@Valid @RequestBody CreateUserRequest request){
        
        User user = userService.register(request);
        
        CreateUserResponse response = new CreateUserResponse(user.getId(), user.getEmail());

        return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);    
    }

    // <==유저 이메일 인증==>
    @PostMapping("/v1/users/nickname")
    public ResponseEntity<EmailCheckResponse> checkNickNameAvailable(@AuthenticationPrincipal UserInfo userInfo, 
                                                                     @RequestBody NickNameCheckRequest request){
        
        User loginUser = userInfo.getUser();
        
        if (loginUser.checkFinishSignUp()){
            throw new InvalidAuthorityException("이미 로그인이 완료된 유저입니다");
        }
        
        try{
            userService.validateUserNickName(request.getNickName());
            return ResponseEntity.ok(new EmailCheckResponse(true));
        } 
        catch (UserAlreadyExistException e){
            return ResponseEntity.ok(new EmailCheckResponse(false));
        }
    }
    
    @PutMapping("/v1/users/nickname")
    public ResponseEntity<UserDto> setNickNameUser(@AuthenticationPrincipal UserInfo userInfo, 
                                                 @RequestBody FinishUserRegisterRequest request){
        
        //여기 함수로 분리?
        User loginUser = userInfo.getUser();
        
        if (loginUser.checkFinishSignUp()){
            throw new InvalidAuthorityException("이미 로그인이 완료된 유저입니다");
        }
        
        User user = userService.finishRegister(loginUser.getId(), request.getNickName());
        
        //여기서 부터 개발 시작
        return ResponseEntity.ok(UserDto.from(user));
    }
    
    // //개발용 기능 , 유저 목록 확인
    // @GetMapping("/users")
    // public ResponseEntity<FindAllUserResponse> findAllUsers(Pageable pageable){
        
    //     Page<User> page = userService.findAllUsers(pageable);
        
    //     List<UserDto> responseData = page.getContent()
    //         .stream()
    //         .map(UserDto::new)
    //         .collect(toList());
        
    //     FindAllUserResponse response = new FindAllUserResponse(page.getTotalPages(), page.getNumber(), responseData);
        
    //     return ResponseEntity.ok(response);
    // }
    
    //유저 상세 정보 확인
    // @GetMapping("/users/{userId}")
    // public ResponseEntity<Long> findUserDetail(@PathVariable("userId") Long userId){
        
    //     User findUser = userService.findOne(userId);
        
    //     return  ResponseEntity
    //                     .status(HttpStatus.BAD_REQUEST)
    //                     .body(null);
    
    // }
    
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