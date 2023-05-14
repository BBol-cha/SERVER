package project.BBolCha.domain.board.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import project.BBolCha.domain.board.dto.TagDto;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DynamicUpdate
@Entity
public class Tag implements Serializable {

    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private Boolean horror;

    private Boolean daily;

    private Boolean romance;

    private Boolean fantasy;

    private Boolean sf;

    @Builder
    private Tag(Long id, Board board, Boolean horror, Boolean daily, Boolean romance, Boolean fantasy, Boolean sf) {
        this.id = id;
        this.board = board;
        this.horror = horror;
        this.daily = daily;
        this.romance = romance;
        this.fantasy = fantasy;
        this.sf = sf;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void updateTag(TagDto.DetailDto request) {
        this.horror = request.getHorror();
        this.daily = request.getDaily();
        this.romance = request.getRomance();
        this.fantasy = request.getFantasy();
        this.sf = request.getSf();
    }
}
