//package project.common.admin.controller;
//
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
//public class AdminWorkspaceController{
//
//    private final WorkspaceService workspaceService;
//    private final UserService userService;
//
//    // 메뉴 화면
//    // @GetMapping("/admin/workspaces")
//    // public String workspaceMenu(){
//    //     return "admin/workspaces/workspaces-menu";
//    // }
//
//    // // 전체 워크스페이스 리스트
//    // @GetMapping("/admin/workspaces/read-workspaces")
//    // public String readWorkspaceList(Model model){
//
//    //     Pageable pageable = PageRequest.of(0,20);
//
//    //     Page<Workspace> page = workspaceService.findAll(pageable);
//
//    //     List<WorkspaceDto> workspaceList = page.getContent()
//    //         .stream()
//    //         .map(WorkspaceDto::new)
//    //         .collect(toList());
//
//    //     model.addAttribute("workspaceList", workspaceList);
//
//    //     return "admin/workspaces/workspaces-list";
//    // }
//
//    // // 워크스페이스 상세 화면
//    // @GetMapping("/admin/workspaces/{workspaceId}")
//    // public String readWorkspaceDetail(@PathVariable Long workspaceId, Model model){
//
//    //     Workspace findWorkspace = workspaceService.findOne(workspaceId);
//
//    //     FindSingleWorkspaceResponse workspace = new FindSingleWorkspaceResponse(findWorkspace);
//
//    //     model.addAttribute("workspace", workspace);
//
//    //     return "admin/workspaces/workspaces-detail";
//    // }
//
//    // // 워크스페이스 생성 화면
//    // @GetMapping("/admin/workspaces/create-workspaces")
//    // public String createWorkspaceForm(Model model){
//
//    //     Pageable pageable = PageRequest.of(0,20);
//    //     List<User> users = userService.findAllUsers(pageable).getContent();
//
//    //     List<String> userList = users.stream()
//    //         .map(u -> u.getEmail())
//    //         .collect(toList());
//
//    //     model.addAttribute("userList", userList);
//    //     model.addAttribute("createWorkspaceRequest", new CreateWorkspaceRequest());
//
//    //     return "admin/workspaces/workspaces-create-form";
//    // }
//
//    // //워크스페이스 생성
//    // @PostMapping("/admin/workspaces/create-workspaces")
//    // public String createWorkspace(@Valid CreateWorkspaceRequest createWorkspaceRequest,
//    //                               BindingResult bindingResult,
//    //                               @RequestParam List<String> userList,
//    //                               Model model){
//
//    //     if(bindingResult.hasErrors()){ // 검증 실패
//    //         model.addAttribute("userList", userList);
//    //         return "admin/workspaces/workspaces-create-form";
//    //     }
//    //     else { //검증 성공
//    //         workspaceService.makeWorkspace(createWorkspaceRequest);
//    //         return "redirect:/admin/workspaces/read-workspaces";
//    //     }
//    // }
//
//    // //워크스페이스 수정 화면
//    // @GetMapping("/admin/workspaces/{workspaceId}/update")
//    // public String updateWorkspaceForm(@PathVariable Long workspaceId, Model model){
//
//    //     Workspace workspace = workspaceService.findOne(workspaceId);
//
//    //     Pageable pageable = PageRequest.of(0,20);
//    //     List<User> users = userService.findAllUsers(pageable).getContent();
//
//    //     List<String> userList = users.stream()
//    //         .map(u -> u.getEmail())
//    //         .collect(toList());
//
//    //     model.addAttribute("userList", userList);
//    //     model.addAttribute("workspaceId", workspaceId);
//    //     model.addAttribute("createWorkspaceRequest", new CreateWorkspaceRequest(workspace));
//
//    //     return "admin/workspaces/workspaces-update-form";
//    // }
//
//    // @PostMapping("/admin/workspaces/{workspaceId}/update")
//    // public String updateWorkspace(@PathVariable Long workspaceId,
//    //                               @Valid CreateWorkspaceRequest request,
//    //                               BindingResult bindingResult,
//    //                               @RequestParam List<String> userList,
//    //                               Model model){
//
//    //     if(bindingResult.hasErrors()){ // 검증 실패
//    //         model.addAttribute("userList", userList);
//    //         model.addAttribute("workspaceId", workspaceId);
//    //         return "admin/workspaces/workspaces-update-form";
//    //     }
//    //     else { //검증 성공
//    //         workspaceService.updateWorkspace(workspaceId, request);
//    //         return "redirect:/admin/workspaces/{workspaceId}";
//    //     }
//    // }
//
//
//    // //워크스페이스 삭제 화면
//    // @PostMapping("/admin/workspaces/{workspaceId}/delete")
//    // public String deleteWorkspace(@PathVariable Long workspaceId){
//
//    //     workspaceService.removeWorkspace(workspaceId);
//
//    //     return "redirect:/admin/workspaces/read-workspaces";
//    // }
//}