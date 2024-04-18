package project.domain.user.dto.user;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class EmailCheckResponse {
    boolean available;
}
