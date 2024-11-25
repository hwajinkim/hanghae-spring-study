package com.hanghae.hanghaeStudy.service;

import com.hanghae.hanghaeStudy.dto.auth.SignInRequestDto;
import com.hanghae.hanghaeStudy.dto.auth.TokenDto;
import com.hanghae.hanghaeStudy.dto.user.UserRequestDto;
import com.hanghae.hanghaeStudy.dto.user.UserResponseDto;
import com.hanghae.hanghaeStudy.entity.User;
import com.hanghae.hanghaeStudy.exception.IncorrectPasswordException;
import com.hanghae.hanghaeStudy.exception.UsernameNotFoundException;
import com.hanghae.hanghaeStudy.repository.UserRepository;
import com.hanghae.hanghaeStudy.security.JwtFilter;
import com.hanghae.hanghaeStudy.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional(readOnly = true)
    public TokenDto signIn(SignInRequestDto signInRequestDto){
        User user = userRepository.findByUsername(signInRequestDto.getUsername())
                .orElseThrow(()-> new UsernameNotFoundException("사용자를 찾을 수 없습니다. : "+signInRequestDto.getUsername()));

        if(!encoder.matches(signInRequestDto.getPassword(), user.getPassword())){
            throw new IncorrectPasswordException("비밀번호가 일치하지 않습니다.:  "+signInRequestDto.getUsername());
        }

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(signInRequestDto.getUsername(), user.getPassword());
        Authentication authentication =  authenticationManagerBuilder.getObject().authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        return new TokenDto(jwt);
    }
}
