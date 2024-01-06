package project.admin.util;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import project.domain.User;

@Slf4j
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver{
    
    private final String LOGIN_USER = "login_user";
    
    @Override
    public boolean supportsParameter(MethodParameter parameter){
        // log.info("suppoersParameter");
        
        boolean hasLogin = parameter.hasParameterAnnotation(Login.class); //로그인 어노테이션이 파라미터에 있는가
        boolean hasUserType = User.class.isAssignableFrom(parameter.getParameterType()); //유저를 파라미터로 받는가
        
        return hasLogin && hasUserType;
    }
    
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        
        HttpSession session = request.getSession(false);
        
        if (session == null){
            return null;
        }
        return session.getAttribute(SessionConst.LOGIN_USER);
    }
}