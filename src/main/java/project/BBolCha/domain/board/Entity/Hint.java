package project.BBolCha.domain.board.Entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import project.BBolCha.domain.board.Dto.HintDto;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@DynamicUpdate
@Entity
public class Hint {

    @Id
    @OneToOne(fetch = FetchType.LAZY)
    private Board board;

    private String hintOne;

    private String hintTwo;

    private String hintThree;

    private String hintFour;

    private String hintFive;

    public void updateHint(HintDto.DetailDto request) {
        this.hintOne = request.getHintOne();
        this.hintTwo = request.getHintTwo();
        this.hintThree = request.getHintThree();
        this.hintFour = request.getHintFour();
        this.hintFive = request.getHintFive();
    }
}
