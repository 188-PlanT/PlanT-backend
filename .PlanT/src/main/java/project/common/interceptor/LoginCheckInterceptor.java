package project.common.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import project.domain.User;
import project.domain.UserRole;
import project.admin.util.SessionConst;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor{
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        
        String requestURI = request.getRequestURI();
            
        HttpSession session = request.getSession(false);
        
        //근데 ADMIN이 아니면 세션 생성을 안하는데 여기서 중복 검증 해야하나??
        if (session == null || !checkSessionUserIsAdmin(session)){ // 세션이 존재하지 않거나, 어드민 유저가 없을 경우
            log.info("위임 전 사용자 요청");
            response.sendRedirect("/admin/login?redirectURL=" + requestURI);
            return false;
        }
        return true;
    }
    
    private boolean checkSessionUserIsAdmin(HttpSession session){
        User user = (User) session.getAttribute(SessionConst.LOGIN_USER);
        
        if (user != null && UserRole.ADMIN.equals(user.getUserRole())){
            return true;
        }
        
        return false;
    }
}