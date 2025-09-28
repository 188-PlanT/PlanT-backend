package project.domain.workspace.dao;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.domain.workspace.domain.Workspace;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    Page<Workspace> findAll(Pageable pageable);

    Optional<Workspace> findById(Long id);
}
