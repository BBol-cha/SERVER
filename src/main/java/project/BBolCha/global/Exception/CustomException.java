package project.BBolCha.global.Exception;

import lombok.Getter;
import lombok.Setter;
import project.BBolCha.global.Model.Result;

@Getter
@Setter
public class CustomException extends RuntimeException {

    private Result result;
    private String debug;

    public CustomException(Result result) {
        this.result = result;
        this.debug = result.getMessage();
    }
}
