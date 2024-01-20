package project.repository;

import project.domain.*;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserWorkspaceRepository extends JpaRepository<UserWorkspace, Long> {
    
    @Query("select uw from UserWorkspace uw  where uw.user =:user")
    public Page<UserWorkspace> searchByUser(User user, Pageable pageable);
}