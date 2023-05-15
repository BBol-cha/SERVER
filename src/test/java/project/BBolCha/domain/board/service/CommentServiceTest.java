package project.BBolCha.domain.board.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import project.BBolCha.domain.board.dto.service.request.BoardServiceRequest;
import project.BBolCha.domain.board.dto.service.request.CommentServiceRequest;
import project.BBolCha.domain.board.entity.*;
import project.BBolCha.domain.board.repository.BoardRepository;
import project.BBolCha.domain.board.repository.CommentRepository;
import project.BBolCha.domain.board.repository.LikeRepository;
import project.BBolCha.domain.user.entity.Authority;
import project.BBolCha.domain.user.entity.User;
import project.BBolCha.domain.user.repository.UserRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CommentServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @DisplayName("유저가 게시글에 댓글을 등록한다.")
    @Test
    void test() {
        // given
        User user = saveAndRetrieveUser();
        Board board = saveAndRetrieveBoard(user);

        CommentServiceRequest.Add request = CommentServiceRequest.Add.builder()
                .note("재밌어요")
                .build();

        // when
        commentService.addComment(board.getId(), request, user);

        // then
        Optional<Comment> commentOptional = commentRepository.findById(1L);

        assertThat(commentOptional.isPresent()).isTrue();
        assertThat(commentOptional.get())
                .extracting("id","user","board")
                .contains(1L,1L,1L);
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
        Set<Authority> authoritySet = new HashSet<>();
        User user = User.builder()
                .name("테스트 계정")
                .email("test@test.com")
                .password("abc123")
                .profileImageUrl("test.png")
                .authorities(authoritySet)
                .build();

        return userRepository.save(user);
    }
}