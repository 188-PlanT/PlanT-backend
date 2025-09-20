package project.domain.user.dto.response;

import static java.util.stream.Collectors.toList;

import java.util.List;
import project.domain.user.domain.User;

public record SearchUserResponse(List<SearchUserDto> users) {
    public static SearchUserResponse from(List<User> users) {
        List<SearchUserDto> userDtos = users.stream().map(SearchUserDto::new).collect(toList());
        return new SearchUserResponse(userDtos);
    }

    public record SearchUserDto(Long userId, String nickName, String email) {
        public SearchUserDto(User user) {
            this(user.getId(), user.getNickName(), user.getEmail());
        }
    }
}
