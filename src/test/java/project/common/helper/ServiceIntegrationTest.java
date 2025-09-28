package project.common.helper;

import static project.common.constant.UserConstant.PASSWORD;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import project.domain.user.dao.UserRepository;
import project.domain.user.domain.User;
import project.domain.workspace.dao.WorkspaceRepository;

@Tag("integration")
@ActiveProfiles("test")
@SpringBootTest
@Transactional // TODO: 직접 초기화 로직 작성하고 제거
public class ServiceIntegrationTest {

    protected UserRepository userRepository;
    protected WorkspaceRepository workspaceRepository;

    protected User createAdminUser(String email) {
        User user = User.ofEmailPassword(email, PASSWORD, null);
        user.finishRegister(email.split("@")[0]);
        ReflectionTestUtils.setField(user, "role", "ADMIN");
        return userRepository.save(user);
    }

    protected User createNormalUser(String email) {
        User user = User.ofEmailPassword(email, PASSWORD, null);
        user.finishRegister(email.split("@")[0]);
        return userRepository.save(user);
    }

    protected User createPendingUser(String email) {
        User user = User.ofEmailPassword(email, PASSWORD, null);
        return userRepository.save(user);
    }
}
