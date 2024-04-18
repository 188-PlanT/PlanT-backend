package project.common.interceptor.auth;

import project.domain.user.domain.UserRole;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PermitUserRole {
	UserRole[] value();
}