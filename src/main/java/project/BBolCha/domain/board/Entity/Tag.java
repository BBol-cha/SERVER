package project.BBolCha.domain.board.Entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import project.BBolCha.domain.board.Dto.TagDto;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@DynamicUpdate
@Entity
public class Tag {

    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private Boolean horror;

    private Boolean daily;

    private Boolean romance;

    private Boolean fantasy;

    private Boolean sf;

    public void updateTag(TagDto.DetailDto request) {
        this.horror = request.getHorror();
        this.daily = request.getDaily();
        this.romance = request.getRomance();
        this.fantasy = request.getFantasy();
        this.sf = request.getSf();
    }
}
