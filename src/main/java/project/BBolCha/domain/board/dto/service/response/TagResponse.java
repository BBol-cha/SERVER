package project.BBolCha.domain.board.dto.service.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.BBolCha.domain.board.entity.Tag;

@Getter
@NoArgsConstructor
public class TagResponse {
    private Boolean horror;
    private Boolean daily;
    private Boolean romance;
    private Boolean fantasy;
    private Boolean sf;

    @Builder
    private TagResponse(Boolean horror, Boolean daily, Boolean romance, Boolean fantasy, Boolean sf) {
        this.horror = horror;
        this.daily = daily;
        this.romance = romance;
        this.fantasy = fantasy;
        this.sf = sf;
    }

    public static TagResponse response(Tag tag) {
        return TagResponse.builder()
                .horror(tag.getHorror())
                .daily(tag.getDaily())
                .romance(tag.getRomance())
                .fantasy(tag.getFantasy())
                .sf(tag.getSf())
                .build();
    }
}
