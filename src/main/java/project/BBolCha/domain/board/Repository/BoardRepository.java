package project.BBolCha.domain.board.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.BBolCha.domain.board.Entity.Board;

public interface BoardRepository extends JpaRepository<Board,Long> {
}
