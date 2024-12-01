package com.hanghae.hanghaeStudy.controller;

import com.hanghae.hanghaeStudy.dto.auth.SignInRequestDto;
import com.hanghae.hanghaeStudy.dto.auth.TokenDto;
import com.hanghae.hanghaeStudy.response.ResponseCode;
import com.hanghae.hanghaeStudy.security.JwtFilter;
import com.hanghae.hanghaeStudy.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @PostMapping("/signin")
    public ResponseEntity<TokenDto> signIn(@Valid @RequestBody SignInRequestDto signInRequestDto){

        TokenDto tokenDto = authService.signIn(signInRequestDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + tokenDto.getJwt());
        return new ResponseEntity<>(tokenDto, httpHeaders, HttpStatus.OK);
    }
}
