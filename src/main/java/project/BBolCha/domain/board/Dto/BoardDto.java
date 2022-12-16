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

        public BoardDto.Request Response(Board board) {
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
}
