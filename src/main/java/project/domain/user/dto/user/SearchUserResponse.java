package project.domain.user.dto.user;

import static java.util.stream.Collectors.toList;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.domain.user.domain.User;

@Getter
@Setter
@NoArgsConstructor
public class SearchUserResponse {
    private List<SearchUserDto> users;

    public static SearchUserResponse from(List<User> users) {
        SearchUserResponse response = new SearchUserResponse();
        response.setUsers(users.stream().map(SearchUserDto::new).collect(toList()));

        return response;
    }

    @Getter
    static class SearchUserDto {
        private Long userId;
        private String nickName;
        private String email;

        public SearchUserDto(User user) {
            this.userId = user.getId();
            this.nickName = user.getNickName();
            this.email = user.getEmail();
        }
    }
}
