package project.BBolCha.domain.user.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.BBolCha.domain.user.Dto.UserDto;
import project.BBolCha.domain.user.Service.UserService;
import project.BBolCha.global.Model.Status;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 로그인
    @PostMapping("login")
    public ResponseEntity<UserDto.loginResponse> login(
            @RequestBody UserDto.login request
    ) {
        return userService.login(request);
    }

    // 회원가입
    @PostMapping
    public ResponseEntity<UserDto.registerResponse> register(
            @RequestBody UserDto.register request
    ) {
        return userService.register(request);
    }

    // 로그인 만료시 atk 재발급
    @GetMapping
    public ResponseEntity<UserDto.loginResponse> reissue(
            @RequestHeader(value = "REFRESH_TOKEN") String rtk
    ) {
        return userService.reissue(rtk);
    }

    // 로그아웃
    @DeleteMapping("logout")
    public ResponseEntity<Status> logout(
            @RequestHeader(value = "Authorization") String auth
    ) {
        return userService.logout(auth);
    }

    // 정보 조회
    @GetMapping("info")
    public ResponseEntity<UserDto.infoResponse> read(
    ) {
        return userService.read();
    }
}
