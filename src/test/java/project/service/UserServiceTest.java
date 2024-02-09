package project.service;

import project.repository.UserRepository;
import project.domain.User;
import project.exception.user.*;
import project.dto.user.*;
import project.dto.login.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.BDDMockito.*;
import static org.mockito.ArgumentMatchers.any;
// import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
// import org.junit.runner.RunWith;
// import org.mockito.junit.MockitoJUnitRunner;
// import org.springframework.beans.factory.annotation.Autowired;
// import static org.assertj.core.api.Assertions.assertThat;


// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.domain.PageRequest;

// @RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class UserServiceTest{
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private UserService userService;
    
    @Test
    public void 회원가입(){
        //given
        String successEmail = "test1234@gmail.com";
        String failEmail = "alreadyExist1234@gmail.com";
        
        SignUpRequest successRequest = new SignUpRequest(successEmail, "test1234");
        SignUpRequest failRequest = new SignUpRequest(failEmail, "test1234");
        
        given(userRepository.existsByEmail(successEmail)).willReturn(false); // 가입 가능 이메일
        given(userRepository.existsByEmail(failEmail)).willReturn(true); // 중복 이메일
        given(passwordEncoder.encode(any(String.class))).willReturn("encodedPassword"); //Entity 의존성 줄여볼까??
        
        //when
        User result = userService.register(successRequest);
        
        //then
        assertEquals(result.getEmail(), successEmail);
        assertEquals(result.getPassword(), "encodedPassword");
        assertNull(result.getNickName());
        
        assertThrows(UserAlreadyExistException.class, () -> {
            userService.register(failRequest);
        });
    }
    @Test
    public void 회원가입_마무리(){
        //given
        String successEmail = "test1234@gmail.com";
        String successNickName = "test1234";
        
        String failEmail = "UnExistEmail1234@gmail.com";
        String failNickName = "alreadyExistNickName";
        
        User user = User.ofEmailPassword(successEmail, "password", passwordEncoder);

        given(userRepository.findByEmail(successEmail)).willReturn(Optional.of(user)); // 가입된 이메일
        // given(userRepository.findByEmail(failEmail)).willReturn(Optional.ofNullable(null)); // 가입안된 이메일

        given(userRepository.existsByNickName(successNickName)).willReturn(false); // 가입 가능 닉네임
        // given(userRepository.existsByNickName(failNickName)).willReturn(true); // 중복 닉네임
        
        given(passwordEncoder.encode(any(String.class))).willReturn("encodedPassword"); //Entity 의존성 줄여볼까??
        
        //when
        User result = userService.finishRegister(successEmail, successNickName);
        
        //then
        assertEquals(result.getEmail(), successEmail);
        assertEquals(result.getPassword(), "encodedPassword");
        assertEquals(result.getNickName(), successNickName);
        
        // assertThrows(NoSuchUserException.class, () -> {
        //     userService.finishRegister(failEmail, successNickName);
        // });
        // assertThrows(UserAlreadyExistException.class, () -> {
        //     userService.finishRegister(successEmail, failNickName);
        // });

    }
    
    // @Test
    // public void 회원(){
    //     //given
    //     SignUpRequest request = new SignUpRequest("test1234@gmail.com", "test1234");
        
    //     given(userRepository.existsByEmail(any(String.class))).willReturn(false); //이거 구현 노출 아님???
    //     given(passwordEncoder.encode(any(String.class))).willReturn("encodedPassword"); //Entity 의존성 줄여볼까??
        
    //     //when
    //     User result = userService.register(request);
        
    //     //then
    //     assertThat(result.getEmail()).isEqualTo("test1234@gmail.com");
    //     assertThat(result.getPassword()).isEqualTo("encodedPassword");
    // }
    
    // @Test
    // public void 유저_리스트_조회(){
    //     //given
    //     List<User> userList = new ArrayList<>();
    //     userList.add(new User("test1", "test1234", "kim"));
    //     userList.add(new User("test2", "test1234", "kim"));
    //     userList.add(new User("test3", "test1234", "kim"));
        
    //     Pageable pageable = PageRequest.of(0,20);
        
    //     Page<User> response = new PageImpl<>(userList, pageable, 3);
        
    //     given(userRepository.searchUsers(any(Pageable.class),any(String.class),any(String.class)))
    //           .willReturn(response);
        
    //     //when
    //     Page<User> result = userService.findAllBySearch(pageable, "test", "test");
        
    //     //then
    //     assertThat(result.getContent().size()).isEqualTo(3);
    //     assertThat(result.getContent().get(0).getAccountId()).isEqualTo("test1");
    //     assertThat(result.getContent().get(1).getAccountId()).isEqualTo("test2");
    //     assertThat(result.getContent().get(2).getAccountId()).isEqualTo("test3");
    // }
    
    // //검색은 respository의 역할이 더 크므로 여기선 생략
    
    // @Test
    // public void 유저_생성(){
    //     //given
    //     User user = new User("test1", "test1234", "kim");
        
    //     given(userRepository.existsByAccountId("test1")).willReturn(false);
    //     given(userRepository.save(any(User.class))).willReturn(user);
        
    //     //when
    //     Long userId = userService.register(user);
    // }
    
    // @Test(expected = UserAlreadyExistException.class)
    // public void 중복_유저_생성(){
    //     //given
    //     User user = new User("test1", "test1234", "kim");
        
    //     given(userRepository.existsByAccountId("test1")).willReturn(true);
        
    //     //when
    //     Long userId = userService.register(user);
    // }
    
    // @Test
    // public void 유저_수정(){
    //     //given
    //     User user = new User("test1", "test1234", "kim");
    //     Long userId = 1L;
        
    //     given(userRepository.findById(userId)).willReturn(Optional.of(user));
        
    //     //when
    //     User updatedUser = userService.updateUser(userId, "updatePw", "park");
        
    //     //then
    //     assertThat(updatedUser.getPassword()).isEqualTo("updatePw");
    //     assertThat(updatedUser.getName()).isEqualTo("park");
    // }
    
    // @Test(expected = NoSuchUserException.class)
    // public void 없는유저_수정(){
    //     //given
    //     User user = new User("test1", "test1234", "kim");
    //     Long undefinedId = 2L;
        
    //     given(userRepository.findById(undefinedId)).willReturn(Optional.ofNullable(null));
        
    //     //when
    //     User updatedUser = userService.updateUser(undefinedId, "updatePw", "park");
    // }
    
    // @Test
    // public void 유저_삭제(){
    //     //given
    //     User user = new User("test1", "test1234", "kim");
    //     Long userId = 1L;
        
    //     given(userRepository.findById(userId)).willReturn(Optional.of(user));
        
    //     //when
    //     userService.deleteUser(userId);
    // }
    
    // @Test(expected = NoSuchUserException.class)
    // public void 없는유저_삭제(){
    //     //given
    //     User user = new User("test1", "test1234", "kim");
    //     Long undefinedId = 2L;
        
    //     given(userRepository.findById(undefinedId)).willReturn(Optional.ofNullable(null));
        
    //     //when
    //     userService.deleteUser(undefinedId);
    // }
}