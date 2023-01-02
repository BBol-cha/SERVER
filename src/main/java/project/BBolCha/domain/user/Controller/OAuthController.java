package project.BBolCha.domain.user.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.BBolCha.domain.user.Dto.UserDto;
import project.BBolCha.domain.user.OAuth.KakaoOAuth;
import project.BBolCha.domain.user.Service.OAuthService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OAuthController {

    private final OAuthService oAuthService;
    private final KakaoOAuth kakaoOAuth;

    @GetMapping("auth/kakao")
    public void getKakaoAuthUrl(HttpServletResponse response) throws IOException {
        response.sendRedirect(kakaoOAuth.responseUrl());
    }

    @GetMapping("auth/kakao/login")
    public ResponseEntity<UserDto.loginResponse> kakaoLogin(
            @RequestParam(name = "code") String code, HttpServletResponse response) throws IOException {
        return oAuthService.kakaoLogin(code,response);
    }

/*    // Sosial 로그인 이후 추가 정보 요청
    @PostMapping("/")
    public ResponseEntity<StatusTrue> socialRegister(
            @RequestBody final UserRegisterDto.socialRequest request
    ) {
        return oAuthService.socialRegister(request);
    }*/
}
