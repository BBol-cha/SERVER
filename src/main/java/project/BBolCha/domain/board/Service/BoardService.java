package project.BBolCha.domain.board.Service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.BBolCha.domain.board.Dto.BoardDto;
import project.BBolCha.domain.board.Dto.CommentDto;
import project.BBolCha.domain.board.Entity.Board;
import project.BBolCha.domain.board.Entity.Comment;
import project.BBolCha.domain.board.Entity.Like;
import project.BBolCha.domain.board.Entity.TagCategory;
import project.BBolCha.domain.board.Repository.BoardRepository;
import project.BBolCha.domain.board.Repository.CommentRepository;
import project.BBolCha.domain.board.Repository.LikeRepository;
import project.BBolCha.domain.board.Repository.TagCategoryRepository;
import project.BBolCha.domain.user.Entity.User;
import project.BBolCha.domain.user.Repository.UserRepository;
import project.BBolCha.global.Model.Status;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static project.BBolCha.global.Model.Status.BOARD_DELETE_TRUE;
import static project.BBolCha.global.Model.Status.BOARD_UPDATE_TRUE;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final TagCategoryRepository tagCategoryRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public ResponseEntity<BoardDto.Request> create(BoardDto.Request request) {

        User user = userRepository.findByEmail(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName()
        ).orElseThrow(
                NullPointerException::new
        );

        String[] tag = request.getTag();

        for (String s : tag) {
            if (tagCategoryRepository.findByTag(s).isEmpty()) {
                tagCategoryRepository.save(
                        TagCategory.builder()
                                .tag(s)
                                .build()
                );
            }
        }

        String strArrayToString = String.join(",", tag); // 문자열 배열을 Join


        Board board = boardRepository.save(
                Board.builder()
                        .userId(user.getId())
                        .title(request.getTitle())
                        .answer(request.getAnswer())
                        .name(user.getName())
                        .bimg(request.getBimg())
                        .note(request.getNote())
                        .views(0)
                        .tag(strArrayToString)
                        .build()
        );

        return new ResponseEntity<>(BoardDto.Request.Response(board, strArrayToString.split(",")), HttpStatus.CREATED);

    }

    @Transactional
    public ResponseEntity<BoardDto.boardImage> putImage(MultipartFile multipartFile) throws IOException {
        log.info("$$$$$$$$$$$$$$$$$$$");
        log.info(multipartFile.getName());
        log.info("$$$$$$$$$$$$$$$$$$$");
        UUID uuid = UUID.randomUUID();
        String imageName = "board/" + uuid;
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getInputStream().available());
        amazonS3Client.putObject(bucket, imageName, multipartFile.getInputStream(), objMeta);

        log.info("%%%%%%%%%%%%%%%");
        log.info(multipartFile.getName());
        log.info("%%%%%%%%%%%%%%%");

        return new ResponseEntity<>(BoardDto.boardImage.response(
                imageName,
                amazonS3Client.getUrl(bucket, imageName).toString()
        ), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Status> deleteImage(BoardDto.boardImage request) {

        amazonS3Client.deleteObject(bucket, request.getImgName());
        return new ResponseEntity<>(Status.IMAGE_DELETE_TRUE, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Page<Board>> read(Integer page, Integer limit) {
        Pageable pageWithTenElements = PageRequest.of(page - 1, limit, Sort.Direction.DESC, "createAt");
        return new ResponseEntity<>(boardRepository.findAll(pageWithTenElements), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Board> readDetail(Long id) {
        Optional<Board> board = boardRepository.findById(id);

        Board note = board.orElseThrow(
                NullPointerException::new
        );

        boardRepository.save(
                Board.builder()
                        .id(id)
                        .userId(note.getUserId())
                        .title(note.getTitle())
                        .answer(note.getAnswer())
                        .name(note.getName())
                        .bimg(note.getBimg())
                        .note(note.getNote())
                        .views(note.getViews() + 1)
                        .tag(note.getTag())
                        .createAt(note.getCreateAt())
                        .build()
        );

        return new ResponseEntity<>(note, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Comment> addComment(Long bid, CommentDto.Request request) {
        User user = userRepository.findByEmail(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName()
        ).orElseThrow(
                NullPointerException::new
        );

        return new ResponseEntity<>(
                commentRepository.save(
                        Comment.builder()
                                .bid(bid)
                                .uid(user.getId())
                                .name(user.getName())
                                .note(request.getNote())
                                .build()
                ), HttpStatus.OK
        );
    }

    @Transactional
    public ResponseEntity<List<Comment>> readComment(Long bid) {
        return new ResponseEntity<>(commentRepository.findByBid(bid), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Status> update(Long id, BoardDto.Request request) {
        log.info(id.toString());

        Board board = boardRepository.findById(id).orElseThrow(
                NullPointerException::new
        );

        String[] tag = request.getTag();

        for (String s : tag) {
            if (tagCategoryRepository.findByTag(s).isEmpty()) {
                tagCategoryRepository.save(
                        TagCategory.builder()
                                .tag(s)
                                .build()
                );
            }
        }

        String strArrayToString = String.join(",", tag); // 문자열 배열을 Join

        boardRepository.save(
                Board.builder()
                        .id(id)
                        .userId(board.getUserId())
                        .title(request.getTitle())
                        .answer(request.getAnswer())
                        .name(board.getName())
                        .bimg(request.getBimg())
                        .note(request.getNote())
                        .views(board.getViews())
                        .tag(strArrayToString)
                        .createAt(board.getCreateAt())
                        .build()
        );

        return new ResponseEntity<>(BOARD_UPDATE_TRUE,HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Status> delete(Long id) {
        boardRepository.deleteById(id);
        return new ResponseEntity<>(BOARD_DELETE_TRUE,HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<BoardDto.Like> addLike(Long bid) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if(likeRepository.existsByEmail(email)){
            likeRepository.deleteByEmail(email);
            return new ResponseEntity<>(
                    BoardDto.Like.response(
                            "cancel","좋아요 취소"
                    ),HttpStatus.OK
            );
        }

        likeRepository.save(
                Like.builder()
                        .bid(bid)
                        .email(SecurityContextHolder.getContext().getAuthentication().getName())
                        .build()
        );

        return new ResponseEntity<>(
                BoardDto.Like.response(
                        "add","좋아요 등록"
                ),HttpStatus.OK
        );
    }
}
