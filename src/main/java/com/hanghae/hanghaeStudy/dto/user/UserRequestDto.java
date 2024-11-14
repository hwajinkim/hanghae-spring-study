package com.hanghae.hanghaeStudy.dto.user;

import com.hanghae.hanghaeStudy.entity.User;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    @Size(min = 4, max = 10, message = "Username은 최소 4자 이상, 10자 이하이어야 합니다.")
    @Pattern(regexp = "^[a-z0-9]+$", message = "Username은 소문자 알파벳(a~z)과 숫자(0~9)만 사용할 수 있습니다.")
    private String username;

    @Size(min = 8, max = 15, message = "Password는 최소 8자 이상, 15자 이하이어야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Password는 알파벳 대소문자(a~z, A~Z)와 숫자(0~9)만 사용할 수 있습니다.")
    private String password;

    public static UserRequestDto toDto(User user){
        return new UserRequestDto(
                user.getUsername(),
                user.getPassword()
        );
    }
}
