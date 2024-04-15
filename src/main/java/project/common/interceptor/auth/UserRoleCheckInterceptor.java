package project.common.interceptor.auth;

import project.domain.*;
import project.common.auth.oauth.UserInfo;
import project.repository.UserWorkspaceRepository;
import project.repository.ScheduleRepository;
import project.repository.UserScheduleRepository;
import project.exception.workspace.NoSuchWorkspaceException;
import project.exception.schedule.NoSuchScheduleException;
import project.exception.user.InvalidAuthorityException;

// import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

// @Slf4j
@Component
@RequiredArgsConstructor
public class UserRoleCheckInterceptor implements HandlerInterceptor{
	
	private final ScheduleRepository scheduleRepository;
	
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
				.orElseThrow(NoSuchScheduleException::new);
			
			return schedule.getWorkspace().getId();
		}
		else {
			throw new NoSuchWorkspaceException("url 형식이 올바르지 않습니다");
		}
	}
	
	private void checkUserAuthority(Long workspaceId, Long loginUserId, UserRole... roles){
		UserWorkspace userWorkspace = userWorkspaceRepository.searchByUserIdAndWorkspaceId(loginUserId, workspaceId)
			.orElseThrow(NoSuchWorkspaceException::new);
		
		for (UserRole role : roles){
			if (userWorkspace.getUserRole().equals(role)){
				return;
			}
		}
		
		throw new InvalidAuthorityException();
	}
}