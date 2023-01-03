package project.BBolCha.domain.board.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.BBolCha.domain.board.Entity.Board;

import java.time.LocalDateTime;

public class BoardDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private Long id;
        private Long userId;
        private String name;
        private String title;
        private String note;
        private Integer views;
        private String answer;
        private String bimg;
        private LocalDateTime createAt;
        private LocalDateTime updateAt;
        private String[] tag;
        private String[] hints;

        public static BoardDto.Request Response(Board board) {
            return Request.builder()
                    .id(board.getId())
                    .userId(board.getUserId())
                    .name(board.getName())
                    .title(board.getTitle())
                    .bimg(board.getBimg())
                    .views(board.getViews())
                    .note(board.getNote())
                    .answer(board.getAnswer())
                    .createAt(board.getCreateAt())
                    .updateAt(board.getUpdateAt())
                    .tag(board.getTag().split(","))
                    .hints(board.getHints().split(","))
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class detailResponse {
        private Long id;
        private Long userId;
        private String name;
        private String title;
        private String note;
        private Integer views;
        private String answer;
        private String bimg;
        private LocalDateTime createAt;
        private LocalDateTime updateAt;
        private String tag;
        private Long likes;

        public static BoardDto.detailResponse response(Board board ,Long likes) {
            return detailResponse.builder()
                    .id(board.getId())
                    .userId(board.getUserId())
                    .name(board.getName())
                    .title(board.getTitle())
                    .bimg(board.getBimg())
                    .views(board.getViews())
                    .note(board.getNote())
                    .answer(board.getAnswer())
                    .createAt(board.getCreateAt())
                    .updateAt(board.getUpdateAt())
                    .tag(board.getTag())
                    .likes(likes)
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
