package project.BBolCha.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("바다거북스프 프로젝트 API Document")
                .version("v0.0.1")
                .description("API는 오로지 Swagger로만 문서화 합니다.");

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
