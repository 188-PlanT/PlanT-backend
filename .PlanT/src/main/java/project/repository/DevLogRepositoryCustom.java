package project.repository;

import java.util.List;
import project.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DevLogRepositoryCustom{
    public Page<DevLog> searchDevLogs(Pageable pageable, Schedule schedule, User user);
}