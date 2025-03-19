package project.domain.user.dao;

import java.util.List;
import java.util.Optional;
import project.domain.user.domain.User;

public interface UserRepositoryCustom {
    public Optional<User> searchUser(String keyword);

    public List<User> searchByKeyword(Long loginUserId, String keyword);
}
