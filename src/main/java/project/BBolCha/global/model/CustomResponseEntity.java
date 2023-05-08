package project.BBolCha.global.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

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

    // 전달할 객체가 없을때 NPE 가 발생하지 않도록 빈 객체를 data 로 전송
    public static <T> CustomResponseEntity<T> success(Void type) {
        T data = (T) new HashMap<>();
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
