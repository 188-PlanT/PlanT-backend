package project.common.interceptor.auth;

import project.domain.UserRole;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PermitUserRole {
	UserRole[] value();
}