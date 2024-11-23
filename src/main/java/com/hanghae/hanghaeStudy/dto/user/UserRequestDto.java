package com.hanghae.hanghaeStudy.dto.user;

import com.hanghae.hanghaeStudy.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    @NotBlank(message = "username을 입력하세요.")
    @Size(min = 4, max = 10, message = "username은 최소 4자 이상, 10자 이하이어야 합니다.")
    @Pattern(regexp = "^[a-z0-9]+$", message = "username은 소문자 알파벳(a~z)과 숫자(0~9)만 사용할 수 있습니다.")
    private String username;

    @NotBlank(message = "password을 입력하세요.")
    @Size(min = 8, max = 15, message = "password는 최소 8자 이상, 15자 이하이어야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "password는 알파벳 대소문자(a~z, A~Z)와 숫자(0~9)만 사용할 수 있습니다.")
    private String password;

    public static UserRequestDto toDto(User user){
        return new UserRequestDto(
                user.getUsername(),
                user.getPassword()
        );
    }
}
