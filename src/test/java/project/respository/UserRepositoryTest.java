// package project.repository;

// import org.junit.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.junit.runner.RunWith;
// import org.springframework.test.context.junit4.SpringRunner;
// import org.springframework.test.context.web.WebAppConfiguration;
// import org.springframework.context.annotation.Import;
// import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
// import static org.assertj.core.api.Assertions.assertThat;

// import project.AppConfig;
// import project.domain.User;
// import java.util.Optional;
// import java.util.List;

// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.domain.PageRequest;


// @RunWith(SpringRunner.class)
// @DataJpaTest
// @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// @Import(AppConfig.class)
// public class UserRepositoryTest{
    
    
//     @Autowired
//     private UserRepository userRepository;
    
//     @Test //유저 생성
//     public void test_save(){
//         //given
//         User user = new User("test1", "test1234!", "kim");
//         //when
//         User saveUser = userRepository.save(user);
        
//         //then
//         assertThat(saveUser.getAccountId()).isEqualTo("test1");
//     }
    
//     @Test //유저 단일 조회
//     public void test_findById(){
//         //given
//         User user = new User("test1", "test1234!", "kim");
//         Long userId = userRepository.save(user).getId();
//         //when
//         Optional<User> findUserOptional = userRepository.findById(userId);
//         //then
        
//         assertThat(findUserOptional.isPresent()).isEqualTo(true);
        
//         User findUser = findUserOptional.get();
//         assertThat(findUser.getAccountId()).isEqualTo("test1");
//         assertThat(findUser.getPassword()).isEqualTo("test1234!");
//         assertThat(findUser.getName()).isEqualTo("kim");
//     }
    
//     @Test //유저 단일 조회 오류
//     public void test_findById_byUndefinedId(){
//         //given
//         User user = new User("test1", "test1234!", "kim");
//         Long undefinedId = 0L;
//         //when
//         Optional<User> findUserOptional = userRepository.findById(undefinedId);
//         //then
//         assertThat(findUserOptional.isPresent()).isEqualTo(false);
//     }
    
//     @Test //유저 존재 확인
//     public void test_existsByAccountId(){
//         //given
//         User user = new User("test1", "test1234!", "kim");
//         String accountId = userRepository.save(user).getAccountId();
//         //when
//         assertThat(userRepository.existsByAccountId(accountId))
//         //then
//         .isEqualTo(true);
//     }
    
//     @Test //유저 존재 확인 오류
//     public void test_existsByAccountId_byUndefinedAccountId(){
//         //given
//         User user = new User("test1", "test1234!", "kim");
//         String undefinedAccountId = "fall";
//         //when
//         assertThat(userRepository.existsByAccountId(undefinedAccountId))
//         //then
//         .isEqualTo(false);
//     }
    
//     @Test
//     public void test_findAllBySearch(){
//         //given    
//         userRepository.save(new User("test1", "test1234!", "kim"));
//         userRepository.save(new User("test2", "test1234!", "park"));
//         userRepository.save(new User("test3", "test1234!", "kim"));
        
//         Pageable pageable = PageRequest.of(0,20);
//         String accountId = "test1";
//         String name = "kim";
        
//         //when
//         List<User> userList = userRepository.searchUsers(pageable, null, null).getContent();
//         List<User> userListByAccountId  = userRepository.searchUsers(pageable, accountId, null).getContent();
//         List<User> userListByName = userRepository.searchUsers(pageable, null, name).getContent();
//         List<User> userListByAccountIdAndName = userRepository.searchUsers(pageable, accountId, name).getContent();
//         //then
//         assertThat(userList.size()).isEqualTo(3);
//         assertThat(userList.get(0).getAccountId()).isEqualTo("test1");
//         assertThat(userList.get(1).getAccountId()).isEqualTo("test2");
//         assertThat(userList.get(2).getAccountId()).isEqualTo("test3");
        
//         assertThat(userListByAccountId.size()).isEqualTo(1);
//         assertThat(userListByAccountId.get(0).getAccountId()).isEqualTo("test1");
        
//         assertThat(userListByName.size()).isEqualTo(2);
//         assertThat(userListByName.get(0).getAccountId()).isEqualTo("test1");
//         assertThat(userListByName.get(1).getAccountId()).isEqualTo("test3");
        
//         assertThat(userListByAccountIdAndName.size()).isEqualTo(1);
//         assertThat(userListByAccountIdAndName.get(0).getAccountId()).isEqualTo("test1");
//     }
// }