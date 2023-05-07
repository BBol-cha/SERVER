package project.BBolCha.domain.board.Entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
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
}
