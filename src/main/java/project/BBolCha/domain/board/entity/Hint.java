package project.BBolCha.domain.board.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import project.BBolCha.domain.board.dto.HintDto;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
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

    public void setId(Long id) {
        this.id = id;
    }

    public void updateHint(HintDto.DetailDto request) {
        this.hintOne = request.getHintOne();
        this.hintTwo = request.getHintTwo();
        this.hintThree = request.getHintThree();
        this.hintFour = request.getHintFour();
        this.hintFive = request.getHintFive();
    }
}
