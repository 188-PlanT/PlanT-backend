package project.common.security.jwt;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    
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