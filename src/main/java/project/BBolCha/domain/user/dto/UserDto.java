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
