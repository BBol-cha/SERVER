package project.BBolCha.domain.board.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.BBolCha.domain.board.Entity.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    @Transactional
    boolean existsByEmail(String email);
    @Transactional
    void deleteByEmail(String email);
    @Transactional
    Long countByBid(Long bid);
    @Transactional
    boolean existsByBidAndEmail(Long bid, String email);
}
