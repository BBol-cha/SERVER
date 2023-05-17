package project.BBolCha.docs.board;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import project.BBolCha.CommonController;
import project.BBolCha.docs.RestDocsSupport;
import project.BBolCha.domain.board.controller.CommentController;
import project.BBolCha.domain.board.dto.controller.request.CommentRequest;
import project.BBolCha.domain.board.dto.service.response.CommentResponse;
import project.BBolCha.domain.board.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentControllerDocsTest extends RestDocsSupport {

    private final CommentService commentService = mock(CommentService.class);

    @Override
    protected Object initController() {
        return new CommentController(commentService);
    }

    @DisplayName("댓글 등록 API")
    @Test
    void addComment() throws Exception {
        // given
        CommentRequest.Add request = new CommentRequest.Add("너무 재밌어요!!");

        // when // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/board/comment")
                                .header("Authority", "JWT_AccessToken")
                                .param("id", "1")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("comment-add",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authority").description("JWT_AccessToken")
                        ),
                        requestParameters(
                                parameterWithName("id").description("게시글 ID")
                        ),
                        requestFields(
                                fieldWithPath("note").type(JsonFieldType.STRING)
                                        .description("댓글 내용")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("data is empty object")
                        )
                ));
    }

    @DisplayName("댓글 조회 API")
    @Test
    void fetchCommentsByPage() throws Exception {
        // given
        CommentResponse.Detail commentDetail = CommentResponse.Detail.builder()
                .userName("테스트 계정")
                .userProfileImageUrl("test.png")
                .note("테스트 댓글 내용")
                .createdAt(LocalDateTime.of(2022, 5, 11, 13, 21))
                .build();

        List<CommentResponse.Detail> commetList = List.of(commentDetail);
        Page<CommentResponse.Detail> commentPage = new PageImpl<>(commetList, PageRequest.of(0, 1), commetList.size());

        given(commentService.fetchCommentsByPage(anyLong(), anyInt()))
                .willReturn(commentPage);

        // when // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/board/list/comment")
                                .param("id", "1")
                                .param("page", "1")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("comment-page",
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("id").description("게시글 ID"),
                                parameterWithName("page").description("요청할 페이지 번호")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data.content[]").type(JsonFieldType.ARRAY)
                                        .description("댓글 목록"),
                                fieldWithPath("data.content[].userName").type(JsonFieldType.STRING)
                                        .description("댓글 작성자"),
                                fieldWithPath("data.content[].userProfileImageUrl").type(JsonFieldType.STRING)
                                        .description("댓글 작성자 프로필 사진"),
                                fieldWithPath("data.content[].note").type(JsonFieldType.STRING)
                                        .description("댓글 내용"),
                                fieldWithPath("data.content[].createdAt").type(JsonFieldType.ARRAY)
                                        .description("댓글 작성 날짜"),
                                fieldWithPath("data.pageable.sort.empty").type(JsonFieldType.BOOLEAN)
                                        .description("정렬 정보가 비었는지 여부"),
                                fieldWithPath("data.pageable.sort.sorted").type(JsonFieldType.BOOLEAN)
                                        .description("정렬이 되었는지 여부"),
                                fieldWithPath("data.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN)
                                        .description("정렬이 안 되었는지 여부"),
                                fieldWithPath("data.pageable.offset").type(JsonFieldType.NUMBER)
                                        .description("페이지 시작점"),
                                fieldWithPath("data.pageable.pageNumber").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지 번호"),
                                fieldWithPath("data.pageable.pageSize").type(JsonFieldType.NUMBER)
                                        .description("페이지 크기"),
                                fieldWithPath("data.pageable.paged").type(JsonFieldType.BOOLEAN)
                                        .description("페이징 여부"),
                                fieldWithPath("data.pageable.unpaged").type(JsonFieldType.BOOLEAN)
                                        .description("페이징이 안된 여부"),
                                fieldWithPath("data.last").type(JsonFieldType.BOOLEAN)
                                        .description("마지막 페이지 여부"),
                                fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER)
                                        .description("전체 페이지 수"),
                                fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER)
                                        .description("전체 요소 수"),
                                fieldWithPath("data.size").type(JsonFieldType.NUMBER)
                                        .description("페이지 크기"),
                                fieldWithPath("data.number").type(JsonFieldType.NUMBER)
                                        .description("페이지 번호"),
                                fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN)
                                        .description("정렬 정보가 비었는지 여부"),
                                fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN)
                                        .description("정렬이 되었는지 여부"),
                                fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN)
                                        .description("정렬이 안 되었는지 여부"),
                                fieldWithPath("data.first").type(JsonFieldType.BOOLEAN)
                                        .description("첫 페이지 여부"),
                                fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지의 요소 수"),
                                fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN)
                                        .description("데이터가 비었는지 여부")
                        )
                ));
    }

    @DisplayName("댓글 삭제 API")
    @Test
    void deleteComment() throws Exception {
        // when // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.delete("/board/comment")
                                .header("Authority","JWT_AccessToken")
                                .param("id", "1")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("comment-delete",
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authority").description("JWT_AccessToken")
                        ),
                        requestParameters(
                                parameterWithName("id").description("댓글 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("data is empty object")
                        )
                ));
    }
}
