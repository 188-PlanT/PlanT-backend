package project.common.admin.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;
import project.domain.auth.service.LoginService;
import project.domain.user.dao.UserRepository;
import project.domain.user.domain.UserRole;
import project.domain.user.service.UserService;
import project.domain.auth.dto.request.LoginRequest;
import project.domain.user.domain.User;
import project.common.admin.util.SessionConst;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.ObjectError;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminController{

    @Value("${api.main-url}")
    private String mainUrl;

    private final LoginService loginService;
    private final UserRepository userRepository;

    @GetMapping("/admin") //홈 화면
    public String home(){
        return "admin/home";
    }

     @GetMapping("/admin/login") //로그인 폼
     public String loginForm(Model model) {

         model.addAttribute("loginRequest", new LoginRequest());
         return "admin/login";
     }

     @PostMapping("/admin/login")
     public String loginUser(@Valid LoginRequest loginRequest,
                             BindingResult bindingResult,
                             Model model,
                             HttpServletRequest request){

         try{
             checkBindingResultHasError(bindingResult);

             loginService.loginByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());

             User loginUser = userRepository.findByEmail(loginRequest.getEmail())
                     .orElseThrow();

             checkUserHasAdmin(loginUser);

             setSessionUser(request, loginUser);

             return "redirect:" + mainUrl + "/admin";
         }
         catch(PlantException e){
             bindingResult.addError(new ObjectError("signInRequest", e.getMessage()));
             return "admin/login";
         }
     }

    // @PostMapping("/admin/logout")
    // public String logoutUser(HttpServletRequest request){

    //     HttpSession session = request.getSession(false);

    //     if (session == null){
    //         log.error("ADMIN : 올바르지 않은 로그아웃, 세션이 존재하지 않습니다");
    //     }

    //     session.removeAttribute(SessionConst.LOGIN_USER);

    //     return "redirect:/admin";
    // }

     //Validation
     private void checkBindingResultHasError(BindingResult bindingResult){
         if (bindingResult.hasErrors()){
             throw new PlantException(ErrorCode.USER_NOT_FOUND, "입력값이 올바르지 않습니다");
         }
     }

    // //Check User has ADMIN
     private void checkUserHasAdmin(User user){
         if (!UserRole.ADMIN.equals(user.getUserRole())){
             throw new PlantException(ErrorCode.USER_AUTHORITY_INVALID, "어드민 유저가 아닙니다");
         }
     }

    // //Set User Session
     private void setSessionUser(HttpServletRequest request, User user){
         HttpSession session = request.getSession(true);
         session.setAttribute(SessionConst.LOGIN_USER, user);
     }
}