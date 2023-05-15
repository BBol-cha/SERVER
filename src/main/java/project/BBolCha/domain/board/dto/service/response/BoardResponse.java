package project.BBolCha.domain.board.dto.service.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.BBolCha.domain.board.entity.Board;

import java.time.LocalDateTime;

public class BoardResponse {

    @Getter
    @NoArgsConstructor
    public static class Save {
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
        private TagResponse tag;
        private HintResponse hint;

        @Builder
        private Save(
                Long id, String authorName, String title,
                String content, String correct, String contentImageUrl,
                Integer likeCount, Integer viewCount, LocalDateTime createdAt,
                LocalDateTime updatedAt, TagResponse tag, HintResponse hint
        ) {
            this.id = id;
            this.authorName = authorName;
            this.title = title;
            this.content = content;
            this.correct = correct;
            this.contentImageUrl = contentImageUrl;
            this.likeCount = likeCount;
            this.viewCount = viewCount;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.tag = tag;
            this.hint = hint;
        }

        public static Save of(Board board) {
            return BoardResponse.Save.builder()
                    .id(board.getId())
                    .authorName(board.getUser().getName())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .correct(board.getCorrect())
                    .contentImageUrl(board.getContentImageUrl())
                    .likeCount(0)
                    .viewCount(board.getViewCount())
                    .createdAt(board.getCreatedAt())
                    .updatedAt(board.getUpdatedAt())
                    .tag(TagResponse.response(board.getTag()))
                    .hint(HintResponse.response(board.getHint()))
                    .build();
        }
    }

    @NoArgsConstructor
    @Getter
    public static class Detail {
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
        private TagResponse tag;
        private HintResponse hint;

        @Builder
        public Detail(
                Long id, String authorName, String title,
                String content, String correct, String contentImageUrl,
                Integer likeCount, Integer viewCount, LocalDateTime createdAt,
                LocalDateTime updatedAt, TagResponse tag, HintResponse hint
        ) {
            this.id = id;
            this.authorName = authorName;
            this.title = title;
            this.content = content;
            this.correct = correct;
            this.contentImageUrl = contentImageUrl;
            this.likeCount = likeCount;
            this.viewCount = viewCount;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.tag = tag;
            this.hint = hint;
        }

        public static BoardResponse.Detail response(Board board) {
            return BoardResponse.Detail.builder()
                    .id(board.getId())
                    .authorName(board.getUser().getName())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .correct(board.getCorrect())
                    .contentImageUrl(board.getContentImageUrl())
                    .likeCount(board.getLike().size())
                    .viewCount(board.getViewCount())
                    .createdAt(board.getCreatedAt())
                    .updatedAt(board.getUpdatedAt())
                    .tag(TagResponse.response(board.getTag()))
                    .hint(HintResponse.response(board.getHint()))
                    .build();
        }
    }
}
