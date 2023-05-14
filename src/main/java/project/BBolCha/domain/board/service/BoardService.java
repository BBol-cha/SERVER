package project.BBolCha.domain.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.BBolCha.domain.board.dto.BoardDto;
import project.BBolCha.domain.board.dto.controller.request.BoardRequest;
import project.BBolCha.domain.board.dto.service.request.BoardServiceRequest;
import project.BBolCha.domain.board.dto.service.response.BoardResponse;
import project.BBolCha.domain.board.entity.Board;
import project.BBolCha.domain.board.entity.Hint;
import project.BBolCha.domain.board.entity.Like;
import project.BBolCha.domain.board.entity.Tag;
import project.BBolCha.domain.board.repository.BoardRepository;
import project.BBolCha.domain.board.repository.LikeRepository;
import project.BBolCha.domain.user.entity.User;
import project.BBolCha.domain.user.repository.UserRepository;
import project.BBolCha.global.config.jwt.SecurityUtil;
import project.BBolCha.global.exception.CustomException;
import project.BBolCha.global.model.Result;

import java.util.Optional;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    private Board getBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(
                () -> new CustomException(Result.NOT_FOUND_BOARD)
        );
    }

    @Transactional
    public BoardResponse.Save createBoard(BoardServiceRequest.Save request, User user) {
        Board board = boardRepository.save(
                Board.builder()
                        .user(user)
                        .title(request.getTitle())
                        .content(request.getContent())
                        .correct(request.getCorrect())
                        .contentImageUrl(request.getContentImageUrl())
                        .viewCount(0)
                        .build()
        );

        Tag tag = Tag.builder()
                .horror(request.getTag().getHorror())
                .daily(request.getTag().getDaily())
                .romance(request.getTag().getRomance())
                .fantasy(request.getTag().getFantasy())
                .sf(request.getTag().getSf())
                .build();

        Hint hint = Hint.builder()
                .hintOne(request.getHint().getHintOne())
                .hintTwo(request.getHint().getHintTwo())
                .hintThree(request.getHint().getHintThree())
                .hintFour(request.getHint().getHintFour())
                .hintFive(request.getHint().getHintFive())
                .build();

        board.saveTagAndHint(tag, hint);

        return BoardResponse.Save.of(board);
    }

    @Transactional
    public BoardDto.DetailDto findBoard(Long id) {
        Board board = getBoard(id);

        return BoardDto.DetailDto.response(board);
    }

    @Transactional
    public BoardDto.DetailDto updateBoard(Long id, BoardDto.UpdateDto request) {
        Board board = getBoard(id);
        board.updateBoard(request);

        return BoardDto.DetailDto.response(board);
    }

    @Transactional
    public Void deleteBoard(Long id, UserDetails userDetails) {
        Board board = getBoard(id);
        String authorEmail = board.getUser().getEmail();
        String email = userDetails.getUsername();

        if (authorEmail.equals(email) == false) {
            throw new CustomException(Result.USER_EMAIL_MISMATCH);
        }

        boardRepository.delete(board);
        return null;
    }

    @Transactional
    public Page<BoardDto.DetailDto> listSortedBoardsPerPage(
            Integer page, Integer limit, String filter, String arrange
    ) {
        /*
         Filter : 조회수 / 생성날짜 (views / createAt)
         arrange : 정렬방식
         */
        Sort.Direction arrangeDirection = (arrange.equals("ASC")) ? ASC : DESC;
        Pageable pageWithTenElements = PageRequest.of(page - 1, limit, arrangeDirection, filter);
        Page<Board> boardPage = boardRepository.findAll(pageWithTenElements);

        return boardPage.map(BoardDto.DetailDto::response);
    }

    @Transactional
    public BoardDto.LikeDto toggleLike(Long id, User user) {
        Board board = getBoard(id);
        Optional<Like> likeOptional = likeRepository.findByBoardAndUser(board, user);

        // 공감을 이미 했을 경우 취소
        if (likeOptional.isPresent()) {
            Like like = likeOptional.get();
            likeRepository.delete(like);
            return BoardDto.LikeDto.response(false);
        }

        // 공감을 하지 않은 경우 등록
        likeRepository.save(
                Like.builder()
                        .board(board)
                        .user(user)
                        .build()
        );
        return BoardDto.LikeDto.response(true);
    }
}
