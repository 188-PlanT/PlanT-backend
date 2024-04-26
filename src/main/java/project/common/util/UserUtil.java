package project.common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;
import project.common.security.oauth.UserInfo;
import project.domain.user.dao.UserRepository;
import project.domain.user.domain.User;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUtil {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User getLoginUser(){
        Long userId = getUserId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new PlantException(ErrorCode.USER_NOT_FOUND));
    }

    private Long getUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();
        return userInfo.getUserId();
    }
}
