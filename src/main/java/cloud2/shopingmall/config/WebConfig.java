package cloud2.shopingmall.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.http.CacheControl.maxAge;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")                  // 다음 경로로 들어오는 요청에 대해
                .allowedOrigins("http://localhost:5173","http://localhost:5173/") // 다음 경로에서 오는 요청을 허용 하며 (현재는 전부)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")   // 허용되는 메서드
                .allowedHeaders("Authorization", "Content-Type")  // 허용되는 헤드
                .exposedHeaders("Authorization", "Content-Type")  // 클라이언트측 응답에서 노출되는 헤더를 지정합니다.
                .allowCredentials(true)
                .maxAge(3000); // 원하는 시간만큼 pre-flight 리퀘스트를 캐싱;                           // 클라이언트 측에 대한 응답에 credentials(예: 쿠키, 인증 헤더)를 포함할 수 있는지 여부를 지정



    }
}