package project.BBolCha.domain.board.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.BBolCha.domain.board.Dto.CommentDto;
import project.BBolCha.domain.board.Entity.Comment;
import project.BBolCha.domain.board.Service.CommentService;
import project.BBolCha.global.Model.Status;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 게시글 댓글 조회
    @GetMapping("board/list/comment")
    public ResponseEntity<Page<Comment>> readComment(
            @RequestParam("bid") Long bid, @RequestParam Integer page
    ) {
        return commentService.readComment(bid, page);
    }

    // 게시글 댓글 작성
    @PostMapping("board/comment")
    public ResponseEntity<Comment> addComment(
            @RequestParam Long bid, @RequestBody CommentDto.Request request
    ) {
        return commentService.addComment(bid, request);
    }

    // 게시글 댓글 삭제
    @DeleteMapping("board/comment")
    public ResponseEntity<Status> deleteComment(
            @RequestParam Long id
    ) {
        return commentService.deleteComment(id);
    }
}
