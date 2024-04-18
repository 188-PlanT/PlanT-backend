package project.domain.schedule.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import project.domain.schedule.domain.DevLog;

public interface DevLogRepository extends JpaRepository<DevLog, Long>{
        
    // public void save(DevLog devLog);
    
    // public void remove(DevLog devLog);
    
    // public DevLog findOne(Long id);
    
    // public Optional<DevLog> findById(Long id);
        
    // public List<DevLog> searchDevLogs(int offset, int limit, Schedule schedule, User user);
    
//    public boolean existsByScheduleAndUser(Schedule schedule, User user);
}