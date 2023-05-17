package project.BBolCha.domain.board.dto.controller.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.BBolCha.domain.board.dto.service.request.TagServiceRequest;

public class TagRequest {
    @NoArgsConstructor
    @Getter
    public static class Save {
        private Boolean horror;
        private Boolean daily;
        private Boolean romance;
        private Boolean fantasy;
        private Boolean sf;

        public TagServiceRequest.Save toServiceRequest() {
            return TagServiceRequest.Save.builder()
                    .horror(horror)
                    .daily(daily)
                    .romance(romance)
                    .fantasy(fantasy)
                    .sf(sf)
                    .build();
        }

        // docs 테스트 생성자
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
