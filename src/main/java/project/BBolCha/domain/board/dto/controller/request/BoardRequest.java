package project.BBolCha.domain.board.dto.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import project.BBolCha.domain.board.dto.service.request.BoardServiceRequest;

public class BoardRequest {

    @Getter
    @NoArgsConstructor
    public static class Save {
        private String authorName;
        private String title;
        private String content;
        private String correct;
        private String contentImageUrl;
        private TagRequest.Save tag;
        private HintRequest.Save hint;

        public BoardServiceRequest.Save toServiceRequest() {
            return BoardServiceRequest.Save.builder()
                    .authorName(authorName)
                    .title(title)
                    .content(content)
                    .correct(correct)
                    .contentImageUrl(contentImageUrl)
                    .tag(tag.toServiceRequest())
                    .hint(hint.toServiceRequest())
                    .build();
        }
    }
}
