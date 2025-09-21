package project.domain.user.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.common.security.jwt.JwtProvider;
import project.common.util.DateFormatUtil;
import project.common.util.UserUtil;
import project.domain.auth.dto.request.EmailSignUpRequest;
import project.domain.auth.dto.response.AccessTokenResponse;
import project.domain.user.domain.User;
import project.domain.user.dto.UserDto;
import project.domain.user.dto.request.*;
import project.domain.user.dto.response.*;
import project.domain.user.service.UserService;

@Tag(name = "[User]", description = "유저 정보 관리 API")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final UserUtil userUtil;

    @Operation(summary = "이메일 회원가입", description = "이메일을 이용해 회원가입을 진행합니다.")
    @PostMapping("/v1/sign-up")
    public ResponseEntity<Long> registerEmailUser(@Valid @RequestBody EmailSignUpRequest request) {
        var response = userService.registerEmailUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "이메일 사용 여부 검증", description = "사용중인 이메일인지 확인합니다.")
    @PostMapping("/v1/users/email")
    public ResponseEntity<EmailCheckResponse> checkEmailAvailable(@Valid @RequestBody EmailCheckRequest request) {
        var response = userService.checkEmailAvailable(request.email());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "닉네임 사용 여부 검증", description = "사용중인 닉네임인지 확인합니다.")
    @PostMapping("/v1/users/nickname")
    public ResponseEntity<NicknameCheckResponse> checkNickNameAvailable(
            @Valid @RequestBody NickNameCheckRequest request) {
        var response = userService.checkNickNameAvailable(request.nickName());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "유저 닉네임 변경", description = "유저 닉네임을 변경합니다. 회원가입이 끝나지 않은 유저는 회원가입 완료도 같이 진행합니다.")
    @PutMapping("/v1/users/nickname")
    public ResponseEntity<AccessTokenResponse> setNickNameUser(@Valid @RequestBody FinishUserRegisterRequest request) {
        // TODO: 서비스로 이동
        User user = userService.finishRegister(request.nickName());
        String accessToken = jwtProvider.createAccessTokenByUser(user);
        var response = AccessTokenResponse.of(accessToken);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "유저 정보 조회", description = "로그인 유저의 정보를 조회합니다.")
    @GetMapping("/v1/users/me")
    public ResponseEntity<UserDto> findUserDetails() {
        var response = userService.getLoginUser();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "유저 정보 수정", description = "로그인 유저의 정보를 수정합니다.")
    @PutMapping("/v1/users/me")
    public ResponseEntity<Void> updateUser(@Valid @RequestBody UpdateUserRequest request) {
        userService.updateUser(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "유저 워크스페이스 정보 조회", description = "로그인 유저의 워크스페이스 정보를 조회합니다.")
    @GetMapping("/v1/users/me/workspaces")
    public ResponseEntity<UserWorkspacesResponse> readUserWorkspaces() {
        var response = userService.findWorkspaces(userUtil.getLoginUserId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "유저 스케줄 정보 달력 조회", description = "해당 달에 로그인 유저가 속한 스케줄 정보를 조회합니다.")
    @GetMapping("/v1/users/me/schedules")
    public ResponseEntity<UserSchedulesResponse> readUserSchedules(
            @Parameter(required = true, description = "yyyyMM") String date) {
        LocalDateTime dateTime = DateFormatUtil.parseStartOfMonth(date);
        var response = userService.findSchedules(userUtil.getLoginUserId(), dateTime);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "유저 검색", description = "이메일 또는 닉네임 기반으로 유저를 검색합니다.")
    @GetMapping("/v1/users/search")
    public ResponseEntity<SearchUserResponse> searchUser(@RequestParam String keyword) {
        var response = userService.searchUser(keyword);
        return ResponseEntity.ok(response);
    }

    // TODO: 이벤트 기반 처리 시에 도메인 분리 검토
    @Operation(summary = "이메일 인증 코드 발급", description = "요청 본문에 입력한 이메일로 인증 코드를 발송합니다.")
    @GetMapping("/v1/users/email/code")
    public ResponseEntity<Void> getEmailValidateCode(@RequestParam String email) {
        userService.sendEmailVerificationCodeMail(email);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "이메일 인증 코드 검증", description = "이메일 인증 코드를 검증합니다.")
    @PostMapping("/v1/users/email/code")
    public ResponseEntity<Void> validateCode(@RequestParam String email, @RequestBody CodeRequest request) {
        userService.validateEmailCode(email, request.code());
        return ResponseEntity.ok().build();
    }
}
