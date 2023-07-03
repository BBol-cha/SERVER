package project.BBolCha.domain.user.dto.service.responce;

import lombok.*;
import project.BBolCha.domain.user.entity.User;

public class UserResponse {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class Login {
        private Long id;
        private String email;
        private String name;
        private String profileImageUrl;
        private String accessToken;

        public static UserResponse.Login response(User user, String accessToken) {
            return UserResponse.Login.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .profileImageUrl(user.getProfileImageUrl())
                    .accessToken(accessToken)
                    .build();
        }
    }

    @NoArgsConstructor
    @Getter
    public static class Detail {

        private Long id;
        private String email;
        private String name;
        private String profileImageUrl;

        @Builder
        private Detail(Long id, String email, String name, String profileImageUrl) {
            this.id = id;
            this.email = email;
            this.name = name;
            this.profileImageUrl = profileImageUrl;
        }

        public static UserResponse.Detail response(User user) {
            return UserResponse.Detail.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .profileImageUrl(user.getProfileImageUrl())
                    .build();
        }
    }

    @NoArgsConstructor
    @Getter
    public static class Reissue {
        private String accessToken;

        @Builder
        private Reissue(String accessToken) {
            this.accessToken = accessToken;
        }

        public static Reissue response(String accessToken) {
            return Reissue.builder()
                    .accessToken(accessToken)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class CheckProfile {
        private String name;
        private String profileImageUrl;

        @Builder
        private CheckProfile(String profileImageUrl, String name) {
            this.name = name;
            this.profileImageUrl = profileImageUrl;
        }

        public static UserResponse.CheckProfile response(User user) {
            return CheckProfile.builder()
                    .profileImageUrl(user.getProfileImageUrl())
                    .name(user.getName())
                    .build();
        }
    }
}
