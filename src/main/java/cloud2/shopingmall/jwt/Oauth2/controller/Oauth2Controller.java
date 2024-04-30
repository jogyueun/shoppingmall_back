package cloud2.shopingmall.jwt.Oauth2.controller;


import cloud2.shopingmall.jwt.Oauth2.dto.Oauth2DTO;
import cloud2.shopingmall.jwt.Oauth2.service.Oauth2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/api")
public class Oauth2Controller {

    private final Oauth2Service oauth2Service;
    @Autowired
    public Oauth2Controller(Oauth2Service oauth2Service) {
        this.oauth2Service = oauth2Service;
    }

    // 네이버 CallBack 맵핑
    @GetMapping("/oauth2/login/code/naver")
    public String NaverLoginRequest(@RequestParam("code") String code, @RequestParam("state") String state) {

        // 새로운 URL을 생성할 때, 코드와 상태를 파라미터에 다시 추가 - 프론트에서 검증
        String redirectUrl = "http://localhost:5173/naverCallBack?code=" + code + "&state=" + state;

        // 리다이렉트할 URL을 반환합니다.
        return "redirect:" + redirectUrl;
    }
    
    // 네이버 로그인
    @PostMapping("/oauth2/login/code/naver")
    public ResponseEntity<String> NaverLoginResponse(@RequestBody Oauth2DTO.Access dto) {

        String token = oauth2Service.naverLogin(dto);

        // 응답 헤더 설정
        HttpHeaders headers = new HttpHeaders();

        // 헤더에 jwt 토큰 생성하여 발급
        headers.add("Authorization", "Bearer " + token);

        // ResponseEntity 생성
        ResponseEntity<String> responseEntity = new ResponseEntity<>("로그인에 성공했습니다.", headers, HttpStatus.OK);

        return responseEntity;
    }

    // 카카오 CallBack 맵핑
    @GetMapping("/oauth2/login/code/kakao")
    public String KakaoLoginRequest(@RequestParam("code") String code, @RequestParam("state") String state) {

        // 새로운 URL을 생성할 때, 코드와 상태를 파라미터에 다시 추가 - 프론트에서 검증
        String redirectUrl = "http://localhost:5173/kakaoCallBack?code=" + code + "&state=" + state;

        // 리다이렉트할 URL을 반환합니다.
        return "redirect:" + redirectUrl;
    }

    // 카카오 로그인
    @PostMapping("/oauth2/login/code/kakao")
    public ResponseEntity<String> KakaoLoginResponse(@RequestBody Oauth2DTO.Access dto) {

        String token = oauth2Service.kakaoLogin(dto);

        // 응답 헤더 설정
        HttpHeaders headers = new HttpHeaders();

        // 헤더에 jwt 토큰 생성하여 발급
        headers.add("Authorization", "Bearer " + token);

        // ResponseEntity 생성
        ResponseEntity<String> responseEntity = new ResponseEntity<>("로그인에 성공했습니다.", headers, HttpStatus.OK);

        return responseEntity;
    }

}
