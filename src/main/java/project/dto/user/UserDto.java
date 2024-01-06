package project.dto.user;

import lombok.Getter;
import lombok.AllArgsConstructor;
import project.domain.User;

@Getter
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String name;
    
    public UserDto(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
    }
}