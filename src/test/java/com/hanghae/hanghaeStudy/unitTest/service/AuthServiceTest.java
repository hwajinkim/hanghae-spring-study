package com.hanghae.hanghaeStudy.unitTest.service;

import com.hanghae.hanghaeStudy.dto.auth.SignInRequestDto;
import com.hanghae.hanghaeStudy.dto.auth.TokenDto;
import com.hanghae.hanghaeStudy.entity.User;
import com.hanghae.hanghaeStudy.exception.IncorrectPasswordException;
import com.hanghae.hanghaeStudy.exception.UsernameNotFoundException;
import com.hanghae.hanghaeStudy.repository.UserRepository;
import com.hanghae.hanghaeStudy.security.TokenProvider;
import com.hanghae.hanghaeStudy.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;



    @Nested
    @DisplayName("로그인")
    class SignIn {

        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {

            @BeforeEach
            public void init(){
                when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
            }

            @Test
            @DisplayName("로그인 성공 테스트")
            void signInSuccess(){
                // Given
                String role = "USER";
                List<String> roles = new ArrayList<>();
                roles.add(role);

                SignInRequestDto signInRequestDto = new SignInRequestDto();
                signInRequestDto.setUsername("testUser");
                signInRequestDto.setPassword("password123!");

                String encPassword = passwordEncoder.encode(signInRequestDto.getPassword());
                User selectedUser = new User(1L, "testUser", encPassword, roles);
                TokenDto tokenDto = new TokenDto("Bearer","sampleToken");

                when(userRepository.findByUsername(signInRequestDto.getUsername())).thenReturn(Optional.of(selectedUser));
                when(passwordEncoder.matches(signInRequestDto.getPassword(), selectedUser.getPassword())).thenReturn(true);
                Authentication authentication = mock(Authentication.class);
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(signInRequestDto.getUsername(), selectedUser.getPassword());
                when(authenticationManagerBuilder.getObject().authenticate(authToken)).thenReturn(authentication);
                when(tokenProvider.createToken(authentication)).thenReturn(tokenDto);
                // When
                TokenDto result = authService.signIn(signInRequestDto);

                // Then
                assertNotNull(result);
                assertEquals(tokenDto, result);
                verify(userRepository).findByUsername(signInRequestDto.getUsername());
                verify(passwordEncoder).matches(signInRequestDto.getPassword(), selectedUser.getPassword());
                verify(authenticationManagerBuilder.getObject()).authenticate(authToken);
                verify(tokenProvider).createToken(authentication);
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCase {

            @Test
            @DisplayName("로그인 실패 테스트 - 해당 사용자를 찾을 수 없음.")
            void signInFail_UsernameNotFound() {
                //Given
                String role = "USER";
                List<String> roles = new ArrayList<>();
                roles.add(role);

                SignInRequestDto signInRequestDto = new SignInRequestDto();
                signInRequestDto.setUsername("testUser");
                signInRequestDto.setPassword("password123!");

                when(userRepository.findByUsername(signInRequestDto.getUsername())).thenReturn(Optional.empty());

                //When
                UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                        ()-> authService.signIn(signInRequestDto));

                //Then
                assertEquals("사용자를 찾을 수 없습니다. : " + signInRequestDto.getUsername(), exception.getMessage());
                verify(userRepository).findByUsername(signInRequestDto.getUsername());
                verifyNoInteractions(passwordEncoder, authenticationManagerBuilder, tokenProvider);
            }

            @Test
            @DisplayName("로그인 실패 테스트 - 비밀번호가 일치하지 않음.")
            void signInFail_IncorrectPassword() {
                //Given
                String role = "USER";
                List<String> roles = new ArrayList<>();
                roles.add(role);

                String rightPassword = "password123!";
                String wrongPassword = "wrongPassword";
                SignInRequestDto signInRequestDto = new SignInRequestDto();
                signInRequestDto.setUsername("testUser");
                signInRequestDto.setPassword(wrongPassword);

                String encPassword = passwordEncoder.encode(rightPassword);
                User selectedUser = new User(1L, "testUser", encPassword, roles);

                when(userRepository.findByUsername(signInRequestDto.getUsername())).thenReturn(Optional.of(selectedUser));
                when(passwordEncoder.matches(signInRequestDto.getPassword(), selectedUser.getPassword())).thenReturn(false);
                //When

                IncorrectPasswordException exception = assertThrows(IncorrectPasswordException.class,
                        ()-> authService.signIn(signInRequestDto));
                //Then
                assertEquals("비밀번호가 일치하지 않습니다.:  "+ signInRequestDto.getUsername(), exception.getMessage());
                verify(userRepository).findByUsername(signInRequestDto.getUsername());
                verify(passwordEncoder).matches(signInRequestDto.getPassword(), selectedUser.getPassword());
                verifyNoInteractions(authenticationManagerBuilder, tokenProvider);
            }
        }
    }
}
