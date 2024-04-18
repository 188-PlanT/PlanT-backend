package project.domain.workspace.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.domain.workspace.domain.Workspace;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long>{
    
    public Page<Workspace> findAll(Pageable pageable);
    
    public Optional<Workspace> findByName(String name);
    
    public boolean existsByName(String name);

    @Query("select w from Workspace w join fetch w.profile where w.id = :id")
    public Optional<Workspace> findById(Long id);
}