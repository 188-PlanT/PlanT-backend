package project.domain.schedule.dao;

import java.util.List;

import project.domain.schedule.domain.Schedule;

public interface ScheduleRepositoryCustom{
    
    public List<Schedule> searchSchedulesByUserEmail(String email);
}