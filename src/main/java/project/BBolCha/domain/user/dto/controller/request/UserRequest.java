package project.BBolCha.domain.user.dto.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import project.BBolCha.domain.user.dto.service.request.UserServiceRequest;

public class UserRequest {

    @NoArgsConstructor
    @Getter
    public static class Login {
        private String email;
        private String password;

        public UserServiceRequest.Login toServiceRequest() {
            return UserServiceRequest.Login.builder()
                    .email(email)
                    .password(password)
                    .build();
        }
    }
}
