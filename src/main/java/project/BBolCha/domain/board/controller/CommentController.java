package project.BBolCha.domain.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.BBolCha.domain.board.dto.controller.request.CommentRequest;
import project.BBolCha.domain.board.dto.service.response.CommentResponse;
import project.BBolCha.domain.board.service.CommentService;
import project.BBolCha.domain.user.entity.User;
import project.BBolCha.global.model.CustomResponseEntity;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 게시글 댓글 등록
    @PostMapping("board/comment")
    public CustomResponseEntity<Void> addComment(
            @RequestParam Long id,
            @Valid @RequestBody CommentRequest.Add request,
            @AuthenticationPrincipal User user
    ) {
        return CustomResponseEntity.success(commentService.addComment(id, request.toServiceRequest(), user));
    }

    // 게시글 댓글 조회
    @GetMapping("board/list/comment")
    public CustomResponseEntity<Page<CommentResponse.Detail>> fetchCommentsByPage(
            @RequestParam Long id, @RequestParam Integer page
    ) {
        return CustomResponseEntity.success(commentService.fetchCommentsByPage(id, page));
    }

    // 게시글 댓글 삭제
    @DeleteMapping("board/comment")
    public CustomResponseEntity<Void> deleteComment(
            @RequestParam Long id,
            @AuthenticationPrincipal User user
    ) {
        return CustomResponseEntity.success(commentService.deleteComment(id, user));
    }
}
