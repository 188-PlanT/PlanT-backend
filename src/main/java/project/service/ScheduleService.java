package project.service;

import project.domain.*;
import project.dto.schedule.*;
import project.exception.schedule.*;
import project.exception.user.*;
import project.exception.workspace.*;
import project.exception.devLog.*;
import project.repository.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import static java.util.stream.Collectors.toList;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService{
    
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;
    private final DevLogRepository devLogRepository;
    
    // <== 스케줄 단일 조회 ==>
    @Transactional(readOnly = true)
    public ScheduleDto findOne(Long id) {
        
        Schedule schedule = scheduleRepository.findById(id)
            .orElseThrow(NoSuchScheduleException::new);
        
        ScheduleDto dto = ScheduleDto.from(schedule);
        
        return dto;
    }
    
    // <== 스케줄 생성 ==>
    @Transactional
    public ScheduleDto createSchedule(CreateScheduleRequest request){
    
        Workspace workspace = workspaceRepository.findById(request.getWorkspaceId())
            .orElseThrow(NoSuchWorkspaceException::new);

        List <User> users = userRepository.findByIdIn(request.getUsers());
        
        //validate
        if(users.size() != request.getUsers().size()){
            throw new NoSuchUserException();
        }
        
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
        
        Schedule schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(NoSuchScheduleException::new);

        List <User> users = userRepository.findByIdIn(request.getUsers());
        
        //validate
        if(users.size() != request.getUsers().size()){
            throw new NoSuchUserException();
        }
        
        schedule.update(request.getName(), request.getStartDate(), request.getEndDate(), request.getContent(), users, request.getState());
        
        ScheduleDto dto = ScheduleDto.from(schedule);
        
        return dto;
    }
    
    // <== 스케줄 삭제 ==>
    @Transactional
    public void removeSchedule(Long id){
        Schedule schedule = scheduleRepository.findById(id)
            .orElseThrow(NoSuchScheduleException::new);
        
        scheduleRepository.delete(schedule);
    }
    
    // <== 스케줄 상태 수정 ==>
    @Transactional
    public ScheduleDto moveScheduleState(Long id, Progress state) {
        Schedule schedule = scheduleRepository.findById(id)
            .orElseThrow(NoSuchScheduleException::new);
        
        schedule.moveProgress(state);
        
        ScheduleDto dto = ScheduleDto.from(schedule);
        
        return dto;
    }
    
    // <== 댓글 추가 ==>
    @Transactional
    public AddChatResponse addChat(String loginUserEmail, Long scheduleId, String content) {
        User loginUser = userRepository.findByEmail(loginUserEmail)
            .orElseThrow(NoSuchUserException::new);
        
        Schedule schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(NoSuchScheduleException::new);
        
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
    public AddChatResponse updateChat(String loginUserEmail, Long scheduleId, Long chatId, String content) {
        User loginUser = userRepository.findByEmail(loginUserEmail)
            .orElseThrow(NoSuchUserException::new);
        
        Schedule schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(NoSuchScheduleException::new);

        DevLog chat = devLogRepository.findById(chatId)
            .orElseThrow(NoSuchChatException::new);
        
        if (chat.getSchedule().equals(schedule) && chat.getUser().equals(loginUser)){
            chat.updateContent(content);
        }
        else{
            throw new InvalidAuthorityException();
        }
        
        devLogRepository.save(chat);
        
        AddChatResponse response = AddChatResponse.from(chat);
        
        return response;
    }
    
    // <== 댓글 삭제 ==>
    @Transactional
    public void removeChat(String loginUserEmail, Long scheduleId, Long chatId) {
        User loginUser = userRepository.findByEmail(loginUserEmail)
            .orElseThrow(NoSuchUserException::new);
        
        Schedule schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(NoSuchScheduleException::new);

        schedule.removeChat(loginUser, chatId);
    }
}