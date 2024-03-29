package project.common.auth.jwt;

import project.exception.auth.*;
import project.dto.ErrorCode;
import project.dto.ErrorResponse;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.http.MediaType;

@Slf4j
public class CustomExceptionHandlerFilter extends OncePerRequestFilter { // 필터에서 토큰 검증 중 토큰 올바르지 않을 때 발생하는 에러 처리
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try{
            filterChain.doFilter(request, response);
            
        }
        catch (InvalidTokenException e){
            setErrorResponse(response, ErrorCode.NOT_FOUND);
        }
    }
    
    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode){
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(errorCode.getCode());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        
        ErrorResponse errorResponse = new ErrorResponse(errorCode, "토큰이 올바르지 않습니다");
        try{
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}