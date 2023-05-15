package project.BBolCha.domain.user.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import project.BBolCha.domain.user.dto.controller.request.UserRequest;
import project.BBolCha.domain.user.dto.service.request.UserServiceRequest;
import project.BBolCha.domain.user.dto.service.responce.UserResponse;
import project.BBolCha.domain.user.entity.Authority;
import project.BBolCha.domain.user.entity.User;
import project.BBolCha.domain.user.repository.UserRepository;
import project.BBolCha.global.config.RedisDao;
import project.BBolCha.global.config.jwt.TokenProvider;
import project.BBolCha.global.exception.CustomException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static project.BBolCha.global.model.Result.PASSWORD_NOT_MATCHED;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisDao redisDao;


    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @DisplayName("사용자가 회원가입을 해서 토큰을 발급받는다.")
    @Test
    void registerNewUser() {
        // given
        HttpServletResponse response = new MockHttpServletResponse();
        UserRequest.Registration request = new UserRequest.Registration("덕배", "kevin@gmail.com", "abc123!");

        // when
        UserResponse.Login userResponse = userService.registerNewUser(request.toServiceRequest(), response);

        // then
        assertThat(userResponse)
                .extracting("email", "name")
                .contains("kevin@gmail.com", "덕배");

        assertThat(userResponse)
                .extracting("accessToken")
                .isNotNull();

        List<User> users = userRepository.findAll();

        assertThat(users).hasSize(1)
                .extracting("email", "name")
                .contains(
                        tuple("kevin@gmail.com", "덕배")
                );

        String refreshTokenValue = redisDao.getValues(userResponse.getEmail());
        MockHttpServletResponse mockResponse = (MockHttpServletResponse) response;
        Cookie targetCookie = mockResponse.getCookie("refreshToken");

        assertThat(targetCookie.getValue()).isEqualTo(refreshTokenValue);
        assertThat(targetCookie.isHttpOnly()).isTrue();
    }

    @DisplayName("유저가 로그인을 해서 토큰을 발급받는다.")
    @Test
    void loginUser() {
        // given
        saveAndRetrieveUser();

        HttpServletResponse response = new MockHttpServletResponse();
        UserRequest.Login request = new UserRequest.Login("test@test.com", "abc123!");

        // when
        UserResponse.Login loginResponse = userService.login(request.toServiceRequest(), response);

        // then
        assertThat(loginResponse)
                .extracting("email", "name", "profileImageUrl")
                .contains("test@test.com", "테스트 계정", "test.png");
        assertThat(loginResponse)
                .extracting("accessToken")
                .isNotNull();

        String refreshTokenValue = redisDao.getValues(loginResponse.getEmail());
        MockHttpServletResponse mockResponse = (MockHttpServletResponse) response;
        Cookie targetCookie = mockResponse.getCookie("refreshToken");

        assertThat(targetCookie.getValue()).isEqualTo(refreshTokenValue);
        assertThat(targetCookie.isHttpOnly()).isTrue();
    }

    @DisplayName("유저가 로그인을 했는데 비밀번호가 틀렸을 경우 예외가 발생한다.")
    @Test
    void incorrectPasswordLoginAttempt() {
        // given
        saveAndRetrieveUser();

        HttpServletResponse response = new MockHttpServletResponse();
        UserRequest.Login request = new UserRequest.Login("test@test.com", "abc12");

        // when // then
        assertThatThrownBy(() -> userService.login(request.toServiceRequest(), response))
                .isInstanceOf(CustomException.class)
                .extracting("result").isEqualTo(PASSWORD_NOT_MATCHED);
    }

    @DisplayName("유저가 자신의 정보를 조회한다.")
    @Test
    void userDetailsRead() {
        // given
        User user = saveAndRetrieveUser();
        saveSecurityContextHolderAndGetAuthentication();

        // when
        UserResponse.Detail response = userService.read(user);

        // then
        assertThat(response)
                .extracting("email", "name", "profileImageUrl")
                .contains("test@test.com", "테스트 계정", "test.png");
    }

    @DisplayName("사용자가 로그인 시간이 만료되어서 토큰을 재발급 받늗다.")
    @Test
    void loginReissue() {
        // given
        User user = saveAndRetrieveUser();

        String refreshToken = tokenProvider.createRefreshToken(user.getEmail());
        redisDao.setValues(user.getEmail(), refreshToken, Duration.ofDays(14));

        // when
        UserResponse.Reissue response = userService.reissue(refreshToken);

        // then
        assertThat(response.getAccessToken())
                .isNotNull();
    }

    @DisplayName("유저가 로그아웃을 하면 액세스 토큰이 블랙리스트로 redis 에 저장된다.")
    @Test
    void logoutUser() {
        // given
        User user = saveAndRetrieveUser();
        Authentication authentication = saveSecurityContextHolderAndGetAuthentication();

        String testAccessToken = tokenProvider.createAccessToken(user, authentication);
        redisDao.setValues(user.getEmail(), "testRefreshToken");

        // when
        userService.logout(testAccessToken);

        // then
        assertThat(redisDao.getValues(user.getEmail())).isNull();
        assertThat(redisDao.getValues(testAccessToken)).isEqualTo("logout");
    }

    private Set<Authority> getAuthorities() {
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();
        return Collections.singleton(authority);
    }


    @DisplayName("유저가 자신의 이름과 프로필 이미지를 변경한다")
    @Test
    void updateNameAndProfileImageUrl() {
        // given
        User user = saveAndRetrieveUser();

        UserServiceRequest.Update request = UserServiceRequest.Update.builder()
                .name("테스트 업데이트 계정")
                .profileImageUrl("update_test_image.png")
                .build();


        // when
        UserResponse.Detail response = userService.update(request,user);

        // then
        assertThat(response)
                .extracting("name","profileImageUrl","email")
                .contains("테스트 업데이트 계정","update_test_image.png","test@test.com");
    }

    private User saveAndRetrieveUser() {
        User user = User.builder()
                .name("테스트 계정")
                .email("test@test.com")
                .password(passwordEncoder.encode("abc123!"))
                .profileImageUrl("test.png")
                .authorities(getAuthorities())
                .build();

        return userRepository.save(user);
    }

    private Authentication saveSecurityContextHolderAndGetAuthentication() {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken("test@test.com", "abc123!");
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }
}