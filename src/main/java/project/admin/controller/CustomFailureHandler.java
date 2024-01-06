package project.admin.controller;

import org.springframework.stereotype.Component;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.ui.Model;
import javax.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

@Component
@Slf4j
public class CustomFailureHandler implements AuthenticationFailureHandler {
    
    @Override
    public void onAuthenticationFailure(
        HttpServletRequest httpServletRequest,
        HttpServletResponse response,
        AuthenticationException e) throws IOException, ServletException {
        
        String errorMessage = getErrerMessage(e);
        
        response.sendRedirect("/admin/login?error=" + errorMessage); 
    }
    
    private String getErrerMessage(AuthenticationException exception){
        if (exception instanceof BadCredentialsException) {
            return "loginError";
        } else if (exception instanceof UsernameNotFoundException) {
            return "noAccount";
        } else if (exception instanceof AccountExpiredException) {
            return "accountExpired";
        } else if (exception instanceof CredentialsExpiredException) {
            return "passwordExpired";
        } else if (exception instanceof DisabledException) {
            return "disabledAccount";
        } else if (exception instanceof LockedException) {
            return "lockedAccount";
        } else {
            return "unknownError";
        }
    }
}