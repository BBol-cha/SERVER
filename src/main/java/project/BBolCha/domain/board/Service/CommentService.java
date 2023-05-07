package project.BBolCha.domain.board.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.BBolCha.domain.board.Dto.CommentDto;
import project.BBolCha.domain.board.Entity.Comment;
import project.BBolCha.domain.board.Repository.BoardRepository;
import project.BBolCha.domain.board.Repository.CommentRepository;
import project.BBolCha.domain.user.Entity.User;
import project.BBolCha.domain.user.Repository.UserRepository;
import project.BBolCha.global.Model.Status;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static project.BBolCha.global.Model.Status.COMMENT_DELETE_TRUE;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    @Transactional
    public ResponseEntity<Comment> addComment(Long bid, CommentDto.Request request) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext()
                .getAuthentication()
                .getName()
        ).orElseThrow(NullPointerException::new);

        return new ResponseEntity<>(commentRepository.save(
                Comment.builder()
                        .bid(bid)
                        .uid(user.getId())
                        .name(user.getName())
                        .note(request.getNote()).build()
        ), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Page<Comment>> readComment(Long bid, Integer page) {
        Pageable pageable = PageRequest.of(page - 1, 10, DESC, "createAt");
        return new ResponseEntity<>(commentRepository.findByBid(bid, pageable), HttpStatus.OK);
    }

    public ResponseEntity<Status> deleteComment(Long id) {
        commentRepository.deleteById(id);
        return new ResponseEntity<>(COMMENT_DELETE_TRUE, HttpStatus.OK);
    }
}
