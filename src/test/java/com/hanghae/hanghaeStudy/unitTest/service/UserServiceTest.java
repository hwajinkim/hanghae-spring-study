package com.hanghae.hanghaeStudy.unitTest.service;

import com.hanghae.hanghaeStudy.dto.auth.SignInRequestDto;
import com.hanghae.hanghaeStudy.dto.auth.TokenDto;
import com.hanghae.hanghaeStudy.dto.user.UserRequestDto;
import com.hanghae.hanghaeStudy.dto.user.UserResponseDto;
import com.hanghae.hanghaeStudy.entity.User;
import com.hanghae.hanghaeStudy.exception.AlreadyExistUserException;
import com.hanghae.hanghaeStudy.repository.UserRepository;
import com.hanghae.hanghaeStudy.service.AuthService;
import com.hanghae.hanghaeStudy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void init(){
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Nested
    @DisplayName("회원가입")
    class SignUp {
        private UserRequestDto userRequestDto;
        private List<String> roles;

        @BeforeEach
        public void init() {
            String role = "USER";
            roles = new ArrayList<>();
            roles.add(role);

            userRequestDto = new UserRequestDto();
            userRequestDto.setUsername("testUser");
            userRequestDto.setPassword("password123!");
            userRequestDto.setRoles(roles);
        }

        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {
            @Test
            @DisplayName("회원가입 성공 테스트")
            void signupSuccess() {
                // Given
                String encPassword = passwordEncoder.encode(userRequestDto.getPassword());
                User savedUser = new User(1L, "testUser", encPassword, roles);

                when(userRepository.existsByUsername(userRequestDto.getUsername())).thenReturn(false);
                when(userRepository.save(any(User.class))).thenReturn(savedUser);
                // When
                UserResponseDto responseDto = userService.signup(userRequestDto);

                // Then
                assertNotNull(responseDto);
                assertEquals("testUser", responseDto.getUsername());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCase {
            @Test
            @DisplayName("회원가입 실패 테스트 - 이미 존재하는 사용자")
            void signupFail_UserAlreadyExists() {
                // Given
                when(userRepository.existsByUsername(userRequestDto.getUsername())).thenReturn(true);
                // When
                AlreadyExistUserException exception = assertThrows(AlreadyExistUserException.class,
                        () -> userService.signup(userRequestDto));
                // Then
                assertEquals("이미 존재하는 회원입니다. :" + userRequestDto.getUsername(), exception.getMessage());
            }
        }
    }
}
