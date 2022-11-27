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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.BBolCha.domain.user.Dto.TokenInfoResponseDto;
import project.BBolCha.domain.user.Dto.UserDto;
import project.BBolCha.domain.user.Entity.Authority;
import project.BBolCha.domain.user.Entity.User;
import project.BBolCha.domain.user.Repository.UserRepository;
import project.BBolCha.global.Exception.CustomException;
import project.BBolCha.global.Exception.ServerException;
import project.BBolCha.global.Model.Status;
import project.BBolCha.global.config.Jwt.SecurityUtil;
import project.BBolCha.global.config.Jwt.TokenProvider;
import project.BBolCha.global.config.RedisDao;

import java.time.Duration;
import java.util.Collections;
import java.util.Objects;

import static project.BBolCha.global.Exception.CustomErrorCode.LOGIN_FALSE;
import static project.BBolCha.global.Exception.CustomErrorCode.REFRESH_TOKEN_IS_BAD_REQUEST;
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


    Authority authority = Authority.builder()
            .authorityName("ROLE_USER")
            .build();

    // Validate 및 단순화 메소드

    private TokenInfoResponseDto getTokenInfo() {
        return TokenInfoResponseDto.Response(
                Objects.requireNonNull(SecurityUtil.getCurrentUsername()
                        .flatMap(
                                userRepository::findOneWithAuthoritiesByEmail)
                        .orElse(null))
        );
    }

    private void LOGIN_VALIDATE(UserDto.login request) {
        userRepository.findByEmail(request.getEmail())
                .orElseThrow(
                        () -> new CustomException(LOGIN_FALSE)
                );

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
    public ResponseEntity<UserDto.registerResponse> register(UserDto.register request) {
        userRepository.save(
                User.builder()
                        .name(request.getName())
                        .email(request.getEmail())
                        .pw(passwordEncoder.encode(request.getPw()))
                        .authorities(Collections.singleton(authority))
                        .build()
        );

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPw());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String atk = tokenProvider.createToken(authentication);
        String rtk = tokenProvider.createRefreshToken(request.getEmail());

        redisDao.setValues(request.getEmail(), rtk, Duration.ofDays(14));

        return new ResponseEntity<>(UserDto.registerResponse.response(
                request.getEmail(),
                request.getName(),
                atk,
                rtk
        ), HttpStatus.CREATED);
    }

    //로그인
    @Transactional
    public ResponseEntity<UserDto.loginResponse> login(UserDto.login request) {
        LOGIN_VALIDATE(request);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPw());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String atk = tokenProvider.createToken(authentication);
        String rtk = tokenProvider.createRefreshToken(request.getEmail());

        redisDao.setValues(request.getEmail(), rtk, Duration.ofDays(14));

        return new ResponseEntity<>(UserDto.loginResponse.response(
                atk,
                rtk
        ), HttpStatus.OK);
    }

    // accessToken 재발급
    @Transactional
    public ResponseEntity<UserDto.loginResponse> reissue(String rtk) {
        String username = tokenProvider.getRefreshTokenInfo(rtk);
        String rtkInRedis = redisDao.getValues(username);

        if (Objects.isNull(rtkInRedis) || !rtkInRedis.equals(rtk))
            throw new ServerException(REFRESH_TOKEN_IS_BAD_REQUEST); // 410

        return new ResponseEntity<>(UserDto.loginResponse.response(
                tokenProvider.reCreateToken(username),
                null
        ), HttpStatus.CREATED);
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
    public ResponseEntity<Status> logout(String auth) {
        String atk = auth.substring(7);
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

    public ResponseEntity<String> version() {
        String version = "version 1.0.0";
        return new ResponseEntity<>(version,HttpStatus.OK);
    }
}
