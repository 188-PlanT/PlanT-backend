package project.service;

import project.domain.*;
import project.dto.schedule.*;
import project.exception.schedule.*;
import project.exception.user.*;
import project.exception.workspace.*;
import project.repository.ScheduleRepository;
import project.repository.UserRepository;
import project.repository.WorkspaceRepository;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import static java.util.stream.Collectors.toList;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class ScheduleService{
    
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;
    
    @Transactional(readOnly = true)
    public Schedule findOne(Long id) {
        return scheduleRepository.findById(id)
            .orElseThrow(NoSuchScheduleException::new);
    }
    
    @Transactional(readOnly = true)
    public Page<Schedule> findSchedules(Pageable pageable){
        
        return scheduleRepository.findAll(pageable);
    }
    
    
    @Transactional
    public Long createSchedule(CreateScheduleRequest request){
    
        Workspace workspace = workspaceRepository.findByName(request.getWorkspace())
            .orElseThrow(NoSuchWorkspaceException::new);

        List <User> users = userRepository.findUsersByEmailList(request.getUsers());
        
        Schedule schedule = Schedule.builder()
                                        .workspace(workspace)
                                        .name(request.getName())
                                        .startDate(request.getStartDate())
                                        .endDate(request.getEndDate())
                                        .content(request.getContent())
                                        .users(users)
                                        .build();
            
        scheduleRepository.save(schedule);
        return schedule.getId();
    }
    
    @Transactional
    public Schedule updateSchedule(Long scheduleId, UpdateScheduleRequest request){
        
        Schedule schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(NoSuchScheduleException::new);

        List <User> users = userRepository.findUsersByEmailList(request.getUsers());
        
        schedule.update(request.getName(), request.getStartDate(), request.getEndDate(), request.getContent(), users);
        return schedule;
    }
    
    @Transactional
    public void removeSchedule(Long id){
        Schedule schedule = scheduleRepository.findById(id)
            .orElseThrow(NoSuchScheduleException::new);
        
        scheduleRepository.delete(schedule);
    }
    
    @Transactional
    public Schedule addUser(Long scheduleId, Long userId){
        Schedule schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(NoSuchScheduleException::new);
        
        User user = userRepository.findById(userId)
            .orElseThrow(NoSuchUserException::new);
        
        schedule.addUser(user);
        return schedule;
    }
    
    @Transactional
    public void removeUser(Long scheduleId, Long userId){
        Schedule schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(NoSuchScheduleException::new);

        User user = userRepository.findById(userId)
            .orElseThrow(NoSuchUserException::new);
        
        schedule.removeUser(user);
    }
}