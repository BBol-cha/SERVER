package project.BBolCha.domain.board.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.BBolCha.domain.user.entity.User;

import static org.assertj.core.api.Assertions.assertThat;

class BoardTest {

    @DisplayName("게시글에 태그와 힌트가 등록되는지 확인한다.")
    @Test
    void saveTagAndHint() {
        // given
        User user = User.builder()
                .name("테스트 계정")
                .email("test@test.com")
                .password("abc123!")
                .profileImageUrl("test.png")
                .build();

        Board board = Board.builder()
                .id(1L)
                .user(user)
                .title("테스트")
                .content("내용 테스트")
                .correct("정답 테스트")
                .contentImageUrl("test.png")
                .viewCount(0)
                .build();

        Tag tag = Tag.builder()
                .id(1L)
                .horror(true)
                .daily(true)
                .romance(false)
                .fantasy(false)
                .sf(true)
                .build();

        Hint hint = Hint.builder()
                .id(1L)
                .hintOne("1")
                .hintTwo("2")
                .hintThree("3")
                .hintFour("4")
                .hintFive("5")
                .build();

        // when
        board.saveTagAndHint(tag, hint);

        // then
        assertThat(board.getTag().getHorror()).isEqualTo(true);
        assertThat(board.getTag().getRomance()).isEqualTo(false);

        assertThat(board.getHint().getHintOne()).isEqualTo("1");
        assertThat(board.getHint().getHintFour()).isEqualTo("4");
    }

}