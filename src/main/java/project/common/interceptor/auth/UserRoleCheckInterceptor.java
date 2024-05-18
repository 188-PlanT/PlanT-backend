package project.common.interceptor.auth;

import project.common.security.oauth.UserInfo;
import project.domain.schedule.domain.Schedule;
import project.domain.user.domain.UserRole;
import project.domain.workspace.domain.UserWorkspace;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;
import project.domain.workspace.dao.UserWorkspaceRepository;
import project.domain.schedule.dao.ScheduleRepository;
import project.domain.workspace.dao.WorkspaceRepository;

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
public class UserRoleCheckInterceptor implements HandlerInterceptor{
	
	private final ScheduleRepository scheduleRepository;
	private final WorkspaceRepository workspaceRepository;
	private final UserWorkspaceRepository userWorkspaceRepository;
	
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
			Long loginUserId = getUserId();
			
			Long workspaceId = getWorkspaceId(request);

			validateWorkspaceId(workspaceId);
			
			checkUserAuthority(workspaceId, loginUserId, permitUserRole.value());
			
			return true;
		}
    }
	
	private Long getUserId(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserInfo loginUser = (UserInfo) authentication.getPrincipal();

		return loginUser.getUserId();
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
	
	private void checkUserAuthority(Long workspaceId, Long loginUserId, UserRole... roles){
		
		UserWorkspace userWorkspace = userWorkspaceRepository.searchByUserIdAndWorkspaceId(loginUserId, workspaceId)
			.orElseThrow(() -> new PlantException(ErrorCode.USER_AUTHORITY_INVALID));
		
		for (UserRole role : roles){
			if (userWorkspace.getUserRole().equals(role)){
				return;
			}
		}
		
		throw new PlantException(ErrorCode.USER_AUTHORITY_INVALID);
	}
	
	private void validateWorkspaceId(Long workspaceId){
		if (!workspaceRepository.existsById(workspaceId)){
			throw new PlantException(ErrorCode.WORKSPACE_NOT_FOUND);
		}
	}
}