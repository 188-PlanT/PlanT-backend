package project.domain.user.dto.response;

import project.domain.user.domain.User;
import project.domain.user.domain.UserRole;

public record FinishUserRegisterResponse(
        Long userId, String nickName, String email, String profile, UserRole state, String accessToken) {
    public static FinishUserRegisterResponse from(User user, String accessToken) {
        return new FinishUserRegisterResponse(
                user.getId(),
                user.getNickName(),
                user.getEmail(),
                user.getProfile().getUrl(),
                user.getUserRole(),
                accessToken);
    }
}
