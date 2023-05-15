package project.BBolCha.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.BBolCha.domain.board.entity.Board;

import java.time.LocalDateTime;

public class BoardDto {

    @Getter
    @NoArgsConstructor
    public static class UpdateDto {
        private Long id;
        private String authorName;
        private String title;
        private String content;
        private String correct;
        private String contentImageUrl;
        private Integer likeCount;
        private Integer viewCount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private TagDto.DetailDto tag;
        private HintDto.DetailDto hint;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LikeDto {
        private Boolean isLiked;

        public static BoardDto.LikeDto response(Boolean isLiked) {
            return LikeDto.builder()
                    .isLiked(isLiked)
                    .build();
        }
    }
}
