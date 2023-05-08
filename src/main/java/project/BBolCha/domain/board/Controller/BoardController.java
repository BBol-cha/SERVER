package project.BBolCha.domain.board.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import project.BBolCha.domain.board.Dto.BoardDto;
import project.BBolCha.domain.board.Service.BoardService;
import project.BBolCha.global.Model.CustomResponseEntity;

@RestController
@RequiredArgsConstructor
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

    // 게시글 삭제 (Soft Delete)
    @DeleteMapping("board/{id}")
    public CustomResponseEntity<Void> deleteBoard(
            @PathVariable Long id,
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        return CustomResponseEntity.success(boardService.deleteBoard(id, userDetails));
    }

    // 게시글 페이지 조회
    @GetMapping("board/list")
    public CustomResponseEntity<Page<BoardDto.DetailDto>> listSortedBoardsPerPage(
            @RequestParam Integer page, @RequestParam Integer limit, @RequestParam String filter, @RequestParam String arrange
    ) {
        return CustomResponseEntity.success(boardService.listSortedBoardsPerPage(page, limit, filter, arrange));
    }

    // 좋아요 등록 및 취소
    @PostMapping("board/like")
    public CustomResponseEntity<BoardDto.LikeDto> toggleLike(
            @RequestParam("id") Long id,
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        return CustomResponseEntity.success(boardService.toggleLike(id, userDetails));
    }
}
