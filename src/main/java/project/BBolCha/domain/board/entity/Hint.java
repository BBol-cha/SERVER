package project.BBolCha.domain.board.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import project.BBolCha.domain.board.dto.HintDto;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DynamicUpdate
@Entity
public class Hint implements Serializable {

    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private String hintOne;

    private String hintTwo;

    private String hintThree;

    private String hintFour;

    private String hintFive;

    @Builder
    private Hint(Long id, Board board, String hintOne, String hintTwo, String hintThree, String hintFour, String hintFive) {
        this.id = id;
        this.board = board;
        this.hintOne = hintOne;
        this.hintTwo = hintTwo;
        this.hintThree = hintThree;
        this.hintFour = hintFour;
        this.hintFive = hintFive;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void updateHint(HintDto.DetailDto request) {
        this.hintOne = request.getHintOne();
        this.hintTwo = request.getHintTwo();
        this.hintThree = request.getHintThree();
        this.hintFour = request.getHintFour();
        this.hintFive = request.getHintFive();
    }
}
