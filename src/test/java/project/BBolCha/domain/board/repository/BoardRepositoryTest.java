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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class BoardRepositoryTest {

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