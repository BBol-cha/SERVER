package project.BBolCha.domain.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.BBolCha.domain.board.dto.service.request.CommentServiceRequest;
import project.BBolCha.domain.board.dto.service.response.CommentResponse;
import project.BBolCha.domain.board.entity.Board;
import project.BBolCha.domain.board.entity.Comment;
import project.BBolCha.domain.board.repository.BoardRepository;
import project.BBolCha.domain.board.repository.CommentRepository;
import project.BBolCha.domain.user.entity.User;
import project.BBolCha.domain.user.repository.UserRepository;
import project.BBolCha.global.exception.CustomException;
import project.BBolCha.global.model.Result;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@Transactional(readOnly = true)
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
    public Void addComment(Long id, CommentServiceRequest.Add request, User user) {
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

    public Page<CommentResponse.Detail> fetchCommentsByPage(Long id, Integer page) {
        Board board = getBoard(id);
        Pageable pageable = PageRequest.of(page - 1, 10, DESC, "createdAt");
        Page<Comment> commentPage = commentRepository.findByBoard(board, pageable);

        return commentPage.map(CommentResponse.Detail::response);
    }

    @Transactional
    public Void deleteComment(Long id) {
        Comment comment = getComment(id);

        commentRepository.delete(comment);
        return null;
    }
}
