package project.repository;

import project.domain.*;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepositoryCustom{
    
    @Query(value = "select s from Schedule s join fetch s.workspace w",
          countQuery = "select count(s) from Schedule s")
    public Page<Schedule> findAll(Pageable pageable);
    
}