package project.domain.schedule.service;

import project.common.util.UserUtil;
import project.domain.schedule.dao.DevLogRepository;
import project.domain.schedule.domain.DevLog;
import project.domain.schedule.domain.UserSchedule;
import project.domain.schedule.dao.ScheduleRepository;
import project.domain.schedule.domain.Progress;
import project.domain.schedule.domain.Schedule;
import project.domain.schedule.dto.AddChatResponse;
import project.domain.schedule.dto.CreateScheduleRequest;
import project.domain.schedule.dto.ScheduleDto;
import project.domain.schedule.dto.UpdateScheduleRequest;
import project.domain.user.dao.UserRepository;
import project.domain.user.domain.User;
import project.domain.workspace.dao.WorkspaceRepository;
import project.domain.workspace.domain.Workspace;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService{
    
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;
    private final DevLogRepository devLogRepository;
    private final UserUtil userUtil;
    
    // <== 스케줄 단일 조회 ==>
    @Transactional(readOnly = true)
    public ScheduleDto findOne(Long id) {
        
        Schedule schedule = findScheduleById(id);
        
        ScheduleDto dto = ScheduleDto.from(schedule);
        
        return dto;
    }
    
    // <== 스케줄 생성 ==>
    @Transactional
    public ScheduleDto createSchedule(CreateScheduleRequest request){
    
        Workspace workspace = workspaceRepository.findById(request.getWorkspaceId())
            .orElseThrow(() -> new PlantException(ErrorCode.WORKSPACE_NOT_FOUND));

        Long loginUserId = userUtil.getLoginUserId();

        // api url에 workspaceId 가 들어가지 않으므로 Interceptor에서 검증 불가능 -> 서비스에서 검증
        workspace.checkUser(loginUserId);

        List <User> users = userUtil.getUserByList(request.getUsers());
        
        Schedule schedule = Schedule.builder()
                                        .workspace(workspace)
                                        .name(request.getName())
                                        .startDate(request.getStartDate())
                                        .endDate(request.getEndDate())
                                        .content(request.getContent())
                                        .users(users)
                                        .state(request.getState())
                                        .build();
            
        scheduleRepository.save(schedule);
        
        ScheduleDto dto = ScheduleDto.from(schedule);
        
        return dto;
    }
    
    // <== 스케줄 수정 ==>
    @Transactional
    public ScheduleDto updateSchedule(Long scheduleId, UpdateScheduleRequest request){
        
        Schedule schedule = findScheduleById(scheduleId);

        List <User> users = userUtil.getUserByList(request.getUsers());
        
        schedule.update(request.getName(), request.getStartDate(), request.getEndDate(), request.getContent(), users, request.getState());
        
        ScheduleDto dto = ScheduleDto.from(schedule);
        
        return dto;
    }
    
    // <== 스케줄 삭제 ==>
    @Transactional
    public void removeSchedule(Long id){
        Schedule schedule = findScheduleById(id);
        
        scheduleRepository.delete(schedule);
    }
    
    // <== 스케줄 상태 수정 ==>
    @Transactional
    public ScheduleDto moveScheduleState(Long id, Progress state) {
        Schedule schedule = findScheduleById(id);
        
        schedule.moveProgress(state);
        
        ScheduleDto dto = ScheduleDto.from(schedule);
        
        return dto;
    }
    
    // <== 댓글 추가 ==>
    @Transactional
    public AddChatResponse addChat(Long scheduleId, String content) {
        User loginUser = userUtil.getLoginUser();
        
        Schedule schedule = findScheduleById(scheduleId);
        
        DevLog chat = DevLog.builder()
                            .schedule(schedule)
                            .user(loginUser)
                            .content(content)
                            .build();
        
        devLogRepository.save(chat);
        
        AddChatResponse response = AddChatResponse.from(chat);
        
        return response;
    }
    
    // <== 댓글 수정 ==>
    @Transactional
    public AddChatResponse updateChat(Long scheduleId, Long chatId, String content) {
        User loginUser = userUtil.getLoginUser();

        Schedule schedule = findScheduleById(scheduleId);

        DevLog chat = devLogRepository.findById(chatId)
            .orElseThrow(() -> new PlantException(ErrorCode.CHAT_NOT_FOUND));
        
        if (chat.getSchedule().equals(schedule) && chat.getUser().equals(loginUser)){
            chat.updateContent(content);
        }
        else{
            throw new PlantException(ErrorCode.USER_AUTHORITY_INVALID);
        }
        
        devLogRepository.save(chat);
        
        AddChatResponse response = AddChatResponse.from(chat);
        
        return response;
    }
    
    // <== 댓글 삭제 ==>
    @Transactional
    public void removeChat(Long scheduleId, Long chatId) {
        User loginUser = userUtil.getLoginUser();

        Schedule schedule = findScheduleById(scheduleId);

        schedule.removeChat(loginUser, chatId);
    }

    // <== admin용 전체 조회 ==>
    @Transactional(readOnly = true)
    public Schedule findOneDetail(Long id){
        Schedule schedule = findScheduleById(id);

        for (UserSchedule us : schedule.getUserSchedules()){
            us.getUser().getEmail();
        }

        for (DevLog chat : schedule.getDevLogs()){
            chat.getContent();
            chat.getUser().getEmail();
        }

        return schedule;
    }

    private Schedule findScheduleById(Long id){
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new PlantException(ErrorCode.SCHEDULE_NOT_FOUND));
    }
}