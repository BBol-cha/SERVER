package project.BBolCha.domain.board.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import project.BBolCha.domain.user.entity.Authority;
import project.BBolCha.domain.user.entity.User;
import project.BBolCha.domain.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

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
                .contains("테스트 계정", "test", "testContent", "testCorrect", "test.png", 0, 5);

        assertThat(response.getTag())
                .extracting("horror", "daily", "romance", "fantasy", "sf")
                .contains(true, true, false, false, true);

        assertThat(response.getHint())
                .extracting("hintOne", "hintTwo", "hintThree", "hintFour", "hintFive")
                .contains("1", "2", "3", "4", "5");
    }

    @DisplayName("유저가 자신이 작성한 게시글을 제목, 내용, 정답, 이미지, 태그, 힌트를 수정한다.")
    @Test
    void test() {
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
                .contains("테스트 계정", "titleUpdate", "contentUpdate", "correctUpdate", "imageUpdate.png", 0, 5);

        assertThat(response.getTag())
                .extracting("horror", "daily", "romance", "fantasy", "sf")
                .contains(false, false, false, false, true);

        assertThat(response.getHint())
                .extracting("hintOne", "hintTwo", "hintThree", "hintFour", "hintFive")
                .contains("6", "7", "8", "9", "10");
    }


    // method
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