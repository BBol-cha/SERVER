package project.BBolCha.domain.board.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.BBolCha.domain.board.Entity.Hint;

@Repository
public interface HintRepository extends JpaRepository<Hint, Long> {
}
