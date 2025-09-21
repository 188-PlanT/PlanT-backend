package project.domain.user.domain;

import static org.assertj.core.api.Assertions.*;
import static project.common.constant.UrlConstant.*;
import static project.common.constant.UserConstant.*;

import org.junit.jupiter.api.Test;
import project.domain.image.domain.Image;

class UserTest {

    @Test
    void 유저_생성시_상태는_PENDING이다() {
        // given
        Image profileImage = new Image(DEFAULT_USER_PROFILE_URL);

        // when
        User emailPasswordUser = User.ofEmailPassword(EMAIL, PASSWORD, profileImage);
        User oauthUser = User.fromOAuth2Attributes(EMAIL, profileImage);

        // then
        assertThat(emailPasswordUser.getUserRole()).isEqualTo(UserRole.PENDING);
        assertThat(oauthUser.getUserRole()).isEqualTo(UserRole.PENDING);
    }

    @Test
    void 닉네임_설정시_회원가입이_완료된다() {
        // given
        Image profileImage = new Image(DEFAULT_USER_PROFILE_URL);
        User emailPasswordUser = User.ofEmailPassword(EMAIL, PASSWORD, profileImage);
        User oauthUser = User.fromOAuth2Attributes(EMAIL, profileImage);

        // when
        emailPasswordUser.finishRegister(NICKNAME);
        oauthUser.finishRegister(NICKNAME);

        // then
        assertThat(emailPasswordUser.getUserRole()).isEqualTo(UserRole.USER);
        assertThat(oauthUser.getUserRole()).isEqualTo(UserRole.USER);
    }
}
