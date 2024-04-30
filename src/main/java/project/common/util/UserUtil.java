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

    @Transactional
    public User getLoginUser(){
        Long userId = getLoginUserId();
        return getUserById(userId);
    }

    // 유저 조회가 아닌 단순히 pk 조회가 필요할때 사용
    public Long getLoginUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();
        return userInfo.getUserId();
    }
    @Transactional
    public User getUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new PlantException(ErrorCode.USER_NOT_FOUND));
    }
}
