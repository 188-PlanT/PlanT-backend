package project.domain.workspace.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.domain.user.domain.UserRole;

@Getter
@Setter
@NoArgsConstructor
public class UpdateUserRequest {
    @NotNull private UserRole authority;
}
