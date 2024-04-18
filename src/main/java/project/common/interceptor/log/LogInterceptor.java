package project.common.interceptor.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor{
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        String httpMethod = request.getMethod();
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();
        
        request.setAttribute("logId", uuid);
        
        log.info("========REQUEST [{}] [{}] {} =========", uuid, httpMethod, requestURI);
        return true;
    }
    
    // @Override
    // public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception{
    //     log.info("postHandle [{}]", modelAndView);
    // }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception{
        String httpMethod = request.getMethod();
        String requestURI = request.getRequestURI();
        String uuid = (String) request.getAttribute("logId");
        
        log.info("========RESPONSE [{}] [{}] {} =========", uuid, httpMethod, requestURI);
        
        if(ex != null){
            log.error("afterCompletion error", ex);
        }
    }
}