package project.BBolCha.domain.board.dto.service.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class HintServiceRequest {
    @NoArgsConstructor
    @Getter
    public static class Save {
        private String hintOne;
        private String hintTwo;
        private String hintThree;
        private String hintFour;
        private String hintFive;

        @Builder
        private Save(String hintOne, String hintTwo, String hintThree, String hintFour, String hintFive) {
            this.hintOne = hintOne;
            this.hintTwo = hintTwo;
            this.hintThree = hintThree;
            this.hintFour = hintFour;
            this.hintFive = hintFive;
        }
    }
}
