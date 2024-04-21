package project.domain.user.dto.user;

import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.domain.user.domain.User;
import project.domain.user.domain.UserRole;

@Setter
@Getter
@NoArgsConstructor
public class FinishUserRegisterResponse {
    private Long userId;
    private String nickName;
    private String email;
    private String profile;
	private UserRole state;
	private String accessToken;
    
    public static FinishUserRegisterResponse from(User user, String accessToken){
        FinishUserRegisterResponse dto = new FinishUserRegisterResponse();

        dto.setUserId(user.getId());
        dto.setNickName(user.getNickName());
        dto.setEmail(user.getEmail());
        dto.setProfile(user.getProfile().getUrl());
		dto.setState(user.getUserRole());
		dto.setAccessToken(accessToken);
        
        return dto;
    }
}