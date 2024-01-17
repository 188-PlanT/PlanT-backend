package project.repository;

import java.util.List;
import project.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom{
    public Page<User> searchUsers(Pageable pageable, String email);
}