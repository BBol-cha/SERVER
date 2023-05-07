package project.BBolCha.domain.board.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.BBolCha.domain.board.Entity.Hint;

public class HintDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class DetailDto {
        private String hintOne;
        private String hintTwo;
        private String hintThree;
        private String hintFour;
        private String hintFive;

        public static DetailDto response(Hint hint) {
            return DetailDto.builder()
                    .hintOne(hint.getHintOne())
                    .hintTwo(hint.getHintTwo())
                    .hintThree(hint.getHintThree())
                    .hintFour(hint.getHintFour())
                    .hintFive(hint.getHintFive())
                    .build();
        }
    }
}
