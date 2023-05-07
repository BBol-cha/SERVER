package project.BBolCha.global.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.BBolCha.domain.user.Dto.UserDto;

@Getter
@Setter
@NoArgsConstructor
public class CustomResponseEntity<T> {

    private int code;
    private String message;
    private T data;

    public static <T> CustomResponseEntity<T> success(T data) {
        return CustomResponseEntity.<T>builder()
                .code(Result.OK.getCode())
                .message(Result.OK.getMessage())
                .data(data)
                .build();
    }

    @Builder
    public CustomResponseEntity(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
