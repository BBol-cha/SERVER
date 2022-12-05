package project.BBolCha.domain.user.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.BBolCha.domain.user.Dto.UserDto;
import project.BBolCha.domain.user.Service.UserService;
import project.BBolCha.global.Model.Status;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // version 확인
    @GetMapping("version")
    public ResponseEntity<String> version(){
        return new ResponseEntity<>("version 1.0.6", HttpStatus.OK);
    }

    // 로그인
    @PostMapping("auth/login")
    public ResponseEntity<UserDto.loginResponse> login(
            @RequestBody UserDto.login request, HttpServletResponse response
    ) {
        return userService.login(request,response);
    }

    // 회원가입
    @PostMapping("auth")
    public ResponseEntity<UserDto.registerResponse> register(
            @RequestBody UserDto.register request, HttpServletResponse response
    ) {
        return userService.register(request,response);
    }

    // 로그인 만료시 atk 재발급
    @PostMapping("auth/rtk")
    public ResponseEntity<UserDto.loginResponse> reissue(
            @CookieValue(value = "refreshToken",required = false) Cookie cookie,HttpServletResponse response
    ) {
        log.info("============================================");
        log.info(cookie.getValue());
        log.info("============================================");
        return userService.reissue(cookie.getValue());
    }

    // 로그아웃
    @DeleteMapping("auth/logout")
    public ResponseEntity<Status> logout(
            @RequestHeader(value = "Authorization") String auth
    ) {
        return userService.logout(auth);
    }

    // 정보 조회
    @GetMapping("auth/info")
    public ResponseEntity<UserDto.infoResponse> read(
    ) {
        return userService.read();
    }
}
