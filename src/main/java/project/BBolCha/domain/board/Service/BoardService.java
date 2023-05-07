package project.BBolCha.domain.board.Service;

import com.amazonaws.services.s3.AmazonS3Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.BBolCha.domain.board.Dto.BoardDto;
import project.BBolCha.domain.board.Dto.CommentDto;
import project.BBolCha.domain.board.Dto.HintDto;
import project.BBolCha.domain.board.Dto.TagDto;
import project.BBolCha.domain.board.Entity.*;
import project.BBolCha.domain.board.Repository.*;
import project.BBolCha.domain.user.Entity.User;
import project.BBolCha.domain.user.Repository.UserRepository;
import project.BBolCha.global.Exception.CustomException;
import project.BBolCha.global.Model.Result;
import project.BBolCha.global.Model.Status;

import static project.BBolCha.global.Model.Status.BOARD_DELETE_TRUE;
import static project.BBolCha.global.Model.Status.COMMENT_DELETE_TRUE;

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

    private TagDto.DetailDto saveTagAndGetDetailDto(BoardDto.SaveDto request, Board board) {
        Tag tag = tagRepository.save(
                Tag.builder()
                        .board(board)
                        .horror(request.getTag().getHorror())
                        .daily(request.getTag().getDaily())
                        .romance(request.getTag().getRomance())
                        .fantasy(request.getTag().getFantasy())
                        .sf(request.getTag().getSf())
                        .build()
        );
        return TagDto.DetailDto.response(tag);
    }

    private HintDto.DetailDto saveHintAndGetDetailDto(BoardDto.SaveDto request, Board board) {
        Hint hint = hintRepository.save(
                Hint.builder()
                        .board(board)
                        .hintOne(request.getHint().getHintOne())
                        .hintTwo(request.getHint().getHintTwo())
                        .hintThree(request.getHint().getHintThree())
                        .hintFour(request.getHint().getHintFour())
                        .hintFive(request.getHint().getHintFive())
                        .build()
        );
        return HintDto.DetailDto.response(hint);
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
        TagDto.DetailDto tag = saveTagAndGetDetailDto(request, board);
        HintDto.DetailDto hint = saveHintAndGetDetailDto(request, board);

        return BoardDto.SaveDto.response(board, user, tag, hint);
    }

    @Transactional
    public BoardDto.DetailDto findBoard(Long id) {
        Board board = getBoard(id);
        TagDto.DetailDto tag = TagDto.DetailDto.response(board.getTag());
        HintDto.DetailDto hint = HintDto.DetailDto.response(board.getHint());

        return BoardDto.DetailDto.response(board, tag, hint);
    }

    @Transactional
    public BoardDto.DetailDto updateBoard(Long id, BoardDto.UpdateDto request) {
        Board board = getBoard(id);
        board.updateBoard(request);
        TagDto.DetailDto tag = TagDto.DetailDto.response(board.getTag());
        HintDto.DetailDto hint = HintDto.DetailDto.response(board.getHint());

        return BoardDto.DetailDto.response(board, tag, hint);
    }

    @Transactional
    public ResponseEntity<Status> deleteImage(BoardDto.boardImage request) {

        amazonS3Client.deleteObject(bucket, "board/" + request.getImgName());
        return new ResponseEntity<>(Status.IMAGE_DELETE_TRUE, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Page<Board>> read(Integer page, Integer limit, String filter, String arrange) {
        /*
         Filter : 조회수 / 생성날짜 (views / createAt)
         arrange : 정렬방식
         */
        if (arrange.equals("ASC")) {
            Pageable pageWithTenElements = PageRequest.of(page - 1, limit, Sort.Direction.ASC, filter);
            return new ResponseEntity<>(boardRepository.findAll(pageWithTenElements), HttpStatus.OK);
        }
        Pageable pageWithTenElements = PageRequest.of(page - 1, limit, Sort.Direction.DESC, filter);
        return new ResponseEntity<>(boardRepository.findAll(pageWithTenElements), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Comment> addComment(Long bid, CommentDto.Request request) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext()
                .getAuthentication()
                .getName()
        ).orElseThrow(NullPointerException::new);

        return new ResponseEntity<>(commentRepository.save(
                Comment.builder()
                        .bid(bid)
                        .uid(user.getId())
                        .name(user.getName())
                        .note(request.getNote()).build()
        ), HttpStatus.OK);
    }

    // 댓글

    @Transactional
    public ResponseEntity<Page<Comment>> readComment(Long bid, Integer page) {
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.Direction.DESC, "createAt");
        return new ResponseEntity<>(commentRepository.findByBid(bid, pageable), HttpStatus.OK);
    }

    @Transactional
    public Void deleteBoard(Long id, UserDetails userDetails) {
        Board board = getBoard(id);
        String authorEmail = board.getUser().getEmail();
        String email = userDetails.getUsername();

        if (authorEmail != email) {
            throw new CustomException(Result.USER_EMAIL_MISMATCH);
        }

        boardRepository.delete(board);
        return null;
    }

    @Transactional
    public ResponseEntity<BoardDto.Like> addLike(Long bid) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if (likeRepository.existsByBidAndEmail(bid, email)) {
            likeRepository.deleteByEmail(email);
            return new ResponseEntity<>(BoardDto.Like.response("cancel", "좋아요 취소"), HttpStatus.OK);
        }

        likeRepository.save(
                Like.builder()
                        .bid(bid)
                        .email(SecurityContextHolder.getContext().getAuthentication().getName())
                        .build()
        );

        return new ResponseEntity<>(BoardDto.Like.response("add", "좋아요 등록"), HttpStatus.OK);
    }

    public ResponseEntity<Status> deleteComment(Long id) {
        commentRepository.deleteById(id);
        return new ResponseEntity<>(COMMENT_DELETE_TRUE, HttpStatus.OK);
    }
}
