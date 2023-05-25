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
import project.BBolCha.docs.RestDocsSupport;
import project.BBolCha.domain.board.controller.BoardController;
import project.BBolCha.domain.board.dto.controller.request.BoardRequest;
import project.BBolCha.domain.board.dto.controller.request.HintRequest;
import project.BBolCha.domain.board.dto.controller.request.TagRequest;
import project.BBolCha.domain.board.dto.service.request.BoardServiceRequest;
import project.BBolCha.domain.board.dto.service.response.BoardResponse;
import project.BBolCha.domain.board.dto.service.response.HintResponse;
import project.BBolCha.domain.board.dto.service.response.TagResponse;
import project.BBolCha.domain.board.entity.Hint;
import project.BBolCha.domain.board.entity.Tag;
import project.BBolCha.domain.board.service.BoardService;
import project.BBolCha.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BoardControllerDocsTest extends RestDocsSupport {

    private final BoardService boardService = mock(BoardService.class);

    @Override
    protected Object initController() {
        return new BoardController(boardService);
    }

    @DisplayName("게시글 생성 API")
    @Test
    void createBoard() throws Exception {
        // given
        TagRequest.Save tagRequest = TagRequest.Save.builder()
                .horror(true)
                .daily(true)
                .romance(false)
                .fantasy(false)
                .sf(true)
                .build();

        HintRequest.Save hintRequest = HintRequest.Save.builder()
                .hintOne("1")
                .hintTwo("2")
                .hintThree("3")
                .hintFour("4")
                .hintFive("5")
                .build();

        BoardRequest.Save request = new BoardRequest.Save(
                "테스트 제목", "테스트 내용", "테스트 정답",
                "테스트 게시글 이미지", tagRequest, hintRequest
        );

        Tag tag = Tag.builder()
                .horror(true)
                .daily(true)
                .romance(false)
                .fantasy(false)
                .sf(true)
                .build();

        Hint hint = Hint.builder()
                .hintOne("1")
                .hintTwo("2")
                .hintThree("3")
                .hintFour("4")
                .hintFive("5")
                .build();

        given(boardService.createBoard(any(BoardServiceRequest.Save.class), any(User.class)))
                .willReturn(BoardResponse.Save.builder()
                        .id(1L)
                        .authorName("테스트 계정")
                        .title("테스트 제목")
                        .content("테스트 내용")
                        .correct("테스트 정답")
                        .contentImageUrl("테스트 게시글 이미지")
                        .likeCount(0)
                        .viewCount(0)
                        .createdAt(LocalDateTime.of(2023, 5, 13, 17, 56))
                        .updatedAt(LocalDateTime.of(2023, 5, 13, 17, 56))
                        .tag(TagResponse.response(tag))
                        .hint(HintResponse.response(hint))
                        .build()
                );

        // when // then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/board")
                                .header("Authorization", "JWT_AccessToken")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("board-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT_AccessToken")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("내용"),
                                fieldWithPath("correct").type(JsonFieldType.STRING)
                                        .description("정답"),
                                fieldWithPath("contentImageUrl").type(JsonFieldType.STRING)
                                        .description("게시글 이미지")
                                        .optional(),
                                fieldWithPath("tag.horror").type(JsonFieldType.BOOLEAN)
                                        .description("테마 : 공포")
                                        .optional(),
                                fieldWithPath("tag.daily").type(JsonFieldType.BOOLEAN)
                                        .description("테마 : 일상")
                                        .optional(),
                                fieldWithPath("tag.romance").type(JsonFieldType.BOOLEAN)
                                        .description("테마 : 로맨스")
                                        .optional(),
                                fieldWithPath("tag.fantasy").type(JsonFieldType.BOOLEAN)
                                        .description("테마 : 판타지")
                                        .optional(),
                                fieldWithPath("tag.sf").type(JsonFieldType.BOOLEAN)
                                        .description("테마 : SF")
                                        .optional(),
                                fieldWithPath("hint.hintOne").type(JsonFieldType.STRING)
                                        .description("첫번째 힌트")
                                        .optional(),
                                fieldWithPath("hint.hintTwo").type(JsonFieldType.STRING)
                                        .description("두번째 힌트")
                                        .optional(),
                                fieldWithPath("hint.hintThree").type(JsonFieldType.STRING)
                                        .description("세번째 힌트")
                                        .optional(),
                                fieldWithPath("hint.hintFour").type(JsonFieldType.STRING)
                                        .description("네번째 힌트")
                                        .optional(),
                                fieldWithPath("hint.hintFive").type(JsonFieldType.STRING)
                                        .description("다섯번째 힌트")
                                        .optional()
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("상태 메세지"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("게시글 ID"),
                                fieldWithPath("data.authorName").type(JsonFieldType.STRING)
                                        .description("작성자 이름"),
                                fieldWithPath("data.title").type(JsonFieldType.STRING)
                                        .description("제목"),
                                fieldWithPath("data.content").type(JsonFieldType.STRING)
                                        .description("내용"),
                                fieldWithPath("data.correct").type(JsonFieldType.STRING)
                                        .description("정답"),
                                fieldWithPath("data.likeCount").type(JsonFieldType.NUMBER)
                                        .description("게시글 좋아요 수"),
                                fieldWithPath("data.viewCount").type(JsonFieldType.NUMBER)
                                        .description("게시글 조회수"),
                                fieldWithPath("data.createdAt").type(JsonFieldType.ARRAY)
                                        .description("게시글 작성날짜"),
                                fieldWithPath("data.updatedAt").type(JsonFieldType.ARRAY)
                                        .description("게시글 업데이트 날짜"),
                                fieldWithPath("data.contentImageUrl").type(JsonFieldType.STRING)
                                        .description("게시글 이미지"),
                                fieldWithPath("data.tag.horror").type(JsonFieldType.BOOLEAN)
                                        .description("테마 : 공포"),
                                fieldWithPath("data.tag.daily").type(JsonFieldType.BOOLEAN)
                                        .description("테마 : 일상"),
                                fieldWithPath("data.tag.romance").type(JsonFieldType.BOOLEAN)
                                        .description("테마 : 로맨스"),
                                fieldWithPath("data.tag.fantasy").type(JsonFieldType.BOOLEAN)
                                        .description("테마 : 판타지"),
                                fieldWithPath("data.tag.sf").type(JsonFieldType.BOOLEAN)
                                        .description("테마 : SF"),
                                fieldWithPath("data.hint.hintOne").type(JsonFieldType.STRING)
                                        .description("첫번째 힌트"),
                                fieldWithPath("data.hint.hintTwo").type(JsonFieldType.STRING)
                                        .description("두번째 힌트"),
                                fieldWithPath("data.hint.hintThree").type(JsonFieldType.STRING)
                                        .description("세번째 힌트"),
                                fieldWithPath("data.hint.hintFour").type(JsonFieldType.STRING)
                                        .description("네번째 힌트"),
                                fieldWithPath("data.hint.hintFive").type(JsonFieldType.STRING)
                                        .description("다섯번째 힌트")
                        )
                ));
    }

    @DisplayName("게시글 상세 조회 API")
    @Test
    void readBoardDetail() throws Exception {
        // given
        Tag tag = Tag.builder()
                .horror(true)
                .daily(true)
                .romance(false)
                .fantasy(false)
                .sf(true)
                .build();

        Hint hint = Hint.builder()
                .hintOne("1")
                .hintTwo("2")
                .hintThree("3")
                .hintFour("4")
                .hintFive("5")
                .build();

        given(boardService.findBoard(anyLong()))
                .willReturn(BoardResponse.Detail.builder()
                        .id(1L)
                        .authorName("테스트 계정")
                        .title("테스트 제목")
                        .content("테스트 내용")
                        .correct("테스트 정답")
                        .contentImageUrl("테스트 게시글 이미지")
                        .likeCount(0)
                        .viewCount(0)
                        .createdAt(LocalDateTime.of(2023, 5, 13, 17, 56))
                        .updatedAt(LocalDateTime.of(2023, 5, 13, 17, 56))
                        .build()
                );

        // when // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/board/list/{id}", 1)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("board-detail",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("상태 메세지"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("게시글 ID"),
                                fieldWithPath("data.authorName").type(JsonFieldType.STRING)
                                        .description("작성자 이름"),
                                fieldWithPath("data.title").type(JsonFieldType.STRING)
                                        .description("제목"),
                                fieldWithPath("data.content").type(JsonFieldType.STRING)
                                        .description("내용"),
                                fieldWithPath("data.correct").type(JsonFieldType.STRING)
                                        .description("정답"),
                                fieldWithPath("data.likeCount").type(JsonFieldType.NUMBER)
                                        .description("게시글 좋아요 수"),
                                fieldWithPath("data.viewCount").type(JsonFieldType.NUMBER)
                                        .description("게시글 조회수"),
                                fieldWithPath("data.createdAt").type(JsonFieldType.ARRAY)
                                        .description("게시글 작성날짜"),
                                fieldWithPath("data.updatedAt").type(JsonFieldType.ARRAY)
                                        .description("게시글 업데이트 날짜"),
                                fieldWithPath("data.contentImageUrl").type(JsonFieldType.STRING)
                                        .description("게시글 이미지"),
                                fieldWithPath("data.tag.horror").type(JsonFieldType.BOOLEAN)
                                        .description("테마 : 공포"),
                                fieldWithPath("data.tag.daily").type(JsonFieldType.BOOLEAN)
                                        .description("테마 : 일상"),
                                fieldWithPath("data.tag.romance").type(JsonFieldType.BOOLEAN)
                                        .description("테마 : 로맨스"),
                                fieldWithPath("data.tag.fantasy").type(JsonFieldType.BOOLEAN)
                                        .description("테마 : 판타지"),
                                fieldWithPath("data.tag.sf").type(JsonFieldType.BOOLEAN)
                                        .description("테마 : SF"),
                                fieldWithPath("data.hint.hintOne").type(JsonFieldType.STRING)
                                        .description("첫번째 힌트"),
                                fieldWithPath("data.hint.hintTwo").type(JsonFieldType.STRING)
                                        .description("두번째 힌트"),
                                fieldWithPath("data.hint.hintThree").type(JsonFieldType.STRING)
                                        .description("세번째 힌트"),
                                fieldWithPath("data.hint.hintFour").type(JsonFieldType.STRING)
                                        .description("네번째 힌트"),
                                fieldWithPath("data.hint.hintFive").type(JsonFieldType.STRING)
                                        .description("다섯번째 힌트")
                        )
                ));
    }

    @DisplayName("게시글 수정 API")
    @Test
    void updateBoard() throws Exception {
        // given
        TagRequest.Save tagRequest = TagRequest.Save.builder()
                .horror(true)
                .daily(true)
                .romance(false)
                .fantasy(false)
                .sf(true)
                .build();

        HintRequest.Save hintRequest = HintRequest.Save.builder()
                .hintOne("1")
                .hintTwo("2")
                .hintThree("3")
                .hintFour("4")
                .hintFive("5")
                .build();

        BoardRequest.Update request = new BoardRequest.Update(
                "테스트 제목", "테스트 내용", "테스트 정답",
                "테스트 게시글 이미지", tagRequest, hintRequest
        );

        Tag tag = Tag.builder()
                .horror(true)
                .daily(true)
                .romance(false)
                .fantasy(false)
                .sf(true)
                .build();

        Hint hint = Hint.builder()
                .hintOne("1")
                .hintTwo("2")
                .hintThree("3")
                .hintFour("4")
                .hintFive("5")
                .build();

        given(boardService.updateBoard(anyLong(), any(BoardServiceRequest.Update.class), any(User.class)))
                .willReturn(BoardResponse.Detail.builder()
                        .id(1L)
                        .authorName("테스트 계정")
                        .title("테스트 제목")
                        .content("테스트 내용")
                        .correct("테스트 정답")
                        .contentImageUrl("테스트 게시글 이미지")
                        .likeCount(0)
                        .viewCount(0)
                        .createdAt(LocalDateTime.of(2023, 5, 13, 17, 56))
                        .updatedAt(LocalDateTime.of(2023, 5, 13, 17, 56))
                        .build()
                );

        // when // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.patch("/board/{id}", 1)
                                .header("Authorization", "JWT_AccessToken")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("board-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT_AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("id").description("게시글 ID")
                        )
                        ,
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("내용"),
                                fieldWithPath("correct").type(JsonFieldType.STRING)
                                        .description("정답"),
                                fieldWithPath("contentImageUrl").type(JsonFieldType.STRING)
                                        .description("게시글 이미지")
                                        .optional(),
                                fieldWithPath("tag.horror").type(JsonFieldType.BOOLEAN)
                                        .description("테마 : 공포")
                                        .optional(),
                                fieldWithPath("tag.daily").type(JsonFieldType.BOOLEAN)
                                        .description("테마 : 일상")
                                        .optional(),
                                fieldWithPath("tag.romance").type(JsonFieldType.BOOLEAN)
                                        .description("테마 : 로맨스")
                                        .optional(),
                                fieldWithPath("tag.fantasy").type(JsonFieldType.BOOLEAN)
                                        .description("테마 : 판타지")
                                        .optional(),
                                fieldWithPath("tag.sf").type(JsonFieldType.BOOLEAN)
                                        .description("테마 : SF")
                                        .optional(),
                                fieldWithPath("hint.hintOne").type(JsonFieldType.STRING)
                                        .description("첫번째 힌트")
                                        .optional(),
                                fieldWithPath("hint.hintTwo").type(JsonFieldType.STRING)
                                        .description("두번째 힌트")
                                        .optional(),
                                fieldWithPath("hint.hintThree").type(JsonFieldType.STRING)
                                        .description("세번째 힌트")
                                        .optional(),
                                fieldWithPath("hint.hintFour").type(JsonFieldType.STRING)
                                        .description("네번째 힌트")
                                        .optional(),
                                fieldWithPath("hint.hintFive").type(JsonFieldType.STRING)
                                        .description("다섯번째 힌트")
                                        .optional()
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("상태 메세지"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("게시글 ID"),
                                fieldWithPath("data.authorName").type(JsonFieldType.STRING)
                                        .description("작성자 이름"),
                                fieldWithPath("data.title").type(JsonFieldType.STRING)
                                        .description("제목"),
                                fieldWithPath("data.content").type(JsonFieldType.STRING)
                                        .description("내용"),
                                fieldWithPath("data.correct").type(JsonFieldType.STRING)
                                        .description("정답"),
                                fieldWithPath("data.likeCount").type(JsonFieldType.NUMBER)
                                        .description("게시글 좋아요 수"),
                                fieldWithPath("data.viewCount").type(JsonFieldType.NUMBER)
                                        .description("게시글 조회수"),
                                fieldWithPath("data.createdAt").type(JsonFieldType.ARRAY)
                                        .description("게시글 작성날짜"),
                                fieldWithPath("data.updatedAt").type(JsonFieldType.ARRAY)
                                        .description("게시글 업데이트 날짜"),
                                fieldWithPath("data.contentImageUrl").type(JsonFieldType.STRING)
                                        .description("게시글 이미지"),
                                fieldWithPath("data.tag.horror").type(JsonFieldType.BOOLEAN)
                                        .description("테마 : 공포"),
                                fieldWithPath("data.tag.daily").type(JsonFieldType.BOOLEAN)
                                        .description("테마 : 일상"),
                                fieldWithPath("data.tag.romance").type(JsonFieldType.BOOLEAN)
                                        .description("테마 : 로맨스"),
                                fieldWithPath("data.tag.fantasy").type(JsonFieldType.BOOLEAN)
                                        .description("테마 : 판타지"),
                                fieldWithPath("data.tag.sf").type(JsonFieldType.BOOLEAN)
                                        .description("테마 : SF"),
                                fieldWithPath("data.hint.hintOne").type(JsonFieldType.STRING)
                                        .description("첫번째 힌트"),
                                fieldWithPath("data.hint.hintTwo").type(JsonFieldType.STRING)
                                        .description("두번째 힌트"),
                                fieldWithPath("data.hint.hintThree").type(JsonFieldType.STRING)
                                        .description("세번째 힌트"),
                                fieldWithPath("data.hint.hintFour").type(JsonFieldType.STRING)
                                        .description("네번째 힌트"),
                                fieldWithPath("data.hint.hintFive").type(JsonFieldType.STRING)
                                        .description("다섯번째 힌트")
                        )
                ));
    }

    @DisplayName("게시글 삭제 API")
    @Test
    void deleteBoard() throws Exception {
        // when // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.delete("/board/{id}", 1)
                                .header("Authorization", "JWT_AccessToken")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("board-delete",
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT_AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("id").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("상태 메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("data is empty object")
                        )
                ));
    }

    @DisplayName("게시글 페이지 조회 API")
    @Test
    void listSortedBoardsPerPage() throws Exception {
        // given
        Tag tag = Tag.builder()
                .horror(true)
                .daily(true)
                .romance(false)
                .fantasy(false)
                .sf(true)
                .build();

        Hint hint = Hint.builder()
                .hintOne("1")
                .hintTwo("2")
                .hintThree("3")
                .hintFour("4")
                .hintFive("5")
                .build();

        BoardResponse.Detail boardDetail = BoardResponse.Detail.builder()
                .id(1L)
                .authorName("테스트 계정")
                .title("테스트 제목")
                .content("테스트 내용")
                .correct("테스트 정답")
                .contentImageUrl("테스트 게시글 이미지")
                .likeCount(0)
                .viewCount(0)
                .createdAt(LocalDateTime.of(2023, 5, 13, 17, 56))
                .updatedAt(LocalDateTime.of(2023, 5, 13, 17, 56))
                .build();

        List<BoardResponse.Detail> boardDetailList = List.of(boardDetail);
        given(boardService.listSortedBoardsPerPage(anyInt(), anyInt(), anyString(), anyString()))
                .willReturn(
                        new PageImpl<>(boardDetailList, PageRequest.of(0, 10), 1)
                );

        // when // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/board/list")
                                .param("page", "1")
                                .param("limit", "10")
                                .param("filter", "createdAt")
                                .param("arrange", "asc")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("board-page",
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("찾는 페이지"),
                                parameterWithName("limit").description("한 페이지에 가져올 글의 수"),
                                parameterWithName("filter").description("정렬할 기준 : createdAt"),
                                parameterWithName("arrange").description("정렬할 방식 : asc / desc")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data.content[]").type(JsonFieldType.ARRAY)
                                        .description("게시글 목록"),
                                fieldWithPath("data.content[].id").type(JsonFieldType.NUMBER)
                                        .description("게시글 ID"),
                                fieldWithPath("data.content[].authorName").type(JsonFieldType.STRING)
                                        .description("게시글 작성자 이름"),
                                fieldWithPath("data.content[].title").type(JsonFieldType.STRING)
                                        .description("제목"),
                                fieldWithPath("data.content[].content").type(JsonFieldType.STRING)
                                        .description("내용"),
                                fieldWithPath("data.content[].correct").type(JsonFieldType.STRING)
                                        .description("정답"),
                                fieldWithPath("data.content[].contentImageUrl").type(JsonFieldType.STRING)
                                        .description("게시글 이미지"),
                                fieldWithPath("data.content[].likeCount").type(JsonFieldType.NUMBER)
                                        .description("게시글 좋아요 수"),
                                fieldWithPath("data.content[].viewCount").type(JsonFieldType.NUMBER)
                                        .description("게시글 조회 수"),
                                fieldWithPath("data.content[].createdAt").type(JsonFieldType.ARRAY)
                                        .description("게시글 생성 날짜"),
                                fieldWithPath("data.content[].updatedAt").type(JsonFieldType.ARRAY).
                                        description("게시글 수정 날짜"),
                                fieldWithPath("data.content[].tag.horror").type(JsonFieldType.BOOLEAN)
                                        .description("공포 태그 여부"),
                                fieldWithPath("data.content[].tag.daily").type(JsonFieldType.BOOLEAN)
                                        .description("일상 태그 여부"),
                                fieldWithPath("data.content[].tag.romance").type(JsonFieldType.BOOLEAN)
                                        .description("로맨스 태그 여부"),
                                fieldWithPath("data.content[].tag.fantasy").type(JsonFieldType.BOOLEAN)
                                        .description("판타지 태그 여부"),
                                fieldWithPath("data.content[].tag.sf").type(JsonFieldType.BOOLEAN)
                                        .description("SF 태그 여부"),
                                fieldWithPath("data.content[].hint.hintOne").type(JsonFieldType.STRING)
                                        .description("첫번째 힌트"),
                                fieldWithPath("data.content[].hint.hintTwo").type(JsonFieldType.STRING)
                                        .description("두번째 힌트"),
                                fieldWithPath("data.content[].hint.hintThree").type(JsonFieldType.STRING)
                                        .description("세번째 힌트"),
                                fieldWithPath("data.content[].hint.hintFour").type(JsonFieldType.STRING)
                                        .description("네번째 힌트"),
                                fieldWithPath("data.content[].hint.hintFive").type(JsonFieldType.STRING)
                                        .description("다섯번째 힌트"),
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
                        ))
                );
    }

    @DisplayName("좋아요 등록 및 취소 API")
    @Test
    void toggleLike() throws Exception {
        // given
        given(boardService.toggleLike(anyLong(),any(User.class)))
                .willReturn(BoardResponse.Likes.builder()
                        .id(1L)
                        .isLiked(true)
                        .build());

        // when // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/board/like")
                                .header("Authorization","JWT_AccessToken")
                                .param("id","1")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("board-toggleLike",
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT_AccessToken")
                        ),
                        requestParameters(
                                parameterWithName("id").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("좋아요 ID"),
                                fieldWithPath("data.isLiked").type(JsonFieldType.BOOLEAN)
                                        .description("좋아요 등록여부 : true = 좋아요 등록 / false = 좋아요 취소")
                        )
                        ));
    }
}
