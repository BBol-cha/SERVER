package project.BBolCha.domain.user.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
import project.BBolCha.global.Model.Result;
import project.BBolCha.global.config.Jwt.TokenProvider;
import project.BBolCha.global.config.RedisDao;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import static project.BBolCha.global.Exception.CustomErrorCode.REFRESH_TOKEN_IS_BAD_REQUEST;


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

    private static void setHttpOnlyCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(Result.NOT_FOUND_USER)
        );
    }

    private Set<Authority> getAuthorities() {
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();
        return Collections.singleton(authority);
    }

    private Authentication getAuthentication(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }

    private void LOGIN_VALIDATE(UserDto.LoginDto request, User user) {
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(Result.PASSWORD_NOT_MATCHED);
        }
    }

    // Service
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

    @Transactional
    public UserDto.LoginDto login(UserDto.LoginDto request, HttpServletResponse response) {
        User user = getUser(request.getEmail());
        LOGIN_VALIDATE(request, user);

        Authentication authentication = getAuthentication(request.getEmail(), request.getPassword());
        String refreshToken = tokenProvider.createRefreshToken(request.getEmail());

        setHttpOnlyCookie(response, refreshToken);

        return UserDto.LoginDto.response(
                user,
                tokenProvider.createAccessToken(authentication)
        );
    }

    @Transactional
    public UserDto.AccessTokenRefreshDto reissue(String refreshToken) {
        String email = tokenProvider.getRefreshTokenInfo(refreshToken);
        String rtkInRedis = redisDao.getValues(email);

        if (Objects.isNull(rtkInRedis) || !rtkInRedis.equals(refreshToken))
            throw new CustomException(Result.INVALID_REFRESH_TOKEN_CONSTANT);

        return UserDto.AccessTokenRefreshDto.response(
                tokenProvider.reCreateToken(email)
        );
    }

    @Transactional
    public UserDto.DetailDto read(UserDetails userDetails) {
        User user = getUser(userDetails.getUsername());
        return UserDto.DetailDto.response(user);
    }

    @Transactional
    public Void logout(String bearerToken, UserDetails userDetails) {
        String accessToken = bearerToken.substring(7);
        Long accessTokenExpiration = tokenProvider.getExpiration(accessToken);
        String email = userDetails.getUsername();

        if (redisDao.getValues(email) == null || redisDao.getValues(email).isEmpty()) {
            throw new CustomException(Result.FAIL);
        }

        redisDao.deleteValues(email);
        redisDao.setValues(accessToken, "logout", Duration.ofMillis(accessTokenExpiration));

        return null;
    }

}
