package project.BBolCha.domain.board.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import project.BBolCha.domain.board.dto.BoardDto;
import project.BBolCha.domain.user.entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@Entity
@DynamicUpdate
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE board SET deleted_at = CURRENT_TIMESTAMP where id = ?")
public class Board extends BaseEntity implements Serializable{
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

    @OneToOne(mappedBy = "board", cascade = CascadeType.ALL)
    private Tag tag;

    @OneToOne(mappedBy = "board", cascade = CascadeType.ALL)
    private Hint hint;

    private LocalDateTime deletedAt;

    public void updateBoard(BoardDto.UpdateDto request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.correct = request.getCorrect();
        this.contentImageUrl = request.getContentImageUrl();

        this.tag.updateTag(request.getTag());
        this.hint.updateHint(request.getHint());
    }

    public void saveTagAndHint(Tag tag, Hint hint) {
        tag.setBoard(this);
        this.tag = tag;

        hint.setBoard(this);
        this.hint = hint;
    }
}
