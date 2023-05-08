package project.BBolCha.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import project.BBolCha.domain.user.entity.User;

import java.io.Serializable;

public class UserDto implements Serializable {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class RegistrationDto {
        private String name;
        private String email;
        private String password;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class AccessTokenRefreshDto {
        private String accessToken;
        public static AccessTokenRefreshDto response(String accessToken) {
            return AccessTokenRefreshDto.builder()
                    .accessToken(accessToken)
                    .build();
        }
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class LoginDto {

        private Long id;
        private String email;
        private String name;
        private String profileImageUrl;
        private String password;
        private String accessToken;

        public static LoginDto response(User user, String accessToken) {
            return LoginDto.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .profileImageUrl(user.getProfileImageUrl())
                    .accessToken(accessToken)
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class DetailDto {

        private Long id;
        private String email;
        private String name;
        private String profileImageUrl;

        public static DetailDto response(User user) {
            return DetailDto.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .profileImageUrl(user.getProfileImageUrl())
                    .build();
        }
    }
}
