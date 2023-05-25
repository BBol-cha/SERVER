package project.BBolCha.domain.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import project.BBolCha.global.exception.CustomException;
import project.BBolCha.global.model.Result;

import java.util.Optional;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@Transactional(readOnly = true)
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
        Board boardEntity = Board.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .correct(request.getCorrect())
                .contentImageUrl(request.getContentImageUrl())
                .viewCount(0)
                .build();

        Tag tag = Tag.builder()
                .board(boardEntity)
                .horror(request.getTag().getHorror())
                .daily(request.getTag().getDaily())
                .romance(request.getTag().getRomance())
                .fantasy(request.getTag().getFantasy())
                .sf(request.getTag().getSf())
                .build();

        Hint hint = Hint.builder()
                .board(boardEntity)
                .hintOne(request.getHint().getHintOne())
                .hintTwo(request.getHint().getHintTwo())
                .hintThree(request.getHint().getHintThree())
                .hintFour(request.getHint().getHintFour())
                .hintFive(request.getHint().getHintFive())
                .build();

        boardEntity.saveTagAndHint(tag, hint);

        Board savedBoard = boardRepository.save(boardEntity);

        return BoardResponse.Save.of(savedBoard);
    }

    public BoardResponse.Detail findBoard(Long id) {
        Board board = getBoard(id);
        return BoardResponse.Detail.response(board);
    }

    @Transactional
    public BoardResponse.Detail updateBoard(Long id, BoardServiceRequest.Update request, User user) {
        Board board = getBoard(id);

        if (board.getUser().getId() != user.getId()) {
            throw new CustomException(Result.NOT_MY_POST);
        }

        board.updateBoard(
                request.getTitle(), request.getContent(), request.getCorrect(),
                request.getContentImageUrl(), request.getTag(), request.getHint()
        );

        return BoardResponse.Detail.response(board);
    }

    @Transactional
    public Void deleteBoard(Long id, User user) {
        Board board = getBoard(id);
        Long authorId = board.getUser().getId();
        Long userId = user.getId();

        if (authorId.equals(userId) == false) {
            throw new CustomException(Result.NOT_MY_POST);
        }

        boardRepository.delete(board);
        return null;
    }

    public Page<BoardResponse.Detail> listSortedBoardsPerPage(
            Integer page, Integer limit, String filter, String arrange
    ) {
        /*
         Filter : 조회수 / 생성날짜 (views / createAt)
         arrange : 정렬방식
         */
        Sort.Direction arrangeDirection = (arrange.equals("ASC")) ? ASC : DESC;
        return boardRepository.getPageBoardsAsDto(PageRequest.of(page - 1, limit, arrangeDirection, filter));
    }

    @Transactional
    public BoardResponse.Likes toggleLike(Long id, User user) {
        Board board = getBoard(id);
        Optional<Like> likeOptional = likeRepository.findByBoardAndUser(board, user);

        // 공감을 이미 했을 경우 취소
        if (likeOptional.isPresent()) {
            Like deleteLike = likeOptional.get();
            likeRepository.delete(deleteLike);
            return BoardResponse.Likes.response(deleteLike, false);
        }

        // 공감을 하지 않은 경우 등록
        Like saveLike = likeRepository.save(
                Like.builder()
                        .board(board)
                        .user(user)
                        .build()
        );
        return BoardResponse.Likes.response(saveLike, true);
    }
}
