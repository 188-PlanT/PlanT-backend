package project.common.interceptor.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;
import project.common.util.UserUtil;
import project.domain.schedule.dao.ScheduleRepository;
import project.domain.schedule.domain.Schedule;
import project.domain.user.domain.UserRole;

@Deprecated
@Slf4j
@Component
@RequiredArgsConstructor
public class UserRoleCheckInterceptor implements HandlerInterceptor {

    private final ScheduleRepository scheduleRepository;
    private final UserUtil userUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        PermitUserRole permitUserRole = handlerMethod.getMethodAnnotation(PermitUserRole.class);

        if (permitUserRole == null) {
            return true;
        } else {
            Long workspaceId = getWorkspaceId(request);

            checkUserAuthority(workspaceId, permitUserRole.value());

            return true;
        }
    }

    private Long getWorkspaceId(HttpServletRequest request) {
        Map<String, String> pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        if (pathVariables.get("workspaceId") != null) {
            return Long.parseLong(pathVariables.get("workspaceId"));
        } else if (pathVariables.get("scheduleId") != null) {
            Long scheduleId = Long.parseLong(pathVariables.get("scheduleId"));

            Schedule schedule = scheduleRepository
                    .findById(scheduleId)
                    .orElseThrow(() -> new PlantException(ErrorCode.SCHEDULE_NOT_FOUND));

            return schedule.getWorkspace().getId();
        } else {
            throw new PlantException(ErrorCode.WORKSPACE_NOT_FOUND, "workspace 검증중 오류가 발생했습니다.");
        }
    }

    private void checkUserAuthority(Long workspaceId, UserRole... userRoles) {
        UserRole loginUserRole = userUtil.getLoginUserRole(workspaceId);

        if (loginUserRole == null || !checkUserRoleInUserRoles(loginUserRole, userRoles)) {
            throw new PlantException(ErrorCode.USER_AUTHORITY_INVALID);
        }
    }

    private boolean checkUserRoleInUserRoles(UserRole loginUserRole, UserRole[] userRoles) {

        for (UserRole userRole : userRoles) {
            if (userRole.equals(loginUserRole)) return true;
        }
        return false;
    }
}
