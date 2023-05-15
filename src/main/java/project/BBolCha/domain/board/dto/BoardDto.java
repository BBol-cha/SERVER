package project.BBolCha.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.BBolCha.domain.board.entity.Board;

import java.time.LocalDateTime;

public class BoardDto {

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
