package project.BBolCha.domain.board.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.BBolCha.domain.board.Entity.Comment;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}
