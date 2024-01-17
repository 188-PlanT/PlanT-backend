package project.admin.controller;

import project.domain.*;
import project.dto.devLog.*;
import project.service.ScheduleService;
import project.service.UserService;
import project.service.DevLogService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import javax.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import static java.util.stream.Collectors.toList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminDevLogController{
    
    private final ScheduleService scheduleService;
    private final UserService userService;
    private final DevLogService devLogService;

    // 메뉴 화면
    // @GetMapping("/admin/devLogs")
    // public String devLogMenu(){
    //     return "admin/devLogs/devLogs-menu";
    // }
    
    // // 전체 개발일지 리스트
    // @GetMapping("/admin/devLogs/read-devLogs")
    // public String readDevLogList(Model model){
        
    //     Pageable pageable = PageRequest.of(0,20);
        
    //     List<DevLog> devLogList = devLogService.findAllBySearch(pageable, null, null).getContent();
        
    //     model.addAttribute("devLogList", devLogList);

    //     return "admin/devLogs/devLogs-list";
    // }
    
    // // 개발일지 생성 화면
    // @GetMapping("/admin/devLogs/create-devLogs")
    // public String createDevLogForm(Model model){
        
    //     Pageable pageable = PageRequest.of(0,20);
    //     List<Schedule> scheduleList = scheduleService.findSchedules(pageable).getContent();
    //     List<User> users = userService.findAllUsers(pageable).getContent();
        
    //     List<String> userList = users.stream()
    //         .map(u -> u.getEmail())
    //         .collect(toList());
        
    //     model.addAttribute("scheduleList", scheduleList);
    //     model.addAttribute("userList", userList);
    //     model.addAttribute("createDevLogRequest", new CreateDevLogRequest());
        
    //     return "admin/devLogs/devLogs-create-form";
    // }
    
    // // 개발일지 생성
    // @PostMapping("/admin/devLogs/create-devLogs")
    // public String createDevLog(@Valid CreateDevLogRequest createDevLogRequest,
    //                           BindingResult bindingResult,
    //                           @RequestParam List<String> userList,
    //                           Model model){
        
    //     if(bindingResult.hasErrors()){ // 검증 실패
    //         Pageable pageable = PageRequest.of(0,20);
    //         List<Schedule> scheduleList = scheduleService.findSchedules(pageable).getContent();
            
    //         model.addAttribute("scheduleList", scheduleList);
    //         model.addAttribute("userList", userList);
    //         return "admin/devLogs/devLogs-create-form";
    //     }
    //     else { //검증 성공
    //         devLogService.createDevLog(createDevLogRequest);
        
    //         return "redirect:/admin/devLogs/read-devLogs";
    //     }      
    // }
    
    // // 개발일지 수정 화면
    // @GetMapping("/admin/devLogs/{devLogId}/update")
    // public String updateDevLogForm(@PathVariable Long devLogId, Model model){
        
    //     DevLog devLog = devLogService.findOne(devLogId);
    //     model.addAttribute("devLogId", devLogId);
    //     model.addAttribute("content",devLog.getContent());
        
    //     return "admin/devLogs/devLogs-update-form";
    // }
    
    // //개발일지 수정
    // @PostMapping("/admin/devLogs/{devLogId}/update")
    // public String updateDevLog(@PathVariable Long devLogId, @RequestParam String content, Model model){
        
    //     //Validate 로직
    //     if (!StringUtils.hasText(content)){
    //         model.addAttribute("devLogId", devLogId);
    //         model.addAttribute("content_error", "필수 값입니다");
    //         return "admin/devLogs/devLogs-update-form"; 
    //     }
    //     else{
    //         devLogService.updateDevLog(devLogId, content);
        
    //         return "redirect:/admin/devLogs/read-devLogs";     
    //     } 
    // }
    
    
    // // 개발일지 삭제 화면
    // @PostMapping("/admin/devLogs/{devLogId}/delete")
    // public String deleteDevLog(@PathVariable Long devLogId){
        
    //     devLogService.deleteDevLog(devLogId);
        
    //     return "redirect:/admin/devLogs/read-devLogs";  
    // }
}