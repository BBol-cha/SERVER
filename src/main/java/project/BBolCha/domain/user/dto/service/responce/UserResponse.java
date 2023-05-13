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
}
