package project.BBolCha.domain.board.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import project.BBolCha.domain.ControllerTestSupport;
import project.BBolCha.domain.board.dto.controller.request.BoardRequest;
import project.BBolCha.domain.board.dto.controller.request.HintRequest;
import project.BBolCha.domain.board.dto.controller.request.TagRequest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BoardControllerTest extends ControllerTestSupport {
    @DisplayName("게시글 생성 API")
    @Test
    void createBoard() throws Exception {
        // given
        BoardRequest.Save request = new BoardRequest.Save(
                "title", "content", "correct",
                "test.png", new TagRequest.Save(), new HintRequest.Save()
        );

        // when // then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/board")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("제목을 작성하지 않았을경우 400 에러가 발생한다.")
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
                .andExpect(status().isBadRequest());
    }

    @DisplayName("게시글 상세 조회 API")
    @Test
    void readBoardDetail() throws Exception {
        // given

        // when // then
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/board/list/{id}", 1)
                )
                .andDo(print())
                .andExpect(status().isOk());

    }

    @DisplayName("게시글 수정 API")
    @Test
    void updateBoard() throws Exception {
        // given
        BoardRequest.Update request = new BoardRequest.Update(
                "title", "content", "correct",
                "test.png", new TagRequest.Save(), new HintRequest.Save()
        );

        // when // then
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/board/{id}", 1)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("게시글 수정할때 제목을 입력하지 않으면 400 에러가 발생한다.")
    @Test
    void updateBoardWithEmptyTitle() throws Exception {
        // given
        BoardRequest.Update request = new BoardRequest.Update(
                "", "content", "correct",
                "test.png", new TagRequest.Save(), new HintRequest.Save()
        );

        // when // then
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/board/{id}", 1)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("게시글 삭제 API")
    @Test
    void deleteBoard() throws Exception {
        // when // then
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/board/{id}", 1)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}