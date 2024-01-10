package project.service;

import project.domain.*;
import project.dto.user.*;
import project.exception.user.*;
import project.repository.UserRepository;
import project.common.auth.oauth.UserInfo;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;
import static java.util.stream.Collectors.toList;

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
public class UserService{
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    //회원가입
    @Transactional
    public Long register(User user){
        validateUserEmail(user.getEmail());
        
        user.encodePassword(passwordEncoder);
        
        userRepository.save(user);
        
        return user.getId();
    }
    
    //유저 정보 추가, 회원가입 마무리
    public Long finishRegister(User user, String nickName){
        if (user.checkFinishSignUp()){
            throw new IllegalStateException("이미 회원가입이 완료되었습니다");
        }
        
        validateUserNickName(nickName);
        
        user.updateNickName(nickName);
        return user.getId();
    }
    
    //유저 단건 조회
    public User findOne(Long id){
        User findUser = userRepository.findById(id)
            .orElseThrow(NoSuchUserException::new);
        
        return findUser;
    }
    
    //유저 로그인
    public User signIn(String email, String password){
        
        User findUser = userRepository.findByEmail(email)
            .orElseThrow(NoSuchUserException::new);
        
        if (findUser.checkPassword(password, passwordEncoder)){ //이부분도 고쳐보는게 좋을듯 -> User에서 Exception 날리기
            return findUser;
        }
        else{
            throw new NoSuchUserException("아이디 혹은 비밀번호가 틀립니다");
        }
    }
    
    // 유저 리스트 조회
    public Page<User> findAllUsers(Pageable pageable){
        return userRepository.findAll(pageable);
    }
    
    //유저 정보 수정
    @Transactional
    public User updateUser(Long id, String nickName, String password, String name, String profile){
        User user = userRepository.findById(id)
            .orElseThrow(NoSuchUserException::new);
        
        if(!user.getNickName().equals(nickName)){
            validateUserNickName(nickName);
        }
        
        user.update(nickName, password, name, profile, passwordEncoder);

        return user;
    }
    
    //유저 삭제
    @Transactional
    public void deleteUser(Long id){
        User user = userRepository.findById(id)
            .orElseThrow(NoSuchUserException::new);
        
        userRepository.delete(user);
    }
    
    // < == validate logic ==> //
    private void validateUserEmail(String email){
        
        if (userRepository.existsByEmail(email)){
            throw new UserAlreadyExistException();
        }
    }
    
    private void validateUserNickName(String nickName){
        if (userRepository.existsByNickName(nickName)){
            throw new UserAlreadyExistException();
        }
    }
}