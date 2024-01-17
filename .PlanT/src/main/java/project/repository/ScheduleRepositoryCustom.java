package project.repository;

import java.util.List;
import project.domain.*;

public interface ScheduleRepositoryCustom{
    
    public List<Schedule> searchSchedulesByUserEmail(String email);
}