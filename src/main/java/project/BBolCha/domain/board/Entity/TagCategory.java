package project.BBolCha.domain.board.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "tag_category")
@EntityListeners(AuditingEntityListener.class)
public class TagCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String tag;

    private TagCategory() {
    }
}
