package project.BBolCha.docs.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import project.BBolCha.docs.RestDocsExceptionTest;
import project.BBolCha.domain.board.dto.controller.request.BoardRequest;
import project.BBolCha.domain.board.dto.controller.request.HintRequest;
import project.BBolCha.domain.board.dto.controller.request.TagRequest;
import project.BBolCha.domain.board.dto.service.request.BoardServiceRequest;
import project.BBolCha.domain.board.service.BoardService;
import project.BBolCha.domain.user.entity.User;
import project.BBolCha.global.exception.CustomException;
import project.BBolCha.global.model.Result;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ExceptionControllerDocsTest extends RestDocsExceptionTest {

    @Autowired
    private BoardService boardService;

    @DisplayName("Exception 상태코드 Docs 작성을 위한 테스트")
    @Test
    void createBoardWithEmptyTitle() throws Exception {
        // given
        BoardRequest.Save request = new BoardRequest.Save(
                "", "content", "correct",
                "test.png", new TagRequest.Save(), new HintRequest.Save()
        );

        // when // then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/board")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andDo(document("common-exception",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("상태 코드는 예시입니다."),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("상태 메세지는 예시입니다."),
                                fieldWithPath("data").type(JsonFieldType.NULL)
                                        .description("always null")
                        )
                ));
    }
}
