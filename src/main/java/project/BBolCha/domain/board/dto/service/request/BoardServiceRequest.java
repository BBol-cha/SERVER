package project.BBolCha.domain.board.dto.service.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BoardServiceRequest {

    @Getter
    @NoArgsConstructor
    public static class Save {
        private String authorName;
        private String title;
        private String content;
        private String correct;
        private String contentImageUrl;
        private TagServiceRequest.Save tag;
        private HintServiceRequest.Save hint;

        @Builder
        private Save(String authorName, String title, String content, String correct, String contentImageUrl, TagServiceRequest.Save tag, HintServiceRequest.Save hint) {
            this.authorName = authorName;
            this.title = title;
            this.content = content;
            this.correct = correct;
            this.contentImageUrl = contentImageUrl;
            this.tag = tag;
            this.hint = hint;
        }
    }
}
