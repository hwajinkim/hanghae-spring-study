package com.hanghae.hanghaeStudy.dto.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {
    /*private String jwt;*/
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
