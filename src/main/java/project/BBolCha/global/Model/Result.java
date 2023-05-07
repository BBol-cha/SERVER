package project.BBolCha.global.Model;


import lombok.Getter;

@Getter
public enum Result {
    OK(0,"성공"),
    FAIL(-1,"실패"),
    INVALID_ACCESS_TOKEN_CONSTANT(-1,"토큰이 NULL 또는 잘못된 AccessToken 입니다."),
    INVALID_REFRESH_TOKEN_CONSTANT(-1,"토큰이 NULL 또는 잘못된 RefreshToken 입니다."),

    PASSWORD_NOT_MATCHED(-1, "존재하지 않는 사용자입니다."),
    NOT_FOUND_USER(-1, "존재하지 않는 사용자입니다."),

    NOT_FOUND_BOARD(-1,"존재하지 않는 게시물입니다.");

    private final int code;
    private final String message;

    Result(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
