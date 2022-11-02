package project.BBolCha.domain.user.Dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class UserDto implements Serializable {

    @Data
    @Builder
    public static class register {
        private final Long id;
        private final String name;
        private final String email;
        private final String pw;
    }

    @Data
    @Builder
    public static class registerResponse {
        private final String name;
        private final String email;
        private final String atk;
        private final String rtk;

        public static registerResponse response(String name, String email, String atk, String rtk) {
            return registerResponse.builder()
                    .email(email)
                    .name(name)
                    .atk(atk)
                    .rtk(rtk)
                    .build();
        }
    }

    @Data
    @Builder
    public static class login {
        private final String pw;
        private final String email;
    }

    @Data
    @Builder
    public static class reissue {
        private final String pw;
        private final String email;
        private final String rtk;
    }

    @Data
    @Builder
    public static class loginResponse {
        private final String atk;
        private final String rtk;

        public static loginResponse response(String atk, String rtk) {
            return loginResponse.builder()
                    .atk(atk)
                    .rtk(rtk)
                    .build();
        }
    }

    @Data
    @Builder
    public static class infoResponse {

        private final String name;
        private final String email;

        public static UserDto.infoResponse response(@NotNull String name, @NotNull String email) {
            return UserDto.infoResponse.builder()
                    .email(email)
                    .name(name)
                    .build();
        }
    }
}
