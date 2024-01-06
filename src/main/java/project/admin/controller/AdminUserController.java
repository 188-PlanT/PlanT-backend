package project.admin.controller;

import project.domain.*;
import project.dto.user.*;
import project.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.List;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;

import static java.util.stream.Collectors.toList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminUserController{
    
    private final UserService userService;
    
    // 메뉴 화면
    @GetMapping("/admin/users")
    public String userMenu(){
        return "admin/users/users-menu";
    }
    
    // 전체 유저 리스트
    @GetMapping("/admin/users/read-users")
    public String readUserList(Model model){
        
        Pageable pageable = PageRequest.of(0,20);
        
        Page<User> page = userService.findAllUsers(pageable);
        
        List<User> userList = page.getContent();
        
        model.addAttribute("userList", userList);
        
        return "admin/users/users-list";
    }
    
    // 유저 상세 화면
    @GetMapping("/admin/users/{userId}")
    public String readUserDetail(@PathVariable Long userId, Model model){
        
        User findUser = userService.findOne(userId);
        
        FindSingleUserResponse user = new FindSingleUserResponse(findUser);
        
        model.addAttribute("userId", userId);
        model.addAttribute("user", user);
            
        return "admin/users/users-detail";
    }
    
    // 유저 생성 화면
    @GetMapping("/admin/users/create-users")
    public String createUserForm(Model model){
        
        model.addAttribute("createUserRequest", new CreateUserRequest());
        
        return "admin/users/users-create-form";
    }
    
    //유저 생성
    @PostMapping("/admin/users/create-users")
    public String createUser(@Validated CreateUserRequest createUserRequest, 
                             BindingResult bindingResult){
        
        if(bindingResult.hasErrors()){ // 검증 실패
            return "admin/users/users-create-form";
        }
        else { //검증 성공
            User user = createUserRequest.toUser();
            Long userId = userService.register(user);
            //redirect 해야함
            return "redirect:/admin/users/read-users";    
        }
    }
    
    //유저 수정 화면
    @GetMapping("/admin/users/{userId}/update")
    public String updateUserForm(@PathVariable Long userId, Model model){
        User user = userService.findOne(userId);
        
        model.addAttribute("userId", userId);
        model.addAttribute("updateUserRequest", new UpdateUserRequest(user));
        
        return "admin/users/users-update-form";
    }
    
    //유저 수정
    @PostMapping("/admin/users/{userId}/update")
    public String updateUser(@PathVariable Long userId, 
                             @Validated UpdateUserRequest updateUserRequest, 
                             BindingResult bindingResult){
        
        if(bindingResult.hasErrors()){ // 검증 실패
            return "admin/users/users-update-form";
        }
        else { //검증 성공
            User updateUser = userService.updateUser(userId, 
                                                     updateUserRequest.getPassword(), 
                                                     updateUserRequest.getName());
            return "redirect:/admin/users/{userId}"; 
        }
    }
    
    
    //유저 삭제 화면
    @PostMapping("/admin/users/{userId}/delete")
    public String deleteUser(@PathVariable Long userId){
        
        userService.deleteUser(userId);
        
        return "redirect:/admin/users/read-users";
    }
}