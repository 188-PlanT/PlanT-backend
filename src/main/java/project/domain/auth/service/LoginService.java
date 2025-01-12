package project.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;
import project.common.security.jwt.JwtProvider;
import project.domain.auth.dto.response.LoginResponse;
import project.domain.image.domain.Image;
import project.domain.image.service.ImageService;
import project.domain.user.dao.UserRepository;
import project.domain.user.domain.User;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final ImageService imageService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;


    @Transactional(readOnly = true)
    public LoginResponse loginByEmailAndPassword(String email, String password){
        User findUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new PlantException(ErrorCode.USER_NOT_FOUND));

        findUser.checkPassword(password, passwordEncoder);

        return createJwtTokenResponseByUser(findUser);
    }

    @Transactional
    public LoginResponse loginByOauth2UserEmail(String email){
        User loginUser = saveOrUpdate(email);

        return createJwtTokenResponseByUser(loginUser);
    }

    //OAuth2 유저 생성 로직
    private User saveOrUpdate(String email){

        Optional<User> findUser = userRepository.findByEmail(email);

        if(findUser.isPresent()){
            return findUser.get();
        }
        else{
            Image defaultUserProfile = imageService.getDefaultUserProfile();

            User user = User.fromOAuth2Attributes(email, defaultUserProfile);
            userRepository.save(user);

            return user;
        }
    }

    private LoginResponse createJwtTokenResponseByUser(User user){
        String accessToken = jwtProvider.createAccessTokenByUser(user);
        String refreshToken = jwtProvider.createRefreshTokenByUser(user);

        return new LoginResponse(accessToken, refreshToken);
    }

    // <== Dumy DB 로그인 ==>
    // 비밀번호 암호화 과정 X
    @Transactional(readOnly = true)
    public LoginResponse loginInDumy(String email, String password){
        User findUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new PlantException(ErrorCode.USER_NOT_FOUND));

        if(!findUser.getPassword().equals(password)){
            throw new PlantException(ErrorCode.PASSWORD_INVALD);
        };

        return createJwtTokenResponseByUser(findUser);
    }

}
