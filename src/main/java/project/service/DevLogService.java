package project.service;

import project.domain.*;
import project.dto.devLog.*;
import project.exception.user.*;
import project.exception.schedule.*;
import project.exception.devLog.*;
import project.repository.DevLogRepository;
import project.repository.UserRepository;
import project.repository.ScheduleRepository;
import project.dto.devLog.CreateDevLogRequest;

import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Service
@RequiredArgsConstructor
public class DevLogService{

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final DevLogRepository devLogRepository;
    
    @Transactional
    public Long createDevLog(CreateDevLogRequest createDevLogRequest){
        
        Schedule schedule = scheduleRepository.findById(createDevLogRequest.getScheduleId())
            .orElseThrow(NoSuchScheduleException::new);
        
        User user = userRepository.findById(createDevLogRequest.getUserId())
            .orElseThrow(NoSuchUserException::new);
        
        validateDevLog(schedule, user);
        
        DevLog devLog = DevLog.builder()
                                .schedule(schedule)
                                .user(user)
                                .content(content);
        devLogRepository.save(devLog);
        
        return devLog.getId(); // id만 반환하는거 맞을까?
    }
    
    public Page<DevLog> findAllBySearch(Pageable pageable, Long scheduleId, String email){
        
        //없으면 오류날리는 로직 vs [] 반환하는 로직
        //전자 선택 -> queryDSL에서 탐색하면 join 쿼리 날려야하는데 N+1 또는 페이징 불가 이슈 뜰것 같음
        Schedule schedule = validateScheduleId(scheduleId);
        User user = validateEmail(email);
            
        return devLogRepository.searchDevLogs(pageable, schedule, user);
    }
    
    public DevLog findOne(Long devLogId){
            
        DevLog findDevLog = devLogRepository.findById(devLogId)
            .orElseThrow(NoSuchDevLogException::new);
        
        return findDevLog;
    }
    
    @Transactional
    public DevLog updateDevLog(Long devLogId, String content){
        DevLog findDevLog = devLogRepository.findById(devLogId)
            .orElseThrow(NoSuchDevLogException::new);
        
        findDevLog.updateContent(content);
        
        return findDevLog;
    }
    
    @Transactional
    public void deleteDevLog(Long devLogId){
        
        DevLog findDevLog = devLogRepository.findById(devLogId)
            .orElseThrow(NoSuchDevLogException::new);
        
        devLogRepository.delete(findDevLog);
    }
    
    // < ======== validate logic ======== > //
    private void validateDevLog(Schedule schedule, User user){
        
        if (devLogRepository.existsByScheduleAndUser(schedule, user)){
            throw new IllegalStateException("이미 존재하는 DevLog 입니다");
        }
    }
    
    private Schedule validateScheduleId (Long scheduleId){
        if (scheduleId == null){
            return null;
        }
        return scheduleRepository.findById(scheduleId)
            .orElseThrow(NoSuchScheduleException::new);
    }
    
    private User validateEmail (String email){
        if (email == null){
            return null;
        }
        return userRepository.findByEmail(email)
                .orElseThrow(NoSuchUserException::new);
    }
}