package project.BBolCha.domain.board.Service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.BBolCha.domain.board.Dto.BoardDto;
import project.BBolCha.domain.board.Entity.Board;
import project.BBolCha.domain.board.Repository.BoardRepository;
import project.BBolCha.domain.user.Entity.User;
import project.BBolCha.domain.user.Repository.UserRepository;
import project.BBolCha.global.Model.Status;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

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
                        .bimg(request.getBimg())
                        .note(request.getNote())
                        .views(0)
                        .build()
        ), HttpStatus.CREATED);

    }

    public ResponseEntity<BoardDto.boardImage> putImage(MultipartFile multipartFile) throws IOException {
        UUID uuid = UUID.randomUUID();
        String imageName = "board/" + uuid;
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getInputStream().available());
        amazonS3Client.putObject(bucket,imageName,multipartFile.getInputStream(),objMeta);

        return new ResponseEntity<>(BoardDto.boardImage.response(
                imageName,
                amazonS3Client.getUrl(bucket,imageName).toString()
        ),HttpStatus.OK);
    }

    public ResponseEntity<Status> deleteImage(BoardDto.boardImage request) {

        amazonS3Client.deleteObject(bucket,request.getImgName());
        return new ResponseEntity<>(Status.IMAGE_DELETE_TRUE,HttpStatus.OK);
    }
}
