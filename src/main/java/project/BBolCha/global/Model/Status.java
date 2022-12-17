package project.BBolCha.global.Model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Status {
    LOGOUT_TRUE("로그아웃 성공"),
    IMAGE_DELETE_TRUE("이미지 삭제 성공"),
    BOARD_UPDATE_TRUE("게시판 수정 성공"),
    BOARD_DELETE_TRUE("게시판 삭제 성공");
    private final String statusMessage;
}
