package project.domain.user.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.domain.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom{

    @Query("select u from User u join fetch u.profile p where u.id=:id")
	public Optional<User> findById(Long id);
    
    public boolean existsByEmail(String email);
    
    public Optional<User> findByEmail(String email);
    
    public boolean existsByNickName(String nickName);
    
    public List<User> findByIdIn(List<Long> userIds);
    
    @Query("select u from User u " +
		   "join fetch u.profile p " +
		   "where (u.email like concat('%', :keyword, '%') or u.nickName like concat('%', :keyword, '%')) " +
		   "and u.userRole != 'PENDING' and u.id != :loginUserId")
    public List<User> searchByKeyword(Long loginUserId, String keyword);
}