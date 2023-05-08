package project.BBolCha.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.BBolCha.domain.board.entity.Board;
import project.BBolCha.domain.board.entity.Like;
import project.BBolCha.domain.user.entity.User;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    @Transactional
    Optional<Like> findByBoardAndUser(Board board, User user);
}
