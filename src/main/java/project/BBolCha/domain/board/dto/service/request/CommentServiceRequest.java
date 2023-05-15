package project.BBolCha.domain.board.dto.service.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CommentServiceRequest {

    @Getter
    @NoArgsConstructor
    public static class Add {
        private String note;

        @Builder
        private Add(String note) {
            this.note = note;
        }
    }
}
