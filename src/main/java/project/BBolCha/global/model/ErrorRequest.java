package project.BBolCha.global.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class ErrorRequest {
    @NotBlank
    private String name;

    public ErrorRequest(String name) {
        this.name = name;
    }
}
