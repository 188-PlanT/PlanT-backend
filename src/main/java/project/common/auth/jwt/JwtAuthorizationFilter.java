package project.common.auth.jwt;

import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter{

    private final JwtProvider jwtProvider;
    
    @Autowired
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                 JwtProvider jwtProvider){
        super(authenticationManager);
        this.jwtProvider = jwtProvider;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        
        String accessToken = request.getHeader("Authorization");
        
        if (accessToken== null){
            chain.doFilter(request, response);
            return;
        }
            
        Authentication authentication = jwtProvider.getAuthentication(accessToken);
        
        SecurityContextHolder.getContext().setAuthentication(authentication); 
        
        chain.doFilter(request, response);
    }
}