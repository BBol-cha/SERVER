package project.BBolCha.domain.board.dto.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import project.BBolCha.domain.board.dto.service.request.BoardServiceRequest;
import project.BBolCha.domain.board.entity.Hint;
import project.BBolCha.domain.board.entity.Tag;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class BoardRequest {

    @Getter
    @NoArgsConstructor
    public static class Save {
        @NotBlank(message = "제목은 필수입니다.")
        private String title;

        @NotBlank(message = "내용은 필수입니다.")
        private String content;

        @NotBlank(message = "정답은 필수입니다.")
        private String correct;

        private String contentImageUrl;

        private TagRequest.Save tag;

        private HintRequest.Save hint;

        public BoardServiceRequest.Save toServiceRequest() {
            return BoardServiceRequest.Save.builder()
                    .title(title)
                    .content(content)
                    .correct(correct)
                    .contentImageUrl(contentImageUrl)
                    .tag(tag.toServiceRequest())
                    .hint(hint.toServiceRequest())
                    .build();
        }

        // 테스트 생성자
        public Save(String title, String content, String correct, String contentImageUrl, TagRequest.Save tag, HintRequest.Save hint) {
            this.title = title;
            this.content = content;
            this.correct = correct;
            this.contentImageUrl = contentImageUrl;
            this.tag = tag;
            this.hint = hint;
        }
    }

    @NoArgsConstructor
    @Getter
    public static class Update {
        @NotBlank(message = "제목은 필수입니다.")
        private String title;

        @NotBlank(message = "내용은 필수입니다.")
        private String content;

        @NotBlank(message = "정답은 필수입니다.")
        private String correct;

        private String contentImageUrl;

        private TagRequest.Save tag;

        private HintRequest.Save hint;

        public BoardServiceRequest.Update toServiceRequest() {
            return BoardServiceRequest.Update.builder()
                    .title(title)
                    .content(content)
                    .correct(correct)
                    .contentImageUrl(contentImageUrl)
                    .tag(tag.toServiceRequest())
                    .hint(hint.toServiceRequest())
                    .build();
        }

        public Update(String title, String content, String correct, String contentImageUrl, TagRequest.Save tag, HintRequest.Save hint) {
            this.title = title;
            this.content = content;
            this.correct = correct;
            this.contentImageUrl = contentImageUrl;
            this.tag = tag;
            this.hint = hint;
        }
    }
}
