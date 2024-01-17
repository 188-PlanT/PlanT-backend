package project.admin.controller;

import lombok.RequiredArgsConstructor;

import project.service.UserService;
import project.exception.user.*;
import project.exception.ValidateException;
import project.domain.User;
import project.domain.UserRole;
import project.admin.util.*;
import project.admin.dto.SignInRequest;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminController{
    
    private final UserService userService;
    
    @GetMapping("/admin") //홈 화면
    public String newHome(@Login User findUser, Model model){
        
        if (findUser == null){
            return "admin/home";
        }
        
        model.addAttribute("userName", findUser.getNickName());    
        return "admin/menu";
    }
    
    // @GetMapping("/admin/login") //로그인 폼
    // public String loginForm(Model model) {
        
    //     model.addAttribute("signInRequest", new SignInRequest());
    //     return "admin/login";
    // }
    
    // @PostMapping("/admin/login")
    // public String loginUser(@Valid SignInRequest signInRequest, 
    //                         BindingResult bindingResult,
    //                         Model model,
    //                         HttpServletRequest request){
        
    //     try{
    //         checkBindingResultHasError(bindingResult);
            
    //         User loginUser = userService.signIn(signInRequest.getEmail(), signInRequest.getPassword());
            
    //         checkUserHasAdmin(loginUser);
            
    //         setSessionUser(request, loginUser);
            
    //         return "redirect:/admin";
    //     } 
    //     catch(ValidateException e){
    //         return "admin/login";
    //     } 
    //     catch(NoSuchUserException e){
    //         bindingResult.addError(new ObjectError("signInRequest", "아이디 또는 비밀번호가 올바르지 않습니다"));
    //         return "admin/login";
    //     } 
    //     catch(UnvalidAuthorityException e){
    //         bindingResult.addError(new ObjectError("signInRequest", "ADMIN이 아닌 유저입니다"));
    //         return "admin/login";
    //     }
    // }
    
    // @PostMapping("/admin/logout")
    // public String logoutUser(HttpServletRequest request){
        
    //     HttpSession session = request.getSession(false);
        
    //     if (session == null){
    //         log.error("ADMIN : 올바르지 않은 로그아웃, 세션이 존재하지 않습니다");
    //     }

    //     session.removeAttribute(SessionConst.LOGIN_USER);

    //     return "redirect:/admin";   
    // }
    
    // //Validation
    // private void checkBindingResultHasError(BindingResult bindingResult){
    //     if (bindingResult.hasErrors()){
    //         throw new ValidateException("로그인 입력값이 올바르지 않습니다");
    //     }
    // }
    
    // //Check User has ADMIN  
    // private void checkUserHasAdmin(User user){
    //     if (!UserRole.ADMIN.equals(user.getUserRole())){
    //         throw new UnvalidAuthorityException("ADMIN 유저가 아닙니다");
    //     }
    // }
    
    // //Set User Session
    // private void setSessionUser(HttpServletRequest request, User user){

    //     HttpSession session = request.getSession(true);
    //     session.setAttribute(SessionConst.LOGIN_USER, user);
    // }
}