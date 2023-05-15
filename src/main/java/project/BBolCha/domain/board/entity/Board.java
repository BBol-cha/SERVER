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
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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
    private List<Like> like = new ArrayList<>();

    @OneToOne(mappedBy = "board", cascade = CascadeType.ALL)
    private Tag tag;

    @OneToOne(mappedBy = "board", cascade = CascadeType.ALL)
    private Hint hint;

    private LocalDateTime deletedAt;

    @Builder
    private Board(Long id, User user, String title, String content, String correct, String contentImageUrl, Integer viewCount, List<Like> like, Tag tag, Hint hint, LocalDateTime deletedAt) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.content = content;
        this.correct = correct;
        this.contentImageUrl = contentImageUrl;
        this.viewCount = viewCount;
        this.like = like;
        this.tag = tag;
        this.hint = hint;
        this.deletedAt = deletedAt;
    }

    public void updateBoard(BoardDto.UpdateDto request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.correct = request.getCorrect();
        this.contentImageUrl = request.getContentImageUrl();

        this.tag.updateTag(request.getTag());
        this.hint.updateHint(request.getHint());
    }

    public void saveTagAndHint(Tag tag, Hint hint) {
        this.tag = tag;
        this.hint = hint;
    }
}
