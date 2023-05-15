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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import project.BBolCha.domain.board.dto.BoardDto;
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
        saveSecurityContextHolderAndGetAuthentication();

        TagServiceRequest.Save tagRequest = TagServiceRequest.Save.builder()
                .horror(true)
                .daily(true)
                .romance(false)
                .fantasy(false)
                .sf(true)
                .build();

        HintServiceRequest.Save hintRequest = HintServiceRequest.Save.builder()
                .hintOne("1")
                .hintTwo("2")
                .hintThree("3")
                .hintFour("4")
                .hintFive("5")
                .build();

        BoardServiceRequest.Save request = BoardServiceRequest.Save.builder()
                .authorName("테스트 계정")
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


        board.saveTagAndHint(tag,hint);

        Board saveBoard = boardRepository.save(board);

        // when
        BoardResponse.Detail response = boardService.findBoard(saveBoard.getId());

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

    // method
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

    private Authentication saveSecurityContextHolderAndGetAuthentication() {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken("test@test.com", "abc123!");
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }

    private Set<Authority> getAuthorities() {
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();
        return Collections.singleton(authority);
    }
}