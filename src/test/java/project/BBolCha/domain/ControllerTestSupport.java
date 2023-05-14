package project.BBolCha.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.filter.CorsFilter;
import project.BBolCha.domain.user.controller.UserController;
import project.BBolCha.domain.user.service.UserService;
import project.BBolCha.global.config.RedisDao;
import project.BBolCha.global.config.SecurityConfig;
import project.BBolCha.global.config.jwt.JwtAccessDeniedHandler;
import project.BBolCha.global.config.jwt.JwtAuthenticationEntryPoint;
import project.BBolCha.global.config.jwt.TokenProvider;

@WebMvcTest(controllers = {
        UserController.class
})
@AutoConfigureMockMvc(addFilters = false)
public abstract class ControllerTestSupport {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected UserService userService;

    @MockBean
    private RedisDao redisDao;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private CorsFilter corsFilter;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

}
