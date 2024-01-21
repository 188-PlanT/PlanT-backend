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
    
    //쿼리 4번 나감 -> 페이징 포기하면 1번으로 변경 가능한데..
    @Query("select us from UserSchedule us  where us.user =:user and us.schedule.startDate <= :date and us.schedule.endDate >= :date")
    public Page<UserSchedule> searchByUser(User user, LocalDateTime date, Pageable pageable);
}