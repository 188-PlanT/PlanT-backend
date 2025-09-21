package project.domain.scheduleUser.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import project.domain.scheduleUser.domain.ScheduleUser;

public interface ScheduleUserRepository extends JpaRepository<ScheduleUser, Long> {}
