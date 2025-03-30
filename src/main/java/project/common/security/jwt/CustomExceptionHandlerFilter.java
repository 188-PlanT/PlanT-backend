package project.common.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import project.common.exception.ErrorCode;
import project.common.exception.ErrorResponse;
import project.common.exception.PlantException;

/**
 * 토큰 검증 중 토큰 올바르지 않을 때 발생하는 에러를 처리하는 필터 (401 Unauthorized)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);

        } catch (PlantException e) {
            setErrorResponse(response, ErrorCode.TOKEN_INVALID);
        }
    }

    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) {
        response.setStatus(errorCode.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ErrorResponse errorResponse = new ErrorResponse(errorCode.name(), errorCode.getMessage());
        try {
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (IOException e) {
            log.error("CustomExceptionHandlerFilter.setErrorResponse: {}", e.getMessage());
        }
    }
}
