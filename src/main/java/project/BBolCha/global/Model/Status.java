package project.BBolCha.global.Model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Status {
    LOGOUT_TRUE("로그아웃 성공");
    private final String statusMessage;
}
