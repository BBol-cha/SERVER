package project.BBolCha.global.config.Jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import project.BBolCha.global.Exception.CustomErrorResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static project.BBolCha.global.Exception.CustomErrorCode.Authentication_Entry_Point;


@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        //Content-type : application/json;charset=utf-8
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        CustomErrorResponse error = new CustomErrorResponse();
        error.setStatus(Authentication_Entry_Point);
        error.setStatusMessage("토큰이 NULL 이거나 잘못된 AccessToken 입니다.");

        // {"username":"loop-study", "age":20}
        String result = objectMapper.writeValueAsString(error);
        response.getWriter().write(result);
    }
}
