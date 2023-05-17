package project.BBolCha.domain.board.dto.controller.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.BBolCha.domain.board.dto.service.request.HintServiceRequest;

public class HintRequest {
    @NoArgsConstructor
    @Getter
    public static class Save {
        private String hintOne;
        private String hintTwo;
        private String hintThree;
        private String hintFour;
        private String hintFive;

        public HintServiceRequest.Save toServiceRequest() {
            return HintServiceRequest.Save.builder()
                    .hintOne(hintOne)
                    .hintTwo(hintTwo)
                    .hintThree(hintThree)
                    .hintFour(hintFour)
                    .hintFive(hintFive)
                    .build();
        }

        // docs 테스트 생성자
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
