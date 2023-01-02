package project.BBolCha.domain.user.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.BBolCha.domain.user.Dto.KakaoOAuthTokenDto;
import project.BBolCha.domain.user.Dto.KakaoUserInfoDto;
import project.BBolCha.domain.user.Dto.UserDto;
import project.BBolCha.domain.user.Entity.Authority;
import project.BBolCha.domain.user.Entity.User;
import project.BBolCha.domain.user.OAuth.KakaoOAuth;
import project.BBolCha.domain.user.Repository.UserRepository;
import project.BBolCha.global.config.Jwt.TokenProvider;
import project.BBolCha.global.config.RedisDao;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KakaoOAuth kakaoOAuth;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisDao redisDao;
    UsernamePasswordAuthenticationToken authenticationToken = null;
    // validate 및 단순 메소드
    Authority authority = Authority.builder()
            .authorityName("ROLE_USER")
            .build();


    private ResponseEntity<UserDto.loginResponse> Login(String email, HttpServletResponse response) {
        if (email.contains("gmail")) {
            authenticationToken = new UsernamePasswordAuthenticationToken(email, "google");
        }
        if (email.contains("daum")) {
            authenticationToken = new UsernamePasswordAuthenticationToken(email, "kakao");
        }

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String atk = tokenProvider.createToken(authentication);
        String rtk = tokenProvider.createRefreshToken(email);

        redisDao.setValues(email, rtk, Duration.ofDays(14));

        Cookie cookie = new Cookie("refreshToken", rtk);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        return new ResponseEntity<>(UserDto.loginResponse.response(
                atk,
                "httponly"
        ), HttpStatus.OK);
    }

    private KakaoUserInfoDto getKakaoUserInfoDto(String code) throws JsonProcessingException {
        ResponseEntity<String> accessTokenResponse = kakaoOAuth.requestAccessToken(code);
        KakaoOAuthTokenDto oAuthToken = kakaoOAuth.getAccessToken(accessTokenResponse);
        ResponseEntity<String> userInfoResponse = kakaoOAuth.requestUserInfo(oAuthToken);
        KakaoUserInfoDto kakaoUser = kakaoOAuth.getUserInfo(userInfoResponse);
        return kakaoUser;
    }

    // Service


    // 카카오 로그인 서비스
    @Transactional
    public ResponseEntity<UserDto.loginResponse> kakaoLogin(String code, HttpServletResponse response) throws IOException {
        KakaoUserInfoDto kakaoUser = getKakaoUserInfoDto(code);
        String email = kakaoUser.getKakao_account().getEmail();
        String name = kakaoUser.getProperties().getNickname();
        String profileImagePath = kakaoUser.getProperties().getProfile_image();

        // 회원가입
        if (!userRepository.existsByEmail(email)) {
            userRepository.save(
                    User.builder()
                            .name(name)
                            .email(email)
                            .pw(passwordEncoder.encode("kakao"))
                            .uimg(profileImagePath)
                            .authorities(Collections.singleton(authority))
                            .build()
            );
            Login(email, response);
        }
        return Login(email, response);
    }
}
