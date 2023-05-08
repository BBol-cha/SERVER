package project.BBolCha.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.BBolCha.domain.board.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
