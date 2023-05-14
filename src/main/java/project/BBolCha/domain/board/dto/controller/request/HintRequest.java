package project.BBolCha.domain.board.dto.controller.request;

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
    }
}
