package project.BBolCha.domain.board.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.BBolCha.domain.board.Entity.Board;
import project.BBolCha.domain.user.Entity.User;

import java.time.LocalDateTime;

public class BoardDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SaveDto {
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

        public static BoardDto.SaveDto response(
                Board board, User user,
                TagDto.DetailDto tag, HintDto.DetailDto hint
        ) {
            return SaveDto.builder()
                    .id(board.getId())
                    .authorName(user.getName())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .correct(board.getCorrect())
                    .contentImageUrl(board.getContentImageUrl())
                    .likeCount(0)
                    .viewCount(board.getViewCount())
                    .createdAt(board.getCreatedAt())
                    .updatedAt(board.getUpdatedAt())
                    .tag(tag)
                    .hint(hint)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DetailDto {
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

        public static BoardDto.DetailDto response(
                Board board, TagDto.DetailDto tag, HintDto.DetailDto hint
        ) {
            return DetailDto.builder()
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
                    .tag(tag)
                    .hint(hint)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class boardImage {
        private String imgName;
        private String bimg;

        public static BoardDto.boardImage response(String imgName, String url) {
            return boardImage.builder()
                    .imgName(imgName)
                    .bimg(url)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Like {
        private String status;
        private String statusMessage;

        public static BoardDto.Like response(String status, String statusMessage) {
            return Like.builder()
                    .status(status)
                    .statusMessage(statusMessage)
                    .build();
        }
    }
}
