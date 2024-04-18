package project.domain.user.dto.user;

import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.domain.user.domain.User;
import project.domain.user.domain.UserRole;

@Setter
@Getter
@NoArgsConstructor
public class UserDto {
    private Long userId;
    private String nickName;
    private String email;
    private String profile;
	private UserRole state;
    
    public static UserDto from(User user){
        UserDto dto = new UserDto();

        dto.setUserId(user.getId());
        dto.setNickName(user.getNickName());
        dto.setEmail(user.getEmail());
        dto.setProfile(user.getProfile().getUrl());
		dto.setState(user.getUserRole());
        
        return dto;
    }
}