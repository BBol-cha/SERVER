package project.BBolCha.domain.board.dto.service.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.BBolCha.domain.board.entity.Hint;

@NoArgsConstructor
@Getter
public class HintResponse {
    private String hintOne;
    private String hintTwo;
    private String hintThree;
    private String hintFour;
    private String hintFive;

    @Builder
    private HintResponse(String hintOne, String hintTwo, String hintThree, String hintFour, String hintFive) {
        this.hintOne = hintOne;
        this.hintTwo = hintTwo;
        this.hintThree = hintThree;
        this.hintFour = hintFour;
        this.hintFive = hintFive;
    }

    public static HintResponse response(Hint hint) {
        return HintResponse.builder()
                .hintOne(hint.getHintOne())
                .hintTwo(hint.getHintTwo())
                .hintThree(hint.getHintThree())
                .hintFour(hint.getHintFour())
                .hintFive(hint.getHintFive())
                .build();
    }
}
