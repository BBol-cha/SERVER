package project.BBolCha.domain.board.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import project.BBolCha.domain.board.Dto.CommentDto;
import project.BBolCha.domain.board.Service.CommentService;
import project.BBolCha.global.Model.CustomResponseEntity;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 게시글 댓글 등록
    @PostMapping("board/comment")
    public CustomResponseEntity<Void> addComment(
            @RequestParam Long id,
            @RequestBody CommentDto.AddDto request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return CustomResponseEntity.success(commentService.addComment(id, request, userDetails));
    }

    // 게시글 댓글 조회
    @GetMapping("board/list/comment")
    public CustomResponseEntity<Page<CommentDto.DetailDto>> fetchCommentsByPage(
            @RequestParam("id") Long id, @RequestParam Integer page
    ) {
        return CustomResponseEntity.success(commentService.fetchCommentsByPage(id, page));
    }

    // 게시글 댓글 삭제
    @DeleteMapping("board/comment")
    public CustomResponseEntity<Void> deleteComment(
            @RequestParam Long id
    ) {
        return CustomResponseEntity.success(commentService.deleteComment(id));
    }
}
