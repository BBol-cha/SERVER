package project.BBolCha.domain.board.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import project.BBolCha.domain.board.Dto.BoardDto;
import project.BBolCha.domain.board.Dto.CommentDto;
import project.BBolCha.domain.board.Entity.Board;
import project.BBolCha.domain.board.Entity.Comment;
import project.BBolCha.domain.board.Service.BoardService;
import project.BBolCha.global.Model.CustomResponseEntity;
import project.BBolCha.global.Model.Status;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;

    // 게시글 생성
    @PostMapping("board")
    public CustomResponseEntity<BoardDto.SaveDto> createBoard(
            @RequestBody BoardDto.SaveDto request,
            @AuthenticationPrincipal UserDetails userDetails
            ) {
        return CustomResponseEntity.success(boardService.createBoard(request, userDetails));
    }

    // 게시글 상세 조회
    @GetMapping("board/list/{id}")
    public CustomResponseEntity<BoardDto.DetailDto> findBoard(
            @PathVariable("id") Long id
    ) {
        return CustomResponseEntity.success(boardService.findBoard(id));
    }

    // 게시글 수정
    @PatchMapping("board/{id}")
    public CustomResponseEntity<BoardDto.DetailDto> updateBoard(
            @PathVariable Long id, @RequestBody BoardDto.UpdateDto request
    ) {
        return CustomResponseEntity.success(boardService.updateBoard(id, request));
    }

    // 게시글 삭제
    // SoftDelete 또는 HardDelete 리팩터링 재필요
    @DeleteMapping("board/{id}")
    public CustomResponseEntity<Void> deleteBoard(
            @PathVariable Long id,
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        return CustomResponseEntity.success(boardService.deleteBoard(id, userDetails));
    }

    // 게시글 페이지 조회
    @GetMapping("board/list")
    public CustomResponseEntity<Page<BoardDto.DetailDto>> read(
            @RequestParam Integer page, @RequestParam Integer limit, @RequestParam String filter, @RequestParam String arrange
    ) {
        return CustomResponseEntity.success(boardService.read(page, limit, filter, arrange));
    }

    // 게시글 댓글 조회
    @GetMapping("board/list/comment")
    public ResponseEntity<Page<Comment>> readComment(
            @RequestParam("bid") Long bid, @RequestParam Integer page
    ) {
        return boardService.readComment(bid, page);
    }

    // 이미지 업로드 해놓고 게시판 글작성 취소시 업로드 됐던 이미지 삭제
    @DeleteMapping("board/image")
    public ResponseEntity<Status> deleteImage(
            @RequestBody BoardDto.boardImage request
    ) throws IOException {
        return boardService.deleteImage(request);
    }

    // 게시글 댓글 작성
    @PostMapping("board/comment")
    public ResponseEntity<Comment> addComment(
            @RequestParam Long bid, @RequestBody CommentDto.Request request
    ) {
        return boardService.addComment(bid, request);
    }

    // 게시글 댓글 삭제
    @DeleteMapping("board/comment")
    public ResponseEntity<Status> deleteComment(
            @RequestParam Long id
    ) {
        return boardService.deleteComment(id);
    }

    @PostMapping("board/like")
    public ResponseEntity<BoardDto.Like> addLike(
            @RequestParam("bid") Long bid
    ) {
        return boardService.addLike(bid);
    }

}
