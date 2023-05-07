package project.BBolCha.domain.board.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.BBolCha.domain.board.Dto.CommentDto;
import project.BBolCha.domain.board.Entity.Board;
import project.BBolCha.domain.board.Entity.Comment;
import project.BBolCha.domain.board.Repository.BoardRepository;
import project.BBolCha.domain.board.Repository.CommentRepository;
import project.BBolCha.domain.user.Entity.User;
import project.BBolCha.domain.user.Repository.UserRepository;
import project.BBolCha.global.Exception.CustomException;
import project.BBolCha.global.Model.Result;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final UserRepository userRepository;

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    private Board getBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(
                () -> new CustomException(Result.NOT_FOUND_BOARD)
        );
    }

    private User getUser(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(Result.NOT_FOUND_USER)
        );
    }

    private Comment getComment(Long id) {
        return commentRepository.findById(id).orElseThrow(
                () -> new CustomException(Result.NOT_FOUND_COMMENT)
        );
    }

    @Transactional
    public Void addComment(Long id, CommentDto.AddDto request, UserDetails userDetails) {
        User user = getUser(userDetails);
        Board board = getBoard(id);

        commentRepository.save(
                Comment.builder()
                        .board(board)
                        .user(user)
                        .note(request.getNote())
                        .build()
        );
        return null;
    }

    @Transactional
    public Page<CommentDto.DetailDto> fetchCommentsByPage(Long id, Integer page) {
        Board board = getBoard(id);
        Pageable pageable = PageRequest.of(page - 1, 10, DESC, "createAt");
        Page<Comment> commentPage = commentRepository.findByBoard(board, pageable);
        return commentPage.map(CommentDto.DetailDto::response);
    }

    public Void deleteComment(Long id) {
        Comment comment = getComment(id);

        commentRepository.delete(comment);
        return null;
    }
}
