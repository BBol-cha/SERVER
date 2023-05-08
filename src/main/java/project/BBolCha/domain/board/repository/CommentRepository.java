package project.BBolCha.domain.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.BBolCha.domain.board.entity.Board;
import project.BBolCha.domain.board.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByBoard(Board board, Pageable pageable);
}
