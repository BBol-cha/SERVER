package project.BBolCha.domain.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import project.BBolCha.domain.board.dto.BoardDto;
import project.BBolCha.domain.board.dto.controller.request.BoardRequest;
import project.BBolCha.domain.board.dto.service.response.BoardResponse;
import project.BBolCha.domain.board.service.BoardService;
import project.BBolCha.domain.user.entity.User;
import project.BBolCha.global.model.CustomResponseEntity;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    // 게시글 생성
    @PostMapping("board")
    public CustomResponseEntity<BoardResponse.Save> createBoard(
            @Valid @RequestBody BoardRequest.Save request,
            @AuthenticationPrincipal User user
    ) {
        return CustomResponseEntity.success(boardService.createBoard(request.toServiceRequest(), user));
    }

    // 게시글 상세 조회
    @GetMapping("board/list/{id}")
    public CustomResponseEntity<BoardResponse.Detail> findBoard(
            @PathVariable("id") Long id
    ) {
        return CustomResponseEntity.success(boardService.findBoard(id));
    }

    // 게시글 수정
    @PatchMapping("board/{id}")
    public CustomResponseEntity<BoardResponse.Detail> updateBoard(
            @PathVariable Long id,
            @Valid @RequestBody BoardRequest.Update request,
            @AuthenticationPrincipal User user
    ) {
        return CustomResponseEntity.success(boardService.updateBoard(id, request.toServiceRequest(), user));
    }

    // 게시글 삭제 (Soft Delete)
    @DeleteMapping("board/{id}")
    public CustomResponseEntity<Void> deleteBoard(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        return CustomResponseEntity.success(boardService.deleteBoard(id, user));
    }

    // 게시글 페이지 조회
    @GetMapping("board/list")
    public CustomResponseEntity<Page<BoardResponse.Detail>> listSortedBoardsPerPage(
            @RequestParam Integer page, @RequestParam Integer limit, @RequestParam String filter, @RequestParam String arrange
    ) {
        return CustomResponseEntity.success(boardService.listSortedBoardsPerPage(page, limit, filter, arrange));
    }

    // 좋아요 등록 및 취소
    @PostMapping("board/like")
    public CustomResponseEntity<BoardDto.LikeDto> toggleLike(
            @RequestParam("id") Long id,
            @AuthenticationPrincipal User user
    ) {
        return CustomResponseEntity.success(boardService.toggleLike(id, user));
    }
}
