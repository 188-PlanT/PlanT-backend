package project.api;

import project.domain.*;
import project.dto.user.*;
import project.service.UserService;
import project.exception.user.*;
import project.dto.ErrorResponse;

import java.util.List;
import javax.validation.Valid;
import static java.util.stream.Collectors.toList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;


@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController{
    
    private final UserService userService;
    
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

    // 유저 이메일 인증
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

    // // 유저 닉네임 인증
    // @PostMapping("/v1/users/email")
    // public ResponseEntity<EmailCheckResponse> checkEmailAvailable(@RequestBody String email){
        
    //     try{
    //         userService.validateUserEmail(email);
    //         return ResponseEntity.ok(new EmailCheckResponse(true));
    //     } 
    //     catch (UserAlreadyExistException e){
    //         return ResponseEntity.ok(new EmailCheckResponse(false));
    //     }
    // }

    // //유저 추가
    @PostMapping("/v1/users")
    public ResponseEntity<CreateUserResponse> registerUser(@Valid @RequestBody CreateUserRequest request){
        
        User user = userService.register(request);
        
        CreateUserResponse response = new CreateUserResponse(user.getId(), user.getEmail());

        return ResponseEntity.ok(response);    
    }
    
    @PostMapping("/users/{userId}")
    public ResponseEntity<Long> setNickNameUser(@PathVariable("userId") Long userId, @RequestBody FinishUserRegisterRequest request){

        userService.finishRegister(userId, request.getNickName());
        
        return ResponseEntity.ok(userId);
    }
    
    //유저 상세 정보 확인
    @GetMapping("/users/{userId}")
    public ResponseEntity<FindSingleUserResponse> findUserDetail(@PathVariable("userId") Long userId){
        
        User findUser = userService.findOne(userId);
        
        FindSingleUserResponse response = new FindSingleUserResponse(findUser);
        
        return ResponseEntity.ok(response);
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