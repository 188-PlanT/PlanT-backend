package project.domain.user.service;

import project.common.util.UserUtil;
import project.domain.image.dao.ImageRepository;
import project.domain.image.domain.Image;
import project.domain.schedule.dao.UserScheduleRepository;
import project.domain.schedule.domain.Schedule;
import project.domain.schedule.domain.UserSchedule;
import project.domain.user.dao.UserRepository;
import project.domain.user.domain.User;
import project.domain.user.dto.user.UserSchedulesResponse;
import project.domain.user.dto.user.UserWorkspacesResponse;
import project.domain.workspace.dao.UserWorkspaceRepository;
import project.domain.workspace.domain.UserWorkspace;
import project.domain.auth.dto.request.SignUpRequest;
import project.domain.user.dto.user.UpdateUserRequest;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;
import project.domain.auth.domain.UserInfo;
import project.common.service.RedisService;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.regex.Pattern;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import project.domain.workspace.domain.Workspace;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService{
    
    //왜 퍼블릭?
    public static final String PASSWORD_PATTERN = "^[0-9a-zA-Z@#$%^&+=!]{8,16}$"; // 영문, 숫자, 특수문자
    
    @Value("${s3.default-image-url.user}")
    private String DEFAULT_USER_IMAGE_URL;
    
    private final UserRepository userRepository;
    private final UserWorkspaceRepository userWorkspaceRepository;
    private final UserScheduleRepository userScheduleRepository;
    private final ImageRepository imageRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final UserUtil userUtil;
    
    //<== 회원가입 ==>
    @Transactional
    public User register(SignUpRequest request){
        validateUserEmail(request.getEmail());
        
        validateUserPassword(request.getPassword());
        
        Image defaultUserProfile = imageRepository.findByUrl(DEFAULT_USER_IMAGE_URL)
                                            .orElseThrow(() -> new PlantException(ErrorCode.IMAGE_NOT_FOUND));
        
        User user = User.ofEmailPassword(request.getEmail(), request.getPassword(), defaultUserProfile, passwordEncoder);   
        
        userRepository.save(user);
        
        return user;
    }
    
    // <== 회원가입 마무리 ==>
    @Transactional
    public User finishRegister(String nickName){

        User user = userUtil.getLoginUser();
        
        validateUserNickName(nickName);
        
        user.setNickName(nickName);
        
        // lazy loding
        user.getProfile().getUrl();
        
        return user;
    }
    
    // <== 워크스페이스 조회 ==>
    @Transactional(readOnly = true)
    public UserWorkspacesResponse findWorkspaces(Long userId){

        User user = userUtil.getUserById(userId);

        List<UserWorkspace> userWorkspaces =  userWorkspaceRepository.searchByUser(user);

        return UserWorkspacesResponse.of(user, userWorkspaces);
    }
    
    // <== 스케줄 조회 ==>
    @Transactional(readOnly = true)
    public UserSchedulesResponse findSchedules(Long userId, LocalDateTime date){
        User loginUser = userUtil.getUserById(userId);
		
        List<UserSchedule> userSchedules = userScheduleRepository.searchByUser(loginUser.getEmail(), date, date.plusMonths(1).minusSeconds(1));

        return UserSchedulesResponse.of(loginUser, userSchedules);
    }
    
    // <== 유저 정보 수정 ==>
    @Transactional
    public User updateUser(UpdateUserRequest request){
		
		User user = userUtil.getLoginUser();
		
		validateCurrentPassword(user, request.getCurrentPassword());
		
		String nickName = request.getNickName(); //닉네임 변경값 검증
        if(nickName != null && !user.getNickName().equals(nickName)){
            validateUserNickName(nickName);
        }
		
		String newPassword = request.getNewPassword(); // 비밀번호 변경값 검증
        if (newPassword != null) validateUserPassword(newPassword);
        
		String profileUrl = request.getProfile();
		Image profile = null;
		
		if (profileUrl != null){
			profile = imageRepository.findByUrl(profileUrl)
                .orElseThrow(() -> new PlantException(ErrorCode.IMAGE_NOT_FOUND));	
		}
        
        user.update(nickName, newPassword, profile, passwordEncoder);

        return user;
    }
    
    // // 유저 삭제
     @Transactional
     public void deleteUser(Long id){
         User user = userUtil.getUserById(id);

         userRepository.delete(user);
     }
    
    // <== 유저 검색 ==>
    @Transactional(readOnly = true)
    public List<User> searchUser(String keyword){
        User loginUser = userUtil.getLoginUser();
        
        List<User> users = userRepository.searchByKeyword(loginUser.getId(), keyword);
        
        return users;
    }
    
    // <== 이메일 검증 코드 제작 ==>
    public int getEmailValidateCode(String email){
        validateUserEmail(email);
        
        return ThreadLocalRandom.current().nextInt(100000, 1000000);
    }
    
    // <== 이메일 검증 코드 검증 ==>
    public void validateEmailCode(String email, int code){
        String redisCode = redisService.getValues(email);
        
        if (redisCode == null){
            throw new PlantException(ErrorCode.USER_NOT_FOUND, "잘못된 이메일입니다");
        }
        
        if (!redisCode.equals(code+"")){
            throw new PlantException(ErrorCode.EMAIL_CODE_INVALID);
        }
        
        redisService.deleteByKey(email);
    }
    
    // < == Security 메서드 ==>
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        
        User findUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new PlantException(ErrorCode.USER_NOT_FOUND));
        
        return UserInfo.from(findUser);
    }

    // admin 페이지용 조회
    @Transactional(readOnly = true)
    public User findOneDetail(Long userId){
        User user = userUtil.getUserById(userId);
        //lazy Loding
        for(UserWorkspace uw : user.getUserWorkspaces()){
            Workspace w = uw.getWorkspace();
            w.getName();
        }

        for(UserSchedule us : user.getUserSchedules()){
            Schedule s  = us.getSchedule();
            s.getName();
        }

        return user;
    }
	
	// < == validate logic ==> //
    // <== 이메일 중복 검증 ==>
    public void validateUserEmail(String email){
        
        if (userRepository.existsByEmail(email)){
            throw new PlantException(ErrorCode.USER_ALREADY_EXIST);
        }
    }
    // <== 닉네임 중복 검증 ==>
    public void validateUserNickName(String nickName){
        if (userRepository.existsByNickName(nickName)){
            throw new PlantException(ErrorCode.USER_ALREADY_EXIST);
        }
    }
    
    private void validateUserPassword(String password){
        if (!Pattern.matches(PASSWORD_PATTERN, password)){
            throw new PlantException(ErrorCode.PASSWORD_INVALD);
        }
    }
	
	// 업데이트 로직에서 현재 비밀번호 확인
	private void validateCurrentPassword(User user, String password){
		if (!user.checkPassword(password, passwordEncoder)){
			throw new PlantException(ErrorCode.USER_NOT_FOUND, "비밀번호가 올바르지 않습니다");	
		}
	}
}