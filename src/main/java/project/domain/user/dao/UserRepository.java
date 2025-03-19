package project.domain.user.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.domain.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    @Query(value = "select u from User u join fetch u.profile p where u.id=:id")
    public Optional<User> findById(Long id);

    public boolean existsByEmail(String email);

    public Optional<User> findByEmail(String email);

    public boolean existsByNickName(String nickName);

    public List<User> findByIdIn(List<Long> userIds);
}
