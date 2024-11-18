package com.hanghae.hanghaeStudy.service;

import com.hanghae.hanghaeStudy.dto.user.UserRequestDto;
import com.hanghae.hanghaeStudy.dto.user.UserResponseDto;
import com.hanghae.hanghaeStudy.entity.User;
import com.hanghae.hanghaeStudy.exception.AlreadyExistUserException;
import com.hanghae.hanghaeStudy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signupSuccess(){
        // Given
        UserRequestDto requestDto = new UserRequestDto("testUser2", "password123!");
        User mockUser = User.toEntity(requestDto);
        User savedUser = new User( 1L,"testUser2", "password123");

        when(userRepository.existsByUsername(requestDto.getUsername())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        // When
        UserResponseDto responseDto = userService.signup(requestDto);

        // Then
        assertNotNull(responseDto);
        assertEquals("testUser2", responseDto.getUsername());
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - 이미 존재하는 사용자")
    void signupFail_UserAlreadyExists(){
        // Given
        UserRequestDto requestDto = new UserRequestDto( "testUser", "password123!");

        when(userRepository.existsByUsername(requestDto.getUsername())).thenReturn(true);
        // When
        AlreadyExistUserException exception = assertThrows(AlreadyExistUserException.class,
                ()-> userService.signup(requestDto));
        // Then
        assertEquals("이미 존재하는 회원입니다. :testUser", exception.getMessage());
    }
}
