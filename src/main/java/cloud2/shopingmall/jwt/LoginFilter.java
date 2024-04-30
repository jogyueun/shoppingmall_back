package cloud2.shopingmall.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.Iterator;


public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    // 사용자의 인증 처리 객체, 사용자가 제공한 자격 증명을 검증하고, 이를 기반으로 사용자의 인증 객체 생성
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {

        this.authenticationManager = authenticationManager;

        this.jwtUtil = jwtUtil;

        // 원하는 로그인 경로로 변경
        this.setFilterProcessesUrl("/api/v1/login");
    }

    // 사용자의 인증을 시도
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String username = obtainUsername(request);

        String password = obtainPassword(request);

        // Authentication의 구현체, 사용자 이름과 비밀번호를 저장
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        // AuthenticationManager를 사용하여 사용자를 인증
        // 주어진 Authentication 객체를 사용하여 사용자를 인증하고, 성공하면 Authentication 객체를 반환, 인증이 실패하면 AuthenticationException이 발생합니다.
        return authenticationManager.authenticate(authToken);
    }

    // 인증이 성공하면 실행되는 메서드
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String username = customUserDetails.getUsername();
        String name = customUserDetails.getName();
        String email = customUserDetails.getEmail();


        // 사용자의 권한 목록을 Collection 형태로 바꾼 후, 권한을 반복자를 통해 가져온다
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(username, role, name, email, 6000*6000*1L);

        response.addHeader("Authorization", "Bearer " + token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

        response.setStatus(401);
    }
}
