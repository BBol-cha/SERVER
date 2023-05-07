package project.BBolCha.domain.board.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.BBolCha.domain.board.Entity.Board;
import project.BBolCha.domain.board.Entity.Like;
import project.BBolCha.domain.user.Entity.User;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    @Transactional
    Optional<Like> findByBoardAndUser(Board board, User user);
}
