package project.BBolCha.domain.board.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.BBolCha.domain.board.Dto.BoardDto;
import project.BBolCha.domain.board.Entity.Board;
import project.BBolCha.domain.board.Repository.BoardRepository;
import project.BBolCha.domain.user.Entity.User;
import project.BBolCha.domain.user.Repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public ResponseEntity<Board> create(BoardDto.Request request) {
        User user = userRepository.findByEmail(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName()
        ).orElseThrow(
                NullPointerException::new
        );

        return new ResponseEntity<>(boardRepository.save(
                Board.builder()
                        .userId(user.getId())
                        .title(request.getTitle())
                        .subTitle(request.getSubTitle())
                        .name(user.getName())
                        .note(request.getNote())
                        .views(0)
                        .bimg(null)
                        .build()
        ), HttpStatus.CREATED);

    }
}
