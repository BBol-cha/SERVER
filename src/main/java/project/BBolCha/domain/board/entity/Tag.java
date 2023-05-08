package project.BBolCha.domain.board.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import project.BBolCha.domain.board.dto.TagDto;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
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

    public void setId(Long id) {
        this.id = id;
    }

    public void updateTag(TagDto.DetailDto request) {
        this.horror = request.getHorror();
        this.daily = request.getDaily();
        this.romance = request.getRomance();
        this.fantasy = request.getFantasy();
        this.sf = request.getSf();
    }
}
