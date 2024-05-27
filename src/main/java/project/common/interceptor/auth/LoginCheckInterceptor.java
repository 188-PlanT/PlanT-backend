package project.common.interceptor.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import project.domain.user.domain.User;
import project.domain.user.domain.UserRole;
import project.common.admin.util.SessionConst;
import project.common.security.oauth.UserInfo;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{

        String requestURI = request.getRequestURI();

        HttpSession session = request.getSession(false);

         if (session == null || !isSessionUserAdmin(session)){ // 세션이 존재하지 않거나, 어드민 유저가 아닐 경우
             log.info("위임 전 사용자 요청");
             response.sendRedirect("/admin/login?redirectURL=" + requestURI);
             return false;
         }
         return true;
    }

    private boolean isSessionUserAdmin(HttpSession session){
        User user = (User) session.getAttribute(SessionConst.LOGIN_USER);

        if (user != null && UserRole.ADMIN.equals(user.getUserRole())){
            return true;
        }

        return false;
    }
}