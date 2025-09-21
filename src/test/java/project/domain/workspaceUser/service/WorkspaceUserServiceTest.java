package project.domain.workspaceUser.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.common.helper.ServiceIntegrationTest;

// TODO: 테스트 구현
public class WorkspaceUserServiceTest extends ServiceIntegrationTest {

    @Autowired
    WorkspaceUserService workspaceUserService;

    @Nested
    class 워크스페이스_사용자_조회시 {

        @Test
        void 로그인_사용자가_워크스페이스에_속하지_않으면_실패한다() {}

        @Test
        void 워크스페이스가_존재하지_않으면_실패한다() {}
    }
}
