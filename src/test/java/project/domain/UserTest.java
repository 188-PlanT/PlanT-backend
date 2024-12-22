 package project.domain;

 import org.junit.jupiter.api.*;
 import project.domain.user.domain.User;
 import project.domain.user.domain.UserRole;

 public class UserTest{
     @Test
     @DisplayName("유저 객체 생성 테스트")
     void createUser() {
         User user = User.builder()
                 .email("test1@gmail.com")
                 .nickName("test1")
                 .password("1234")
                 .profile(null)
                 .userRole(UserRole.USER)
                 .build();

         Assertions.assertEquals("test1@gmail.com", user.getEmail());
     }
 }