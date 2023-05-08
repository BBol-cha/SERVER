package project.BBolCha.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserRequest {

    @NoArgsConstructor
    @Getter
    public static class Login {
        private String email;
        private String password;
    }
}
