package project.BBolCha.domain.board.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import project.BBolCha.domain.ControllerTestSupport;
import project.BBolCha.domain.board.dto.controller.request.CommentRequest;
import project.BBolCha.domain.user.entity.User;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentControllerTest extends ControllerTestSupport {

    @DisplayName("댓글 등록 API")
    @Test
    void addComment() throws Exception {
        // given
        CommentRequest.Add request = new CommentRequest.Add("너무 재밌어요!!");

        // when // then
        mockMvc.perform(
                MockMvcRequestBuilders.post("/board/comment")
                        .param("id","1")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("댓글 등록시 아무것도 작성하지 않으면 400 에러가 발생한다.")
    @Test
    void addCommentWithNotAddNote() throws Exception {
        // given
        CommentRequest.Add request = new CommentRequest.Add("");

        // when // then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/board/comment")
                                .param("id","1")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("게시글의 댓글들을 페이지로 조회한다.")
    @Test
    void fetchCommentsByPage() throws Exception {
        // when // then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/board/list/comment")
                        .param("id","1")
                        .param("page","1")
        )
                .andDo(print())
                .andExpect(status().isOk());
    }
}