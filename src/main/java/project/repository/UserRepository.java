package project.repository;

import project.domain.*;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom{
    
    public boolean existsByEmail(String email);
    
    public Optional<User> findByEmail(String email);
    
    public Optional<User> findByNickName(String nickName);
    
    public boolean existsByNickName(String nickName);
    
    @Query("select u from User u where u.email in :userEmails")
    public List<User> findUsersByEmailList(@Param("userEmails") List<String> userEmails);
    
    public List<User> findByIdIn(List<Long> userIds);
    
    @Query("select u from User u where (u.email = :keyword or u.nickName = :keyword) and u.userRole != 'PENDING' ")
    public Optional<User> searchByKeyword(String keyword);
}