package com.hanghae.hanghaeStudy.dto.user;

import com.hanghae.hanghaeStudy.entity.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private Long id;
    private String username;
    private String password;

    public static UserResponseDto toDto(User user){
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getPassword()
        );
    }
}
