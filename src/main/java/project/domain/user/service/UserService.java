package project.domain.user.service;

import static project.common.constant.UrlConstant.DEFAULT_USER_PROFILE_URL;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.common.constant.MailContant;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;
import project.common.property.EmailVerificationProperty;
import project.common.util.UserUtil;
import project.domain.auth.dto.request.EmailSignUpRequest;
import project.domain.image.dao.ImageRepository;
import project.domain.image.domain.Image;
import project.domain.scheduleUser.dao.ScheduleUserRepository;
import project.domain.scheduleUser.domain.ScheduleUser;
import project.domain.user.dao.UserRepository;
import project.domain.user.domain.User;
import project.domain.user.dto.UserDto;
import project.domain.user.dto.request.UpdateUserRequest;
import project.domain.user.dto.response.*;
import project.domain.workspaceUser.dao.WorkspaceUserRepository;
import project.domain.workspaceUser.domain.WorkspaceUser;
import project.infra.mail.application.MailService;
import project.infra.mail.dto.MailDto;
import project.infra.redis.application.RedisService;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private static final String PASSWORD_PATTERN = "^[0-9a-zA-Z@#$%^&+=!]{8,16}$"; // 영문, 숫자, 특수문자

    private final UserRepository userRepository;
    private final WorkspaceUserRepository workspaceUserRepository;
    private final ScheduleUserRepository scheduleUserRepository;
    private final ImageRepository imageRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final MailService mailService;
    private final EmailVerificationProperty emailVerificationProperty;
    private final UserUtil userUtil;

    // <== 회원가입 ==>
    @Transactional
    public Long registerEmailUser(EmailSignUpRequest request) {
        validateUserEmail(request.email());
        validatePasswordPattern(request.password());

        Image defaultUserProfile = imageRepository
                .findByUrl(DEFAULT_USER_PROFILE_URL)
                .orElseThrow(() -> new PlantException(ErrorCode.IMAGE_NOT_FOUND));

        User user = User.ofEmailPassword(request.email(), encodePassword(request.password()), defaultUserProfile);

        userRepository.save(user);

        return user.getId();
    }

    // <== 회원가입 마무리 ==>
    @Transactional
    public User finishRegister(String nickName) {
        User user = userUtil.getLoginUser();
        validateUserNickName(nickName);

        user.finishRegister(nickName);

        // lazy loding
        user.getProfile().getUrl();
        return user;
    }

    // <== 워크스페이스 조회 ==>
    @Transactional(readOnly = true)
    public UserWorkspacesResponse findUserWorkspaces() {
        Long loginUserId = userUtil.getLoginUserId();
        List<WorkspaceUser> workspaceUsers = workspaceUserRepository.searchByUserId(loginUserId);

        return UserWorkspacesResponse.from(loginUserId, workspaceUsers);
    }

    // <== 스케줄 조회 ==>
    @Transactional(readOnly = true)
    public UserSchedulesResponse findUserSchedules(LocalDateTime startDate, LocalDateTime endDate) {
        Long loginUserId = userUtil.getLoginUserId();
        List<ScheduleUser> scheduleUsers = scheduleUserRepository.searchByUserAndDate(loginUserId, startDate, endDate);

        return UserSchedulesResponse.from(loginUserId, scheduleUsers);
    }

    // <== 유저 정보 수정 ==>
    @Transactional
    public void updateUser(UpdateUserRequest request) {
        User user = userUtil.getLoginUser();

        validateCurrentPassword(user, request.currentPassword());

        String nickName = request.nickName(); // 닉네임 변경값 검증
        if (nickName != null && !user.getNickName().equals(nickName)) {
            validateUserNickName(nickName);
        }

        String newPassword = request.newPassword(); // 비밀번호 변경값 검증
        if (newPassword != null) validatePasswordPattern(newPassword);
        String encodedPassword = newPassword != null ? encodePassword(newPassword) : user.getPassword();

        String profileUrl = request.profile();
        Image profile = null;

        if (profileUrl != null) {
            profile = imageRepository
                    .findByUrl(profileUrl)
                    .orElseThrow(() -> new PlantException(ErrorCode.IMAGE_NOT_FOUND));
        }

        user.update(nickName, encodedPassword, profile);
    }

    // <== 유저 검색 ==>
    @Transactional(readOnly = true)
    public SearchUserResponse searchUser(String keyword) {
        User loginUser = userUtil.getLoginUser();
        List<User> users = userRepository.searchByKeyword(loginUser.getId(), keyword);

        return SearchUserResponse.of(users);
    }

    // <== 이메일 검증 메일 발송 ==>
    public void sendEmailVerificationCodeMail(String email) {
        validateUserEmail(email);

        int code = generateSixDigitCode();

        MailDto emailVerificationMail = createEmailVerificationMail(email, code);

        mailService.sendMail(emailVerificationMail);
        redisService.setValues(email, String.valueOf(code));
        redisService.setExpiration(email, emailVerificationProperty.getExpirationSeconds());
    }

    private int generateSixDigitCode() {
        return ThreadLocalRandom.current().nextInt(100000, 1000000);
    }

    private MailDto createEmailVerificationMail(String email, int code) {
        String subject = MailContant.VERIFICATION_MAIL_SUBJECT;
        String content = String.format(MailContant.VERIFICATION_MAIL_CONTENT, String.valueOf(code));
        return MailDto.from(email, subject, content);
    }

    // 검증 로직
    public void validateEmailCode(String email, String code) {
        String redisCode = redisService.getValues(email);

        if (redisCode == null) {
            throw new PlantException(ErrorCode.USER_NOT_FOUND, "잘못된 이메일입니다");
        }

        if (!redisCode.equals(code)) {
            throw new PlantException(ErrorCode.EMAIL_CODE_INVALID);
        }

        redisService.deleteByKey(email);
    }

    @Transactional(readOnly = true)
    public UserDto getLoginUser() {
        Long userId = userUtil.getLoginUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new PlantException(ErrorCode.USER_NOT_FOUND));
        return UserDto.from(user);
    }

    @Transactional(readOnly = true)
    public EmailCheckResponse checkEmailAvailable(String email) {
        boolean existsUserByEmail = userRepository.existsByEmail(email);
        return EmailCheckResponse.of(!existsUserByEmail);
    }

    @Transactional(readOnly = true)
    public NicknameCheckResponse checkNickNameAvailable(String nickName) {
        boolean existsUserByNickName = userRepository.existsByNickName(nickName);
        return NicknameCheckResponse.of(!existsUserByNickName);
    }

    private void validateUserEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new PlantException(ErrorCode.USER_ALREADY_EXIST);
        }
    }

    public void validateUserNickName(String nickName) {
        if (userRepository.existsByNickName(nickName)) {
            throw new PlantException(ErrorCode.USER_ALREADY_EXIST);
        }
    }

    private void validatePasswordPattern(String password) {
        if (!Pattern.matches(PASSWORD_PATTERN, password)) {
            throw new PlantException(ErrorCode.PASSWORD_INVALD);
        }
    }

    private void validateCurrentPassword(User user, String password) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new PlantException(ErrorCode.USER_NOT_FOUND, "비밀번호가 올바르지 않습니다");
        }
    }

    // 비밀번호 암호화
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
