package project.BBolCha.domain.board.dto.service.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.BBolCha.domain.board.entity.Comment;

import java.time.LocalDateTime;

public class CommentResponse {

    @NoArgsConstructor
    @Getter
    public static class Detail {
        private String userName;
        private String userProfileImageUrl;
        private String note;
        private LocalDateTime createdAt;

        @Builder
        private Detail(String userName, String userProfileImageUrl, String note, LocalDateTime createdAt) {
            this.userName = userName;
            this.userProfileImageUrl = userProfileImageUrl;
            this.note = note;
            this.createdAt = createdAt;
        }

        public static CommentResponse.Detail response(Comment comment) {
            return CommentResponse.Detail.builder()
                    .userName(comment.getUser().getName())
                    .userProfileImageUrl(comment.getUser().getProfileImageUrl())
                    .note(comment.getNote())
                    .build();
        }
    }
}
