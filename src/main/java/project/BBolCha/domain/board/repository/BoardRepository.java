package project.BBolCha.domain.board.repository;

import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.BBolCha.domain.board.entity.Board;
import project.BBolCha.domain.board.repository.querydsl.BoardQueryDslRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardQueryDslRepository {

    @Query(
            "select b " +
            "from Board b " +
                    "join fetch b.user " +
                    "join fetch b.tag " +
                    "join fetch b.hint " +
                    "where b.id = :id"
    )
    Optional<Board> fetchFindById(@Param(value = "id") Long id);
}
