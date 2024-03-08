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
    
    @Query("select uw from UserWorkspace uw join fetch uw.user u join fetch uw.workspace w join fetch w.profile p where uw.user =:user")
    public List<UserWorkspace> searchByUser(User user);
	
	@Query("select uw from UserWorkspace uw " + 
		   "join fetch uw.user u join fetch uw.workspace w join fetch w.profile p " + 
		   "where u.id =:userId and w.id =:workspaceId")
	public Optional<UserWorkspace> searchByUserIdAndWorkspaceId(Long userId, Long workspaceId);
}