package project.domain.schedule.dao;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.domain.schedule.domain.UserSchedule;

public interface UserScheduleRepository extends JpaRepository<UserSchedule, Long> {
    
    @Query("select us from UserSchedule us " + 
			"join fetch us.user u join fetch us.schedule s join fetch s.workspace w " +
			"where us.user.email =:email and " +
            "((us.schedule.startDate < :startDate and us.schedule.endDate >= :startDate) or (us.schedule.startDate between :startDate and :endDate))")
    public List<UserSchedule> searchByUser(String email, LocalDateTime startDate, LocalDateTime endDate);
}