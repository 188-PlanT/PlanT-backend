package project.service;

import project.domain.*;
import project.dto.user.*;
import project.dto.login.SignUpRequest;
import project.exception.user.*;
import project.repository.UserRepository;
import project.repository.UserWorkspaceRepository;
import project.repository.UserScheduleRepository;
import project.common.auth.oauth.UserInfo;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;
import static java.util.stream.Collectors.toList;
import java.util.regex.Pattern;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService{
    
    public static final String PASSWORD_PATTERN = "^[0-9a-zA-Z@#$%^&+=!]{8,16}$"; // 영문, 숫자, 특수문자
    
    private final UserRepository userRepository;
    private final UserWorkspaceRepository userWorkspaceRepository;
    private final UserScheduleRepository userScheduleRepository;
    private final PasswordEncoder passwordEncoder;
    
    //회원가입
    @Transactional
    public User register(SignUpRequest request){
        validateUserEmail(request.getEmail());
        
        validateUserPassword(request.getPassword());
        
        User user = User.ofEmailPassword(request.getEmail(), request.getPassword(), passwordEncoder);   
        
        userRepository.save(user);
        
        return user;
    }
    
    //유저 정보 추가, 회원가입 마무리
    @Transactional
    public User finishRegister(Long userId, String nickName){
        
        User user = userRepository.findById(userId)
            .orElseThrow(NoSuchUserException::new);
        
        validateUserNickName(nickName);
        
        user.setNickName(nickName);
        
        return user;
    }
    
    //유저 단건 조회
    @Transactional(readOnly = true)
    public User findOne(Long id){
        User findUser = userRepository.findById(id)
            .orElseThrow(NoSuchUserException::new);
        
        return findUser;
    }
    
    //유저 로그인
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
    
    // 워크스페이스 조회
    @Transactional(readOnly = true)
    public Page<UserWorkspace> findWorkspaces(User user, Pageable pageable){
        Page<UserWorkspace> userWorkspaces =  userWorkspaceRepository.searchByUser(user, pageable);
        
        //lazy loding
        userWorkspaces.getContent().stream().forEach(uw -> uw.getWorkspace().getName());
        
        return userWorkspaces;
    }
    
    // 스케줄 조회
    @Transactional(readOnly = true)
    public Page<UserSchedule> findSchedules(User user, LocalDateTime date, Pageable pageable){
        Page<UserSchedule> userSchedules = userScheduleRepository.searchByUser(user, date, pageable);
        
        //lazy loding
        userSchedules.getContent().stream().forEach(uw -> uw.getSchedule().getWorkspace().getName());

        return userSchedules;
    }
    
    //유저 정보 수정
    @Transactional
    public User updateUser(Long id, String nickName, String password, String profile){
        User user = userRepository.findById(id)
            .orElseThrow(NoSuchUserException::new);
        
        if(!user.getNickName().equals(nickName)){
            validateUserNickName(nickName);
        }
        
        validateUserPassword(password);
        
        user.update(nickName, password, profile, passwordEncoder);

        return user;
    }
    
    // 유저 삭제
    @Transactional
    public void deleteUser(Long id){
        User user = userRepository.findById(id)
            .orElseThrow(NoSuchUserException::new);
        
        userRepository.delete(user);
    }
    
    // 유저 검색
    @Transactional(readOnly = true)
    public User searchUser(String email, String nickName){
        
        if (email == null && nickName == null){
            throw new NoSuchUserException();
        }
        
        return userRepository.searchUser(email, nickName)
            .orElseThrow(NoSuchUserException::new);
    }
    
    
    // < == validate logic ==> //
    public void validateUserEmail(String email){
        
        if (userRepository.existsByEmail(email)){
            throw new UserAlreadyExistException();
        }
    }
    
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
    
    
    //<== security 설정 ==> //
    @Transactional(readOnly = true)
    public User findByEmail(String email){
        
        return userRepository.findByEmail(email)
            .orElseThrow(NoSuchUserException::new);
    }
    
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        
        User findUser = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다"));
        
        return UserInfo.from(findUser);
    }
}