package project.common.interceptor.auth;

import lombok.extern.slf4j.Slf4j;
import project.domain.auth.domain.UserInfo;
import project.domain.schedule.domain.Schedule;
import project.domain.user.domain.UserRole;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;
import project.domain.schedule.dao.ScheduleRepository;

import lombok.RequiredArgsConstructor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

// @Slf4j
@Component
@RequiredArgsConstructor
@Slf4j
public class UserRoleCheckInterceptor implements HandlerInterceptor{
	
	private final ScheduleRepository scheduleRepository;
	
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
		if (!(handler instanceof  HandlerMethod)) {
			return true;
		}

		HandlerMethod handlerMethod = (HandlerMethod) handler;
		PermitUserRole permitUserRole = handlerMethod.getMethodAnnotation(PermitUserRole.class);

		if (permitUserRole == null){
			return true;
		}
		else {
			Long workspaceId = getWorkspaceId(request);

			UserInfo loginUserInfo = getUserInfo();

			checkUserAuthority(workspaceId, loginUserInfo, permitUserRole.value());
			
			return true;
		}
    }

	private UserInfo getUserInfo(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (UserInfo) authentication.getPrincipal();
	}
	
	private Long getWorkspaceId(HttpServletRequest request){
		Map <String, String> pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		
		if (pathVariables.get("workspaceId") != null){
			return Long.parseLong(pathVariables.get("workspaceId"));
		}
		else if (pathVariables.get("scheduleId") != null){
			Long scheduleId = Long.parseLong(pathVariables.get("scheduleId"));
			
			Schedule schedule = scheduleRepository.findById(scheduleId)
				.orElseThrow(() -> new PlantException(ErrorCode.SCHEDULE_NOT_FOUND));
			
			return schedule.getWorkspace().getId();
		}
		else {
			throw new PlantException(ErrorCode.WORKSPACE_NOT_FOUND, "workspace 검증중 오류가 발생했습니다.");
		}
	}
	private void checkUserAuthority(Long workspaceId, UserInfo userInfo, UserRole... userRoles) {
		UserRole loginUserRole = getLoginUserRole(workspaceId, userInfo);

		if(loginUserRole == null || !checkUserRoleInUserRoles(loginUserRole, userRoles)) {
			throw new PlantException(ErrorCode.USER_AUTHORITY_INVALID);
		}
	}

	private UserRole getLoginUserRole(Long workspaceId, UserInfo userInfo){
		if(userInfo.getWorkspaceUserIds().stream()
				.anyMatch((workspaceUserId) -> workspaceUserId.equals(workspaceId))) return UserRole.USER;


		if (userInfo.getWorkspaceAdminIds().stream()
				.anyMatch((workspaceAdminId) -> workspaceAdminId.equals(workspaceId))) return UserRole.ADMIN;

		return null;
	}

	private boolean checkUserRoleInUserRoles(UserRole loginUserRole, UserRole[] userRoles){

		for(UserRole userRole : userRoles){
			if(userRole.equals(loginUserRole)) return true;
		}
		return false;
	}
}