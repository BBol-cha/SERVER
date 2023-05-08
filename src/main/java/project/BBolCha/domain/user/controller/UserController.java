package project.BBolCha.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import project.BBolCha.domain.user.dto.UserDto;
import project.BBolCha.domain.user.dto.request.UserRequest;
import project.BBolCha.domain.user.dto.responce.UserResponse;
import project.BBolCha.domain.user.service.UserService;
import project.BBolCha.global.model.CustomResponseEntity;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // version 확인
    @GetMapping("version")
    public ResponseEntity<String> version() {
        return new ResponseEntity<>("version 2.0.0", HttpStatus.OK);
    }

    // 로그인
    @PostMapping("auth/login")
    public CustomResponseEntity<UserResponse.Login> login(
            @RequestBody
            UserRequest.Login request,
            HttpServletResponse response
    ) {
        return CustomResponseEntity.success(userService.login(request, response));
    }

    // 회원가입
    @PostMapping("auth")
    public CustomResponseEntity<UserDto.LoginDto> register(
            @RequestBody UserDto.RegistrationDto request, HttpServletResponse response
    ) {
        return CustomResponseEntity.success(userService.register(request, response));
    }

    // 로그인 만료시 atk 재발급
    @GetMapping("auth")
    public CustomResponseEntity<UserDto.AccessTokenRefreshDto> reissue(
            @CookieValue(value = "refreshToken", required = false) Cookie cookie, HttpServletResponse response
    ) {
        return CustomResponseEntity.success(userService.reissue(cookie.getValue()));
    }

    // 로그아웃
    @DeleteMapping("auth/logout")
    public CustomResponseEntity<Void> logout(
            @RequestHeader(value = "Authorization") String bearerToken,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return CustomResponseEntity.success(userService.logout(bearerToken, userDetails));
    }

    // 정보 조회
    @GetMapping("auth/info")
    public CustomResponseEntity<UserDto.DetailDto> read(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return CustomResponseEntity.success(userService.read(userDetails));
    }
}
