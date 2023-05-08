package project.BBolCha.domain.board.Service;

import com.amazonaws.services.s3.AmazonS3Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.BBolCha.domain.board.Dto.BoardDto;
import project.BBolCha.domain.board.Entity.Board;
import project.BBolCha.domain.board.Entity.Hint;
import project.BBolCha.domain.board.Entity.Like;
import project.BBolCha.domain.board.Entity.Tag;
import project.BBolCha.domain.board.Repository.*;
import project.BBolCha.domain.user.Entity.User;
import project.BBolCha.domain.user.Repository.UserRepository;
import project.BBolCha.global.Exception.CustomException;
import project.BBolCha.global.Model.Result;

import java.util.Optional;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final HintRepository hintRepository;
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private User getUser(UserDetails userDetails) {
        String email = userDetails.getUsername();
        return userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(Result.NOT_FOUND_USER)
        );
    }

    private Board getBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(
                () -> new CustomException(Result.NOT_FOUND_BOARD)
        );
    }

    @Transactional
    public BoardDto.SaveDto createBoard(BoardDto.SaveDto request, UserDetails userDetails) {
        User user = getUser(userDetails);

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

        return BoardDto.SaveDto.response(board);
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
    public Page<BoardDto.DetailDto> listSortedBoardsPerPage(Integer page, Integer limit, String filter, String arrange) {
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
    public BoardDto.LikeDto toggleLike(Long id, UserDetails userDetails) {
        User user = getUser(userDetails);
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
