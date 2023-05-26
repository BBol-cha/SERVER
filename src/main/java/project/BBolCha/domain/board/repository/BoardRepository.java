package project.BBolCha.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.BBolCha.domain.board.entity.Board;
import project.BBolCha.domain.board.repository.querydsl.BoardQueryDslRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardQueryDslRepository {
}
