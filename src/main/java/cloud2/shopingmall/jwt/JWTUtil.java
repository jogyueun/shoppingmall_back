package cloud2.shopingmall.jwt;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private SecretKey secretKey;

    // application.yml 파일의 키값을 가져온다
    // 주어진 시크릿을 UTF-8 문자 인코딩을 사용하여 바이트 배열로 변환 -> HMAC-SHA256 알고리즘을 사용하여 시크릿 키를 생성
    //  Jwts.SIG.HS256는 JWT의 서명 알고리즘을 나타내며, HS256은 HMAC-SHA256 알고리즘을 사용한다는 것을 의미
    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getUsername(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String createJwt(String username, String role, String name, String email, Long expiredMs) {

        return Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .claim("name", name)
                .claim("email", email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}
