package com.hanghae.hanghaeStudy.service;

import com.hanghae.hanghaeStudy.dto.user.UserRequestDto;
import com.hanghae.hanghaeStudy.dto.user.UserResponseDto;
import com.hanghae.hanghaeStudy.entity.User;
import com.hanghae.hanghaeStudy.exception.AlreadyExistUserException;
import com.hanghae.hanghaeStudy.exception.IncorrectPasswordException;
import com.hanghae.hanghaeStudy.exception.UsernameNotFoundException;
import com.hanghae.hanghaeStudy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto signup(UserRequestDto userRequestDto){
        if(userRepository.existsByUsername(userRequestDto.getUsername())){
            throw new AlreadyExistUserException("이미 존재하는 회원입니다. :"+ userRequestDto.getUsername());
        }

        User user = User.builder()
                .username(userRequestDto.getUsername())
                .password(passwordEncoder.encode(userRequestDto.getPassword()))
                .roles(userRequestDto.getRoles())
                .build();

        return UserResponseDto.toDto(userRepository.save(user));
    }
}
