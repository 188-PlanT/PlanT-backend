package project.service;

import project.domain.*;
import project.dto.user.*;
import project.dto.login.SignUpRequest;
import project.exception.user.*;
import project.exception.auth.InvalidCodeException;
import project.exception.image.NoSuchImageException;
import project.repository.*;
import project.common.auth.oauth.UserInfo;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;
import static java.util.stream.Collectors.toList;
import java.util.regex.Pattern;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Value;

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
    
    //<== 회원가입 ==>
    @Transactional
    public User register(SignUpRequest request){
        validateUserEmail(request.getEmail());
        
        validateUserPassword(request.getPassword());
        
        Image defaultUserProfile = imageRepository.findByUrl(DEFAULT_USER_IMAGE_URL)
                                            .orElseThrow(NoSuchImageException::new);
        
        User user = User.ofEmailPassword(request.getEmail(), request.getPassword(), defaultUserProfile, passwordEncoder);   
        
        userRepository.save(user);
        
        return user;
    }
    
    // <== 회원가입 마무리 ==>
    @Transactional
    public User finishRegister(String email, String nickName){
        
        User user = userRepository.findByEmail(email)
            .orElseThrow(NoSuchUserException::new);
        
        validateUserNickName(nickName);
        
        user.setNickName(nickName);
        
        // lazy loding
        user.getProfile().getUrl();
        
        return user;
    }
    
    // <== 유저 로그인 ==>
    @Transactional(readOnly = true)
    public User signIn(String email, String password){
        
        User findUser = userRepository.findByEmail(email)
            .orElseThrow(NoSuchUserException::new);
        
        if (findUser.checkPassword(password, passwordEncoder)){
            return findUser;
        }
        else{
            throw new NoSuchUserException("아이디 혹은 비밀번호가 틀립니다");
        }
    }
    
    // <== 워크스페이스 조회 ==>
    @Transactional(readOnly = true)
    public List<UserWorkspace> findWorkspaces(String email){
        User user = userRepository.findByEmail(email)
            .orElseThrow(NoSuchUserException::new);
		
        List<UserWorkspace> userWorkspaces =  userWorkspaceRepository.searchByUser(user);

        return userWorkspaces;
    }
    
    // <== 스케줄 조회 ==>
    @Transactional(readOnly = true)
    public List<UserSchedule> findSchedules(String email, LocalDateTime date){
		
		// log.info("startDate = {}, endDate = {}", date, date.plusMonths(1).minusSeconds(1));
		
        List<UserSchedule> userSchedules = userScheduleRepository.searchByUser(email, date, date.plusMonths(1).minusSeconds(1));

        return userSchedules;
    }
    
    // <== 유저 정보 수정 ==>
    @Transactional
    public User updateUser(String email, String nickName, String password, String profileUrl){
        User user = userRepository.findByEmail(email)
            .orElseThrow(NoSuchUserException::new);
        
        if(!user.getNickName().equals(nickName)){
            validateUserNickName(nickName);
        }
        
        validateUserPassword(password);
        
        Image profile = imageRepository.findByUrl(profileUrl)
                                        .orElseThrow(NoSuchImageException::new);
        
        user.update(nickName, password, profile, passwordEncoder);

        return user;
    }
    
    // // 유저 삭제
    // @Transactional
    // public void deleteUser(Long id){
    //     User user = userRepository.findById(id)
    //         .orElseThrow(NoSuchUserException::new);
        
    //     userRepository.delete(user);
    // }
    
    // <== 유저 검색 ==>
    @Transactional(readOnly = true)
    public List<User> searchUser(String keyword){
        
        List<User> users = userRepository.searchByKeyword(keyword);
        
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
            throw new NoSuchUserException("잘못된 이메일입니다");
        }
        
        if (!redisCode.equals(code+"")){
            throw new InvalidCodeException("잘못된 코드입니다");
        }
        
        redisService.deleteByKey(email);
    }
    
    
    // < == validate logic ==> //
    // <== 이메일 중복 검증 ==>
    public void validateUserEmail(String email){
        
        if (userRepository.existsByEmail(email)){
            throw new UserAlreadyExistException();
        }
    }
    // <== 닉네임 중복 검증 ==>
    public void validateUserNickName(String nickName){
        if (userRepository.existsByNickName(nickName)){
            throw new UserAlreadyExistException();
        }
    }
    
    private void validateUserPassword(String password){
        
        if (!Pattern.matches(PASSWORD_PATTERN, password)){
            throw new InvalidPasswordException(); 
        }
    }
    
    // <== 이메일로 조회 ==>
    @Transactional(readOnly = true)
    public User findByEmail(String email){
        
        User user = userRepository.findByEmail(email)
            .orElseThrow(NoSuchUserException::new);
        
        // lazy loding
        user.getProfile().getUrl();
        
        return user;
    }
    
    // < == Security 메서드 ==>
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        
        User findUser = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다"));
        
        return UserInfo.from(findUser);
    }
	
	// < == dumyDB 조회용 로그인 ==>
    @Transactional(readOnly = true)
    public User signInDumy(String email, String password){
        
        User findUser = userRepository.findByEmail(email)
            .orElseThrow(NoSuchUserException::new);
        
        if (findUser.getPassword().equals(password)){
            return findUser;
        }
        else{
            throw new NoSuchUserException("아이디 혹은 비밀번호가 틀립니다");
        }
    }
}