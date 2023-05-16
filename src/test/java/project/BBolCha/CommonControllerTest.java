package project.BBolCha;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import project.BBolCha.global.model.CustomResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class CommonControllerTest {

    @Autowired
    private CommonController commonController;

    @DisplayName("현재 Version이 몇인지 확인한다.")
    @Test
    void verSionTest() {
        // when
        CustomResponseEntity<String> response = commonController.version();

        // then
        assertThat(response)
                .extracting("code","message","data")
                .contains(0,"성공","version 2.0.0");
    }
}