package project.api;

import project.domain.*;
import project.dto.user.*;
import project.service.UserService;
import project.exception.user.NoSuchUserException;
import project.dto.ErrorResponse;

import java.util.List;
import javax.validation.Valid;
import static java.util.stream.Collectors.toList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;


import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;



@RestController
@RequiredArgsConstructor
public class UserController{
    
    private final UserService userService;
    
    //근데 accountId로 검색이 굳이 필요할까..?
    @GetMapping("/users")
    public ResponseEntity<FindAllUserResponse> findAllUsers(Pageable pageable){
        
        Page<User> page = userService.findAllUsers(pageable);
        
        List<UserDto> responseData = page.getContent()
            .stream()
            .map(UserDto::new)
            .collect(toList());
        
        FindAllUserResponse response = new FindAllUserResponse(page.getTotalPages(), page.getNumber(), responseData);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/users")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody CreateUserRequest request){
        
        User user = request.toUser();
        
        Long userId = userService.register(user);
        
        return ResponseEntity.ok(userId);    
    }
    
    @GetMapping("/users/{userId}")
    public ResponseEntity<FindSingleUserResponse> findUserDetail(@PathVariable("userId") Long userId){
        
        User findUser = userService.findOne(userId);
        
        FindSingleUserResponse response = new FindSingleUserResponse(findUser);
        
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/users/{userId}")
    public ResponseEntity<FindSingleUserResponse> updateUser(@PathVariable("userId") Long userId,
                                                            @RequestBody UpdateUserRequest request){
        
        User updateUser = userService.updateUser(userId, request.getPassword(), request.getName());
        
        FindSingleUserResponse response = new FindSingleUserResponse(updateUser);
        
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<DeleteUserResponse> deleteUser(@PathVariable("userId") Long userId){
        userService.deleteUser(userId);
        
        return ResponseEntity.ok(new DeleteUserResponse());
    }
}