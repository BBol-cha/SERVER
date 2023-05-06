package project.BBolCha.domain.user.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.BBolCha.domain.user.Dto.UserDto;
import project.BBolCha.domain.user.Entity.Authority;
import project.BBolCha.domain.user.Entity.User;
import project.BBolCha.domain.user.Repository.UserRepository;
import project.BBolCha.global.Exception.CustomException;
import project.BBolCha.global.Exception.ServerException;
import project.BBolCha.global.Model.Result;
import project.BBolCha.global.Model.Status;
import project.BBolCha.global.config.Jwt.SecurityUtil;
import project.BBolCha.global.config.Jwt.TokenProvider;
import project.BBolCha.global.config.RedisDao;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import static project.BBolCha.global.Exception.CustomErrorCode.*;
import static project.BBolCha.global.Model.Status.LOGOUT_TRUE;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisDao redisDao;
    @Value("${jwt.token-validity-in-seconds}")
    long tokenValidityInSeconds;

    // Validate 및 단순화 메소드
    private TokenInfoResponseDto getTokenInfo() {
        return TokenInfoResponseDto.Response(
                Objects.requireNonNull(SecurityUtil.getCurrentUsername()
                        .flatMap(
                                userRepository::findOneWithAuthoritiesByEmail)
                        .orElse(null))
        );
    }

    private void LOGIN_VALIDATE(UserDto.LoginDto request) {
        userRepository.findByEmail(request.getEmail())
                .orElseThrow(
                        () -> new CustomException(LOGIN_FALSE)
                );

        if (request.getPw().equals("kakao"))
            throw new CustomException(NOT_SOCIAL_LOGIN);

        if (!passwordEncoder.matches(
                request.getPw(),
                userRepository.findByEmail(request.getEmail())
                        .orElseThrow(
                                () -> new CustomException(LOGIN_FALSE)
                        ).getPw())
        ) {
            throw new CustomException(LOGIN_FALSE);
        }
    }

    // Service
    // 회원가입
    @Transactional
    public UserDto.LoginDto register(UserDto.RegistrationDto request, HttpServletResponse response) {

        User user = userRepository.save(
                User.builder()
                        .name(request.getName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .profileImageUrl("test.png")
                        .authorities(getAuthorities())
                        .build()
        );

        Authentication authentication = getAuthentication(request.getEmail(), request.getPassword());
        String rtk = tokenProvider.createRefreshToken(request.getEmail());
        setHttpOnlyCookie(response, rtk);

        return UserDto.LoginDto.response(
                user,
                tokenProvider.createAccessToken(authentication)
        );
    }


    //로그인
    @Transactional
    public UserDto.LoginDto login(UserDto.LoginDto request, HttpServletResponse response) {
        LOGIN_VALIDATE(request);

        User user = getUser(request.getEmail());
        Authentication authentication = getAuthentication(request.getEmail(), request.getPassword());
        String refreshToken = tokenProvider.createRefreshToken(request.getEmail());

        setHttpOnlyCookie(response, refreshToken);

        return UserDto.LoginDto.response(
                user,
                tokenProvider.createAccessToken(authentication)
        );
    }

    private static void setHttpOnlyCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
    private Authentication getAuthentication(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }


    // accessToken 재발급
    @Transactional
    public UserDto.AccessTokenRefreshDto reissue(String refreshToken) {
        String email = tokenProvider.getRefreshTokenInfo(refreshToken);
        String rtkInRedis = redisDao.getValues(email);

        if (Objects.isNull(rtkInRedis) || !rtkInRedis.equals(refreshToken))
            throw new ServerException(REFRESH_TOKEN_IS_BAD_REQUEST); // 410

        return UserDto.AccessTokenRefreshDto.response(
                tokenProvider.reCreateToken(email)
        );
    }

    // 정보 조회
    public ResponseEntity<UserDto.infoResponse> read() {
        TokenInfoResponseDto userInfo = getTokenInfo();
        return new ResponseEntity<>(UserDto.infoResponse.builder()
                .email(userInfo.getEmail())
                .name(userInfo.getName())
                .build(), HttpStatus.OK);
    }

    // 로그아웃
    public ResponseEntity<Status> logout(String bearerToken, UserDetails userDetails) {
        String accessToken = bearerToken.substring(7);
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        if (redisDao.getValues(email) != null) {
            redisDao.deleteValues(email);
        }

        redisDao.setValues(atk, "logout", Duration.ofMillis(
                tokenProvider.getExpiration(atk)
        ));
        return new ResponseEntity<>(LOGOUT_TRUE, HttpStatus.OK);
    }

    private Set<Authority> getAuthorities() {
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();
        return Collections.singleton(authority);
    }
    private User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(Result.NOT_FOUND_USER)
        );
    }
}
