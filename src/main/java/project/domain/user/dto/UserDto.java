package project.domain.user.dto;

import project.domain.user.domain.User;
import project.domain.user.domain.UserRole;

public record UserDto(Long userId, String nickName, String email, String profile, UserRole state) {

    public static UserDto from(User user) {
        return new UserDto(
                user.getId(),
                user.getNickName(),
                user.getEmail(),
                user.getProfile().getUrl(),
                user.getUserRole());
    }
}
