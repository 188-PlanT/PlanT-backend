// package project.service;

// import static org.mockito.BDDMockito.*;
// import static org.mockito.ArgumentMatchers.any;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.junit.runner.RunWith;
// import org.mockito.junit.MockitoJUnitRunner;
// import org.junit.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import static org.assertj.core.api.Assertions.assertThat;

// import project.repository.UserRepository;
// import project.domain.User;
// import project.exception.user.*;
// import java.util.Optional;
// import java.util.List;
// import java.util.ArrayList;

// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.domain.PageRequest;

// @RunWith(MockitoJUnitRunner.class)
// public class UserServiceTest{
    
//     @Mock
//     private UserRepository userRepository;
    
//     @InjectMocks
//     private UserServiceImpl userService;
    
//     @Test
//     public void 유저_단건_조회(){
//         //given
//         User user = new User("test1", "test1234", "kim");
//         given(userRepository.findById(any(Long.class))).willReturn(Optional.of(user));
//         Long userId = 1L;
        
//         //when
//         User result = userService.findOne(userId);
        
//         //then
//         assertThat(result.getAccountId()).isEqualTo("test1");
//     }
    
//     @Test(expected = NoSuchUserException.class)
//     public void 없는_유저아이디_조회() throws Exception{
//         //given
//         Long undefinedId = 1L;
        
//         given(userRepository.findById(undefinedId)).willReturn(Optional.ofNullable(null));
        
//         //when
//         User result = userService.findOne(undefinedId);
//     }
    
//     @Test
//     public void 유저_리스트_조회(){
//         //given
//         List<User> userList = new ArrayList<>();
//         userList.add(new User("test1", "test1234", "kim"));
//         userList.add(new User("test2", "test1234", "kim"));
//         userList.add(new User("test3", "test1234", "kim"));
        
//         Pageable pageable = PageRequest.of(0,20);
        
//         Page<User> response = new PageImpl<>(userList, pageable, 3);
        
//         given(userRepository.searchUsers(any(Pageable.class),any(String.class),any(String.class)))
//               .willReturn(response);
        
//         //when
//         Page<User> result = userService.findAllBySearch(pageable, "test", "test");
        
//         //then
//         assertThat(result.getContent().size()).isEqualTo(3);
//         assertThat(result.getContent().get(0).getAccountId()).isEqualTo("test1");
//         assertThat(result.getContent().get(1).getAccountId()).isEqualTo("test2");
//         assertThat(result.getContent().get(2).getAccountId()).isEqualTo("test3");
//     }
    
//     //검색은 respository의 역할이 더 크므로 여기선 생략
    
//     @Test
//     public void 유저_생성(){
//         //given
//         User user = new User("test1", "test1234", "kim");
        
//         given(userRepository.existsByAccountId("test1")).willReturn(false);
//         given(userRepository.save(any(User.class))).willReturn(user);
        
//         //when
//         Long userId = userService.register(user);
//     }
    
//     @Test(expected = UserAlreadyExistException.class)
//     public void 중복_유저_생성(){
//         //given
//         User user = new User("test1", "test1234", "kim");
        
//         given(userRepository.existsByAccountId("test1")).willReturn(true);
        
//         //when
//         Long userId = userService.register(user);
//     }
    
//     @Test
//     public void 유저_수정(){
//         //given
//         User user = new User("test1", "test1234", "kim");
//         Long userId = 1L;
        
//         given(userRepository.findById(userId)).willReturn(Optional.of(user));
        
//         //when
//         User updatedUser = userService.updateUser(userId, "updatePw", "park");
        
//         //then
//         assertThat(updatedUser.getPassword()).isEqualTo("updatePw");
//         assertThat(updatedUser.getName()).isEqualTo("park");
//     }
    
//     @Test(expected = NoSuchUserException.class)
//     public void 없는유저_수정(){
//         //given
//         User user = new User("test1", "test1234", "kim");
//         Long undefinedId = 2L;
        
//         given(userRepository.findById(undefinedId)).willReturn(Optional.ofNullable(null));
        
//         //when
//         User updatedUser = userService.updateUser(undefinedId, "updatePw", "park");
//     }
    
//     @Test
//     public void 유저_삭제(){
//         //given
//         User user = new User("test1", "test1234", "kim");
//         Long userId = 1L;
        
//         given(userRepository.findById(userId)).willReturn(Optional.of(user));
        
//         //when
//         userService.deleteUser(userId);
//     }
    
//     @Test(expected = NoSuchUserException.class)
//     public void 없는유저_삭제(){
//         //given
//         User user = new User("test1", "test1234", "kim");
//         Long undefinedId = 2L;
        
//         given(userRepository.findById(undefinedId)).willReturn(Optional.ofNullable(null));
        
//         //when
//         userService.deleteUser(undefinedId);
//     }
// }