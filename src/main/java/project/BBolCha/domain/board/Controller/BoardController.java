package project.BBolCha.domain.board.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.BBolCha.domain.board.Dto.BoardDto;
import project.BBolCha.domain.board.Dto.CommentDto;
import project.BBolCha.domain.board.Entity.Board;
import project.BBolCha.domain.board.Entity.Comment;
import project.BBolCha.domain.board.Service.BoardService;
import project.BBolCha.global.Model.Status;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;

    // 게시글 생성
    @PostMapping("board")
    public ResponseEntity<BoardDto.Request> create(
            @RequestBody BoardDto.Request request
    ) {
        return boardService.create(request);
    }

    // 게시글 페이지 조회
    @GetMapping("board/list")
    public ResponseEntity<Page<Board>> read(
            @RequestParam Integer page, @RequestParam Integer limit
    ) {
        return boardService.read(page, limit);
    }

    // 게시글 상세 조회
    @GetMapping("board/list/{id}")
    public ResponseEntity<Board> readDetail(
            @PathVariable("id") Long id
    ){
        return boardService.readDetail(id);
    }

    // 게시글 상세 댓글 조회
    @GetMapping("board/list/comment/{bid}")
    public ResponseEntity<List<Comment>> readComment(
            @PathVariable("bid") Long bid
    ){
        return boardService.readComment(bid);
    }

    // 게시판 이미지 업로드
    @PostMapping("board/image")
    public ResponseEntity<BoardDto.boardImage> putImage(
            @RequestParam("file") MultipartFile multipartFile
    ) throws IOException {
        return boardService.putImage(multipartFile);
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
            ){
        return boardService.addComment(bid, request);
    }

}
