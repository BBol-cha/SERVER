package project.BBolCha.global.model;


import lombok.Getter;

@Getter
public enum Result {
    OK(0,"성공"),
    FAIL(-1,"실패"),

    INVALID_ACCESS_TOKEN_CONSTANT(-1001,"토큰이 NULL 또는 잘못된 AccessToken 입니다."),
    INVALID_REFRESH_TOKEN_CONSTANT(-1002,"토큰이 NULL 또는 잘못된 RefreshToken 입니다."),

    PASSWORD_NOT_MATCHED(-2001, "아이디 또는 비밀번호가 잘못되었습니다."),
    NOT_FOUND_USER(-2002, "존재하지 않는 사용자입니다."),

    NOT_FOUND_BOARD(-3001,"존재하지 않는 게시물입니다."),
    NOT_MY_POST(-3002,"본인이 작성한 게시물이 아닙니다."),
    NOT_MY_COMMENT(-3003,"본인이 작성한 댓글이 아닙니다."),
    NOT_FOUND_COMMENT(-3004,"존재하지 않는 댓글입니다.");

    private final int code;
    private final String message;

    Result(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
