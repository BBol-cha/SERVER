package project.BBolCha.domain.board.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.BBolCha.domain.board.Entity.Board;
import project.BBolCha.domain.board.Entity.Tag;

import javax.persistence.IdClass;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
}
