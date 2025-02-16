package project.common.interceptor.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import project.common.util.UrlUtil;
import project.domain.user.domain.User;
import project.domain.user.domain.UserRole;
import project.common.admin.util.SessionConst;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor{

    private final UrlUtil urlUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{

        String requestURI = request.getRequestURI();

        HttpSession session = request.getSession(false);

         if (session == null || !isSessionUserAdmin(session)){ // 세션이 존재하지 않거나, 어드민 유저가 아닐 경우
             log.info("위임 전 사용자 요청");
             response.sendRedirect( urlUtil.getApiUrl() + "/admin/login");
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