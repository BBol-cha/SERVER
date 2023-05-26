package project.BBolCha.domain.board.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import project.BBolCha.domain.board.dto.service.request.BoardServiceRequest;
import project.BBolCha.domain.board.dto.service.request.HintServiceRequest;
import project.BBolCha.domain.board.dto.service.request.TagServiceRequest;
import project.BBolCha.domain.board.dto.service.response.BoardResponse;
import project.BBolCha.domain.board.entity.Board;
import project.BBolCha.domain.board.entity.Hint;
import project.BBolCha.domain.board.entity.Like;
import project.BBolCha.domain.board.entity.Tag;
import project.BBolCha.domain.board.repository.BoardRepository;
import project.BBolCha.domain.board.repository.LikeRepository;
import project.BBolCha.domain.user.entity.Authority;
import project.BBolCha.domain.user.entity.User;
import project.BBolCha.domain.user.repository.UserRepository;
import project.BBolCha.global.exception.CustomException;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static project.BBolCha.global.model.Result.NOT_MY_POST;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @DisplayName("유저가 새로운 게시글을 등록한다.")
    @Test
    void createBoard() {
        // given
        User user = saveAndRetrieveUser();

        TagServiceRequest.Save tagRequest = tagRequestToEntity(true, true, false, false, true);
        HintServiceRequest.Save hintRequest = hintRequestToEntity("1", "2", "3", "4", "5");

        BoardServiceRequest.Save request = BoardServiceRequest.Save.builder()
                .title("테스트")
                .content("내용 테스트")
                .correct("정답 테스트")
                .contentImageUrl("test.png")
                .tag(tagRequest)
                .hint(hintRequest)
                .build();

        // when
        BoardResponse.Save response = boardService.createBoard(request, user);

        // then
        assertThat(response)
                .extracting("authorName", "title", "content", "correct", "contentImageUrl")
                .contains("테스트 계정", "테스트", "내용 테스트", "정답 테스트", "test.png");

        assertThat(response.getTag())
                .extracting("horror", "daily", "romance", "fantasy", "sf")
                .contains(true, true, false, false, true);

        assertThat(response.getHint())
                .extracting("hintOne", "hintTwo", "hintThree", "hintFour", "hintFive")
                .contains("1", "2", "3", "4", "5");
    }

    @DisplayName("게시글의 상세 내용을 조회한다")
    @Test
    void readBoardDetail() {
        // given
        User user = saveAndRetrieveUser();
        Board board = saveAndRetrieveBoard(user);

        // when
        BoardResponse.Detail response = boardService.findBoard(board.getId());

        // then
        assertThat(response)
                .extracting("authorName", "title", "content", "correct", "contentImageUrl", "likeCount", "viewCount")
                .contains("테스트 계정", "test", "testContent", "testCorrect", "test.png", 0L, 5);

        assertThat(response)
                .extracting("horror", "daily", "romance", "fantasy", "sf")
                .contains(true, true, false, false, true);

        assertThat(response)
                .extracting("hintOne", "hintTwo", "hintThree", "hintFour", "hintFive")
                .contains("1", "2", "3", "4", "5");
    }

    @DisplayName("유저가 자신이 작성한 게시글을 제목, 내용, 정답, 이미지, 태그, 힌트를 수정한다.")
    @Test
    void updateBoard() {
        // given
        User user = saveAndRetrieveUser();
        Board board = saveAndRetrieveBoard(user);

        TagServiceRequest.Save tagRequest = tagRequestToEntity(false, false, false, false, true);
        HintServiceRequest.Save hintRequest = hintRequestToEntity("6", "7", "8", "9", "10");

        BoardServiceRequest.Update request = BoardServiceRequest.Update.builder()
                .title("titleUpdate")
                .content("contentUpdate")
                .correct("correctUpdate")
                .contentImageUrl("imageUpdate.png")
                .tag(tagRequest)
                .hint(hintRequest)
                .build();

        // when
        BoardResponse.Detail response = boardService.updateBoard(board.getId(), request, user);

        // then
        assertThat(response)
                .extracting("authorName", "title", "content", "correct", "contentImageUrl", "likeCount", "viewCount")
                .contains("테스트 계정", "titleUpdate", "contentUpdate", "correctUpdate", "imageUpdate.png", 0L, 5);

        assertThat(response)
                .extracting("horror", "daily", "romance", "fantasy", "sf")
                .contains(false, false, false, false, true);

        assertThat(response)
                .extracting("hintOne", "hintTwo", "hintThree", "hintFour", "hintFive")
                .contains("6", "7", "8", "9", "10");
    }

    @DisplayName("자신이 작성한 글을 삭제한다.")
    @Test
    void deleteBoard() {
        // given
        User user = saveAndRetrieveUser();
        Board board = saveAndRetrieveBoard(user);

        // when
        boardService.deleteBoard(board.getId(), user);

        // then
        Optional<Board> boardOptional = boardRepository.findById(board.getId());
        assertThat(boardOptional.isEmpty()).isTrue();
    }

    @DisplayName("자신이 작성하지 않은 글을 삭제하려 할때 CustomException 발생한다.")
    @Test
    void deleteBoardWithNotMyPost() {
        // given
        User postUser = saveAndRetrieveUser();
        User notPostUser = saveAndRetrieveUser();
        Board board = saveAndRetrieveBoard(postUser);

        // when // then
        assertThatThrownBy(() -> boardService.deleteBoard(board.getId(), notPostUser))
                .isInstanceOf(CustomException.class)
                .extracting("result").isEqualTo(NOT_MY_POST);
    }

    @DisplayName("게시글이 작성된 날짜 순으로 오름차순으로 정렬하여서 첫번째 페이지의 10개 항목을 가져온다.")
    @Test
    void shouldReturnFirstPageWithTenItemsOrderedByCreatedDate() throws JsonProcessingException {
        // given
        User user = saveAndRetrieveUser();

        saveAndRetrieveBoard(user, "1_content");
        saveAndRetrieveBoard(user, "2_content");
        saveAndRetrieveBoard(user, "3_content");
        saveAndRetrieveBoard(user, "4_content");

        // when
        Page<BoardResponse.Detail> response = boardService.listSortedBoardsPerPage(1, 10, "createdAt", "ASC");

        // then
        assertThat(response)
                .hasSize(4)
                .extracting("title", "content", "authorName")
                .containsExactlyInAnyOrder(
                        tuple("test", "1_content", "테스트 계정"),
                        tuple("test", "2_content", "테스트 계정"),
                        tuple("test", "3_content", "테스트 계정"),
                        tuple("test", "4_content", "테스트 계정")
                );

        assertThat(response.getTotalPages()).isEqualTo(1);
        assertThat(response.getTotalElements()).isEqualTo(4L);
    }

    @DisplayName("게시글이 작성된 날짜 순으로 내림차순으로 정렬하여서 두번째 페이지의 5개 항목을 가져온다.")
    @Test
    void shouldReturnSecondPageWithFiveItemsOrderedByCreatedDate() {
        // given
        User user = saveAndRetrieveUser();

        saveAndRetrieveBoard(user, "1_content");
        saveAndRetrieveBoard(user, "2_content");
        saveAndRetrieveBoard(user, "3_content");
        saveAndRetrieveBoard(user, "4_content");
        saveAndRetrieveBoard(user, "5_content");
        saveAndRetrieveBoard(user, "6_content");
        saveAndRetrieveBoard(user, "7_content");

        // when
        Page<BoardResponse.Detail> response = boardService.listSortedBoardsPerPage(2, 5, "createdAt", "DESC");

        // then
        assertThat(response)
                .hasSize(2)
                .extracting("title", "content", "authorName")
                .containsExactlyInAnyOrder(
                        tuple("test", "6_content", "테스트 계정"),
                        tuple("test", "7_content", "테스트 계정")
                );

        assertThat(response.getTotalPages()).isEqualTo(2);
        assertThat(response.getTotalElements()).isEqualTo(7L);
    }

    @DisplayName("내가 좋아요를 남기지 않은 게시글에 좋아요를 등록한다.")
    @Test
    void likeAddNotLikedBoardByUser() {
        // given
        User user = saveAndRetrieveUser();
        Board board = saveAndRetrieveBoard(user);

        // when
        BoardResponse.Likes response = boardService.toggleLike(board.getId(), user);

        // then
        assertThat(response.getIsLiked()).isTrue();

        Optional<Like> likeOptional = likeRepository.findById(response.getId());
        assertThat(likeOptional.isPresent()).isTrue();
    }

    @DisplayName("내가 좋아요를 남긴 게시글에 좋아요를 취소한다.")
    @Test
    void likeCancelLikedBoardByUser() {
        // given
        User user = saveAndRetrieveUser();
        Board board = saveAndRetrieveBoard(user);

        Like like = Like.builder()
                .id(1L)
                .user(user)
                .board(board)
                .build();
        likeRepository.save(like);

        // when
        BoardResponse.Likes response = boardService.toggleLike(board.getId(), user);

        // then
        assertThat(response.getIsLiked()).isFalse();

        Optional<Like> likeOptional = likeRepository.findById(response.getId());

        assertThat(likeOptional.isPresent()).isFalse();
    }

    // method
    private void saveAndRetrieveBoard(User user, String content) {
        List<Like> likes = new ArrayList<>();

        Board board = Board.builder()
                .user(user)
                .title("test")
                .content(content)
                .correct("testCorrect")
                .contentImageUrl("test.png")
                .like(likes)
                .viewCount(5)
                .build();

        Tag tag = Tag.builder()
                .board(board)
                .horror(true)
                .daily(true)
                .romance(false)
                .fantasy(false)
                .sf(true)
                .build();

        Hint hint = Hint.builder()
                .board(board)
                .hintOne("1")
                .hintTwo("2")
                .hintThree("3")
                .hintFour("4")
                .hintFive("5")
                .build();


        board.saveTagAndHint(tag, hint);
        boardRepository.save(board);
    }

    private Board saveAndRetrieveBoard(User user) {
        List<Like> likes = new ArrayList<>();

        Board board = Board.builder()
                .user(user)
                .title("test")
                .content("testContent")
                .correct("testCorrect")
                .contentImageUrl("test.png")
                .like(likes)
                .viewCount(5)
                .build();

        Tag tag = Tag.builder()
                .board(board)
                .horror(true)
                .daily(true)
                .romance(false)
                .fantasy(false)
                .sf(true)
                .build();

        Hint hint = Hint.builder()
                .board(board)
                .hintOne("1")
                .hintTwo("2")
                .hintThree("3")
                .hintFour("4")
                .hintFive("5")
                .build();


        board.saveTagAndHint(tag, hint);

        return boardRepository.save(board);
    }

    private User saveAndRetrieveUser() {
        User user = User.builder()
                .name("테스트 계정")
                .email("test@test.com")
                .password(passwordEncoder.encode("abc123!"))
                .profileImageUrl("test.png")
                .authorities(getAuthorities())
                .build();

        return userRepository.save(user);
    }

    private Set<Authority> getAuthorities() {
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();
        return Collections.singleton(authority);
    }

    private static TagServiceRequest.Save tagRequestToEntity(
            boolean horror, boolean daily, boolean romance,
            boolean fantasy, boolean sf
    ) {
        return TagServiceRequest.Save.builder()
                .horror(horror)
                .daily(daily)
                .romance(romance)
                .fantasy(fantasy)
                .sf(sf)
                .build();
    }

    private static HintServiceRequest.Save hintRequestToEntity(
            String hintOne, String hintTwo, String hintThree,
            String hintFour, String hintFive
    ) {
        return HintServiceRequest.Save.builder()
                .hintOne(hintOne)
                .hintTwo(hintTwo)
                .hintThree(hintThree)
                .hintFour(hintFour)
                .hintFive(hintFive)
                .build();
    }
}