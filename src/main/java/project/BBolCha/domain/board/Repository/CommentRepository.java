package project.BBolCha.domain.board.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.BBolCha.domain.board.Entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByBid(Long bid);
    Page<Comment> findByBid(Long bid,Pageable pageable);
}
