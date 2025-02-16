package project.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;
import project.domain.auth.domain.UserInfo;
import project.domain.user.dao.UserRepository;
import project.domain.user.domain.User;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import project.domain.user.domain.UserRole;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserUtil {
    private final UserRepository userRepository;

    @Transactional
    public User getLoginUser(){
        Long userId = getLoginUserId();
        return getUserById(userId);
    }

    // 로그인한 유저 정보 조회가 아닌 단순히 로그인 유저의 pk 조회가 필요할때 사용
    public Long getLoginUserId(){
        return getLoginUserInfo().getUserId();
    }

    private UserInfo getLoginUserInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserInfo) authentication.getPrincipal();
    }

    // 로그인한 유저의 워크스페이스 권한 조회
    public UserRole getLoginUserRole(Long workspaceId){
        UserInfo userInfo = getLoginUserInfo();

        if(userInfo.getWorkspaceUserIds().stream()
                .anyMatch((workspaceUserId) -> workspaceUserId.equals(workspaceId))) return UserRole.USER;


        if (userInfo.getWorkspaceAdminIds().stream()
                .anyMatch((workspaceAdminId) -> workspaceAdminId.equals(workspaceId))) return UserRole.ADMIN;

        return null;
    }

    // 다양한 서비스에서 쓰이는 유저 조회를 위한 유틸 클래스
    @Transactional
    public User getUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new PlantException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public List<User> getUserByList(List<Long> userIdList){
        List<User> result = userRepository.findByIdIn(userIdList);

        if(result.size() != userIdList.size()){
            throw new PlantException(ErrorCode.USER_NOT_FOUND, "잘못된 유저 정보가 포함되어 있습니다");
        }

        return result;
    }
}
