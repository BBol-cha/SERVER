package project.BBolCha.domain.board.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import project.BBolCha.domain.board.Dto.BoardDto;
import project.BBolCha.domain.board.Entity.Board;
import project.BBolCha.domain.board.Service.BoardService;

import java.io.IOException;

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

}
