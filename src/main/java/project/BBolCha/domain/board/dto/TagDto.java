package project.BBolCha.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.BBolCha.domain.board.entity.Tag;

public class TagDto {
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class DetailDto {
        private Boolean horror;
        private Boolean daily;
        private Boolean romance;
        private Boolean fantasy;
        private Boolean sf;

        public static DetailDto response(Tag tag) {
            return DetailDto.builder()
                    .horror(tag.getHorror())
                    .daily(tag.getDaily())
                    .romance(tag.getRomance())
                    .fantasy(tag.getFantasy())
                    .sf(tag.getRomance())
                    .build();
        }
    }
}
