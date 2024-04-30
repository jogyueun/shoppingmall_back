package cloud2.shopingmall.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        setResponse(response, "Unauthorized", HttpStatus.UNAUTHORIZED);
    }

    private void setResponse(HttpServletResponse response, String message, HttpStatus httpStatus) throws IOException {
        // 로깅
        System.out.println("[exceptionHandle] JwtAuthenticationEntryPoint: " + message);

        // JSON 응답 설정
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");

        // 응답에 메시지 전달
        StatusResponseDto statusResponseDto = new StatusResponseDto(httpStatus.value(), message);
        response.getWriter().write(objectMapper.writeValueAsString(statusResponseDto));
    }

    // 응답에 포함될 JSON 형식의 응답 객체
    public static class StatusResponseDto {
        private final int statusCode;
        private final String message;

        public StatusResponseDto(int statusCode, String message) {
            this.statusCode = statusCode;
            this.message = message;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public String getMessage() {
            return message;
        }
    }
}