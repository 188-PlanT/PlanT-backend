package project.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    
    USER("ROLE_USER", "사용자"),
    ADMIN("ROLE_ADMIN", "어드민"),
    PENDING("ROLE_PENDING", "가입대기");
    
    private final String key;
    private final String title;
}