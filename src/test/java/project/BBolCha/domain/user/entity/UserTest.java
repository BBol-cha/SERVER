package project.BBolCha.domain.user.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @DisplayName("유저의 이름과 프로필 이미지를 재설정한다.")
    @Test
    void test() {
        // given
        User user = User.builder()
                .name("테스트 계정")
                .email("test@test.com")
                .profileImageUrl("test.png")
                .build();
        // when
        user.updateNameAndProfileImageUrl("업데이트 계정","update_test_image.png");

        // then
        assertThat(user)
                .extracting("name","profileImageUrl")
                .contains("업데이트 계정", "update_test_image.png");
    }
}