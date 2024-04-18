//package project.common.admin.controller;
//
//import project.domain.schedule.service.ScheduleService;
//import project.domain.workspace.service.WorkspaceService;
//import project.domain.user.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Controller
//@RequiredArgsConstructor
//public class AdminScheduleController{
//
//    private final ScheduleService scheduleService;
//    private final WorkspaceService workspaceService;
//    private final UserService userService;
//    private final DevLogService devLogService;
//
//    // 메뉴 화면
//    // @GetMapping("/admin/schedules")
//    // public String scheduleMenu(){
//    //     return "admin/schedules/schedules-menu";
//    // }
//
//    // // 전체 스케줄 리스트
//    // @GetMapping("/admin/schedules/read-schedules")
//    // public String readScheduleList(Model model){
//
//    //     Pageable pageable = PageRequest.of(0,20);
//
//    //     Page<Schedule> page = scheduleService.findSchedules(pageable);
//
//    //     List<ScheduleDto> scheduleList = page.getContent()
//    //         .stream()
//    //         .map(ScheduleDto::new)
//    //         .collect(toList());
//
//    //     model.addAttribute("scheduleList", scheduleList);
//
//    //     return "admin/schedules/schedules-list";
//    // }
//
//    // // 스케줄 상세 화면
//    // @GetMapping("/admin/schedules/{scheduleId}")
//    // public String readScheduleDetail(@PathVariable Long scheduleId, Model model){
//
//    //     Schedule findSchedule = scheduleService.findOne(scheduleId);
//
//    //     FindSingleScheduleResponse schedule = new FindSingleScheduleResponse(findSchedule);
//
//    //     model.addAttribute("schedule", schedule);
//
//    //     return "admin/schedules/schedules-detail";
//    // }
//
//    // // 스케줄 생성 화면
//    // @GetMapping("/admin/schedules/create-schedules")
//    // public String createScheduleForm(Model model){
//
//    //     Pageable pageable = PageRequest.of(0,20);
//    //     List<Workspace> workspaces = workspaceService.findAll(pageable).getContent();
//    //     List<User> users = userService.findAllUsers(pageable).getContent();
//
//    //     List<String> workspaceList = workspaces.stream()
//    //         .map(w -> w.getName())
//    //         .collect(toList());
//
//    //     List<String> userList = users.stream()
//    //         .map(u -> u.getEmail())
//    //         .collect(toList());
//
//    //     model.addAttribute("workspaceList", workspaceList);
//    //     model.addAttribute("userList", userList);
//    //     model.addAttribute("createScheduleRequest", new CreateScheduleRequest());
//
//    //     return "admin/schedules/schedules-create-form";
//    // }
//
//    // // 스케줄 생성
//    // @PostMapping("/admin/schedules/create-schedules")
//    // public String createSchedule(@Valid CreateScheduleRequest createScheduleRequest,
//    //                              BindingResult bindingResult,
//    //                              @RequestParam List<String> workspaceList,
//    //                              @RequestParam List<String> userList,
//    //                              Model model){
//
//    //     if(bindingResult.hasErrors()){ // 검증 실패
//    //         model.addAttribute("workspaceList", workspaceList);
//    //         model.addAttribute("userList", userList);
//    //         return "admin/schedules/schedules-create-form";
//    //     }
//    //     else { //검증 성공
//
//    //         scheduleService.createSchedule(createScheduleRequest);
//
//    //         return "redirect:/admin/schedules/read-schedules";
//    //     }
//    // }
//
//    // //스케줄 수정 화면
//    // @GetMapping("/admin/schedules/{scheduleId}/update")
//    // public String updateScheduleForm(@PathVariable Long scheduleId, Model model){
//
//    //     Schedule schedule = scheduleService.findOne(scheduleId);
//
//    //     Pageable pageable = PageRequest.of(0,20);
//    //     List<User> users = userService.findAllUsers(pageable).getContent();
//
//    //     List<String> userList = users.stream()
//    //         .map(u -> u.getEmail())
//    //         .collect(toList());
//
//    //     model.addAttribute("userList", userList);
//    //     model.addAttribute("scheduleId", scheduleId);
//    //     model.addAttribute("updateScheduleRequest", new UpdateScheduleRequest(schedule));
//
//    //     return "admin/schedules/schedules-update-form";
//    // }
//
//    // //스케줄 수정
//    // @PostMapping("/admin/schedules/{scheduleId}/update")
//    // public String updateSchedule(@PathVariable Long scheduleId,
//    //                              @Valid UpdateScheduleRequest updateScheduleRequest,
//    //                              BindingResult bindingResult,
//    //                              @RequestParam List<String> userList,
//    //                              Model model){
//
//    //     if(bindingResult.hasErrors()){ // 검증 실패
//    //         model.addAttribute("userList", userList);
//    //         model.addAttribute("scheduleId", scheduleId);
//    //         return "admin/schedules/schedules-update-form";
//    //     }
//    //     else { //검증 성공
//
//    //         scheduleService.updateSchedule(scheduleId, updateScheduleRequest);
//
//    //         return "redirect:/admin/schedules/{scheduleId}";
//    //     }
//    // }
//
//
//    // //스케줄 삭제 화면
//    // @PostMapping("/admin/schedules/{scheduleId}/delete")
//    // public String deleteSchedule(@PathVariable Long scheduleId){
//
//    //     scheduleService.removeSchedule(scheduleId);
//
//    //     return "redirect:/admin/schedules/read-schedules";
//    // }
//
//    // // 개발일지 추가 화면
//    // @GetMapping("/admin/schedules/{scheduleId}/devLogs")
//    // public String addDevLogsForm(@PathVariable Long scheduleId, Model model){
//
//    //     Pageable pageable = PageRequest.of(0,20);
//    //     List<User> users = userService.findAllUsers(pageable).getContent();
//
//    //     List<String> userList = users.stream()
//    //         .map(u -> u.getEmail())
//    //         .collect(toList());
//
//    //     model.addAttribute("userList", userList);
//    //     model.addAttribute("createDevLogRequest", new CreateDevLogRequest(scheduleId,null,null));
//
//    //     return "admin/schedules/devLogs-add-form.html";
//    // }
//
//    // // 개발일지 추가
//    // @PostMapping("/admin/schedules/{scheduleId}/devLogs")
//    // public String addDevLogs(@PathVariable Long scheduleId,
//    //                         @Valid CreateDevLogRequest createDevLogRequest,
//    //                         BindingResult bindingResult,
//    //                         @RequestParam List<String> userList,
//    //                         Model model){
//
//    //     if(bindingResult.hasErrors()){ // 검증 실패
//    //         model.addAttribute("userList", userList);
//    //         return "admin/schedules/devLogs-add-form.html";
//    //     }
//    //     else { //검증 성공
//
//    //         devLogService.createDevLog(createDevLogRequest);
//
//    //         return "redirect:/admin/schedules/{scheduleId}";
//    //     }
//    // }
//}