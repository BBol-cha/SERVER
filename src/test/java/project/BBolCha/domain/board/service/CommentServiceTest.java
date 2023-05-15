package project.BBolCha.domain.board.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import project.BBolCha.domain.board.dto.service.request.BoardServiceRequest;
import project.BBolCha.domain.board.dto.service.request.CommentServiceRequest;
import project.BBolCha.domain.board.dto.service.response.CommentResponse;
import project.BBolCha.domain.board.entity.*;
import project.BBolCha.domain.board.repository.BoardRepository;
import project.BBolCha.domain.board.repository.CommentRepository;
import project.BBolCha.domain.board.repository.LikeRepository;
import project.BBolCha.domain.user.entity.Authority;
import project.BBolCha.domain.user.entity.User;
import project.BBolCha.domain.user.repository.UserRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
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
    void addComment() {
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
                .extracting("id", "user", "board", "note")
                .contains(1L, 1L, 1L, "재밌어요");
    }

    @DisplayName("게시글에 댓글을 페이지 조회한다.")
    @Test
    void test() {
        // given
        User user = saveAndRetrieveUser();
        Board board = saveAndRetrieveBoard(user);

        saveAndRetrieveComment(user, board, "1");
        saveAndRetrieveComment(user, board, "2");
        saveAndRetrieveComment(user, board, "3");
        saveAndRetrieveComment(user, board, "4");

        // when
        Page<CommentResponse.Detail> response = commentService.fetchCommentsByPage(1L, 1);

        // then
        assertThat(response)
                .hasSize(4)
                .extracting("userName", "userProfileImageUrl", "note")
                .containsExactlyInAnyOrder(
                        tuple("테스트 계정","test.png","1"),
                        tuple("테스트 계정","test.png","2"),
                        tuple("테스트 계정","test.png","3"),
                        tuple("테스트 계정","test.png","4")
                );

        assertThat(response.getTotalPages()).isEqualTo(1);
        assertThat(response.getTotalElements()).isEqualTo(4L);
    }

    private Comment saveAndRetrieveComment(User user, Board board, String note) {
        Comment comment = Comment.builder()
                .user(user)
                .board(board)
                .note(note)
                .build();
        return commentRepository.save(comment);
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