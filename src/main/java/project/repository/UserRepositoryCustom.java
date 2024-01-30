package project.repository;

import project.domain.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom{
    public Optional<User> searchUser(String keyword);
}