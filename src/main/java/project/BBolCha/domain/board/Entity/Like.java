package project.BBolCha.domain.board.Entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "board_like")
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String email;
    @NotNull
    private Long bid;


    private Like() {
    }
}
