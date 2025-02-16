package project.domain.user.dao;

import project.domain.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryCustom{
    public Optional<User> searchUser(String keyword);

    public List<User> searchByKeyword(Long loginUserId, String keyword);
}