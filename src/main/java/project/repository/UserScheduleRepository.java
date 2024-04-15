package project.repository;

import project.domain.*;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserScheduleRepository extends JpaRepository<UserSchedule, Long> {
    
    @Query("select us from UserSchedule us " + 
			"join fetch us.user u join fetch us.schedule s join fetch s.workspace w " +
			"where us.user.email =:email and " +
            "((us.schedule.startDate between :startDate and :endDate) or (us.schedule.endDate between :startDate and :endDate))")
    public List<UserSchedule> searchByUser(String email, LocalDateTime startDate, LocalDateTime endDate);
}