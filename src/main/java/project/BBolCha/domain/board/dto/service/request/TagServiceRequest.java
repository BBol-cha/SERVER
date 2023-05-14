package project.BBolCha.domain.board.dto.service.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TagServiceRequest {
    @NoArgsConstructor
    @Getter
    public static class Save {
        private Boolean horror;
        private Boolean daily;
        private Boolean romance;
        private Boolean fantasy;
        private Boolean sf;

        @Builder
        private Save(Boolean horror, Boolean daily, Boolean romance, Boolean fantasy, Boolean sf) {
            this.horror = horror;
            this.daily = daily;
            this.romance = romance;
            this.fantasy = fantasy;
            this.sf = sf;
        }
    }
}
