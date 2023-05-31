package project.BBolCha.domain.board.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import project.BBolCha.domain.board.dto.service.response.BoardResponse;
import project.BBolCha.domain.board.entity.Board;
import project.BBolCha.domain.board.entity.Hint;
import project.BBolCha.domain.board.entity.Like;
import project.BBolCha.domain.board.entity.Tag;
import project.BBolCha.domain.user.entity.Authority;
import project.BBolCha.domain.user.entity.User;
import project.BBolCha.domain.user.repository.UserRepository;
import project.BBolCha.global.exception.CustomException;
import project.BBolCha.global.model.Result;

import javax.persistence.EntityManager;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class BoardRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;

    @DisplayName("게시글의 ID를 통해 단건을 조회한다.")
    @Test
    void findById() {
        // given
        User user = saveAndRetrieveUser();
        Board savedBoard = saveAndRetrieveBoard(user);

        em.flush();
        em.clear();

        System.out.println("===============================");
        // when
        Board board = boardRepository.findById(savedBoard.getId())
                .orElseThrow(
                        () -> new CustomException(Result.NOT_FOUND_BOARD)
                );

        // then
        assertThat(board)
                .extracting("title", "content", "correct", "contentImageUrl", "viewCount")
                .contains("test", "testContent", "testCorrect", "test.png", 5);

        assertThat(board.getTag())
                .extracting("horror", "daily", "romance", "fantasy", "sf")
                .contains(true, true, false, false, true);

        assertThat(board.getHint())
                .extracting("hintOne", "hintTwo", "hintThree", "hintFour", "hintFive")
                .contains("1", "2", "3", "4", "5");

        assertThat(board.getLike().size()).isZero();
    }

    @DisplayName("게시글의 ID를 통해 단건을 fetch join을 통해 조회한다.")
    @Test
    void findByIdFetch() {
        // given
        User user = saveAndRetrieveUser();
        Board savedBoard = saveAndRetrieveBoard(user);

        Like like1 = Like.builder()
                .board(savedBoard)
                .user(user)
                .build();

        Like like2 = Like.builder()
                .board(savedBoard)
                .user(user)
                .build();

        Like like3 = Like.builder()
                .board(savedBoard)
                .user(user)
                .build();

        Like like4 = Like.builder()
                .board(savedBoard)
                .user(user)
                .build();

        likeRepository.saveAll(List.of(like1, like2, like3, like4));

        em.flush();
        em.clear();

        System.out.println("===============================");
        // when
        Board board = boardRepository.getBoardDetail(savedBoard.getId())
                .orElseThrow(
                        () -> new CustomException(Result.NOT_FOUND_BOARD)
                );

        // then
        assertThat(board)
                .extracting("user.name", "title", "content", "correct", "contentImageUrl", "viewCount")
                .contains("테스트 계정", "test", "testContent", "testCorrect", "test.png", 5);

        assertThat(board.getTag())
                .extracting("horror", "daily", "romance", "fantasy", "sf")
                .contains(true, true, false, false, true);

        assertThat(board.getHint())
                .extracting("hintOne", "hintTwo", "hintThree", "hintFour", "hintFive")
                .contains("1", "2", "3", "4", "5");
    }

    // method
    private Board saveAndRetrieveBoard(User user) {
        Board board = Board.builder()
                .user(user)
                .title("test")
                .content("testContent")
                .correct("testCorrect")
                .contentImageUrl("test.png")
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
                .password("abc123!")
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
}