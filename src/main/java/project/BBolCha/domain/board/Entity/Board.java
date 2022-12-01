package project.BBolCha.domain.board.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "board")
@EntityListeners(AuditingEntityListener.class)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uid")
    @NotNull
    private Long userId;

    @NotNull
    private String name;
    @Column(name = "title",length = 65)
    @NotNull
    private String title;
    @Column(name = "note",length = 10000)
    @NotNull
    private String note;
    @Column(name = "views")
    @NotNull
    private Integer views;

    @Column(name = "stitle")
    private String subTitle;

    private String bimg;

    @Column(name = "create_at")
    @CreatedDate
    @NotNull
    private LocalDateTime creatAt;

    @Column(name = "update_at")
    @LastModifiedDate
    @NotNull
    private LocalDateTime updateAt;

    private Board() {
    }
}
