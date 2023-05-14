package project.BBolCha.domain.board.dto.service.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.BBolCha.domain.board.dto.BoardDto;
import project.BBolCha.domain.board.dto.HintDto;
import project.BBolCha.domain.board.dto.TagDto;
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
        private TagDto.DetailDto tag;
        private HintDto.DetailDto hint;

        @Builder
        private Save(
                Long id, String authorName, String title,
                String content, String correct, String contentImageUrl,
                Integer likeCount, Integer viewCount, LocalDateTime createdAt,
                LocalDateTime updatedAt, TagDto.DetailDto tag, HintDto.DetailDto hint
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
                    .tag(TagDto.DetailDto.response(board.getTag()))
                    .hint(HintDto.DetailDto.response(board.getHint()))
                    .build();
        }
    }
}
