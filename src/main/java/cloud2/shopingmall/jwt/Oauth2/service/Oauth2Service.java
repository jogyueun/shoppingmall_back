package cloud2.shopingmall.jwt.Oauth2.service;

import cloud2.shopingmall.jwt.JWTUtil;
import cloud2.shopingmall.jwt.Oauth2.dto.Oauth2DTO;
import cloud2.shopingmall.user.entity.User;
import cloud2.shopingmall.user.entity.UserProfile;
import cloud2.shopingmall.user.repository.UserProfileRepository;
import cloud2.shopingmall.user.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class Oauth2Service {
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final JWTUtil jwtUtil;

    @Value("${naver.client-id}")
    private String naverClientId;

    @Value("${naver.client-secret}")
    private String naverClientSecret;

    @Value("${kakao.rest-key}")
    private String kakaoClientId;

    @Value("${kakao.client-secret}")
    private String kakaoClientSecret;

    @Autowired
    public Oauth2Service(UserRepository userRepository, UserProfileRepository userProfileRepository, JWTUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.jwtUtil = jwtUtil;
    }

    public String naverLogin(Oauth2DTO.Access dto) {

        String code = dto.getAuthorizationCode();
        String state = dto.getState();
        Oauth2DTO.Join joinDTO = new Oauth2DTO.Join();

        // 네이버 API에 액세스 토큰 요청을 위한 URL
        String tokenUrl = "https://nid.naver.com/oauth2.0/token?client_id=" + naverClientId +
                "&client_secret=" + naverClientSecret +
                "&grant_type=authorization_code&state=" + state +
                "&code=" + code;
        // HTTP 요청을 보내고 응답을 받는 데 사용
        RestTemplate restTemplate = new RestTemplate();
        // 네이버 API에 엑세스 토큰 요청
        ResponseEntity<String> tokenResponse = restTemplate.exchange(tokenUrl, HttpMethod.GET, null, String.class);

        // 엑세스 토큰 요청 실패할 경우
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("네이버 로그인에 실패했습니다.");
        }

        // 액세스 토큰을 성공적으로 받아왔을 경우
        // 응답에서 JSON 문자열 추출
        String responseBody = tokenResponse.getBody();

        // JSON 라이브러리를 사용하여 access_token 값 추출
        JSONObject jsonObject = new JSONObject(responseBody);
        String accessToken = jsonObject.getString("access_token");

        // HttpHeaders 생성
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken); // 액세스 토큰을 Bearer 에 추가

        // HTTP 요청 시 헤더에 HttpHeaders 추가
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // 네이버 API에 회원정보 조회 요청
        ResponseEntity<String> userInfo = restTemplate.exchange("https://openapi.naver.com/v1/nid/me", HttpMethod.GET, requestEntity, String.class);

        // 회원정보의 응답(body) 가져오기
        String userInfoJson = userInfo.getBody();

        // Jackson ObjectMapper 생성
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // JSON 문자열을 JsonNode 객체로 파싱
            JsonNode jsonNode = objectMapper.readTree(userInfoJson);

            // "response" 안의 JSON 데이터 가져오기
            JsonNode responseData = jsonNode.get("response");

            // 필요한 데이터 추출
            String userId = responseData.get("id").asText();
            String email = responseData.get("email").asText();
            String name = responseData.get("name").asText();
            String  birth = responseData.get("birthyear").asText() + "-" + responseData.get("birthday").asText();
            String gender = responseData.get("gender").asText();

            joinDTO.setName(name);
            joinDTO.setEmail(email);
            joinDTO.setBirth(birth);
            joinDTO.setUsername(userId);
            if(gender.equals("M")){
                joinDTO.setGender("MALE");
            }
            else {
                joinDTO.setGender("FEMALE");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // DTO의 데이터로 회원가입 진행
        User existData = userRepository.findByUsername(joinDTO.getUsername());
        // DB에 데이터가 없을 경우 (신규 등록)
        if (existData == null) {

            User user = new User();
            UserProfile userProfile = new UserProfile();

            user.setUsername(joinDTO.getUsername());
            user.setUserRole("ROLE_USER");
            user.setLoginType("Naver");
            user.setStatus(User.Status.ACTIVE);
            user.setPassword("temporary");
            User savedUser = userRepository.save(user);

            userProfile.setEmail(joinDTO.getEmail());
            userProfile.setName(joinDTO.getName());
            userProfile.setUser(savedUser);
            userProfile.setGender(joinDTO.getGender());
            userProfile.setPoint(100000); // 처음 가입 시 10만 포인트 증정

            userProfileRepository.save(userProfile);

        }
        // DB에 데이터가 있을 경우 (수정)
        else {

            existData.getUserProfile().setEmail(joinDTO.getEmail());
            existData.getUserProfile().setName(joinDTO.getName());

            userProfileRepository.save(existData.getUserProfile());
        }

        String token = jwtUtil.createJwt(joinDTO.getUsername(), "ROLE_USER", joinDTO.getUsername(), joinDTO.getEmail(), 6000*6000*1L);


        return token;
    }

    public String kakaoLogin(Oauth2DTO.Access dto) {

        String code = dto.getAuthorizationCode();
        String state = dto.getState();
        Oauth2DTO.Join joinDTO = new Oauth2DTO.Join();

        // 카카오 API에 액세스 토큰 요청을 위한 URL
        String tokenUrl = "https://kauth.kakao.com/oauth/token?" +
                "grant_type=authorization_code" +
                "&client_id=" + kakaoClientId +
                "&redirect_uri=http://localhost:8080/api/oauth2/login/code/kakao" +
                "&code=" + code +
                "&client_secret=" + kakaoClientSecret;

        // HTTP 요청을 보내고 응답을 받는 데 사용
        RestTemplate restTemplate = new RestTemplate();
        // HttpHeaders 생성
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        // HTTP 요청 시 헤더에 HttpHeaders 추가
        HttpEntity<String> requestEntity = new HttpEntity<>(requestHeaders);
        // 네이버 API에 엑세스 토큰 요청
        ResponseEntity<String> tokenResponse = restTemplate.exchange(tokenUrl, HttpMethod.POST, requestEntity, String.class);

        // 엑세스 토큰 요청 실패할 경우
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return null;
        }
        // 액세스 토큰을 성공적으로 받아왔을 경우
        // 응답에서 JSON 문자열 추출
        String responseBody = tokenResponse.getBody();

        // JSON 라이브러리를 사용하여 access_token 이라는 필드의 값 추출
        JSONObject jsonObject = new JSONObject(responseBody);
        String accessToken = jsonObject.getString("access_token");

        // HttpHeaders 생성
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken); // 액세스 토큰을 Bearer 에 추가
        headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 시 헤더에 HttpHeaders 추가
        HttpEntity<String> tokenRequestEntity = new HttpEntity<>(headers);

        // 네이버 API에 회원정보 조회 요청
        ResponseEntity<String> userInfo = restTemplate.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.GET, tokenRequestEntity, String.class);

        // 회원정보의 응답(body) 가져오기
        String userInfoJson = userInfo.getBody();

        // Jackson ObjectMapper 생성
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // JSON 문자열을 JsonNode 객체로 파싱
            JsonNode jsonNode = objectMapper.readTree(userInfoJson);

            // "response" 안의 JSON 데이터 가져오기
            JsonNode responseData = jsonNode.get("response");

            // 필요한 데이터 추출
            String userId = responseData.get("id").asText();
            String email = responseData.get("email").asText();
            String name = responseData.get("name").asText();
            String  birth = responseData.get("birthyear").asText() + "-" + responseData.get("birthday").asText();
            String gender = responseData.get("gender").asText();

            joinDTO.setName(name);
            joinDTO.setEmail(email);
            joinDTO.setBirth(birth);
            joinDTO.setUsername(userId);
            if(gender.equals("M")){
                joinDTO.setGender("MALE");
            }
            else {
                joinDTO.setGender("FEMALE");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // DTO의 데이터로 회원가입 진행
        User existData = userRepository.findByUsername(joinDTO.getUsername());
        // DB에 데이터가 없을 경우 (신규 등록)
        if (existData == null) {

            User user = new User();
            UserProfile userProfile = new UserProfile();

            user.setUsername(joinDTO.getUsername());
            user.setUserRole("ROLE_USER");
            user.setLoginType("Kakao");
            user.setStatus(User.Status.ACTIVE);
            user.setPassword("temporary");
            User savedUser = userRepository.save(user);

            userProfile.setEmail(joinDTO.getEmail());
            userProfile.setName(joinDTO.getName());
            userProfile.setUser(savedUser);
            userProfile.setGender(joinDTO.getGender());
            userProfile.setPoint(100000); // 처음 가입 시 10만 포인트 증정

            userProfileRepository.save(userProfile);

        }
        // DB에 데이터가 있을 경우 (수정)
        else {

            existData.getUserProfile().setEmail(joinDTO.getEmail());
            existData.getUserProfile().setName(joinDTO.getName());

            userProfileRepository.save(existData.getUserProfile());
        }

        String token = jwtUtil.createJwt(joinDTO.getUsername(), "ROLE_USER", joinDTO.getUsername(), joinDTO.getEmail(), 6000*6000*1L);


        return token;
    }
}
