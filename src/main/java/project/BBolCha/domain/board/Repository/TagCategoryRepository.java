package project.BBolCha.domain.board.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.BBolCha.domain.board.Entity.TagCategory;

import java.util.Optional;

@Repository
public interface TagCategoryRepository extends JpaRepository<TagCategory, Long> {
    Optional<TagCategory> findByTag(String tag);
}
