package project.BBolCha.docs.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import project.BBolCha.docs.RestDocsSupport;
import project.BBolCha.domain.user.controller.UserController;
import project.BBolCha.domain.user.dto.controller.request.UserRequest;
import project.BBolCha.domain.user.dto.service.request.UserServiceRequest;
import project.BBolCha.domain.user.dto.service.responce.UserResponse;
import project.BBolCha.domain.user.entity.User;
import project.BBolCha.domain.user.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerDocsTest extends RestDocsSupport {

    private final UserService userService = mock(UserService.class);

    @Override
    protected Object initController() {
        return new UserController(userService);
    }

    @DisplayName("회원가입 API")
    @Test
    void registerNewUser() throws Exception {
        // given
        UserRequest.Registration request = new UserRequest.Registration("덕배", "kevin@gmail.com", "abc123!");

        given(userService.registerNewUser(any(UserServiceRequest.Registration.class), any(HttpServletResponse.class)))
                .willReturn(UserResponse.Login.builder()
                        .id(1L)
                        .email("test@test.com")
                        .name("테스트 계정")
                        .profileImageUrl("null")
                        .accessToken("issued accessToken")
                        .build()
                );

        // when // then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/auth")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-register",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .description("유저 이름"),
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("유저 이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("유저 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("상태 메세지"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("유저 ID / Long"),
                                fieldWithPath("data.email").type(JsonFieldType.STRING)
                                        .description("유저 이메일"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING)
                                        .description("유저 이름"),
                                fieldWithPath("data.profileImageUrl").type(JsonFieldType.STRING)
                                        .description("유저 프로필 사진"),
                                fieldWithPath("data.accessToken").type(JsonFieldType.STRING)
                                        .description("issued_AccessToken")
                        )
                ));
    }

    @DisplayName("로그인 API")
    @Test
    void login() throws Exception {
        // given

        UserRequest.Login request = new UserRequest.Login("test@test.com", "test");
        given(userService.login(any(UserServiceRequest.Login.class), any(HttpServletResponse.class)))
                .willReturn(UserResponse.Login.builder()
                        .id(1L)
                        .email("test@test.com")
                        .name("테스트 계정")
                        .profileImageUrl("test.png")
                        .accessToken("issued accessToken")
                        .build());
        // when // then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/auth/login")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("상태 메세지"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("유저 ID"),
                                fieldWithPath("data.email").type(JsonFieldType.STRING)
                                        .description("유저 이메일"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING)
                                        .description("유저 이름"),
                                fieldWithPath("data.profileImageUrl").type(JsonFieldType.STRING)
                                        .description("유저 프로필 사진"),
                                fieldWithPath("data.accessToken").type(JsonFieldType.STRING)
                                        .description("issued JWT_AccessToken")
                        )
                ));
    }

    @DisplayName("정보 조회 API")
    @Test
    void readInfo() throws Exception {
        User user = User.builder().build();

        // given
        given(userService.read(any(User.class)))
                .willReturn(UserResponse.Detail.builder()
                        .id(1L)
                        .email("test@test.com")
                        .name("테스트 계정")
                        .profileImageUrl("test.png")
                        .build()
                );

        // when // then
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/auth/info")
                                .header("Authorization", "JWT_AccessToken")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-readInfo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT_AccessToken")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("상태 메세지"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("유저 ID"),
                                fieldWithPath("data.email").type(JsonFieldType.STRING)
                                        .description("유저 이메일"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING)
                                        .description("유저 이름"),
                                fieldWithPath("data.profileImageUrl").type(JsonFieldType.STRING)
                                        .description("유저 프로필 사진")
                        )
                ));
    }

    @DisplayName("토큰 재발급 API")
    @Test
    void reissueToken() throws Exception {
        // given
        given(userService.reissue(any(String.class)))
                .willReturn(UserResponse.Reissue.builder()
                        .accessToken("reissued AccessToken")
                        .build()
                );

        // when // then
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/auth")
                                .cookie(new Cookie("refreshToken", "test"))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-reissue",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("상태 메세지"),
                                fieldWithPath("data.accessToken").type(JsonFieldType.STRING)
                                        .description("reissued JWT_AccessToken")
                        )
                ));
    }

    @DisplayName("로그아웃 API")
    @Test
    void logout() throws Exception {
        // when // then
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/auth/logout")
                                .header("Authorization", "JWT_AccessToken")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-logout",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT_AccessToken")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("상태 메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("data is empty object")
                        )
                        ));
    }

    @DisplayName("회원정보 수정 API")
    @Test
    void updateUserNameAndProfileImageUrl() throws Exception {
        // given
        UserRequest.Update request = new UserRequest.Update("test","test");
        given(userService.update(any(UserServiceRequest.Update.class),any(User.class)))
                .willReturn(UserResponse.Detail.builder()
                        .id(1L)
                        .email("test@test.com")
                        .name("테스트 계정")
                        .profileImageUrl("test.png")
                        .build()
                );
        // when // then
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/auth")
                                .header("Authorization","JWT_AccessToken")
                                .content(objectMapper.writeValueAsString(request.toServiceRequest()))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT_AccessToken")
                        ),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .description("유저 이름"),
                                fieldWithPath("profileImageUrl").type(JsonFieldType.STRING)
                                        .description("유저 프로필 사진")
                        )
                        ,
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("상태 메세지"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("유저 ID"),
                                fieldWithPath("data.email").type(JsonFieldType.STRING)
                                        .description("유저 이메일"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING)
                                        .description("유저 이름"),
                                fieldWithPath("data.profileImageUrl").type(JsonFieldType.STRING)
                                        .description("유저 프로필 사진")
                        )
                ));

    }
}
