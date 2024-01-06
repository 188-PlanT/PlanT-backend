package project.repository;

import project.domain.*;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DevLogRepository extends JpaRepository<DevLog, Long>, DevLogRepositoryCustom{
        
    // public void save(DevLog devLog);
    
    // public void remove(DevLog devLog);
    
    // public DevLog findOne(Long id);
        
    // public List<DevLog> searchDevLogs(int offset, int limit, Schedule schedule, User user);
    
    public boolean existsByScheduleAndUser(Schedule schedule, User user);
}