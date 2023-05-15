package project.BBolCha.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.BBolCha.domain.board.entity.Comment;

import java.time.LocalDateTime;

public class CommentDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class DetailDto {
        private String userName;
        private String userProfileImageUrl;
        private String note;
        private LocalDateTime createdAt;

        public static DetailDto response(Comment comment) {
            return DetailDto.builder()
                    .userName(comment.getUser().getName())
                    .userProfileImageUrl(comment.getUser().getProfileImageUrl())
                    .note(comment.getNote())
                    .build();
        }
    }

}
