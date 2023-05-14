package project.BBolCha.domain.user.dto.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.BBolCha.domain.user.dto.service.request.UserServiceRequest;

import javax.validation.constraints.NotBlank;

public class UserRequest {

    @NoArgsConstructor
    @Getter
    public static class Login {
        @NotBlank(message = "이메일은 필수입니다.")
        private String email;
        @NotBlank(message = "비밀번호는 필수입니다.")
        private String password;

        public UserServiceRequest.Login toServiceRequest() {
            return UserServiceRequest.Login.builder()
                    .email(email)
                    .password(password)
                    .build();
        }

        // 테스트 생성자
        public Login(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }

    @NoArgsConstructor
    @Getter
    public static class Registration {
        @NotBlank(message = "유저 이름은 필수입니다.")
        private String name;

        @NotBlank(message = "이메일은 필수입니다.")
        private String email;

        @NotBlank(message = "비밀번호는 필수입니다.")
        private String password;

        public UserServiceRequest.Registration toServiceRequest() {
            return UserServiceRequest.Registration.builder()
                    .email(email)
                    .password(password)
                    .name(name)
                    .build();
        }

        // 테스트 생성자
        public Registration(String name, String email, String password) {
            this.name = name;
            this.email = email;
            this.password = password;
        }
    }
}
