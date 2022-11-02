package project.BBolCha.domain.user.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.BBolCha.domain.user.Entity.User;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenInfoResponseDto {
    @JsonIgnore
    private Long id;
    private String pw;
    private String email;
    private String name;


    public static TokenInfoResponseDto Response(@NotNull User user) {
        return TokenInfoResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .pw(user.getPw())
                .build();
    }
}
