package project.BBolCha.domain.board.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.BBolCha.domain.board.Dto.BoardDto;
import project.BBolCha.domain.board.Entity.Board;
import project.BBolCha.domain.board.Service.BoardService;
import project.BBolCha.global.Model.Status;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;

    @PostMapping("board")
    public ResponseEntity<Board> create(
            @RequestBody BoardDto.Request request
    ){
        return boardService.create(request);
    }

    @GetMapping("board/list")
    public ResponseEntity<List<Board>> read(){
        return boardService.read();
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

}
