package project.BBolCha.domain.board.dto.service.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.BBolCha.domain.board.dto.controller.request.HintRequest;
import project.BBolCha.domain.board.dto.controller.request.TagRequest;
import project.BBolCha.domain.board.entity.Hint;
import project.BBolCha.domain.board.entity.Tag;

public class BoardServiceRequest {

    @Getter
    @NoArgsConstructor
    public static class Save {
        private String title;
        private String content;
        private String correct;
        private String contentImageUrl;
        private TagServiceRequest.Save tag;
        private HintServiceRequest.Save hint;

        @Builder
        private Save(String title, String content, String correct, String contentImageUrl, TagServiceRequest.Save tag, HintServiceRequest.Save hint) {
            this.title = title;
            this.content = content;
            this.correct = correct;
            this.contentImageUrl = contentImageUrl;
            this.tag = tag;
            this.hint = hint;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Update {
        private String title;
        private String content;
        private String correct;
        private String contentImageUrl;
        private TagServiceRequest.Save tag;
        private HintServiceRequest.Save hint;

        @Builder
        private Update(String title, String content, String correct, String contentImageUrl, TagServiceRequest.Save tag, HintServiceRequest.Save hint) {
            this.title = title;
            this.content = content;
            this.correct = correct;
            this.contentImageUrl = contentImageUrl;
            this.tag = tag;
            this.hint = hint;
        }
    }
}
