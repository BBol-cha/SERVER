package project.BBolCha.domain.user.dto.service.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserServiceRequest {

    @NoArgsConstructor
    @Getter
    public static class Login {
        private String email;
        private String password;

        @Builder
        private Login(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }

    @NoArgsConstructor
    @Getter
    public static class Registration {
        private String name;
        private String email;
        private String password;

        @Builder
        private Registration(String name, String email, String password) {
            this.name = name;
            this.email = email;
            this.password = password;
        }
    }

    @NoArgsConstructor
    @Getter
    public static class Update {
        private String name;
        private String profileImageUrl;

        @Builder
        private Update(String name, String profileImageUrl) {
            this.name = name;
            this.profileImageUrl = profileImageUrl;
        }
    }
}
