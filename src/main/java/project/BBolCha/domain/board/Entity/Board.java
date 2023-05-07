package project.BBolCha.domain.board.Entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import project.BBolCha.domain.user.Entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@Entity
@DynamicUpdate
public class Board extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 65)
    @NotNull
    private String title;

    @Column(length = 10000)
    @NotNull
    private String content;

    @NotNull
    private String correct;

    private String contentImageUrl;

    private Integer viewCount;

    @OneToMany(mappedBy = "board")
    private List<Like> like;

    @OneToOne(mappedBy = "board")
    private Tag tag;

    @OneToOne(mappedBy = "board")
    private Hint hint;

}
