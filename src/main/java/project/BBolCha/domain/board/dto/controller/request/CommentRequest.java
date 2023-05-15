package project.BBolCha.domain.board.dto.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import project.BBolCha.domain.board.dto.service.request.CommentServiceRequest;

import javax.validation.constraints.NotBlank;

public class CommentRequest {

    @Getter
    @NoArgsConstructor
    public static class Add {
        @NotBlank
        private String note;

        public CommentServiceRequest.Add toServiceRequest() {
            return CommentServiceRequest.Add.builder()
                    .note(note)
                    .build();
        }

        // 테스트 생성자
        public Add(String note) {
            this.note = note;
        }
    }
}
