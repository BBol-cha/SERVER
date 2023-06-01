package project.BBolCha.domain.board.dto.service.response;

import lombok.*;
import project.BBolCha.domain.board.entity.Board;
import project.BBolCha.domain.board.entity.Like;

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
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    @Getter
    @ToString
    public static class Detail {
        private Long id;
        private String authorName;
        private String title;
        private String content;
        private String correct;
        private String contentImageUrl;
        private Long likeCount;
        private Integer viewCount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        private Boolean horror;
        private Boolean daily;
        private Boolean romance;
        private Boolean fantasy;
        private Boolean sf;

        private String hintOne;
        private String hintTwo;
        private String hintThree;
        private String hintFour;
        private String hintFive;

        public static BoardResponse.Detail response(Board board) {
            return BoardResponse.Detail.builder()
                    .id(board.getId())
                    .authorName(board.getUser().getName())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .correct(board.getCorrect())
                    .contentImageUrl(board.getContentImageUrl())
                    .likeCount((long) board.getLike().size())
                    .viewCount(board.getViewCount())
                    .createdAt(board.getCreatedAt())
                    .updatedAt(board.getUpdatedAt())
                    .horror(board.getTag().getHorror())
                    .daily(board.getTag().getDaily())
                    .romance(board.getTag().getRomance())
                    .fantasy(board.getTag().getFantasy())
                    .sf(board.getTag().getSf())
                    .hintOne(board.getHint().getHintOne())
                    .hintTwo(board.getHint().getHintTwo())
                    .hintThree(board.getHint().getHintThree())
                    .hintFour(board.getHint().getHintFour())
                    .hintFive(board.getHint().getHintFive())
                    .build();
        }
    }

    @NoArgsConstructor
    @Getter
    public static class DetailDsl {
        private Board board;
        private Long likeCount;

        public BoardResponse.Detail response() {
            return BoardResponse.Detail.builder()
                    .id(board.getId())
                    .authorName(board.getUser().getName())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .correct(board.getCorrect())
                    .contentImageUrl(board.getContentImageUrl())
                    .likeCount(likeCount)
                    .viewCount(board.getViewCount())
                    .createdAt(board.getCreatedAt())
                    .updatedAt(board.getUpdatedAt())
                    .horror(board.getTag().getHorror())
                    .daily(board.getTag().getDaily())
                    .romance(board.getTag().getRomance())
                    .fantasy(board.getTag().getFantasy())
                    .sf(board.getTag().getSf())
                    .hintOne(board.getHint().getHintOne())
                    .hintTwo(board.getHint().getHintTwo())
                    .hintThree(board.getHint().getHintThree())
                    .hintFour(board.getHint().getHintFour())
                    .hintFive(board.getHint().getHintFive())
                    .build();
        }
    }

    @NoArgsConstructor
    @Getter
    public static class Likes {
        private Long id;
        private Boolean isLiked;

        @Builder
        private Likes(Long id, Boolean isLiked) {
            this.id = id;
            this.isLiked = isLiked;
        }

        public static BoardResponse.Likes response(Like like, boolean isLiked) {
            return Likes.builder()
                    .id(like.getId())
                    .isLiked(isLiked)
                    .build();
        }
    }
}
