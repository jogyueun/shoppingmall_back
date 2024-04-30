package cloud2.shopingmall.jwt;


import cloud2.shopingmall.user.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //request에서 Authorization 헤더를 찾음
        String authorization = request.getHeader("Authorization");

        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);

            // "Authorization" 헤더가 존재하지 않거나 "Bearer "로 시작하지 않으면 인증 실패
            // 다음 필터로 요청을 전달
            return;
        }

        // 토큰의 "Bearer" 의 뒷 부분 토큰 키 값 가져오기
        String token = authorization.split(" ")[1];

        //토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {

            filterChain.doFilter(request, response);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰 시간이 만료되었습니다.");

            // 토큰의 인증시간이 만료일 경우 인증 실패
            // 다음 필터로 요청을 전달
            return;
        }


        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        User user = new User();
        user.setUsername(username);
        user.setPassword("temppassword"); // 임시로 패스워드를 설정 ( 실제로는 JWT 토큰에서 패스워드를 추출 X )
        user.setUserRole(role);

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        // UsernamePasswordAuthenticationToken을 사용하여 사용자의 인증 정보를 생성 이 때, 인증된 사용자의 상세 정보인 customUserDetails와 사용자의 권한 정보를 전달
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        // 생성된 인증 정보를 SecurityContextHolder에 저장, 이를 통해 스프링 시큐리티는 현재 사용자가 인증되었음을 파악하고, 해당 사용자의 정보와 권한을 사용하여 인증 및 권한 부여를 수행
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
