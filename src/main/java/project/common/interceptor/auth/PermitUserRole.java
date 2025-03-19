package project.common.interceptor.auth;

import java.lang.annotation.*;
import project.domain.user.domain.UserRole;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PermitUserRole {
    UserRole[] value();
}
